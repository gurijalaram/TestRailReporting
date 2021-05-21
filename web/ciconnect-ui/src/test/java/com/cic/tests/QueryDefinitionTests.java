package com.cic.tests;

import com.apriori.features.QueryDefinitionFeatures;
import com.apriori.pageobjects.LoginPage;
import com.apriori.pageobjects.NewWorkflowPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.validators.QueryDefinitionsValidator;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class QueryDefinitionTests extends TestBase {
    private QueryDefinitionsValidator validator;
    private QueryDefinitionFeatures queryDefinitionFeatures;
    private LoginPage loginPage;
    private WorkflowPage workflowPage;
    private NewWorkflowPage newWorkflowPage;
    private Map<String, Object> values;
    private Map<String, Boolean> valuesB;

    public QueryDefinitionTests() {
        super();
    }

    @Before
    public void setup() {
        validator = new QueryDefinitionsValidator();
        queryDefinitionFeatures = new QueryDefinitionFeatures(driver);
        loginPage = new LoginPage(driver);
        workflowPage = new WorkflowPage(driver);
        newWorkflowPage = new NewWorkflowPage(driver);
        values = new HashMap<>();
        valuesB = new HashMap<>();
    }


    @Test
    @TestRail(testCaseId = {"3597"})
    @Description("Test input fields functionality")
    public void testInputFields() {
        gotoQueryDefinitions();
        valuesB = queryDefinitionFeatures.checkInputtFields();
        validator.validateInputFields(valuesB);
    }

    @Test
    @TestRail(testCaseId = {"3598"})
    @Description("Test navigation button states")
    public void testNavigationButtons() {
        gotoQueryDefinitions();
        valuesB = queryDefinitionFeatures.checkNavigation();
        validator.validateNavigation(valuesB);
    }

    @Test
    @TestRail(testCaseId = {"4303"})
    @Description("Validate main functionality exists to define queries")
    public void testRulesListFunctionality() {
        gotoQueryDefinitions();
        values = queryDefinitionFeatures.checkRulesListFunctionality();
        validator.validateRulesList(values);
    }

    private void gotoQueryDefinitions() {
        loginPage.login();
        workflowPage.newWorkflow();
        newWorkflowPage.gotoQueryDefinitions("0 0 0 0 0 Query-Definition Test");
    }
}
