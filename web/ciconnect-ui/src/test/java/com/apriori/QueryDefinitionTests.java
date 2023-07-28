package com.apriori;

import com.apriori.dataservice.TestDataService;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueryDefinitionTests extends TestBaseUI {

    private static WorkFlowData workFlowData;
    private UserCredentials currentUser = UserUtil.getUser();
    private WorkflowHome workflowHome;
    private QueryDefinitions queryDefinitions;

    private CicLoginPage cicLoginPage;

    public QueryDefinitionTests() {
        super();
    }

    @Before
    public void setup() {
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
    }

    @Test
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

        Assert.assertFalse("Verify Operator Value drop down doesn't displayed", queryDefinitions.verifyQueryDefinitionRuleOperatorDdl(0));
        Assert.assertFalse("Verify rule field doesn't displayed", queryDefinitions.verifyQueryDefinitionRuleValueTxt(0));

        Assert.assertEquals("Verify Rule Name drop down", true, queryDefinitions.getQueryDefinitionRuleNameDdl(0).isDisplayed());
        queryDefinitions.selectRuleNameFromDdl(workFlowData.getQueryDefinitionsData().get(0).getFieldName());
        Assert.assertTrue("Verify Operator Name drop down displayed", queryDefinitions.getQueryDefinitionRuleOperatorDdl(0).isDisplayed());
        Assert.assertTrue("Verify Operator Value drop down displayed", queryDefinitions.getQueryDefinitionRuleValueTxt(0).isDisplayed());
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
        Assert.assertTrue("Verify previous button in query definitions", detailsPart.getNameTextFieldElement().isDisplayed());
        queryDefinitions = (QueryDefinitions) detailsPart.clickWFDetailsNextBtn();
        Assert.assertTrue("Verify next button is displayed in Query definitions", queryDefinitions.getQueryDefinitionRuleNameDdl(0).isDisplayed());

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

        Assert.assertEquals("rule wasn't added", 1, queryDefinitions.getNumberOfRules());
        queryDefinitions.clickAddRuleButton();
        Assert.assertEquals("rule was added", 2, queryDefinitions.getNumberOfRules());
        queryDefinitions.deleteRule();
        Assert.assertEquals("rule was deleted", 1, queryDefinitions.getNumberOfRules());
        Assert.assertTrue("Add Group button is displayed", queryDefinitions.isGroupButtonDisplayed());

        DetailsPart detailsPart = queryDefinitions.clickPreviousBtn();
        detailsPart.selectWorkflowConnector("Teamcenter");
        queryDefinitions = (QueryDefinitions) detailsPart.clickWFDetailsNextBtn();
        Assert.assertFalse("Add Group button is not displayed for teamcenter", queryDefinitions.isGroupButtonDisplayed());

    }
}
