package com.cic.tests;

import com.apriori.GenerateStringUtil;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pagedata.CostingServiceSettingsData;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import entity.response.AgentWorkflowJobPartsResult;
import entity.response.PlmSearchPart;
import enums.CICPartSelectionType;
import enums.CostingInputFields;
import enums.MappingRule;
import enums.PlmPartDataType;
import enums.QueryDefinitionFields;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.PlmApiTestUtil;
import utils.PlmPartsUtil;
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

public class PlmIntegrationTests extends WorkflowTestUtil {

    private CostingServiceSettingsData costingServiceSettingsData;
    private static SoftAssertions softAssertions;
    private CIConnectHome ciConnectHome;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {21965})
    @Description("Test 'Mapped from PLM' mapping rule with value set in PLM system")
    public void testWorkflowMapSetInPlm() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MAPPED);
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

        PlmSearchPart plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo("Aluminum, ANSI 3003");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo("Stock Machining");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo("aPriori Brazil");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(3100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(2100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("3924 scenario name");
    }

    @Test
    @TestRail(id = {4329})
    @Description("Test 'Default if no PLM Value' mapping rule with NO value set in PLM system")
    public void testWorkflowMapNoPlm() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_NOT_MAPPED);
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

        PlmSearchPart plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());
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
    @TestRail(id = {21966})
    @Description("Test 'Default if no PLM Value' mapping rule with value set in PLM system")
    public void testWorkflowMapSameAsSetInPlm() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MAPPED);
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

        PlmSearchPart plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(plmPartData.getMaterial());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(plmPartData.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo("aPriori Brazil");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(3100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(2100);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProductionLife()).isEqualTo(1.0);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("scenario name 1234");
    }

    @Test
    @TestRail(id = {4330})
    @Description("Test 'Constant' mapping rule")
    public void testWorkflowMapWithConstantPlm() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_NOT_MAPPED);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
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

        PlmSearchPart plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

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
    @TestRail(id = {4328})
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

        ciConnectHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickCostingServiceSettings()
            .enterScenarioName(costingServiceSettingsData.getScenarioName())
            .selectProcessGroup(costingServiceSettingsData.getProcessGroup())
            .selectDigitalFactory(costingServiceSettingsData.getDigitalFactory())
            .enterBatchSize(costingServiceSettingsData.getBatchSize().toString())
            .enterAnnualVolume(costingServiceSettingsData.getAnnualVolume().toString())
            .enterProductionLife(costingServiceSettingsData.getProductionVolume().toString())
            .clickSaveButton();

        softAssertions.assertThat(ciConnectHome.getStatusMessage()).contains("Costing Settings updated");

        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_NOT_MAPPED);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
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

        PlmSearchPart plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(costingServiceSettingsData.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(costingServiceSettingsData.getDigitalFactory());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(costingServiceSettingsData.getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(costingServiceSettingsData.getBatchSize());
    }

    @After
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
    }
}
