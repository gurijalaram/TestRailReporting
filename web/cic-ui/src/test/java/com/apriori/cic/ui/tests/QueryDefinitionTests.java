package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class QueryDefinitionTests extends TestBaseUI {

    private static WorkFlowData workFlowData;
    private UserCredentials currentUser = UserUtil.getUser();
    private WorkflowHome workflowHome;
    private QueryDefinitions queryDefinitions;

    private CicLoginPage cicLoginPage;

    public QueryDefinitionTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {4303})
    @Description("Test input fields functionality")
    public void testInputFields() {
        queryDefinitions = (QueryDefinitions) new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        assertFalse(queryDefinitions.verifyQueryDefinitionRuleOperatorDdl(0), "Verify Operator Value drop down doesn't displayed");
        assertFalse(queryDefinitions.verifyQueryDefinitionRuleValueTxt(0), "Verify rule field doesn't displayed");

        assertTrue(queryDefinitions.getQueryDefinitionRuleNameDdl(0).isDisplayed(), "Verify Rule Name drop down");
        queryDefinitions.selectRuleNameFromDdl(workFlowData.getQueryDefinitionsData().get(0).getFieldName());
        assertTrue(queryDefinitions.getQueryDefinitionRuleOperatorDdl(0).isDisplayed(), "Verify Operator Name drop down displayed");
        assertTrue(queryDefinitions.getQueryDefinitionRuleValueTxt(0).isDisplayed(), "Verify Operator Value drop down displayed");
    }

    @Test
    @TestRail(id = {3957})
    @Description("Test navigation button states")
    public void testNavigationButtons() {
        queryDefinitions = (QueryDefinitions) new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        DetailsPart detailsPart = queryDefinitions.clickPreviousBtn();
        assertTrue(detailsPart.getNameTextFieldElement().isDisplayed(), "Verify previous button in query definitions");
        queryDefinitions = (QueryDefinitions) detailsPart.clickWFDetailsNextBtn();
        assertTrue(queryDefinitions.getQueryDefinitionRuleNameDdl(0).isDisplayed(), "Verify next button is displayed in Query definitions");

    }

    @Test
    @TestRail(id = {4306, 3958})
    @Description("Verify query widget is correct for each possible PLM System selection")
    public void testRulesListFunctionality() {
        queryDefinitions = (QueryDefinitions) new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        assertEquals(1, queryDefinitions.getNumberOfRules(), "rule wasn't added");
        queryDefinitions.clickAddRuleButton();
        assertEquals(2, queryDefinitions.getNumberOfRules(), "rule was added");
        queryDefinitions.deleteRule();
        assertEquals(1, queryDefinitions.getNumberOfRules(), "rule was deleted");
        assertTrue(queryDefinitions.isGroupButtonDisplayed(), "Add Group button is displayed");

        DetailsPart detailsPart = queryDefinitions.clickPreviousBtn();
        detailsPart.selectWorkflowConnector("Teamcenter");
        queryDefinitions = (QueryDefinitions) detailsPart.clickWFDetailsNextBtn();
        assertFalse(queryDefinitions.isGroupButtonDisplayed(), "Add Group button is not displayed for teamcenter");

    }
}
