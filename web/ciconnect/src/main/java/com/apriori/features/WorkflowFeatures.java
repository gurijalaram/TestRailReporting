package com.apriori.features;

import com.apriori.pageobjects.DeleteWorkflowPage;
import com.apriori.pageobjects.EditWorkflowPage;
import com.apriori.pageobjects.NewWorkflowPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowFeatures {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowFeatures.class);

    public static String DELETE_HEADER_TEXT = "Are you sure?";
    public static String DELETE_MESSAGE_TEXT = "Do you really want to delete this workflow? This action cannot be undone.";
    public static String NEW_WORKFLOW_HEADER = "Create a New Workflow";
    public static String EDIT_WORKFLOW_HEADER = "Edit Workflow";

    private WebDriver driver;
    private WorkflowPage workflowPage;
    private NewWorkflowPage newWorkflowPage;
    private EditWorkflowPage editWorkflowPage;
    private DeleteWorkflowPage deleteWorkflowPage;
    private PageUtils pageUtils;
    private UIUtils uiUtils;
    private Map<String, Object> values;
    private Map<String, Boolean> valuesB;

    private enum WorkflowAction {
        CREATE,
        EDIT
    }

    public WorkflowFeatures(WebDriver driver) {
        this.driver = driver;
        this.workflowPage = new WorkflowPage(this.driver);
        this.newWorkflowPage = new NewWorkflowPage(driver);
        this.editWorkflowPage = new EditWorkflowPage(driver);
        this.deleteWorkflowPage = new DeleteWorkflowPage(driver);
        this.pageUtils = PageUtils.getInstance(driver);
        this.uiUtils = new UIUtils();
    }

    /**
     * Create new workflow with default values
     *
     * @return value map of field states and values
     */
    public Map<String, Object>  createWorkflow() {
        return manageWorkflow(Constants.DEFAULT_WORKFLOW_NAME, WorkflowAction.CREATE, null, null,1);
    }

    /**
     * Create a new workflow
     *
     * @param name workflow name
     * @param description workflow description
     * @return value map of field states and values
     */
    public Map<String, Object>  createWorkflow(String name, String description) {
        return manageWorkflow(Constants.DEFAULT_WORKFLOW_NAME, WorkflowAction.CREATE, null,null, 1);
    }

    /**
     * Edit an existing workflow
     *
     * @param workflowName New workflow name
     * @return value map of field states and values
     */
    public Map<String, Object> editWorklow(String workflowName) {
        return manageWorkflow(workflowName, WorkflowAction.EDIT, null, Constants.DEFAULT_EDITED_WORKFLOW_NAME, null);
    }

    /**
     * Manage CRUD operations on new or existing workflow
     *
     * @param workflowName New workflow name or existing workflow to select
     * @param action Create or Edit
     * @param description Workflow description
     * @param newWorkflowName New workflow name
     * @param iteration The number of workflows created at the time this method is called
     * @return value map of field states and values
     */
    public Map<String, Object> manageWorkflow(String workflowName, WorkflowAction action, String description,
                                              String newWorkflowName, Integer iteration) {
        values = new HashMap();
        String label;
        String name;
        boolean exists;

        switch (action) {
            case CREATE:
                workflowPage.newWorkflow();
                label = newWorkflowPage.getLabel();
                name = UIUtils.saltString(workflowName);
                newWorkflowPage.createNewWorkflow(name, iteration);
                break;
            case EDIT:
                workflowPage.selectWorkflow(workflowName);
                workflowPage.editWorkflow();
                label = editWorkflowPage.getLabel();
                name = UIUtils.saltString(newWorkflowName);
                editWorkflowPage.editWorkflow("NAME", name);
                break;
            default:
                logger.info("Invalid workflow action");
                return null;
        }

        workflowPage.refreshPage();
        //name = correctWorkflowNameSpacing(name);
        exists = workflowPage.workflowExists(name);

        values.put("label", label);
        values.put("workflowExists", exists);
        values.put("workflowName", name);

        return values;
    }

    /**
     * Delete workflow
     *
     * @param workflowName
     * @return value map of field states and values
     */
    public Map<String, Object> deleteWorklow(String workflowName) {
        Map<String, Object> values = new HashMap();
        workflowPage.refreshPage();

        workflowPage.selectWorkflow(workflowName);
        workflowPage.clickDeleteButton();
        values.put("header", deleteWorkflowPage.getModalHeader());
        values.put("message", deleteWorkflowPage.getModalMessage());

        deleteWorkflowPage.deleteWorkflow();
        workflowPage.refreshPage();

        boolean exists = workflowPage.workflowExists(workflowName);
        values.put("workflowExists", exists);

        return values;
    }

    /**
     * Checks the button states on workflow schedule page
     *
     * @return value map of field states and values
     */
    public Map<String, Object> getButtonStates() {
        values = new HashMap();
        boolean isEnabled;
        boolean popupIsDisplayed;

        isEnabled = workflowPage.getButtonState("EDIT");
        values.put("editButtonEnabled", isEnabled);
        if (isEnabled) {
            workflowPage.clickEditButton();
            popupIsDisplayed = editWorkflowPage.modalExists();
            values.put("editPopupDisplayed", popupIsDisplayed);
            editWorkflowPage.cancelEdit();
        }

        isEnabled = workflowPage.getButtonState("INVOKE");
        values.put("invokeButtonEnabled", isEnabled);

        isEnabled = workflowPage.getButtonState("DELETE");
        values.put("deleteButtonEnabled", isEnabled);
        if (isEnabled) {
            workflowPage.clickDeleteButton();
            popupIsDisplayed = deleteWorkflowPage.modalExists();
            values.put("deletePopupDisplayed", popupIsDisplayed);
        }

        return values;
    }

    /**
     * Check workflow table's default sorting
     *
     * @return
     */
    public Map<String, Object> defaultSorting() {
        List<String> workflows = new ArrayList();
        int iteration = 1;
        values = new HashMap<>();
        values.put("lowerNameIndex", 3);
        values.put("upperNameIndex", 2);
        values.put("numericNameIndex", 1);

        workflowPage.newWorkflow();
        String lowerName = UIUtils.saltString(Constants.DEFAULT_NAME_LOWER_CASE);
        newWorkflowPage.createNewWorkflow(lowerName, iteration);
        workflowPage.refreshPage();
        workflowPage.refreshWorkflowTable();
        values.put("lowerName", lowerName);
        workflows.add(lowerName);
        iteration++;

        workflowPage.newWorkflow();
        String numericName = UIUtils.saltString(Constants.DEFAULT_NAME_WITH_NUMBER);
        newWorkflowPage.createNewWorkflow(numericName, iteration);
        workflowPage.refreshPage();
        workflowPage.refreshWorkflowTable();
        values.put("numericName", numericName);
        workflows.add(numericName);
        iteration++;

        workflowPage.newWorkflow();
        String upperName = UIUtils.saltString(Constants.DEFAULT_NAME_UPPER_CASE);
        newWorkflowPage.createNewWorkflow(upperName, iteration);
        workflowPage.refreshPage();
        workflowPage.refreshWorkflowTable();
        values.put("upperName", upperName);
        workflows.add(upperName);
        workflowPage.refreshPage();

        values.put("workflows", workflows);
        return values;
    }

    private String correctWorkflowNameSpacing(String workflowName) {
        /* Need to modify the name because CIC removes multiple whitespaces and replaces it with
           a single space. The workflow name uses triple spaces to assure that the new workflow is at the top of the
           list
         */
        return workflowName.replace("   ", " ");
    }

    private boolean getPopoup(WebElement webElement) {
        workflowPage.clickEditButton();
        boolean popupExists = editWorkflowPage.modalExists();
        editWorkflowPage.cancelEdit();
        return popupExists;
    }
}
