package com.apriori.features;

import com.apriori.pageobjects.DeleteWorkflowPage;
import com.apriori.pageobjects.EditWorkflowPage;
import com.apriori.pageobjects.LoginPage;
import com.apriori.pageobjects.NavBarPage;
import com.apriori.pageobjects.NewWorkflowPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.TableUtils;
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
    private LoginPage loginPage;
    private NavBarPage navBarPage;
    private WorkflowPage workflowPage;
    private NewWorkflowPage newWorkflowPage;
    private EditWorkflowPage editWorkflowPage;
    private DeleteWorkflowPage deleteWorkflowPage;
    private PageUtils pageUtils;
    private UIUtils uiUtils;
    private TableUtils tableUtils;
    private Map<String, Object> values;
    private Map<String, Boolean> valuesB;
    private Map<String, String> valuesS;

    private enum WorkflowAction {
        CREATE,
        EDIT
    }

    public WorkflowFeatures(WebDriver driver) {
        this.driver = driver;
        this.loginPage = new LoginPage(driver);
        this.navBarPage = new NavBarPage(driver);
        this.workflowPage = new WorkflowPage(this.driver);
        this.newWorkflowPage = new NewWorkflowPage(driver);
        this.editWorkflowPage = new EditWorkflowPage(driver);
        this.deleteWorkflowPage = new DeleteWorkflowPage(driver);
        this.pageUtils = new PageUtils(driver);
        this.uiUtils = new UIUtils();
        this.tableUtils = new TableUtils(driver);
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

        workflowPage.selectWorkflow(workflowName);
        workflowPage.clickDeleteButton();
        values.put("header", deleteWorkflowPage.getModalHeader());
        values.put("message", deleteWorkflowPage.getModalMessage());

        deleteWorkflowPage.deleteWorkflow();
        workflowPage.refreshPage();
        workflowPage.sortBy("Connector");

        boolean exists = workflowPage.workflowExists(workflowName);
        values.put("workflowExists", exists);

        return values;
    }

    /**
     * Delete workflow for a specific connector
     *
     * @return value map of field states and values
     */
    public void deleteWorklowByConnector() {
        boolean isNull = false;
        WebElement row;

        workflowPage.refreshPage();
        while (!isNull) {
            workflowPage.sortBy("Connector");

            row = workflowPage.selectWorkflowByConnector();
            if (row == null) {
                break;
            } else {
                row.click();
            }

            workflowPage.clickDeleteButton();
            deleteWorkflowPage.deleteWorkflow();
            workflowPage.refreshPage();
        }
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

        workflowPage.newWorkflow();
        String lowerName = UIUtils.saltString(Constants.DEFAULT_NAME_LOWER_CASE);
        newWorkflowPage.createNewWorkflow(lowerName, iteration);
        workflowPage.refreshPage();
        workflowPage.refreshWorkflowTable();
        workflowPage.refreshPage();
        values.put("lowerName", lowerName);
        workflows.add(lowerName);
        iteration++;

        workflowPage.newWorkflow();
        String numericName = UIUtils.saltString(Constants.DEFAULT_NAME_WITH_NUMBER);
        newWorkflowPage.createNewWorkflow(numericName, iteration);
        workflowPage.refreshPage();
        workflowPage.refreshWorkflowTable();
        workflowPage.refreshPage();
        values.put("numericName", numericName);
        workflows.add(numericName);
        iteration++;

        workflowPage.newWorkflow();
        String upperName = UIUtils.saltString(Constants.DEFAULT_NAME_UPPER_CASE);
        newWorkflowPage.createNewWorkflow(upperName, iteration);
        workflowPage.refreshPage();
        workflowPage.refreshWorkflowTable();
        workflowPage.refreshPage();
        values.put("upperName", upperName);
        workflows.add(upperName);
        workflowPage.refreshPage();


        values.put("upperIndex", workflowPage.getWorkflowIndex(upperName));
        values.put("numericIndex", workflowPage.getWorkflowIndex(numericName));
        values.put("lowerIndex", workflowPage.getWorkflowIndex(lowerName));

        values.put("workflows", workflows);
        return values;
    }

    /**
     * Retrieve the last modified user after a workflow has been updated
     *
     * @return The last modified user
     */
    public Map<String, String> lastModifiedFieldUpdated() {
        valuesS = new HashMap<>();

        Map<String, Object> cwfValues = createWorkflow();
        String preModifiedName = workflowPage.getRowValue(1, WorkflowPage.Field.LAST_MODIFIED);
        editWorkflowPage.editWorkflow("Description", "Edited Workflow");
        navBarPage.logOut();
        loginPage.login(Constants.SECOND_USER_EMAIL, Constants.SECOND_USER_PASSWORD);

        String postModifiedName;
        if (workflowPage.workflowListExists()) {
            postModifiedName = workflowPage.getRowValue(1, WorkflowPage.Field.LAST_MODIFIED);
            valuesS.put("postModifiedName", postModifiedName);
            valuesS.put("preModifiedName", preModifiedName);
            valuesS.put("workflowName", cwfValues.get("workflowName").toString());
            return valuesS;
        }

        return null;
    }

    /**
     * Inspect the Schedule page paginator
     *
     * @return Paginator values and state
     */
    public Map<String, Object> inspectSchedulePaginator() {
        workflowPage.refreshPage();
        values = new HashMap<>();
        values.put("rowRange", workflowPage.getRowRange());

        String[] totalSplit = workflowPage.getRowTotal().split("of");
        Integer rowTotal = Integer.parseInt(totalSplit[totalSplit.length - 1].trim());
        values.put("rowTotal", rowTotal);

        String[] firstPageRange = workflowPage.getRowRange().split("-");
        Integer firstPageRowCount = Integer.parseInt(firstPageRange[1].trim());

        if (firstPageRowCount != 25) {
            Integer rowCount = workflowPage.getRowCount();
            values.put("firstPageRowCount", rowCount);

        } else {
            values.put("firstPageRowCount", 25);

            workflowPage.pageNext();
            Integer seecondPageRowCount = Integer.parseInt(PropertiesContext.get("${env}.ci-connect.second_page_starting_range_number"))
                    + workflowPage.getRowCount();
            values.put("nextRowRange", workflowPage.getRowRange());
            values.put("pageNextRowCount", seecondPageRowCount);

            workflowPage.pageBack();
            values.put("previousRowRange", workflowPage.getRowRange());

            workflowPage.refreshPage();
            workflowPage.pageToTheEnd();
            String rowRange = workflowPage.getRowRange();
            Boolean lastRangeContainsTotalRows = rowRange.contains(rowTotal.toString());
            values.put("isLastRowRange", lastRangeContainsTotalRows);

            workflowPage.pageToTheBeginning();
            values.put("beginningRowRange", workflowPage.getRowRange());
        }
        return values;
    }

    /**
     * Changes the maximum page size
     *
     * @return
     */
    public Map<String, Integer> inspectPageSizeSettings() {
        Map<String, Integer> valuesI = new HashMap<>();
        int pageSize = workflowPage.getPageSize(Constants.DEFAULT_PAGE_SIZE);

        valuesI.put("pageSize", pageSize);
        valuesI.put("displayedWorkflows", workflowPage.getNumberOfDisplayedWorkflows());

        workflowPage.openMaxPageDropDown(Constants.DEFAULT_PAGE_SIZE);
        workflowPage.clickOnMaxSizeDropDown(Constants.DEFAULT_PAGE_SIZE, 5);
        pageSize = workflowPage.getPageSize(5);
        valuesI.put("changedMaxPageSize", pageSize);
        valuesI.put("displayedWorkflowsUpdated", workflowPage.getNumberOfDisplayedWorkflows());

        workflowPage.refreshPage();
        return valuesI;
    }

    /**
     * Check workflowlist display
     * @return
     */
    public Map<String, Object> checkWorkflowListSorting() {
        values = new HashMap<>();
        List<String> workflowNames = new ArrayList<>();

        String lowerName = UIUtils.saltString("0 0 0 0 0 0 0 Automation");
        String upperName = UIUtils.saltString("zzzzzzzz Automation");
        String workflowName;

        workflowPage.newWorkflow();
        newWorkflowPage.createNewWorkflow(upperName, 1);
        workflowPage.newWorkflow();
        newWorkflowPage.createNewWorkflow(lowerName, 2);
        workflowNames.add(lowerName);
        workflowNames.add(upperName);
        values.put("upper-name", upperName);
        values.put("lower-name", lowerName);

        //Before click
        workflowPage.refreshPage();
        values.put("upper-before-click-index", workflowPage.getWorkflowIndex(upperName));
        values.put("lower-before-click-index", workflowPage.getWorkflowIndex(lowerName));

        // First Click
        workflowPage.clickOnFirstColumn();
        values.put("upper-first-click-index", workflowPage.getWorkflowIndex(upperName));
        values.put("lower-first-click-index", workflowPage.getWorkflowIndex(lowerName));

        // Second click
        workflowPage.clickOnFirstColumn();
        values.put("upper-second-click-index", workflowPage.getWorkflowIndex(upperName));
        values.put("lower-second-click-index", workflowPage.getWorkflowIndex(lowerName));

        List<String> headers = workflowPage.getWorkflowListHeaders();
        values.put("workflowListHeaders", headers);

        return values;
    }

    /**
     * When a workflow is created the workflow nam is displaed with a single space between words, even if the name
     * contained 2 or more spaces. For validation purpose the name nput string is also converted to single spaces
     *
     * @param workflowName Worflow name
     * @return Single space string
     */
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
