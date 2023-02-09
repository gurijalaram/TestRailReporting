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

public class CicAgentJobResultsTest extends TestBase {

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
    @TestRail(testCaseId = {"16282", "16288", "16289"})
    @Description("Get Job results with valid workflow and job identity and validate response body data")
    public void testGetWorkflowJobResult() {
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

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16305"})
    @Description("Get Job results with fields that have no value")
    public void testGetWorkflowJobResultWithNoFields() {
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

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getDescription()).isNull();
    }

    @Test
    @TestRail(testCaseId = {"16308"})
    @Description("Get Job results with workflow created using part selection type query")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        JobDefinition jdData = CicApiTestUtil.getJobDefinitionData();
        WorkflowRequest wfrQueryDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.QUERY, false);
        ResponseWrapper<String> cwfResponse = CicApiTestUtil.CreateWorkflow(wfrQueryDataBuilder, loginSession);
        softAssertions.assertThat(cwfResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(cwfResponse.getBody()).contains(">true<");
        AgentWorkflow awfResponse = CicApiTestUtil.getMatchedWorkflowId(wfrQueryDataBuilder.getName());

        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(awfResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        ;
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(awfResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(awfResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartNumber()).isEqualTo(wfrQueryDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());

        jdData.setJobDefinition(awfResponse.getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jdData);
    }

    @Test
    @TestRail(testCaseId = {"16291", "16309", "16315"})
    @Description("get Job Results maximum parts job size for part selection type REST and windchill agent")
    public void testGetWorkflowJobResultWithMaxLimitParts() {
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());

        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 5);

        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(5);
    }

    @Test
    @TestRail(testCaseId = {"16285"})
    @Description("Get Job results with invalid workflow Identity")
    public void testGetJobResultWithInvalidWorkFlow() {
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

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult("INVALID",
            agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow 'INVALID' not found", agentWorkflowJobRunResponse.getJobId()));
    }

    @Test
    @TestRail(testCaseId = {"16286"})
    @Description("Get Job results with valid workflow Identity and invalid job identity")
    public void testGetJobResultWithInvalidJobIdentity() {
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

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            "INVALID",
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job 'INVALID' in workflow '%s' not found", agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(testCaseId = {"16306"})
    @Description("Get Job results with cancelled job identity")
    public void testGetWorkflowJobResultWithCancelledJob() {
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

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.COSTING)).isTrue();
        CicApiTestUtil.cancelWorkflow(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());
        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.CANCELLED)).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16310"})
    @Description("Get Job results with errored job identity")
    public void testGetWorkflowJobResultWithErroredJob() {
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

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.get(0).getCicStatus()).isEqualTo("ERRORED");
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

        AgentErrorMessage agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentWorkflowJobResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", agentWorkflowJobRunResponse.getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.COSTING)).isTrue();

        AgentErrorMessage agentJobCostingResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentJobCostingResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", agentWorkflowJobRunResponse.getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.PLM_WRITE_ACTION)).isTrue();

        AgentErrorMessage agentJobPlmWriteResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentJobPlmWriteResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", agentWorkflowJobRunResponse.getJobId()));

    }

    @Test
    @TestRail(testCaseId = {"16284"})
    @Description("get Job Results for part selection type REST and windchill agent")
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

        AgentErrorMessage agentErrorMessage = (AgentErrorMessage) HTTPRequest.build(RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_RESULT, AgentErrorMessage.class)
            .inlineVariables(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())
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
    @TestRail(testCaseId = {"16312"})
    @Description("Get Job Results for parts re-costed with different costing inputs")
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

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());

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

        AgentWorkflowJobResults agentReCostedJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentReCostedJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
    }

    @Test
    @TestRail(testCaseId = {"16311"})
    @Description("Get Job results with deleted workflow Identity")
    public void testGetJobResultWithDeletedWorkFlow() {
        WorkflowRequest wfrDataBuilder = CicApiTestUtil.getWorkflowBaseData(CICPartSelectionType.REST, false);
        ResponseWrapper<String> createWfResponse = CicApiTestUtil.CreateWorkflow(wfrDataBuilder, loginSession);

        JobDefinition jDData = CicApiTestUtil.getJobDefinitionData();
        softAssertions.assertThat(createWfResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWfResponse.getBody()).contains(">true<");
        AgentWorkflow awfResponse = CicApiTestUtil.getMatchedWorkflowId(wfrDataBuilder.getName());

        WorkflowParts workflowPartsDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobRun agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            awfResponse.getId(),
            workflowPartsDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(awfResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        jDData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(wfrDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jDData);

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult(awfResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow '%s' not found", agentWorkflowJobRunResponse.getJobId(), awfResponse.getId()));
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
        softAssertions.assertAll();
    }
}
