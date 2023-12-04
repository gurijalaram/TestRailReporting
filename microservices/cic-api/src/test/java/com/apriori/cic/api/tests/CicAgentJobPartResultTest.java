package com.apriori.cic.api.tests;

import com.apriori.cic.api.enums.CICAPIEnum;
import com.apriori.cic.api.enums.CICAgentStatus;
import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.models.response.AgentErrorMessage;
import com.apriori.cic.api.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

@ExtendWith(TestRulesAPI.class)
public class CicAgentJobPartResultTest extends WorkflowTestUtil {

    private SoftAssertions softAssertions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST).emptyCostingInputRow().build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(1);
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {16318, 16324, 16325})
    @Description("Get request with valid workflow, job, part identity, verify response body and data")
    public void testGetWorkflowJobPartResult() {
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.createRestWorkflowAndGetJobPartResult();

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(id = {16335})
    @Description("Get Part Results for parts re-costed with different costing inputs")
    public void testGetPartResultWithReCostedParts() {
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.createRestWorkflowAndGetJobPartResult();

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

        AgentWorkflowJobPartsResult reCostedJobPartsResult = invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobPartResult(workflowPartsRequestDataBuilder.getParts().get(0).getId());

        softAssertions.assertThat(reCostedJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
    }

    @Test
    @TestRail(id = {16330})
    @Description("Get part results with errored Part identity")
    public void testGetPartResultWithErroredPart() {
        this.workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Plastic");
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.createRestWorkflowAndGetJobPartResult();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getErrorMessage()).contains("Unable to find process group with name 'Plastic'");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getCicStatus()).isEqualTo("ERRORED");
    }

    @Test
    @TestRail(id = {16332, 16338})
    @Description("get Part Results for part selection type REST and windchill agent")
    public void testGetPartResultWithRestForWC() {
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.createRestWorkflowAndGetJobPartResult();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(id = {16328})
    @Description("Get part results with fields that have no value")
    public void testGetPartResultWithNoFields() {
        this.workflowRequestDataBuilder.setDescription("");
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.createRestWorkflowAndGetJobPartResult();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(this.workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();
    }

    @Test
    @TestRail(id = {16321})
    @Description("Get Part results with invalid workflow Identity")
    public void testGetPartResultWithInvalidWorkFlow() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track();

        AgentErrorMessage aJobPartsResultError = CicApiTestUtil.getCicAgentWorkflowJobPartsResult("INVALID",
            this.agentWorkflowJobRunResponse.getJobId(),
            this.workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(aJobPartsResultError.getMessage()).contains(String.format("Job '%s' in workflow 'INVALID' not found", this.agentWorkflowJobRunResponse.getJobId()));
    }

    @Test
    @TestRail(id = {16322})
    @Description("Get Part results with valid workflow Identity and invalid job identity")
    public void testGetPartResultWithInvalidJobIdentity() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(this.agentWorkflowResponse.getId(),
            "INVALID",
            this.workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("Job 'INVALID' in workflow '%s' not found", this.agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(id = {16341})
    @Description("Get Part results with valid workflow, job identity and invalid part identity")
    public void testGetPartResultWithInvalidPartIdentity() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(),
            "INVALID",
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("Part 'INVALID' for job '%s' in workflow '%s' not found", this.agentWorkflowJobRunResponse.getJobId(), this.agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(id = {16287})
    @Description("Get Job results during processing with different status")
    public void testGetWorkflowJobInProgress() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow();

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.QUERY_IN_PROGRESS)).isTrue();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("The job '%s' is not in a terminal state", this.agentWorkflowJobRunResponse.getJobId()));
        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId(), CICAgentStatus.COSTING)).isTrue();
    }

    @Test
    @TestRail(id = {16320})
    @Description("get Part Results for part selection type REST and windchill agent")
    public void testGetPartResultInvalidAuthenticationKey() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow();

        AgentErrorMessage agentErrorMessage = (AgentErrorMessage) HTTPRequest.build(RequestEntityUtil_Old.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_PART_RESULT, AgentErrorMessage.class)
            .inlineVariables(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId(), workflowPartsRequestDataBuilder.getParts().get(0).getId())
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
    @TestRail(id = {16334})
    @Description("Get part results with deleted workflow Identity")
    public void testGetPartResultWithDeletedWorkFlow() {
        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow();

        this.deleteWorkflow();

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(this.agentWorkflowResponse.getId(),
            this.agentWorkflowJobRunResponse.getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow '%s' not found", this.agentWorkflowJobRunResponse.getJobId(), this.agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(id = {16331})
    @Description("Get Part results with workflow created using part selection type query")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MAPPED).getPlmPartNumber())
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartNumber()).isEqualTo(workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.getJobPartResult(agentWorkflowJobResult.get(0).getPartId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartNumber()).isEqualTo(workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();
    }

    @AfterEach
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
        this.close();
    }
}