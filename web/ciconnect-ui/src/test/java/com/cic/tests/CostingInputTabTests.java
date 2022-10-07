package com.cic.tests;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.LoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.StringUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public class CostingInputTabTests extends TestBase {
    private UserCredentials currentUser = UserUtil.getUser();
    WorkflowHome workflowHome;
    SoftAssertions softAssertions;

    public CostingInputTabTests() {
        super();
    }

    @Before
    public void setUpAndLogin() {
    }

    @Test
    @TestRail(testCaseId = {"4297", "4305"})
    @Description("Test Cost Inputs Tab Scenario Name field validation,  ability add and delete row")
    public void testValidateScenarioField() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(StringUtils.saltString(workFlowData.getWorkflowName()));
        softAssertions = new SoftAssertions();
        DetailsPart detailsPart = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn();

        log.info("###### Validate Scenario name with more than 255 characters ####");
        workFlowData.getCostingInputsData().get(0).setFieldValue(StringUtils.generateString(266));
        costingInputsPart = costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size());
        softAssertions.assertThat(costingInputsPart.getCiNextButton().isEnabled()).isEqualTo(false);

        log.info("###### Validate message when all rows are deleted ####");
        costingInputsPart.clickRowDeleteButton(0);
        softAssertions.assertThat(costingInputsPart.getNoCostingInputLabel().getText()).isEqualTo("No costing inputs defined.");

        log.info("###### Validate Scenario name with special characters ####");
        workFlowData.getCostingInputsData().get(0).setFieldValue(new GenerateStringUtil().getRandomString() + "@#$%");
        costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size());
        softAssertions.assertThat(costingInputsPart.getCiNextButton().isEnabled()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"4297"})
    @Description("Test Cost Inputs Tab Description validation")
    public void testValidateDescriptionField() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputNegativeTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(StringUtils.saltString(workFlowData.getWorkflowName()));
        softAssertions = new SoftAssertions();
        DetailsPart detailsPart = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn();

        log.info("###### Validate Description field  with more than 255 characters ####");
        costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size());
        softAssertions.assertThat(costingInputsPart.getCiNextButton().isEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"4304"})
    @Description("Verify correct fields are present on Costing Inputs tab")
    public void testVerifyCorrectFields() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputTestData.json",WorkFlowData.class);
        workFlowData.setWorkflowName(StringUtils.saltString(workFlowData.getWorkflowName()));
        String[] expectedConnecFields = new String[] {"Scenario Name", "Process Group", "Annual Volume"};
        String[] mappingRuleFields = new String[] {"Mapped from PLM", "Default If No PLM Value", "Constant"};
        softAssertions = new SoftAssertions();
        DetailsPart detailsPart = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn();

        log.info(String.format("###### Validate scenario field names >> %s << and mapping rules >> %s << ####", expectedConnecFields, mappingRuleFields));
        softAssertions.assertThat(costingInputsPart.isCiConnectFieldExist(Arrays.asList(expectedConnecFields))).isTrue();
        softAssertions.assertThat(costingInputsPart.isMappingRuleFieldExist(Arrays.asList(mappingRuleFields))).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"4308"})
    @Description("Verify Date picker field is displayed for Costing Inputs Custom Date field value")
    public void testVerifyConnectFieldCustomDateField() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CostingInputCustomDateTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(StringUtils.saltString(workFlowData.getWorkflowName()));
        softAssertions = new SoftAssertions();
        DetailsPart detailsPart = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn();

        costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size());
        softAssertions.assertThat(costingInputsPart.getCustomDateFieldValueElement().isDisplayed()).isTrue();

        softAssertions.assertAll();
    }
}
