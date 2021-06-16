package com.cic.tests;

import com.apriori.features.NewWorkflowFeatures;
import com.apriori.features.WorkflowFeatures;
import com.apriori.pageobjects.LoginPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.validators.NewWorkflowValidator;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewWorkflowDetailsTests extends TestBase {
    private NewWorkflowFeatures newWorkflowFeatures;
    private List<String> workflowNames;
    private LoginPage loginPage;
    private NewWorkflowValidator validator;
    private WorkflowPage workflowPage;
    private WorkflowFeatures workflowFeatures;
    private Map<String, Object> values;
    private Map<String, Boolean> valuesB;

    private enum DetailFields {
        NAME,
        DESCRIPTION
    }

    public NewWorkflowDetailsTests() {
        super();
    }

    @Before
    public void setup() {
        loginPage = new LoginPage(driver);
        newWorkflowFeatures = new NewWorkflowFeatures(driver);
        workflowNames = new ArrayList<>();
        validator = new NewWorkflowValidator(driver);
        workflowPage = new WorkflowPage(driver);
        workflowFeatures = new WorkflowFeatures(driver);
        values = new HashMap<>();
        valuesB = new HashMap<>();
    }

    @After
    public void cleanup() {
        workflowPage.refreshPage();
        workflowNames.forEach(name -> {
            workflowPage.sortBy("Last Modified By")
                    .sortBy("Last Modified By");
            workflowFeatures.deleteWorklow(name);
        });
    }

    @Test
    @TestRail(testCaseId = {"4302"})
    @Description("Verify correct input fields present on Details Tab")
    public void validateInputFields() {
        gotoDetailsTab();
        valuesB = newWorkflowFeatures.doDetailsTabFieldInspection();
        validator.validateInputFields(valuesB);
    }

    @Test
    @TestRail(testCaseId = {"3991"})
    @Description("Details tab input validation ")
    public void inputValidation() {
        gotoDetailsTab();
        values = new HashMap<>();
        valuesB = new HashMap<>();

        // Test maximum character name string
        values = newWorkflowFeatures.doFieldInputInspection(DetailFields.NAME.toString());
        valuesB.put("maximum-name", (Boolean)values.get("workflowExists"));
        workflowNames.add(values.get("name").toString());
        workflowPage.newWorkflow();

        // Test minimum character name string
        values = newWorkflowFeatures.doFieldInputInspectionMin(DetailFields.NAME.toString());
        valuesB.put("minimum-name", (Boolean)values.get("workflowExists"));
        workflowNames.add(values.get("name").toString());
        workflowPage.newWorkflow();

        // Test maximum character name string with special characters
        values = newWorkflowFeatures.doFieldInputInspectionSpecial(DetailFields.NAME.toString());
        valuesB.put("maximum-special-name", (Boolean)values.get("workflowExists"));
        workflowNames.add(values.get("name").toString());
        workflowPage.newWorkflow();

        // Test maximum character description string
        values = newWorkflowFeatures.doFieldInputInspection(DetailFields.DESCRIPTION.toString());
        valuesB.put("maximum-description", (Boolean)values.get("workflowExists"));
        workflowNames.add(values.get("name").toString());
        workflowPage.newWorkflow();

        // Test name string with an unsupported special character
        values = newWorkflowFeatures.doFieldInputInspectionUnsupportedSpecial(DetailFields.NAME.toString());
        valuesB.put("unsupported-character-error", (Boolean)values.get("unsupported-character-error"));

        validator.validateFieldInput(valuesB);
    }

    @Test
    @TestRail(testCaseId = {"3993"})
    @Description("Test navigation by next (disabled until required fields are populated) and previous buttons")
    public void testNextButtonState() {
        gotoDetailsTab();
        valuesB = newWorkflowFeatures.checkNWFNextButtonState("DETAILS");
        validator.validateNextButton(valuesB);
    }

    @Test
    @TestRail(testCaseId = {"3593"})
    @Description("All schedule types formatted correctly in Schedule column of Schedule tab")
    public void testSchedulingFunctionality() {
        loginPage.login();

        values =  newWorkflowFeatures.checkScheduleFunctionality();
        workflowNames = (List)values.get("workflows");
        validator.validateScheduling(values);
    }

    private void gotoDetailsTab() {
        loginPage.login();
        workflowPage.newWorkflow();
    }
}
