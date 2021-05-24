package com.apriori.validators;

import org.junit.Assert;

import java.util.Map;

public class QueryDefinitionsValidator {

    /**
     * Validate input field functionality
     *
     * @param values
     */
    public void validateInputFields(Map<String, Boolean> values) {
        Assert.assertTrue("filter drop down doesn't exist", values.get("filter-exists-before-selection"));
        Assert.assertFalse("operator drop down exists before filter selection", values.get("operator-exists-before" +
                "-selection"));
        Assert.assertFalse("value field exists before filter selection", values.get("value-exists-before-selection"));
        Assert.assertTrue("operator drop down doesn't exist after filter selection", values.get("operator-exists" +
                "-after-selection"));
        Assert.assertTrue("value field doesn't exist after filter selection", values.get("value-exists-after-selection"));
    }


    /**
     * Validate the navigation button states
     *
     * @param values
     */
    public void validateNavigation(Map<String, Boolean> values) {
        Assert.assertTrue("previous buttton doesn't exist", values.get("previous-button-exists"));
        Assert.assertTrue("next button was enabled before any rule was added", values.get("next-disabled-button" +
                "-enabled-before-rule"));
        Assert.assertTrue("next button wasn't enabled after a rule was added", values.get("next-button-enabled-after" +
                        "-rule"));
    }

    /**
     * Validate trule list functionality
     *
     * @param values
     */
    public void validateRulesList(Map<String, Object> values) {
        Assert.assertEquals("rule wasn't added", 2, (int)values.get("rule-count-after-add"));
        Assert.assertEquals("rule wasn't deleted", (int)values.get("rule-count-after-add") - 1, (int)values.get(
                "rule-count-after-delete"));
        // TODO: Find method to properly detect if rules list is scrollable
        //Assert.assertTrue("scroll bar doesn't exist", (boolean)values.get("scrollbar-exists"));
        Assert.assertTrue("group button doesn't exist", (boolean)values.get("group-button-exists"));
    }
}
