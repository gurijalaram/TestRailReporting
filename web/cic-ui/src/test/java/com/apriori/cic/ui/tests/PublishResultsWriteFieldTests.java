package com.apriori.cic.ui.tests;

import com.apriori.bcs.api.enums.MachiningMode;
import com.apriori.cic.api.enums.CICAgentType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.PublishResultsWriteRule;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults.WriteFieldsTab;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PublishResultsWriteFieldTests extends TestBaseUI {
    private SoftAssertions softAssertions;
    private WorkFlowData workFlowData;
    private JobDefinition jobDefinitionData;
    private WorkflowHome workflowHome;
    private UserCredentials currentUser;
    private AgentPort agentPort;
    private String scenarioName;
    private PartData partData;
    private PublishResultsPart publishResultsPart;


    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        scenarioName = new GenerateStringUtil().generateScenarioName();
        partData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PARTIAL);
        workFlowData.setWorkflowName(workFlowData.getWorkflowName() + new GenerateStringUtil().getRandomNumbers());
        workFlowData.getQueryDefinitionsData().get(0).setFieldName(PlmTypeAttributes.PLM_PART_NUMBER.getCicGuiField());
        workFlowData.getQueryDefinitionsData().get(0).setFieldOperator(RuleOperatorEnum.EQUAL.getRuleOperator());
        workFlowData.getQueryDefinitionsData().get(0).setFieldValue(partData.getPlmPartNumber());
    }

    @Test
    @TestRail(id = {4092})
    @Description("Test the PLM Write Action Sub Tab UI")
    public void testWriteActionSubTab() {
        agentPort = CicApiTestUtil.getAgentPortData();
        WriteFieldsTab writeFieldsTab = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, partData.getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab()
            .clickAddRowBtn();
        softAssertions.assertThat(writeFieldsTab.verifyWritingRuleOption(PublishResultsWriteRule.CONSTANT)).isTrue();
        softAssertions.assertThat(writeFieldsTab.verifyWritingRuleOption(PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE)).isTrue();
        softAssertions.assertThat(writeFieldsTab.getSaveButtonElement().isEnabled()).isFalse();
        writeFieldsTab.selectWritingRule(PublishResultsWriteRule.CONSTANT);
        softAssertions.assertThat(writeFieldsTab.isFieldValueElementEnabled(PublishResultsWriteRule.CONSTANT)).isFalse();
        writeFieldsTab.selectWritingRule(PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE);
        softAssertions.assertThat(writeFieldsTab.isFieldValueElementEnabled(PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE)).isTrue();
        writeFieldsTab.deleteRow();
        softAssertions.assertThat(writeFieldsTab.getWriteFieldRows().size()).isEqualTo(1);
    }

    @Test
    @TestRail(id = {4093})
    @Description("A CI Connect Field can only be selected once per workflow")
    public void testDuplicateFields() {
        agentPort = CicApiTestUtil.getAgentPortData();
        WriteFieldsTab writeFieldsTab = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, partData.getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab()
            .addWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "");
        softAssertions.assertThat(writeFieldsTab.getSaveButtonElement().isEnabled()).isFalse();
        writeFieldsTab.deleteRow();
        softAssertions.assertThat(writeFieldsTab.getSaveButtonElement().isEnabled()).isTrue();
    }

    @Test
    @TestRail(id = {5013})
    @Description("Verify 'Workflow Generated Value' writing rule allowed only for UDAs that have been set as costing inputs")
    public void testVerifyCustomUdaFields() {
        agentPort = CicApiTestUtil.getAgentPortData();
        WriteFieldsTab writeFieldsTab = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, partData.getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_NUMBER, MappingRule.CONSTANT, "1234")
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab()
            .clickAddRowBtn();
        writeFieldsTab.selectCiConnectField(PlmTypeAttributes.PLM_CUSTOM_NUMBER);
        softAssertions.assertThat(writeFieldsTab.verifyWritingRuleOption(PublishResultsWriteRule.CONSTANT)).isTrue();
        softAssertions.assertThat(writeFieldsTab.verifyWritingRuleOption(PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE)).isTrue();
        writeFieldsTab.deleteRow().clickAddRowBtn();
        writeFieldsTab.selectCiConnectField(PlmTypeAttributes.PLM_CUSTOM_STRING);
        softAssertions.assertThat(writeFieldsTab.getWritingRuleElement().isEnabled()).isTrue();
    }

    @Test
    @TestRail(id = {4094})
    @Description("Test Data persistence between tabs and opening/closing dialog")
    public void testVerifyForDataPersists() {
        agentPort = CicApiTestUtil.getAgentPortData();
        WriteFieldsTab writeFieldsTab = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, partData.getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_NUMBER, MappingRule.CONSTANT, "1234")
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab()
            .addWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addWriteFieldsRow(PlmTypeAttributes.PLM_ANNUAL_VOLUME, PublishResultsWriteRule.CONSTANT, "2000")
            .clickPreviousButton()
            .clickCINotificationNextBtn()
            .selectAttachReportTab()
            .selectWriteFieldsTab();

        softAssertions.assertThat(writeFieldsTab.getWriteFieldRows().size()).isEqualTo(3);
        workflowHome = writeFieldsTab.clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        writeFieldsTab = workflowHome.selectScheduleTab()
            .selectWorkflow(workFlowData.getWorkflowName())
            .clickEditWorkflowBtn()
            .clickNextBtnInDetailsTab()
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab();
        softAssertions.assertThat(writeFieldsTab.getWriteFieldRows().size()).isEqualTo(2);
        workflowHome = writeFieldsTab.clickCancelButton();
        workflowHome.selectScheduleTab()
            .selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton()
            .clickConfirmAlertBoxDelete();
    }

    @Test
    @TestRail(id = {4744})
    @Description("If PLM type = File System then Part ID and aPriori Part Number are mandatory PLM write fields")
    public void testVerifyPlmFieldsForFileSystem() {
        agentPort = CicApiTestUtil.getAgentPortData(CICAgentType.FILE_SYSTEM);
        CostingInputsPart costingInputsPart = (CostingInputsPart) new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickWFDetailsNextBtn();

        WriteFieldsTab writeFieldsTab = costingInputsPart.clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab();
        softAssertions.assertThat(writeFieldsTab.getWritingRuleElement().isEnabled()).isFalse();
        softAssertions.assertThat(writeFieldsTab.verifyWritingRuleOption(PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE)).isTrue();
        softAssertions.assertThat(writeFieldsTab.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_ID, 1).isEnabled()).isFalse();
        softAssertions.assertThat(writeFieldsTab.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_APRIORI_PART_NUMBER, 1).isEnabled()).isFalse();
    }

    @Test
    @TestRail(id = {28918})
    @Description("Machining Mode is set by drop down when writing rule 'Constant' is selected")
    public void testVerifyPlmFieldsMachiningMode() {
        agentPort = CicApiTestUtil.getAgentPortData();
        WriteFieldsTab writeFieldsTab = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, partData.getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab()
            .clickAddRowBtn()
            .selectCiConnectField(PlmTypeAttributes.PLM_MACHINING_MODE)
            .selectWritingRule(PublishResultsWriteRule.CONSTANT);

        softAssertions.assertThat(writeFieldsTab.verifyFieldValueOption(MachiningMode.MAY_BE_MACHINED.getMachiningMode())).isTrue();
        softAssertions.assertThat(writeFieldsTab.verifyFieldValueOption(MachiningMode.NOT_MACHINED.getMachiningMode())).isTrue();

    }

    @Test
    @TestRail(id = {5080})
    @Description("Verify UI handles constant write back of multiselect UDA values")
    public void testVerifyPlmFieldsCustomMulti() {
        List<String> customMultiValues = Arrays.asList("Value 1", "Value 2", "Value 3");
        agentPort = CicApiTestUtil.getAgentPortData();
        WriteFieldsTab writeFieldsTab = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, partData.getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .selectWriteFieldsTab()
            .clickAddRowBtn()
            .selectCiConnectField(PlmTypeAttributes.PLM_CUSTOM_MULTI);
        List<String> actualValues = writeFieldsTab.getMultiFieldOptions();
        softAssertions.assertThat(actualValues.size()).isEqualTo(customMultiValues.size());
        softAssertions.assertThat(actualValues).isEqualTo(customMultiValues);
        writeFieldsTab.selectMultiSelectField(customMultiValues.get(0));
        softAssertions.assertThat(writeFieldsTab.getSelectedMultiFieldOptions().size()).isEqualTo(1);
        writeFieldsTab.deleteSelectedMultiFieldOptions(customMultiValues.get(0));
        softAssertions.assertThat(writeFieldsTab.getSelectedMultiFieldOptions().size()).isEqualTo(0);
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
    }
}
