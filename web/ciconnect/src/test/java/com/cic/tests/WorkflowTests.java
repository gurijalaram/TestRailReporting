package com.cic.tests;

import com.apriori.features.WorkflowFeatures;
import com.apriori.pageobjects.DeleteWorkflowPage;
import com.apriori.pageobjects.LoginPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.validators.WorkflowValidator;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowTests  extends TestBase {
    private boolean skipDeletion;
    private WorkflowFeatures workflowFeatures;
    private List<String> workflowNames;
    private WorkflowValidator validator;
    private LoginPage loginPage;
    private WorkflowPage workflowPage;
    private Map<String, Object> values;
    private Map<String, Integer> valuesI;

    public WorkflowTests() {
        super();
    }

    @Before
    public void setup() {
        workflowFeatures = new WorkflowFeatures(driver);
        workflowNames = new ArrayList<>();
        validator = new WorkflowValidator(driver);
        workflowPage = new WorkflowPage(driver);
        values = new HashMap<>();
        valuesI = new HashMap<>();

        skipDeletion = false;
    }

    @After
    public void cleanup() {
        if (!skipDeletion) {
            workflowNames.forEach(name -> workflowFeatures.deleteWorklow(name));
        }
    }

    @Test
    @TestRail(testCaseId = {"4109", "3586", "3588", "35877"})
    @Description("Test creating, editing and deletion of a worflow")
    public void testWorkflowCreateEditDelete() {
        workflowPage = new WorkflowPage(driver);
        loginPage = new LoginPage(driver);
        loginPage.login();

        /** Create Workflow **/
        Map<String, Object> values = workflowFeatures.createWorkflow();
        workflowNames.add((String)values.get("workflowName"));
        validator.validateCreatedWorkflow(values);

        /** Edit Workflow **/
        String workflowName = (String)values.get("workflowName");
        Map<String, Object> editedValues = workflowFeatures.editWorklow(workflowName);
        workflowNames.clear();
        workflowNames.add((String)editedValues.get("workflowName"));
        validator.validateEditedWorkflow(editedValues);

        /** Delete Workflow **/
        String workflowToDelete = (String)editedValues.get("workflowName");
        Map<String, Object> deletedValues = workflowFeatures.deleteWorklow(workflowToDelete);
        validator.validateDeletedWorkflow(deletedValues);

        // Since deletion is part of the test scenario and the prior features have passed, there's no need to attempt
        // to delete again
        skipDeletion = true;
    }

    @Test
    @TestRail(testCaseId = {"4273"})
    @Description("Test the state of the edit, delete and invoke buttons on the WF schedule screen. With a WF selected" +
            "and with no WF selected")
    public void testButtonState() {
        workflowPage = new WorkflowPage(driver);
        loginPage = new LoginPage(driver);
        loginPage.login();

        Map<String, Object> values = workflowFeatures.getButtonStates();
        validator.validateButtonStates(values, false);

        workflowPage.clickOnFirstWorkflowRow();

        values = workflowFeatures.getButtonStates();
        validator.validateButtonStates(values, true);
        validator.validatePopupDisplayed(values, true);
    }

    @Test
    @TestRail(testCaseId = {"3590"})
    @Description("Test default sorting of the WF schedule table")
    public void testDefaultSorting() {
        workflowPage = new WorkflowPage(driver);
        loginPage = new LoginPage(driver);
        loginPage.login();

        Map<String, Object> values = workflowFeatures.defaultSorting();
        validator.validateDefaultWorkflowOrdering(values);
        workflowNames = (ArrayList<String>) values.get("workflows");
    }

    @Test
    @TestRail(testCaseId = {"3594"})
    @Description("Schedule tab paginator functions correctly")
    public void testPaginationFunctionality() {
        workflowPage = new WorkflowPage(driver);
        loginPage = new LoginPage(driver);
        loginPage.login();

        valuesI = workflowFeatures.inspectPageSizeSettings();
        values = workflowFeatures.inspectSchedulePaginator();
        validator.validateSchedulePagination(values, valuesI);;
    }

    @Test
    @TestRail(testCaseId = {"3809"})
    @Description("Test sorting of workflowfs by name in the schedule table")
    public void testWorkflowListSorting() {
        workflowPage = new WorkflowPage(driver);
        loginPage = new LoginPage(driver);
        loginPage.login();

        values = workflowFeatures.checkWorkflowListSorting();
        workflowFeatures.deleteWorklow(values.get("lower-name").toString());
        workflowPage.clickOnFirstColumn();
        workflowPage.clickOnFirstColumn();
        deleteWorkflow(values.get("upper-name").toString());

        validator.validateWorkflowListSorting(values);
    }

    private void deleteWorkflow(String workflow) {
        DeleteWorkflowPage deleteWorkflowPage = new DeleteWorkflowPage(driver);
        workflowPage.selectWorkflow(workflow);
        workflowPage.clickDeleteButton();
        deleteWorkflowPage.deleteWorkflow();
        workflowPage.refreshPage();

    }
}
