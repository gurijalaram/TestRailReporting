package com.apriori.features;

import com.apriori.pageobjects.NewWorkflowPage;
import com.apriori.pageobjects.WorkflowPage;

import org.openqa.selenium.WebDriver;

import utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class QueryDefinitionFeatures {
    Map<String, Object> values;
    Map<String, Boolean> valuesB;
    WebDriver driver;
    NewWorkflowPage newWorkflowPage;
    WorkflowPage workflowPage;

    public QueryDefinitionFeatures(WebDriver driver) {
        this.driver = driver;
        newWorkflowPage = new NewWorkflowPage(driver);
        workflowPage = new WorkflowPage(driver);

    }

    /**
     * Checks rule functionality
     *
     * @return
     */
    public Map<String, Object> checkRulesListFunctionality() {
        values = new HashMap<>();
        int ruleCount;

        newWorkflowPage.fillQueryDefinitions(false);
        ruleCount = newWorkflowPage.addRule();

        // Verify user able to add rules
        // Verify user able to join rules
        // Verify Fields to be queried may be selected from a drop-down
        // Verify Drop down of operators to be used in query
        // Verify Text field to enter query argument
        values.put("rule-count-after-add", ruleCount);

        // Verify user able to delete rules
        ruleCount = newWorkflowPage.deleteRule();
        values.put("rule-count-after-delete", ruleCount);

        // Verify 'Add Group' button present allowing user to create nested queries
        boolean exists = newWorkflowPage.groupsButtonExists();
        values.put("group-button-exists", exists);

        // Verify modal scrolls if content exceeds length of modal
        newWorkflowPage.addEmptyRules(8);
        boolean scrollbarExists = newWorkflowPage.rulesScrollBarExists();
        values.put("scrollbar-exists", scrollbarExists);

        return values;
    }

    /**
     * Checks the state of the Previous & Next Buttons
     *
     * @return
     */
    public Map<String, Boolean> checkNavigation() {
        valuesB = new HashMap<>();
        Map<String, Boolean> states = new HashMap<>();

        // check previous buttons state
        states = newWorkflowPage.getNavigationButtonState(NewWorkflowPage.Tab.QUERY,
                NewWorkflowPage.NavigationButton.PREVIOUS);
        valuesB.put("previous-button-exists", states.get("exists"));
        valuesB.put("previous-button-enabled", states.get("enabled"));

        // check the next button's state
        states = newWorkflowPage.getNavigationButtonState(NewWorkflowPage.Tab.QUERY,
                NewWorkflowPage.NavigationButton.NEXT);
        boolean isEnabled;
        isEnabled = newWorkflowPage.isQueryNextButtonDisabled();
        valuesB.put("next-disabled-button-enabled-before-rule", isEnabled);
        newWorkflowPage.fillQueryDefinitions(false);
        valuesB.put("next-button-enabled-after-rule", states.get("enabled"));

        return valuesB;
    }

    /**
     * Checks the states of the input fields
     *
     * @return
     */
    public Map<String, Boolean> checkInputtFields() {
        valuesB = new HashMap<>();
        boolean filterFieldExists;
        boolean operatorFieldExists;
        boolean valuesFieldExists;
        
        filterFieldExists = newWorkflowPage.queryFilterExists();
        operatorFieldExists = newWorkflowPage.queryOperatorExists();
        valuesFieldExists = newWorkflowPage.queryValueExists();
        valuesB.put("filter-exists-before-selection", filterFieldExists);
        valuesB.put("operator-exists-before-selection", operatorFieldExists);
        valuesB.put("value-exists-before-selection", valuesFieldExists);
        
        newWorkflowPage.selectQueryFilter();

        operatorFieldExists = newWorkflowPage.queryOperatorExists();
        valuesFieldExists = newWorkflowPage.queryValueExists();
        valuesB.put("filter-exists-after-selection", filterFieldExists);
        valuesB.put("operator-exists-after-selection", operatorFieldExists);
        valuesB.put("value-exists-after-selection", valuesFieldExists);

        return valuesB;
    }
}
