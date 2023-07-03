package com.apriori.cic.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
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
import entity.response.PlmSearchResponse;
import enums.CICAgentStatus;
import enums.CICPartSelectionType;
import enums.CostingInputFields;
import enums.MappingRule;
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
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

import java.util.Collections;

public class CicAgentRunPartsTest extends TestBase {

    private static String loginSession;
    private static JobDefinition jobDefinitionData;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static WorkflowParts workflowPartsRequestDataBuilder;
    private static PartData plmPartData;
    private static WorkflowTestUtil workflowTestUtil;
    private static SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser())
            .navigateToUserMenu()
            .getWebSession();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        workflowTestUtil = new WorkflowTestUtil();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
    }

    @Test
    @TestRail(testCaseId = {"15574"})
    @Description("Get CIC Agent Workflow by identity and verify Part selection type REST")
    public void testWorkflowAndVerifyPartSelectionType() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);

        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName());

        AgentWorkflow agentWorkflow = CicApiTestUtil.getCicAgentWorkflow(workflowTestUtil.getAgentWorkflowResponse().getId());
        softAssertions.assertThat(agentWorkflow.getPartSelectionType()).isEqualTo(CICPartSelectionType.REST.getPartSelectionType());
    }

    @Test
    @TestRail(testCaseId = {"16697"})
    @Description("All standard costing inputs set by runPartList request when no costing inputs are set in workflow")
    public void testGetWorkflowRunPartsListWithSinglePart() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobRun agentWorkflowJobRunResponse = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .getAgentWorkflowJobRunResponse();
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"16698"})
    @Description("RunPartList - all standard costing inputs overridden by runPartList request when all costing inputs are set in workflow with constant value constant")
    public void testGetWorkflowRunPartsOverriddenWithConstant() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.CONSTANT, "testScenario123")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.CONSTANT, DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.CONSTANT, MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.CONSTANT, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.CONSTANT, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.CONSTANT, "1")
            .build();
        WorkflowParts wfPartDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobPartsResult agentWorkflowJobPartResult = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(wfPartDataBuilder)
            .track()
            .getJobPartResult(wfPartDataBuilder.getParts().get(0).getId());

        softAssertions.assertThat(agentWorkflowJobPartResult.getInput().getMaterialName()).isEqualTo(wfPartDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartResult.getInput().getProcessGroupName()).isEqualTo(wfPartDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
    }

    @Test
    @TestRail(testCaseId = {"15580"})
    @Description("RunPartList - cost parts with list of IDs")
    public void testGetWorkflowRunPartsListWithMultipleParts() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 2);
        AgentWorkflowJobRun agentWorkflowJobRunResponse = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .getAgentWorkflowJobRunResponse();
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"15582"})
    @Description("RunPartList - cost parts with empty parts")
    public void testGetWorkflowRunPartsListWithEmptyParts() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName());

        ResponseWrapper<String> agentWorkflowJobRunResponse =
            CicApiTestUtil.runCicAgentWorkflowPartList(
                workflowTestUtil.getAgentWorkflowResponse().getId(),
                new WorkflowParts(),
                null,
                HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getBody()).contains(String.format("Empty part list for workflow with id '%s'", workflowTestUtil.getAgentWorkflowResponse().getId()));
    }

    @Test
    @TestRail(testCaseId = {"15581"})
    @Description("RunPartList - duplicate IDs in list are processed only once")
    public void testRunPartsListWithDuplicateParts() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();

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

        AgentWorkflowJobResults agentWorkflowJobResult = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobResult();

        softAssertions.assertThat(CicApiTestUtil.verifyAgentErrorMessage(agentWorkflowJobResult, "This part appeared multiple times in the job")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"15583"})
    @Description("RunPartList - number of submitted parts exceeds agent max parts to return")
    public void testGetWorkflowRunPartsListWithMaxLimitParts() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 6);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName());

        ResponseWrapper<String> agentWorkflowJobRunResponse =
            CicApiTestUtil.runCicAgentWorkflowPartList(
                workflowTestUtil.getAgentWorkflowResponse().getId(),
                workflowPartsRequestDataBuilder,
                null,
                HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getBody()).contains("Max size for part list is exceeded");
    }

    @Test
    @TestRail(testCaseId = {"15586"})
    @Description("RunPartList - Invalid part")
    public void testRunPartsListWithInvalidPart() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id("InvalidPartIdentity")
                .costingInputs(new CostingInputs())
                .build()))
            .build();

        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track();

        AgentWorkflowJob agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobStatus(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId());

        softAssertions.assertThat(agentWorkflowJobResult.getErrorMessage()).contains("Transition from state 'CAD_DOWNLOAD_FAILED' to state 'CAD_DOWNLOAD_FAILED' is not allowed");
    }

    @Test
    @TestRail(testCaseId = {"15584"})
    @Description("Submit run parts list with workflow with partSelectionType 'Query'")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .build();
        WorkflowParts wfPartRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);

        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName());

        AgentErrorMessage errorMessage = CicApiTestUtil.runCicAgentWorkflowPartList(
            workflowTestUtil.getAgentWorkflowResponse().getId(),
            wfPartRequestDataBuilder,
            AgentErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(errorMessage.getMessage()).contains(String.format("Workflow with id %s of type 'QUERY' doesn't support the action 'runPartList'", workflowTestUtil.getAgentWorkflowResponse().getId()));
    }

    @Test
    @TestRail(testCaseId = {"15579"})
    @Description("Submit run request for workflows with partSelectionType REST and verify error")
    public void testGetWorkflowRunErrorForRest() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();

        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName());

        AgentErrorMessage errorMessage = CicApiTestUtil.runCicAgentWorkflow(
            workflowTestUtil.getAgentWorkflowResponse().getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(errorMessage.getMessage()).contains(String.format("Workflow with id %s of type 'REST' doesn't support the action 'run'", workflowTestUtil.getAgentWorkflowResponse().getId()));
    }

    @After
    public void cleanup() {
        CicApiTestUtil.deleteWorkFlow(loginSession, CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
        softAssertions.assertAll();
    }
}
