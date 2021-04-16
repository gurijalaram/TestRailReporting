package com.apriori.validators;

import com.apriori.pageobjects.WorkflowPage;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NewWorkflowValidator {
    private static final Logger logger = LoggerFactory.getLogger(NewWorkflowValidator.class);

    private WorkflowPage workflowPage;

    public NewWorkflowValidator(WebDriver driver) {
        workflowPage = new WorkflowPage(driver);
    }

    /**
     * Validates that that expected fields are present on the DETAILS tab
     *
     * @param values
     */
    public void validateInputFields(Map<String, Boolean> values) {
        Assert.assertTrue("Name field doesn't exist on the Details tab", values.get("name"));
        Assert.assertTrue("Description field doesn't exist on the Details tab", values.get("description"));
        Assert.assertTrue("Connector dropdown doesn't exist on the Details tab", values.get("connector"));
        Assert.assertTrue("Minutes schedule doesn't exist on the Details tab", values.get("minutes"));
        Assert.assertTrue("Hourly schedule doesn't exist on the Details tab", values.get("hourly"));
        Assert.assertTrue("Daily schedule doesn't exist on the Details tab", values.get("daily"));
        Assert.assertTrue("Weekly schedule doesn't exist on the Details tab", values.get("weekly"));
        Assert.assertTrue("Monthly schedule doesn't exist on the Details tab", values.get("monthly"));
        Assert.assertTrue("Yearly schedule doesn't exist on the Details tab", values.get("yearly"));
    }

    /**
     * Validates string validation for input fields
     *
     * @param values
     */
    public void validateFieldInput(Map<String, Boolean> values) {
        Assert.assertTrue("Fail to create workflow with the maximum length name", values.get("maximum-name"));
        Assert.assertTrue("Fail to create workflow with the minimum length name", values.get("minimum-name"));
        Assert.assertTrue("Fail to create workflow with a name containing supported special characters", values.get(
                "maximum-special-name"));
        Assert.assertFalse("Successfully created workflow with a name containing unsupported characters", values.get(
                "maximum-unspecial-name"));
        Assert.assertTrue("Fail to create workflow with the maximum length description", values.get("maximum" +
                "-description"));
    }

    /**
     * Validates the NEXT button is only enabled if the NAME and CONNECTOR fields are filled out
     *
     * @param values
     */
    public void validateNextButton(Map<String, Boolean> values) {
        Assert.assertFalse("Next button was enabled after entering a name only", values.get("name-only"));
        Assert.assertFalse("Next button was enabled without selecting a connector", values.get("add-description"));
        Assert.assertTrue("Next button was disabled after entering a name, description & connector", values.get(
                "select-connector"));
    }

    /**
     * Validates scheduling functionality
     *
     * @param values
     */
    public void validateScheduling(Map<String, Object> values) {
        Assert.assertTrue("Workflow with minutes schedule was not created", (boolean)values.get("minutes-schedule" +
                "-workflow-exists"));
        Assert.assertTrue("Workflow with every hour schedule was not created", (boolean)values.get("hourly-every" +
                "-schedule-workflow-exists"));
        Assert.assertTrue("Workflow with hourly schedule was not created", (boolean)values.get("hourly-schedule" +
                "-workflow-exists"));
        Assert.assertTrue("Workflow with every day schedule was not created", (boolean)values.get("daily-every" +
                "-schedule-workflow-exists"));
        Assert.assertTrue("Workflow with daily schedule was not created", (boolean)values.get("daily-schedule" +
                "-workflow-exists"));
        Assert.assertTrue("Workflow with weekly schedule was not created", (boolean)values.get("weekly-schedule" +
                "-workflow-exists"));
        Assert.assertTrue("Workflow with every month schedule was not created", (boolean)values.get("monthly" +
                "-every-schedule-workflow-exists"));
        Assert.assertTrue("Workflow with monthly schedule was not created", (boolean)values.get("monthly-schedule" +
                "-workflow-exists"));
        Assert.assertTrue("Workflow with every year schedule was not created", (boolean)values.get("yearly-ever" +
                "-schedule-workflow-exists"));
        Assert.assertTrue("Workflow with yearly schedule was not created", (boolean)values.get("yearly-schedule" +
                "-workflow-exists"));
    }
}
