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
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkflowTests  extends TestBase {
    private boolean skipDeletion;
    private WorkflowFeatures workflowFeatures;
    private List<String> workflowNames;
    private WorkflowValidator validator;
    private LoginPage loginPage;
    private WorkflowPage workflowPage;

    public WorkflowTests() {
        super();
    }

    @Before
    public void setup() {
        loginPage = new LoginPage(driver);
        workflowFeatures = new WorkflowFeatures(driver);
        workflowNames = new ArrayList<>();
        validator = new WorkflowValidator(driver);
        workflowPage = new WorkflowPage(driver);

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
        loginPage.login();

        /** Create Workflow **/
        Map<String, Object> values = workflowFeatures.creteWorkflow();
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
        loginPage.login();

        Map<String, Object> values = workflowFeatures.defaultSorting();
        validator.validateDefaultWorkflowOrdering(values);

    }

    private void deleteWorkflows(ArrayList<String> workflows, WorkflowPage workflowPage, DeleteWorkflowPage deleteWorkflowPage) {
        for (String workflow : workflows) {
            workflowPage.selectWorkflow(workflow);
            workflowPage.clickDeleteButton();
            deleteWorkflowPage.deleteWorkflow();
            workflowPage.refeshPage(workflowPage);
        }
    }
}
