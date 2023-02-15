package com.apriori.cic.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowParts;
import entity.request.WorkflowRequest;
import entity.response.AgentErrorMessage;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowJobRun;
import entity.response.PlmParts;
import enums.CICAPIEnum;
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
import utils.PlmPartsUtil;
import utils.SearchFilter;

import java.util.HashMap;

public class CicAgentJobPartResultTest extends TestBase {

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
        loginSession = CicApiTestUtil.getLoginSession(UserUtil.getUser(), driver);
        plmPartData = PlmPartsUtil.getPlmPartData();
        workflowRequestDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.REST, false);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, loginSession);

        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
    }

    @Test
    @TestRail(testCaseId = {"16318", "16324", "16325"})
    @Description("Get request with valid workflow, job, part identity, verify response body and data")
    public void testGetWorkflowJobPartResult() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16335"})
    @Description("Get Part Results for parts re-costed with different costing inputs")
    public void testGetPartResultWithReCostedParts() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());

        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Sheet Metal");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setMaterialName("Steel, Cold Worked, AISI 1020");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setVpeName("aPriori Brazil");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setAnnualVolume(5000);
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setBatchSize(50);

        AgentWorkflowJobRun reCostedJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(reCostedJobRunResponse.getJobId()).isNotNull();
        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), reCostedJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult reCostedJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            reCostedJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(reCostedJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
    }

    @Test
    @TestRail(testCaseId = {"16330"})
    @Description("Get part results with errored Part identity")
    public void testGetPartResultWithErroredPart() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Plastic");

        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCicStatus()).isEqualTo("ERRORED");
    }

    @Test
    @TestRail(testCaseId = {"16332", "16338"})
    @Description("get Part Results for part selection type REST and windchill agent")
    public void testGetPartResultWithRestForWC() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16328"})
    @Description("Get part results with fields that have no value")
    public void testGetPartResultWithNoFields() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJob agentWorkflowJob = CicApiTestUtil.getCicAgentWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());
        softAssertions.assertThat(agentWorkflowJob.getStatus()).isEqualTo("Finished");

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();
    }

    @Test
    @TestRail(testCaseId = {"16321"})
    @Description("Get Part results with invalid workflow Identity")
    public void testGetPartResultWithInvalidWorkFlow() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentErrorMessage aJobPartsResultError = CicApiTestUtil.getCicAgentWorkflowJobPartsResult("INVALID",
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);


        softAssertions.assertThat(aJobPartsResultError.getMessage()).contains(String.format("Job '%s' in workflow 'INVALID' not found", agentWorkflowJobRunResponse.getJobId()));
    }

    @Test
    @TestRail(testCaseId = {"16322"})
    @Description("Get Part results with valid workflow Identity and invalid job identity")
    public void testGetPartResultWithInvalidJobIdentity() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            "INVALID",
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("Job 'INVALID' in workflow '%s' not found", agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(testCaseId = {"16341"})
    @Description("Get Part results with valid workflow, job identity and invalid part identity")
    public void testGetPartResultWithInvalidPartIdentity() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            "INVALID",
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("Part 'INVALID' for job '%s' in workflow '%s' not found", agentWorkflowJobRunResponse.getJobId(), agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(testCaseId = {"16287"})
    @Description("Get Job results during processing with different status")
    public void testGetWorkflowJobInProgress() {
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

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.QUERY_IN_PROGRESS)).isTrue();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("The job '%s' is not in a terminal state", agentWorkflowJobRunResponse.getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.COSTING)).isTrue();

        AgentErrorMessage agentCostingErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentCostingErrorMessage.getMessage()).contains(String.format("The job '%s' is not in a terminal state", agentWorkflowJobRunResponse.getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.PLM_WRITE_ACTION)).isTrue();

        AgentErrorMessage agentPlmWriteErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentPlmWriteErrorMessage.getMessage()).contains(String.format("The job '%s' is not in a terminal state", agentWorkflowJobRunResponse.getJobId()));
    }

    @Test
    @TestRail(testCaseId = {"16320"})
    @Description("get Part Results for part selection type REST and windchill agent")
    public void testGetPartResultInvalidAuthenticationKey() {
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

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentErrorMessage agentErrorMessage = (AgentErrorMessage) HTTPRequest.build(RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_PART_RESULT, AgentErrorMessage.class)
            .inlineVariables(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), workflowPartsRequestDataBuilder.getParts().get(0).getId())
            .expectedResponseCode(HttpStatus.SC_FORBIDDEN)
            .headers(new HashMap<String, String>() {
                {
                    put("Accept", "application/json");
                    put("Content-Type", "application/json");
                    put("Authorization", "InvalidKey");
                }
            })).get().getResponseEntity();

        softAssertions.assertThat(agentErrorMessage.getMessage()).isEqualTo("Access Denied");
    }

    @Test
    @TestRail(testCaseId = {"16334"})
    @Description("Get part results with deleted workflow Identity")
    public void testGetPartResultWithDeletedWorkFlow() {
        JobDefinition jDData = CicApiTestUtil.getJobDefinitionData();
        WorkflowRequest wfrRestDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.REST, false);
        ResponseWrapper<String> createWfResponse = CicApiTestUtil.CreateWorkflow(wfrRestDataBuilder, loginSession);

        softAssertions.assertThat(createWfResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWfResponse.getBody()).contains(">true<");
        AgentWorkflow awfResponse = CicApiTestUtil.getMatchedWorkflowId(wfrRestDataBuilder.getName());

        WorkflowParts wfPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            awfResponse.getId(),
            wfPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(awfResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        jDData.setJobDefinition(awfResponse.getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jDData);

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(awfResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            wfPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow '%s' not found", agentWorkflowJobRunResponse.getJobId(), awfResponse.getId()));
    }

    @Test
    @TestRail(testCaseId = {"16331"})
    @Description("Get Part results with workflow created using part selection type query")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        JobDefinition jdData = CicApiTestUtil.getJobDefinitionData();
        WorkflowRequest wfrQueryDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.QUERY, false);
        ResponseWrapper<String> cwfResponse = CicApiTestUtil.CreateWorkflow(wfrQueryDataBuilder, loginSession);
        softAssertions.assertThat(cwfResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(cwfResponse.getBody()).contains(">true<");
        AgentWorkflow awfResponse = CicApiTestUtil.getMatchedWorkflowId(wfrQueryDataBuilder.getName());

        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(awfResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);;
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(awfResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(awfResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartNumber()).isEqualTo(wfrQueryDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(awfResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            agentWorkflowJobResult.get(0).getPartId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartNumber()).isEqualTo(wfrQueryDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();

        jdData.setJobDefinition(awfResponse.getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jdData);
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
        softAssertions.assertAll();
    }
}
