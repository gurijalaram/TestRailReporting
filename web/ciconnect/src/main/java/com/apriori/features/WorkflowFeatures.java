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
    private final Logger logger = LoggerFactory.getLogger(WorkflowFeatures.class);
    
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
    private Map<String, Object> values;
    
    
    public WorkflowFeatures(WebDriver driver) {
        this.driver = driver;
        this.workflowPage = new WorkflowPage(this.driver);
        this.newWorkflowPage = new NewWorkflowPage(driver);
        this.editWorkflowPage = new EditWorkflowPage(driver);
        this.deleteWorkflowPage = new DeleteWorkflowPage(driver);
        this.pageUtils = PageUtils.getInstance(driver);
    }
        
    public Map<String, Object>  creteWorkflow() {
        return createWorklow(Constants.DEFAULT_WORKFLOW_NAME, 1);
    }
    
    public Map<String, Object> createWorklow(String workflowName, int iteration) {
        values = new HashMap();
        workflowPage.newWorkflow();

        String label = newWorkflowPage.getLabel();
        values.put("label", label);

        workflowName = UIUtils.saltString(workflowName);
        newWorkflowPage.createNewWorkFlow(workflowName, iteration);
        workflowPage.refeshPage(workflowPage);

        workflowName = correctWorkflowNameSpacing(workflowName);
        boolean exists = workflowPage.workflowExists(workflowName);
        values.put("workflowExists", exists);
        values.put("workflowName", workflowName);
        
        return values;
    }
    
    public Map<String, Object> editWorklow(String workflowName) {
        return editWorklow(workflowName, Constants.DEFAULT_EDITED_WORKFLOW_NAME);
    }

    public Map<String, Object> editWorklow(String workflowName, String newWorkflowName) {
        Map<String, Object> values = new HashMap();
        workflowPage.selectWorkflow(workflowName);
        workflowPage.editWorkflow();

        String editLabel = editWorkflowPage.getLabel();
        values.put("label", editLabel);

        newWorkflowName = UIUtils.saltString(newWorkflowName);
        editWorkflowPage.editWorkflow("NAME", newWorkflowName);
        workflowPage.refeshPage(workflowPage);
        
        newWorkflowName = correctWorkflowNameSpacing(newWorkflowName);
        values.put("workflowName", newWorkflowName);
        boolean exists = workflowPage.workflowExists(newWorkflowName);
        values.put("workflowExists", exists);
        values.put("workflowName", newWorkflowName);
        
        return values;
    }
    
    public Map<String, Object> deleteWorklow(String workflowName) {
        Map<String, Object> values = new HashMap();
        workflowPage.refeshPage(workflowPage);
        
        workflowPage.selectWorkflow(workflowName);
        workflowPage.clickDeleteButton();
        values.put("header", deleteWorkflowPage.getModalHeader());
        values.put("message", deleteWorkflowPage.getModalMessage());

        deleteWorkflowPage.deleteWorkflow();
        workflowPage.refeshPage(workflowPage);

        boolean exists = workflowPage.workflowExists(workflowName);
        values.put("workflowExists", exists);
        
        return values;
    }

    public Map<String, Object> getButtonStates() {
        Map<String, Object> values = new HashMap();
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

    public Map<String, Object> defaultSorting() {
        List<String> workflows = new ArrayList();
        int iteration = 1;
        values = new HashMap<>();
        values.put("lowerNameIndex", 3);
        values.put("upperNameIndex", 2);
        values.put("numericNameIndex", 1);

        workflowPage.newWorkflow();
        String lowerName = UIUtils.saltString(Constants.DEFAULT_NAME_LOWER_CASE);
        newWorkflowPage.createNewWorkFlow(lowerName, iteration);
        workflowPage.refeshPage(workflowPage);
        workflowPage.refreshWorkflowTable();
        lowerName = correctWorkflowNameSpacing(lowerName);
        values.put("lowerName", lowerName);
        workflows.add(lowerName);
        iteration++;

        workflowPage.newWorkflow();
        String numericName = UIUtils.saltString(Constants.DEFAULT_NAME_WITH_NUMBER);
        newWorkflowPage.createNewWorkFlow(numericName, iteration);
        workflowPage.refeshPage(workflowPage);
        workflowPage.refreshWorkflowTable();
        numericName = correctWorkflowNameSpacing(numericName);
        values.put("numericName", numericName);
        workflows.add(numericName);
        iteration++;

        workflowPage.newWorkflow();
        String upperName = UIUtils.saltString(Constants.DEFAULT_NAME_UPPER_CASE);
        newWorkflowPage.createNewWorkFlow(numericName, iteration);
        workflowPage.refeshPage(workflowPage);
        workflowPage.refreshWorkflowTable();
        upperName = correctWorkflowNameSpacing(upperName);
        values.put("upperName", upperName);
        workflows.add(upperName);

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
