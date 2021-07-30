package com.apriori.validators;

import com.apriori.features.WorkflowFeatures;
import com.apriori.pageobjects.WorkflowPage;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.List;
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
     * Validates default ordering of workflows
     *
     * @param values
     */
    public void validateDefaultWorkflowOrdering(Map<String, Object> values) {
        String firstWorkflowName = workflowPage.getRowValue((int)values.get("numericIndex"),
                WorkflowPage.Field.NAME);
        String secondWorkflowName = workflowPage.getRowValue((int)values.get("upperIndex"),
                WorkflowPage.Field.NAME);
        String thirdWorkflowName = workflowPage.getRowValue((int)values.get("lowerIndex"),
                WorkflowPage.Field.NAME);

        Assert.assertEquals(values.get("numericName"), firstWorkflowName);
        Assert.assertEquals(values.get("upperName"), secondWorkflowName);
        Assert.assertEquals(values.get("lowerName"), thirdWorkflowName);
        Assert.assertTrue((int)values.get("numericIndex") < (int)values.get("upperIndex"));
        Assert.assertTrue((int)values.get("upperIndex") < (int)values.get("lowerIndex"));
    }

    /**
     * Validates the workflow display functionality
     *
     * @param values
     */
    public void validateWorkflowListSorting(Map<String, Object> values) {
        Assert.assertEquals("Names not sorted ascedning by default", (int)values.get("upper-before-click-index"),
                -1);
        Assert.assertNotEquals("Names not sorted ascedning by default", (int)values.get("lower-before-click-index"),
                -1);
        Assert.assertEquals("Names not sorted ascedning by default", (int)values.get("upper-first-click-index"),
                -1);
        Assert.assertNotEquals("Names not sorted ascedning by default", (int)values.get("lower-first-click-index"),
                -1);
        Assert.assertEquals("Names not sorted ascedning by default", (int)values.get("lower-second-click-index"),
                -1);
        Assert.assertNotEquals("Names not sorted ascedning by default", (int)values.get("upper-second-click-index"),
                -1);

        String[] headers = workflowPage.getExpectedWorkflowHeaders();
        List<String> actualHeaders = (List<String>)values.get("workflowListHeaders");
        for (int idx = 0; idx < headers.length; idx++) {
            Assert.assertTrue(String.format("%s was not present as a header",
                    headers[idx]), actualHeaders.contains(headers[idx]));
        }
    }

    /**
     * Validate schedule tab pagination
     *
     * @param values
     */
    public void validateSchedulePagination(Map<String, Object> values, Map<String, Integer> valuesI) {
        Assert.assertEquals("Displayed Pagesize was incorrect", Constants.DEFAULT_PAGE_SIZE, (int)valuesI.get(
                "pageSize"));
        Assert.assertEquals("Number of displayed workflows was incorrect", Constants.DEFAULT_PAGE_SIZE,
                (int)valuesI.get("displayedWorkflows"));
        Assert.assertEquals("Default row range is incorrect", Constants.DEFAULT_ROW_RANGE, values.get("rowRange").toString());
        Assert.assertEquals("Next row range is incorrect", Constants.NEXT_ROW_RANGE,
                values.get("nextRowRange").toString());
        Assert.assertEquals("Previous row range is incorrect", Constants.DEFAULT_ROW_RANGE,
                values.get("previousRowRange").toString());
        Assert.assertEquals("Beginning row range is incorrect", Constants.DEFAULT_ROW_RANGE, values.get(
                "beginningRowRange"));
        Assert.assertEquals("Updated pagesize is incorrect", 5, (int)valuesI.get("changedMaxPageSize"));
        Assert.assertTrue("Number of updated displayed workflows is greater than 100",
                valuesI.get("displayedWorkflowsUpdated") <= 5);

    }
}
