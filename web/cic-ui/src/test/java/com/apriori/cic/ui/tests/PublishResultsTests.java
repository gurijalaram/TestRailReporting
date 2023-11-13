package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.CostingInputFields;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.PublishResultsWriteRule;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.PlmFieldDefinitions;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.PlmPartResponse;
import com.apriori.cic.api.models.response.PlmSearchPart;
import com.apriori.cic.api.utils.PlmApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.AttachReportTab;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.NotificationsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults.PRAttachReportTab;
import com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.DateUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class PublishResultsTests extends WorkflowTestUtil {

    private static SoftAssertions softAssertions;
    private static PlmSearchPart plmPart;
    private static PlmApiTestUtil plmApiTestUtil;
    private static PlmFieldDefinitions plmFieldDefinitions;
    private static AgentPort agentPort;
    private static String plmPartNumber;

    @BeforeEach
    public void setUpAndLogin() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        plmApiTestUtil = new PlmApiTestUtil();
        plmFieldDefinitions = new PlmFieldDefinitions();
        plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();
        agentPort = new AgentPort();
    }

    @Test
    @TestRail(id = {4042})
    @Description("Test Reports Tab on the publish results tab during workflow creation")
    public void testPublishResultsAttachReportTab() {
        WorkFlowData workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        workFlowData.setConnectorName(agentPort.getConnector());
        workFlowData.getQueryDefinitionsData().get(0).setFieldValue("000001042");
        softAssertions = new SoftAssertions();
        DetailsPart detailsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
            .clickWFQueryDefNextBtn();

        NotificationsPart notificationsPart = costingInputsPart.clickCINextBtn();
        notificationsPart.selectEmailTab().selectEmailTemplate().selectRecipient();
        AttachReportTab attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(attachReportTab.getCurrencyCodeDdl().isDisplayed()).isEqualTo(true);

        workFlowData.getNotificationsData().setReportName(ReportsEnum.PART_COST.getReportName());
        attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(attachReportTab.getEmptyReportLbl().getText()).isEqualTo("No report configuration defined.");

        workFlowData.getNotificationsData().setReportName(ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY.getReportName());
        notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(notificationsPart.selectAttachReport().getCostMetricDdl().isDisplayed()).isEqualTo(true);
        PublishResultsPart publishResultsPart = notificationsPart.clickCINotificationNextBtn();

        workFlowData.getPublishResultsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        PRAttachReportTab prAttachReportTab = publishResultsPart.selectAttachReportTab().selectReportName();
        softAssertions.assertThat(prAttachReportTab.getCurrencyCodeDdl().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(prAttachReportTab.getCostRoundingDdl().isDisplayed()).isEqualTo(true);

        workFlowData.getPublishResultsData().setReportName(ReportsEnum.PART_COST.getReportName());
        prAttachReportTab = publishResultsPart.selectAttachReportTab().selectReportName();
        softAssertions.assertThat(prAttachReportTab.getEmptyPRReportLbl().getText()).isEqualTo("No report configuration defined.");
        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {5077})
    @Description("Test PLM write of multiselect UDA using mapping rule 'Constant'")
    public void testMultiSelectUdaConstantMappedRule() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_PUBLISH_CONSTANT);
        String multiString = "[\"Value 1\",\"Value 3\"]";
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .emptyCostingInputRow()
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_MULTI, PublishResultsWriteRule.CONSTANT, multiString)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);

        plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(plmPartResponse.getMultiselectString()).isEqualTo(multiString);
    }

    @Test
    @TestRail(id = {25960})
    @Description("Test PLM write UDA of each data type (String, Number and Date) using mapping rule 'Constant'")
    public void testEachUdaConstantMappedRule() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_PUBLISH_CONSTANT);
        Double customNumber = new GenerateStringUtil().getRandomDoubleWithTwoDecimals();
        String customString = new GenerateStringUtil().getRandomStringSpecLength(10);
        String customDate = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmss) + ".000Z";

        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .emptyCostingInputRow()
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_STRING, PublishResultsWriteRule.CONSTANT, customString)
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_NUMBER, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_DATE, PublishResultsWriteRule.CONSTANT, customDate)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);

        plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(plmPartResponse.getRealNumber1()).isEqualTo(customNumber);
        softAssertions.assertThat(plmPartResponse.getDateTime1()).contains(DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMdd));
        softAssertions.assertThat(plmPartResponse.getRealNumber1()).isEqualTo(customNumber);
    }

    @Test
    @TestRail(id = {4095, 4867})
    @Description("Test each Standard Value can be written back to the PLM system with 'Workflow Generated Value'" +
        "Test PLM write of all standard fields mapped with usage 'Read and Write'")
    public void testEachStandardWFGVWritingRule() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_PUBLISH_GENERATED);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.CONSTANT, "testScenario123")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.CONSTANT, DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.CONSTANT, MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.CONSTANT, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.CONSTANT, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.CONSTANT, "1")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_FULLY_BURDENED_COST, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DFM_RISK, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_TOTAL_COST, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_MATERIAL_COST, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CYCLE_TIME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_LABOR_TIME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_FINISH_MASS, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_ROUGH_MASS, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DFM_SCORE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_UTILIZATION, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_MATERIAL_NAME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_PROCESS_GROUP, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DIGITAL_FACTORY, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_ANNUAL_VOLUME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_PRODUCTION_LIFE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);

        plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getProcessGroupName()).isEqualTo(plmPartResponse.getApPG());
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getMaterialName()).isEqualTo(plmPartResponse.getApMaterial());
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getVpeName()).isEqualTo(plmPartResponse.getVpe());
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getAnnualVolume()).isEqualTo(plmPartResponse.getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getBatchSize()).isEqualTo(plmPartResponse.getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getProductionLife()).isEqualTo(plmPartResponse.getProductionLife());

        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getDfmRisk()).isEqualTo(plmPartResponse.getApDFMRating());
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getFullyBurdenedCost().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getApFBC())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getTotalCost().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getApPPC())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getMaterialCost().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getMaterialCost())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getCycleTime().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getApCycleTime())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getLaborTime().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getLaborTime())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getFinishMass().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getFinishMass())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getRoughMass().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getRoughMass())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getUtilization().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getUtilization())).intValue()));
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getDfmScore().toString()).contains(String.valueOf(new BigDecimal(String.valueOf(plmPartResponse.getApDFMRiskScore())).intValue()));
    }

    @Test
    @TestRail(id = {5078})
    @Description("Test PLM write of multiselect UDA using mapping rule 'Workdflow Generated Value'")
    public void testMultiUDAWorkflowGeneratedWritingRule() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_PUBLISH_GENERATED);
        String multiString = "[\"Value 1\",\"Value 3\"]";
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.CONSTANT, "testScenario")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.CONSTANT, DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.CONSTANT, MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.CONSTANT, multiString)
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_MULTI, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);

        plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(plmPartResponse.getMultiselectString()).isEqualTo("Value 1,Value 3");

    }

    @Test
    @TestRail(id = {25961})
    @Description("Test PLM write of UDA data type(String, Number and Date) using mapping rule 'Workflow Generated Value'")
    public void testEachUdaWfGVMappedRule() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_PUBLISH_GENERATED);
        Double customNumber = new GenerateStringUtil().getRandomDoubleWithTwoDecimals();
        String customString = new GenerateStringUtil().getRandomStringSpecLength(10);
        String customDate = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmss) + ".000Z";

        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.CONSTANT, customString)
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.CONSTANT, String.valueOf(customNumber))
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.CONSTANT, customDate)
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_STRING, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, customString)
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_NUMBER, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CUSTOM_DATE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, customDate)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);

        plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(plmPartResponse.getRealNumber1()).isEqualTo(customNumber);
        softAssertions.assertThat(plmPartResponse.getDateTime1()).contains(DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMdd));
        softAssertions.assertThat(plmPartResponse.getRealNumber1()).isEqualTo(customNumber);
    }

    @Test
    @TestRail(id = {4865})
    @Description("Test write back of all CI Connect standard fields using 'Constant' value")
    public void testEachStandardConstantWritingRule() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_PUBLISH_CONSTANT);
        Double customNumber = new GenerateStringUtil().getRandomDoubleWithTwoDecimals();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .emptyCostingInputRow()
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_FULLY_BURDENED_COST, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DFM_RISK, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_TOTAL_COST, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_MATERIAL_COST, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_CYCLE_TIME, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_LABOR_TIME, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_FINISH_MASS, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_ROUGH_MASS, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DFM_SCORE, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_UTILIZATION, PublishResultsWriteRule.CONSTANT, String.valueOf(customNumber))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_MATERIAL_NAME, PublishResultsWriteRule.CONSTANT, MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_PROCESS_GROUP, PublishResultsWriteRule.CONSTANT, ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DIGITAL_FACTORY, PublishResultsWriteRule.CONSTANT, DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory())
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_ANNUAL_VOLUME, PublishResultsWriteRule.CONSTANT, String.valueOf(3000))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.CONSTANT, String.valueOf(50))
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_PRODUCTION_LIFE, PublishResultsWriteRule.CONSTANT, String.valueOf(1.0))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);

        plmPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()).isEqualTo(plmPartResponse.getApPG());
        softAssertions.assertThat(MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName()).isEqualTo(plmPartResponse.getApMaterial());
        softAssertions.assertThat(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory()).isEqualTo(plmPartResponse.getVpe());
        softAssertions.assertThat(plmPartResponse.getAnnualVolume()).isEqualTo(3000);
        softAssertions.assertThat(plmPartResponse.getBatchSize()).isEqualTo(50);
        softAssertions.assertThat(plmPartResponse.getProductionLife()).isEqualTo(1.0);

        softAssertions.assertThat(plmPartResponse.getApDFMRating()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getApFBC().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getApPPC().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getMaterialCost().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getApCycleTime().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getLaborTime().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getFinishMass().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getRoughMass().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getUtilization().toString()).isEqualTo(String.valueOf(customNumber));
        softAssertions.assertThat(plmPartResponse.getApDFMRiskScore().toString()).isEqualTo(String.valueOf(customNumber));
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        this.deleteWorkflow();
        plmApiTestUtil.updatePartInfoToPlm(plmPart.getId(), plmFieldDefinitions, "Update part to reset the data");
    }
}
