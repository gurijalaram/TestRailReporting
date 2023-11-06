package com.apriori.cic.api.tests;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.CostingInputFields;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartsSearch;
import com.apriori.cic.api.enums.PlmWCType;
import com.apriori.cic.api.models.request.CostingInputs;
import com.apriori.cic.api.models.request.WorkflowPart;
import com.apriori.cic.api.models.request.WorkflowParts;
import com.apriori.cic.api.models.response.AgentErrorMessage;
import com.apriori.cic.api.models.response.AgentWorkflow;
import com.apriori.cic.api.models.response.AgentWorkflowJob;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.models.response.PlmSearchResponse;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.SearchFilter;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collections;

@ExtendWith(TestRulesAPI.class)
public class CicAgentRunPartsTest extends WorkflowTestUtil {

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
    @TestRail(id = {15574})
    @Description("Get CIC Agent Workflow by identity and verify Part selection type REST")
    public void testWorkflowAndVerifyPartSelectionType() {
        this.cicLogin()
            .create()
            .getWorkflowId();

        AgentWorkflow agentWorkflow = CicApiTestUtil.getCicAgentWorkflow(this.agentWorkflowResponse.getId());
        softAssertions.assertThat(agentWorkflow.getPartSelectionType()).isEqualTo(CICPartSelectionType.REST.getPartSelectionType());
    }

    @Test
    @TestRail(id = {16697})
    @Description("All standard costing inputs set by runPartList request when no costing inputs are set in workflow")
    public void testGetWorkflowRunPartsListWithSinglePart() {
        AgentWorkflowJobRun agentWorkflowJobRunResponse = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .getAgentWorkflowJobRunResponse();

        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();
    }

    @Test
    @TestRail(id = {16698, 16700})
    @Description("RunPartList - all standard costing inputs overridden by runPartList request when all costing inputs are set in workflow with constant value constant, " +
        "A subset of all standard costing inputs overridden by runPartList request when all costing inputs are set in workflow with constant value constant")
    public void testGetWorkflowRunPartsOverriddenWithConstant() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.CONSTANT, DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.CONSTANT, MaterialNameEnum.ABS.getMaterialName())
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobResults agentWorkflowJobResult = this.invokeRestWorkflow().track().getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(id = {15580})
    @Description("RunPartList - cost parts with list of IDs")
    public void testGetWorkflowRunPartsListWithMultipleParts() {
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(2);
        AgentWorkflowJobRun agentWorkflowJobRunResponse = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .getAgentWorkflowJobRunResponse();

        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();
    }

    @Test
    @TestRail(id = {15582})
    @Description("RunPartList - cost parts with empty parts")
    public void testGetWorkflowRunPartsListWithEmptyParts() {
        this.cicLogin()
            .create()
            .getWorkflowId();

        ResponseWrapper<String> agentWorkflowJobRunResponse =
            CicApiTestUtil.runCicAgentWorkflowPartList(
                this.agentWorkflowResponse.getId(),
                WorkflowParts.builder().parts(new ArrayList<>()).build(),
                null,
                HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getBody()).contains(String.format("Empty part list for workflow with id '%s'", this.agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(id = {15581})
    @Description("RunPartList - duplicate IDs in list are processed only once")
    public void testRunPartsListWithDuplicateParts() {
        PlmSearchResponse plmParts = CicApiTestUtil.searchPlmWindChillParts(new SearchFilter()
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

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getApwScenarioLink()).isNotNull();
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getCidPartLink()).isNotNull();
    }

    @Test
    @TestRail(id = {15583})
    @Description("RunPartList - number of submitted parts exceeds agent max parts to return")
    public void testGetWorkflowRunPartsListWithMaxLimitParts() {
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(6);
        this.cicLogin()
            .create()
            .getWorkflowId();

        ResponseWrapper<String> agentWorkflowJobRunResponse =
            CicApiTestUtil.runCicAgentWorkflowPartList(
                this.agentWorkflowResponse.getId(),
                workflowPartsRequestDataBuilder,
                null,
                HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getBody()).contains("Max size for part list is exceeded");
    }

    @Test
    @TestRail(id = {15586})
    @Description("RunPartList - Invalid part")
    public void testRunPartsListWithInvalidPart() {
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id("InvalidPartIdentity")
                .costingInputs(new CostingInputs())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track();

        AgentWorkflowJob agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobStatus(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId());

        softAssertions.assertThat(agentWorkflowJobResult.getErrorMessage()).contains("Transition from state 'CAD_DOWNLOAD_FAILED' to state 'CAD_DOWNLOAD_FAILED' is not allowed");
    }

    @Test
    @TestRail(id = {15584})
    @Description("Submit run parts list with workflow with partSelectionType 'Query'")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        AgentErrorMessage errorMessage = CicApiTestUtil.runCicAgentWorkflowPartList(
            this.agentWorkflowResponse.getId(),
            workflowPartsRequestDataBuilder,
            AgentErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(errorMessage.getMessage()).contains(String.format("Workflow with id %s of type 'QUERY' doesn't support the action 'runPartList'", this.agentWorkflowResponse.getId()));
    }

    @Test
    @TestRail(id = {15579})
    @Description("Submit run request for workflows with partSelectionType REST and verify error")
    public void testGetWorkflowRunErrorForRest() {
        this.cicLogin()
            .create()
            .getWorkflowId();

        AgentErrorMessage errorMessage = CicApiTestUtil.runCicAgentWorkflow(
            this.agentWorkflowResponse.getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(errorMessage.getMessage()).contains(String.format("Workflow with id %s of type 'REST' doesn't support the action 'run'", this.agentWorkflowResponse.getId()));
    }

    @AfterEach
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
        this.close();
    }
}
