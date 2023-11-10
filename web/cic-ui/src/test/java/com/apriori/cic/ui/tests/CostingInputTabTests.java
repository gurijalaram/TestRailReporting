package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@Slf4j
public class CostingInputTabTests extends WorkflowTestUtil {

    private UserCredentials currentUser;
    private AgentPort agentPort;
    private WorkflowHome workflowHome;
    private SoftAssertions softAssertions;
    private WorkFlowData workFlowData;
    private String plmPartNumber;


    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        agentPort = CicApiTestUtil.getAgentPortData();
        plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();
        currentUser = UserUtil.getUser();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {4297, 4305})
    @Description("Test Cost Inputs Tab Scenario Name field validation,  ability add and delete row")
    public void testValidateScenarioField() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        CostingInputsPart costingInputsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn();

        log.info("###### Validate Scenario name with more than 255 characters ####");
        costingInputsPart = costingInputsPart.addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, GenerateStringUtil.generateString(266));
        softAssertions.assertThat(costingInputsPart.getCiNextButton().isEnabled()).isFalse();

        log.info("###### Validate message when all rows are deleted ####");
        costingInputsPart.clickRowDeleteButton(0);
        softAssertions.assertThat(costingInputsPart.getNoCostingInputLabel().getText()).isEqualTo("No costing inputs defined.");

        log.info("###### Validate Scenario name with special characters ####");
        costingInputsPart.addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, new GenerateStringUtil().getRandomString() + "@#$%");
        softAssertions.assertThat(costingInputsPart.getCiNextButton().isEnabled()).isTrue();
    }

    @Test
    @TestRail(id = {4297})
    @Description("Test Cost Inputs Tab Description validation")
    public void testValidateDescriptionField() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputNegativeTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        CostingInputsPart costingInputsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn();

        log.info("###### Validate Description field  with more than 255 characters ####");
        costingInputsPart = costingInputsPart.addCostingInputRow(PlmTypeAttributes.PLM_DESCRIPTION, MappingRule.CONSTANT, GenerateStringUtil.generateString(266));
        softAssertions.assertThat(costingInputsPart.getCiNextButton().isEnabled()).isFalse();
    }

    @Test
    @TestRail(id = {4304})
    @Description("Verify correct fields are present on Costing Inputs tab")
    public void testVerifyCorrectFields() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        String[] expectedConnecFields = new String[] {"Scenario Name", "Process Group", "Annual Volume"};
        String[] mappingRuleFields = new String[] {"Mapped from PLM", "Default If No PLM Value", "Constant"};
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        CostingInputsPart costingInputsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn();

        log.info(String.format("###### Validate scenario field names >> %s << and mapping rules >> %s << ####", expectedConnecFields, mappingRuleFields));
        softAssertions.assertThat(costingInputsPart.isCiConnectFieldExist(Arrays.asList(expectedConnecFields))).isTrue();
        softAssertions.assertThat(costingInputsPart.isMappingRuleFieldExist(Arrays.asList(mappingRuleFields))).isTrue();
    }

    @Test
    @TestRail(id = {4308})
    @Description("Verify Date picker field is displayed for Costing Inputs Custom Date field value")
    public void testVerifyConnectFieldCustomDateField() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputCustomDateTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        CostingInputsPart costingInputsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn();

        costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size());
        softAssertions.assertThat(costingInputsPart.getCustomDateFieldValueElement().isDisplayed()).isTrue();
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
    }
}
