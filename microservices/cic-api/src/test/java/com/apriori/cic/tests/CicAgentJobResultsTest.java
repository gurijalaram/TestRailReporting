package com.apriori.cic.tests;

import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import entity.response.AgentErrorMessage;
import entity.response.AgentWorkflowJobResults;
import enums.CICAPIEnum;
import enums.CICAgentStatus;
import enums.CICPartSelectionType;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.PlmPartsUtil;
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

import java.util.HashMap;

public class CicAgentJobResultsTest extends WorkflowTestUtil {

    private SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST).emptyCostingInputRow().build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(1);
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {16282, 16288, 16289})
    @Description("Get Job results with valid workflow and job identity and validate response body data")
    public void testGetWorkflowJobResult() {
        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(id = {16305})
    @Description("Get Job results with fields that have no value")
    public void testGetWorkflowJobResultWithNoFields() {
        workflowRequestDataBuilder.setDescription("");
        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getDescription()).isNull();
    }

    @Test
    @TestRail(id = {16308})
    @Description("Get Job results with workflow created using part selection type query")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartNumber()).isEqualTo(workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());
    }

    @Test
    @TestRail(id = {16291, 16309, 16315})
    @Description("get Job Results maximum parts job size for part selection type REST and windchill agent")
    public void testGetWorkflowJobResultWithMaxLimitParts() {
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(5);
        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(5);
    }

    @Test
    @TestRail(id = {16285})
    @Description("Get Job results with invalid workflow Identity")
    public void testGetJobResultWithInvalidWorkFlow() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track();

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult("INVALID",
            this.agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow 'INVALID' not found", this.agentWorkflowJobRunResponse.getJobId()));
    }

    @Test
    @TestRail(id = {16286})
    @Description("Get Job results with valid workflow Identity and invalid job identity")
    public void testGetJobResultWithInvalidJobIdentity() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track();

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult(this.agentWorkflowResponse.getId(),
            "INVALID",
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job 'INVALID' in workflow '%s' not found", this.agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(id = {16306})
    @Description("Get Job results with cancelled job identity")
    public void testGetWorkflowJobResultWithCancelledJob() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track();

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.COSTING)).isTrue();
        CicApiTestUtil.cancelWorkflow(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId());
        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.CANCELLED)).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId(),
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
    @TestRail(id = {16310})
    @Description("Get Job results with errored job identity")
    public void testGetWorkflowJobResultWithErroredJob() {
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Plastic");
        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getCicStatus()).isEqualTo("ERRORED");
    }

    @Test
    @TestRail(id = {16287})
    @Description("Get Job results during processing with different status")
    public void testGetWorkflowJobInProgress() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow();

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(),
            CICAgentStatus.QUERY_IN_PROGRESS)).isTrue();

        AgentErrorMessage agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentWorkflowJobResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", this.agentWorkflowJobRunResponse.getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.COSTING)).isTrue();

        AgentErrorMessage agentJobCostingResult = CicApiTestUtil.getCicAgentWorkflowJobResult(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentJobCostingResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", this.agentWorkflowJobRunResponse.getJobId()));
    }

    @Test
    @TestRail(id = {16284})
    @Description("get Job Results for part selection type REST and windchill agent")
    public void testGetPartResultInvalidAuthenticationKey() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow();

        AgentErrorMessage agentErrorMessage = (AgentErrorMessage) HTTPRequest.build(RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_RESULT, AgentErrorMessage.class)
            .inlineVariables(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId())
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
    @TestRail(id = {16312})
    @Description("Get Job Results for parts re-costed with different costing inputs")
    public void testGetJobResultWithReCostedParts() {
        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getId());

        this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Sheet Metal");
        this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setMaterialName("Steel, Cold Worked, AISI 1020");
        this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setVpeName("aPriori Brazil");
        this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setAnnualVolume(5000);
        this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setBatchSize(50);

        AgentWorkflowJobResults agentReCostedJobResult = this.invokeRestWorkflow()
            .track()
            .getJobResult();

        softAssertions.assertThat(agentReCostedJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getMaterialName()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getVpeName()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getBatchSize()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
    }

    @Test
    @TestRail(id = {16311})
    @Description("Get Job results with deleted workflow Identity")
    public void testGetJobResultWithDeletedWorkFlow() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .deleteWorkflow();

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow '%s' not found", this.agentWorkflowJobRunResponse.getJobId(), this.agentWorkflowResponse.getId()));
    }

    @After
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
        this.close();
    }
}