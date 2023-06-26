package com.apriori.cic.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.CostingInputs;
import entity.request.JobDefinition;
import entity.request.WorkflowPart;
import entity.request.WorkflowParts;
import entity.request.WorkflowRequest;
import entity.response.AgentErrorMessage;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowJobRun;
import entity.response.PlmParts;
import enums.CICAgentStatus;
import enums.CICPartSelectionType;
import enums.PlmPartsSearch;
import enums.PlmWCType;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.CicLoginUtil;
import utils.PlmPartsUtil;
import utils.SearchFilter;

import java.util.Collections;

public class CicAgentRunPartsTest extends TestBase {

    private static String loginSession;
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static ResponseWrapper<String> createWorkflowResponse;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static WorkflowParts workflowPartsRequestDataBuilder;
    private static PartData plmPartData;
    private static SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser())
            .navigateToUserMenu()
            .getWebSession();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        workflowRequestDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.REST, false);
        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, loginSession);

        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
    }

    @Test
    @TestRail(testCaseId = {"15574"})
    @Description("Get CIC Agent Workflow by identity and verify Part selection type REST")
    public void testWorkflowAndVerifyPartSelectionType() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        AgentWorkflow agentWorkflow = CicApiTestUtil.getCicAgentWorkflow(agentWorkflowResponse.getId());
        softAssertions.assertThat(agentWorkflow.getPartSelectionType()).isEqualTo(CICPartSelectionType.REST.getPartSelectionType());
    }

    @Test
    @TestRail(testCaseId = {"16697"})
    @Description("All standard costing inputs set by runPartList request when no costing inputs are set in workflow")
    public void testGetWorkflowRunPartsListWithSinglePart() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"16698"})
    @Description("RunPartList - all standard costing inputs overridden by runPartList request when all costing inputs are set in workflow with constant value constant")
    public void testGetWorkflowRunPartsOverriddenWithConstant() {
        JobDefinition jdData = CicApiTestUtil.getJobDefinitionData();
        WorkflowRequest wfrDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.REST, true);
        ResponseWrapper<String> cwfResponse = CicApiTestUtil.createWorkflow(wfrDataBuilder, loginSession);

        softAssertions.assertThat(cwfResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(cwfResponse.getBody()).contains(">true<");
        AgentWorkflow agentWfResponse = CicApiTestUtil.getMatchedWorkflowId(wfrDataBuilder.getName());

        WorkflowParts wfPartDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWfResponse.getId(),
            wfPartDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        CicApiTestUtil.trackWorkflowJobStatus(agentWfResponse.getId(), agentWorkflowJobRunResponse.getJobId());

        AgentWorkflowJobPartsResult agentWorkflowJobPartResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWfResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            wfPartDataBuilder.getParts().get(0).getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartResult.getInput().getMaterialName()).isEqualTo(wfPartDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartResult.getInput().getProcessGroupName()).isEqualTo(wfPartDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());

        jdData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(wfrDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jdData);
    }

    @Test
    @TestRail(testCaseId = {"15580"})
    @Description("RunPartList - cost parts with list of IDs")
    public void testGetWorkflowRunPartsListWithMultipleParts() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 2);

        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"15582"})
    @Description("RunPartList - cost parts with empty parts")
    public void testGetWorkflowRunPartsListWithEmptyParts() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        ResponseWrapper<String> agentWorkflowJobRunResponse =
            CicApiTestUtil.runCicAgentWorkflowPartList(
                agentWorkflowResponse.getId(),
                new WorkflowParts(),
                null,
                HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getBody()).contains(String.format("Empty part list for workflow with id '%s'", agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(testCaseId = {"15581"})
    @Description("RunPartList - duplicate IDs in list are processed only once")
    public void testRunPartsListWithDuplicateParts() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        PlmParts plmParts = CicApiTestUtil.searchPlmWindChillParts(new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(), plmPartData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build());

        WorkflowPart workFlowPart = WorkflowPart.builder()
            .id(plmParts.getItems().get(0).getId())
            .costingInputs(new CostingInputs())
            .build();

        workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.nCopies(2, workFlowPart))
            .build();

        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(CicApiTestUtil.verifyAgentErrorMessage(agentWorkflowJobResult, "This part appeared multiple times in the job")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"15583"})
    @Description("RunPartList - number of submitted parts exceeds agent max parts to return")
    public void testGetWorkflowRunPartsListWithMaxLimitParts() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 6);

        ResponseWrapper<String> agentWorkflowJobRunResponse =
            CicApiTestUtil.runCicAgentWorkflowPartList(
                agentWorkflowResponse.getId(),
                workflowPartsRequestDataBuilder,
                null,
                HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getBody()).contains("Max size for part list is exceeded");
    }

    @Test
    @TestRail(testCaseId = {"15586"})
    @Description("RunPartList - Invalid part")
    public void testRunPartsListWithInvalidPart() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id("InvalidPartIdentity")
                .costingInputs(new CostingInputs())
                .build()))
            .build();

        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            CICAgentStatus.QUERY_IN_PROGRESS)).isTrue();

        AgentWorkflowJob agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobStatus(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId());

        softAssertions.assertThat(agentWorkflowJobResult.getErrorMessage()).contains("Transition from state 'CAD_DOWNLOAD_FAILED' to state 'CAD_DOWNLOAD_FAILED' is not allowed");
    }

    @Test
    @TestRail(testCaseId = {"15584"})
    @Description("Submit run parts list with workflow with partSelectionType 'Query'")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        JobDefinition jdData = CicApiTestUtil.getJobDefinitionData();
        WorkflowRequest wfrQueryDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.QUERY, false);
        ResponseWrapper<String> cwfResponse = CicApiTestUtil.createWorkflow(wfrQueryDataBuilder, loginSession);
        softAssertions.assertThat(cwfResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(cwfResponse.getBody()).contains(">true<");
        AgentWorkflow awfResponse = CicApiTestUtil.getMatchedWorkflowId(wfrQueryDataBuilder.getName());

        WorkflowParts wfPartRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 5);

        AgentErrorMessage errorMessage = CicApiTestUtil.runCicAgentWorkflowPartList(
            awfResponse.getId(),
            wfPartRequestDataBuilder,
            AgentErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(errorMessage.getMessage()).contains(String.format("Workflow with id %s of type 'QUERY' doesn't support the action 'runPartList'", awfResponse.getId()));

        jdData.setJobDefinition(awfResponse.getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jdData);
    }

    @Test
    @TestRail(testCaseId = {"15579"})
    @Description("Submit run request for workflows with partSelectionType REST and verify error")
    public void testGetWorkflowRunErrorForRest() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        AgentWorkflow awfResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        AgentErrorMessage errorMessage = CicApiTestUtil.runCicAgentWorkflow(
            awfResponse.getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(errorMessage.getMessage()).contains(String.format("Workflow with id %s of type 'REST' doesn't support the action 'run'", awfResponse.getId()));
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
        softAssertions.assertAll();
    }
}
