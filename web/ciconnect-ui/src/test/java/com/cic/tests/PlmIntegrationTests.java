package com.cic.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowRequest;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobRun;
import entity.response.PlmPart;
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
import utils.SearchFilter;

public class PlmIntegrationTests extends TestBase {

    private static String loginSession;
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static ResponseWrapper<String> createWorkflowResponse;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser()).navigateToUserMenu().getWebSession();
    }

    @Test
    @TestRail(testCaseId = {"21965"})
    @Description("Test 'Mapped from PLM' mapping rule with value set in PLM system")
    public void testWorkflowMapSetInPlm() {
        workflowRequestDataBuilder = new TestDataService().getTestData("C21965WorkflowData.json", WorkflowRequest.class);
        workflowRequestDataBuilder.setCustomer(CicApiTestUtil.getCustomerName());
        workflowRequestDataBuilder.setPlmSystem(CicApiTestUtil.getAgent());
        workflowRequestDataBuilder.setName("CIC" + System.currentTimeMillis());
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, loginSession);
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            plmPart.getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo("Aluminum, ANSI 3003");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo("Stock Machining");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo("aPriori Brazil");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(3100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(2100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("3924 scenario name");
    }

    @Test
    @TestRail(testCaseId = {"4329"})
    @Description("Test 'Default if no PLM Value' mapping rule with NO value set in PLM system")
    public void testWorkflowMapNoPlm() {
        workflowRequestDataBuilder = new TestDataService().getTestData("C4329WorkflowData.json", WorkflowRequest.class);
        workflowRequestDataBuilder.setCustomer(CicApiTestUtil.getCustomerName());
        workflowRequestDataBuilder.setPlmSystem(CicApiTestUtil.getAgent());
        workflowRequestDataBuilder.setName("CIC" + System.currentTimeMillis());
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, loginSession);
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            plmPart.getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isEqualTo("3925 description");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo("Stock Machining");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo("Aluminum, ANSI 3003");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo("aPriori Mexico");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(5000);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(1000);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("3925 scenario");
    }

    @Test
    @TestRail(testCaseId = {"21966"})
    @Description("Test 'Default if no PLM Value' mapping rule with value set in PLM system")
    public void testWorkflowMapSameAsSetInPlm() {
        workflowRequestDataBuilder = new TestDataService().getTestData("C21966WorkflowData.json", WorkflowRequest.class);
        workflowRequestDataBuilder.setCustomer(CicApiTestUtil.getCustomerName());
        workflowRequestDataBuilder.setPlmSystem(CicApiTestUtil.getAgent());
        workflowRequestDataBuilder.setName("CIC" + System.currentTimeMillis());
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, loginSession);
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            plmPart.getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo("Aluminum, ANSI 3003");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo("Stock Machining");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo("aPriori Brazil");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(3100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(2100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("3924 scenario name");
    }

    @Test
    @TestRail(testCaseId = {"4330"})
    @Description("Test 'Constant' mapping rule")
    public void testWorkflowMapWithConstantPlm() {
        workflowRequestDataBuilder = new TestDataService().getTestData("C4330WorkflowData.json", WorkflowRequest.class);
        workflowRequestDataBuilder.setCustomer(CicApiTestUtil.getCustomerName());
        workflowRequestDataBuilder.setPlmSystem(CicApiTestUtil.getAgent());
        workflowRequestDataBuilder.setName("CIC" + System.currentTimeMillis());
        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, loginSession);
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            plmPart.getId(),
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isEqualTo("1234 description");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo("Sheet Metal");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo("Steel, Cold Worked, AISI 1020");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo("aPriori Italy");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(5000);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("1234 scenario");
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
        softAssertions.assertAll();
    }
}
