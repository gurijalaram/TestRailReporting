package com.cic.tests;

import com.apriori.pagedata.CostingServiceSettingsData;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowRequest;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobRun;
import entity.response.PlmPart;
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
import utils.PlmPartsUtil;
import utils.SearchFilter;
import utils.WorkflowDataUtil;

public class PlmIntegrationTests extends TestBase {

    private CostingServiceSettingsData costingServiceSettingsData;
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static ResponseWrapper<String> createWorkflowResponse;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static SoftAssertions softAssertions;
    private static PartData plmPartData;
    private CIConnectHome ciConnectHome;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        ciConnectHome = new CicLoginPage(driver).login(UserUtil.getUser());
    }

    @Test
    @TestRail(testCaseId = {"21965"})
    @Description("Test 'Mapped from PLM' mapping rule with value set in PLM system")
    public void testWorkflowMapSetInPlm() {
        plmPartData = PlmPartsUtil.getPlmPartData("Mapped");
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
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
        plmPartData = PlmPartsUtil.getPlmPartData("NotMapped");
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getScenarioName())
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getMaterial())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.DEFAULT_NO_PLM_VALUE, String.valueOf(plmPartData.getAnnualVolume()))
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.DEFAULT_NO_PLM_VALUE, String.valueOf(plmPartData.getBatchSize()))
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.DEFAULT_NO_PLM_VALUE, String.valueOf(plmPartData.getProductionLife()))
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.DEFAULT_NO_PLM_VALUE, "3925 description")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
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
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(plmPartData.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(plmPartData.getMaterial());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(plmPartData.getDigitalFactory());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(plmPartData.getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(plmPartData.getBatchSize());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo(plmPartData.getScenarioName());
    }

    @Test
    @TestRail(testCaseId = {"21966"})
    @Description("Test 'Default if no PLM Value' mapping rule with value set in PLM system")
    public void testWorkflowMapSameAsSetInPlm() {
        plmPartData = PlmPartsUtil.getPlmPartData("Mapped");
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.DEFAULT_NO_PLM_VALUE, "scenario name 1234")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.DEFAULT_NO_PLM_VALUE, "aPriori Brazil")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getMaterial())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.DEFAULT_NO_PLM_VALUE, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.DEFAULT_NO_PLM_VALUE, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.DEFAULT_NO_PLM_VALUE, "1")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.DEFAULT_NO_PLM_VALUE, "description")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
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
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(plmPartData.getMaterial());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(plmPartData.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo("aPriori Brazil");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(3100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(2100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("scenario name 1234");
    }

    @Test
    @TestRail(testCaseId = {"4330"})
    @Description("Test 'Constant' mapping rule")
    public void testWorkflowMapWithConstantPlm() {
        plmPartData = PlmPartsUtil.getPlmPartData("NotMapped");
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.CONSTANT, "scenario name 1234")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.CONSTANT, DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.CONSTANT, MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.CONSTANT, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.CONSTANT, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.CONSTANT, "1")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.CONSTANT, "description 1234")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
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
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isEqualTo("description 1234");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(ProcessGroupEnum.SHEET_METAL.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(3100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(2100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("scenario name 1234");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getErrorMessage()).isNull();
    }

    @Test
    @TestRail(testCaseId = {"4328"})
    @Description("Test 'Mapped from PLM' mapping rule with NO value set in PLM")
    public void testWorkflowMapFromPlmWithNoValueInPlm() {
        costingServiceSettingsData = CostingServiceSettingsData.builder()
            .scenarioName("SN" + new GenerateStringUtil().getRandomNumbersSpecLength(5))
            .processGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .digitalFactory(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory())
            .batchSize(Integer.parseInt(new GenerateStringUtil().getRandomNumbersSpecLength(2)))
            .annualVolume(Integer.parseInt(new GenerateStringUtil().getRandomNumbersSpecLength(4)))
            .productionVolume(Integer.parseInt(new GenerateStringUtil().getRandomNumbersSpecLength(2)))
            .build();

        ciConnectHome.clickCostingServiceSettings()
            .enterScenarioName(costingServiceSettingsData.getScenarioName())
            .selectProcessGroup(costingServiceSettingsData.getProcessGroup())
            .selectDigitalFactory(costingServiceSettingsData.getDigitalFactory())
            .enterBatchSize(costingServiceSettingsData.getBatchSize().toString())
            .enterAnnualVolume(costingServiceSettingsData.getAnnualVolume().toString())
            .enterProductionLife(costingServiceSettingsData.getProductionVolume().toString())
            .clickSaveButton();

        softAssertions.assertThat(ciConnectHome.getStatusMessage()).contains("Costing Settings updated");

        plmPartData = PlmPartsUtil.getPlmPartData("NotMapped");
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);

        createWorkflowResponse = CicApiTestUtil.CreateWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
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
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(costingServiceSettingsData.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(costingServiceSettingsData.getDigitalFactory());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(costingServiceSettingsData.getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(costingServiceSettingsData.getBatchSize());
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(ciConnectHome.getSession(), jobDefinitionData);
        softAssertions.assertAll();
    }
}
