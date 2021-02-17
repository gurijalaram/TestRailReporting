package com.apriori.validators;

import com.apriori.features.WorkflowFeatures;
import com.apriori.pageobjects.WorkflowPage;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WorkflowValidator {
    private final Logger logger = LoggerFactory.getLogger(WorkflowValidator.class);

    private WorkflowPage workflowPage;

    public WorkflowValidator(WebDriver driver) {
        workflowPage = new WorkflowPage(driver);
    }

    public void validateCreatedWorkflow(Map<String, Object> values) {
        Assert.assertEquals("New Workflow popup header is incorrect", WorkflowFeatures.NEW_WORKFLOW_HEADER,
                (String)values.get("label"));
        Assert.assertTrue("Workflow was not created", (boolean)values.get("workflowExists"));
    }

    public void validateEditedWorkflow(Map<String, Object> values) {
        Assert.assertEquals("Edit Workflow modal header is incorrect", WorkflowFeatures.EDIT_WORKFLOW_HEADER,
                (String)values.get("label"));
        Assert.assertTrue("Workflow was not edited", (boolean)values.get("workflowExists"));
    }

    public void validateDeletedWorkflow(Map<String, Object> values) {
        Assert.assertEquals("Delete workflow modal header is incorrect", WorkflowFeatures.DELETE_HEADER_TEXT,
                (String)values.get("header"));
        Assert.assertEquals("Delete workflow message is incorrect", WorkflowFeatures.DELETE_MESSAGE_TEXT,
                (String)values.get("message"));
        Assert.assertFalse("Workflow was not deleted", (boolean)values.get("workflowExists"));
    }

    public void validateButtonStates(Map<String, Object> values, boolean expectedValue) {
        Assert.assertEquals("Edit button was enabled", expectedValue, (boolean)values.get("editButtonEnabled"));
        Assert.assertEquals("Delete button was enable", expectedValue, (boolean)values.get("deleteButtonEnabled"));
        Assert.assertEquals("Invoke button was enabled", expectedValue, (boolean)values.get("invokeButtonEnabled"));
    }

    public void validatePopupDisplayed(Map<String, Object> values, boolean expectedValue) {
        Assert.assertEquals("Edit popup is displayed", expectedValue, (boolean)values.get("editPopupDisplayed"));
        Assert.assertEquals("Delete popup is displayed", expectedValue, (boolean)values.get("deletePopupDisplayed"));;
    }

    public void validateDefaultWorkflowOrdering(Map<String, Object> values) {
        String firstWorkflowName = workflowPage.getRowName((int)values.get("numericNameIndex"));
        String secondWorkflowName = workflowPage.getRowName((int)values.get("upperNameIndex"));
        String thirdWorkflowName = workflowPage.getRowName((int)values.get("lowerNameIndex"));

        Assert.assertEquals(firstWorkflowName, values.get("numericName"));
        Assert.assertEquals(secondWorkflowName, values.get("upperName"));
        Assert.assertEquals(thirdWorkflowName, values.get("lowerName"));
    }
}
