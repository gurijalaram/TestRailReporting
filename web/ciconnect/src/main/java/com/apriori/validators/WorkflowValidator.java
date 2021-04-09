package com.apriori.validators;

import com.apriori.features.WorkflowFeatures;
import com.apriori.pageobjects.WorkflowPage;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WorkflowValidator {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowValidator.class);

    private WorkflowPage workflowPage;

    public WorkflowValidator(WebDriver driver) {
        workflowPage = new WorkflowPage(driver);
    }

    /**
     * Validate a new workflow has been created
     *
     * @param values
     */
    public void validateCreatedWorkflow(Map<String, Object> values) {
        Assert.assertEquals("New Workflow popup header is incorrect", WorkflowFeatures.NEW_WORKFLOW_HEADER,
                (String)values.get("label"));
        Assert.assertTrue("Workflow was not created", (boolean)values.get("workflowExists"));
    }

    /**
     * Validate the selected workflow has been edited
     *
     * @param values
     */
    public void validateEditedWorkflow(Map<String, Object> values) {
        Assert.assertEquals("Edit Workflow modal header is incorrect", WorkflowFeatures.EDIT_WORKFLOW_HEADER,
                (String)values.get("label"));
        Assert.assertTrue("Workflow was not edited", (boolean)values.get("workflowExists"));
    }

    /**
     * Validate the selected workflow has been deleted
     *
     * @param values
     */
    public void validateDeletedWorkflow(Map<String, Object> values) {
        Assert.assertEquals("Delete workflow modal header is incorrect", WorkflowFeatures.DELETE_HEADER_TEXT,
                (String)values.get("header"));
        Assert.assertEquals("Delete workflow message is incorrect", WorkflowFeatures.DELETE_MESSAGE_TEXT,
                (String)values.get("message"));
        Assert.assertFalse("Workflow was not deleted", (boolean)values.get("workflowExists"));
    }

    /**
     * Validates the appropriate enabled state of the buttons on the Workflow schedule page
     *
     * @param values a collection of the actual button states
     * @param expectedValue
     */
    public void validateButtonStates(Map<String, Object> values, boolean expectedValue) {
        Assert.assertEquals("Edit button was enabled", expectedValue, (boolean)values.get("editButtonEnabled"));
        Assert.assertEquals("Delete button was enable", expectedValue, (boolean)values.get("deleteButtonEnabled"));
        Assert.assertEquals("Invoke button was enabled", expectedValue, (boolean)values.get("invokeButtonEnabled"));
    }

    /**
     * Validates that the popup is displayed when appropriate
     *
     * @param values popup displayed state
     * @param expectedValue
     */
    public void validatePopupDisplayed(Map<String, Object> values, boolean expectedValue) {
        Assert.assertEquals("Edit popup is displayed", expectedValue, (boolean)values.get("editPopupDisplayed"));
        Assert.assertEquals("Delete popup is displayed", expectedValue, (boolean)values.get("deletePopupDisplayed"));;
    }

    /**
     * Validates default ordering of worflows
     *
     * @param values
     */
    public void validateDefaultWorkflowOrdering(Map<String, Object> values) {
        String firstWorkflowName = workflowPage.getRowName((int)values.get("numericNameIndex"));
        String secondWorkflowName = workflowPage.getRowName((int)values.get("upperNameIndex"));
        String thirdWorkflowName = workflowPage.getRowName((int)values.get("lowerNameIndex"));

        Assert.assertEquals(values.get("numericName"), firstWorkflowName);
        Assert.assertEquals(values.get("upperName"), secondWorkflowName);
        Assert.assertEquals(values.get("lowerName"), thirdWorkflowName);
    }
}
