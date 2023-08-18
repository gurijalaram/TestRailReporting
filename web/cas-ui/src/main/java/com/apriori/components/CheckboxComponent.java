package com.apriori.components;

import com.apriori.PageUtils;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the aPriori SVG Checkbox component.
 *
 * This component is a little weird in that it uses an SVG to
 * represent the check state instead of an input.  This enables
 * it to do a tri state checkbox.
 */
public final class CheckboxComponent extends CommonComponent {
    private static final String ATTRIBUTE_CHECK_ICON = "data-testid";
    private static final String ATTRIBUTE_CHECK_ICON_CHECKED = "CheckBoxIcon";
    private static final String ATTRIBUTE_CHECK_ICON_INDETERMINATE = "IndeterminateCheckBoxIcon";
    private static final By BY_INPUT_CHECKBOX = By.cssSelector(".PrivateSwitchBase-input");
    private static final By BY_SVG = By.tagName("svg");

    /**
     * @inheritDoc
     */
    public CheckboxComponent(final WebDriver driver, final WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the underlying svg.
     *
     * @return The underlying svg element.
     */
    private WebElement getSvg() {
        return getPageUtils().waitForElementToAppear(BY_SVG, PageUtils.DURATION_INSTANT, getRoot());
    }

    /**
     * Gets the underlying checkbox input.
     *
     * @return the underlying checkbox input element
     */
    private WebElement getCheckboxInput() {
        return getPageUtils().waitForElementToAppear(BY_INPUT_CHECKBOX, PageUtils.DURATION_INSTANT, getRoot());
    }

    /**
     * Gets a value that determines if the checkbox is in the given state.
     *
     * @param state The state to check.
     *
     * @return True if the data-icon attribute equals the given state.
     */
    private boolean isInState(final String state) {
        WebElement check = getSvg();
        return StringUtils.equals(check.getAttribute(ATTRIBUTE_CHECK_ICON), state);
    }

    /**
     * Gets a flag that determines if this checkbox is in an indeterminate state.
     *
     * @return True if this checkbox is in an indeterminate state.
     */
    public boolean isIndeterminate() {
        return isInState(ATTRIBUTE_CHECK_ICON_INDETERMINATE);
    }

    /**
     * Gets a flag that determines if this checkbox is in a checked state.
     *
     * @return True if this row is checked
     */
    public boolean isChecked() {
        return isInState(ATTRIBUTE_CHECK_ICON_CHECKED);
    }

    /**
     * Sets the check state of the checkbox.
     *
     * If the row is already in the specified check state, then this
     * method does nothing.  Otherwise, it will guarantee that the end result
     * check state is checked or unchecked.
     *
     * @param checked The check state to force.
     *
     * @return This component.
     */
    public CheckboxComponent check(boolean checked) {
        if (isChecked() == checked) {
            // We're already in the state we want to be in.
            return this;
        }

        getPageUtils().scrollWithJavaScript(getRoot(), true);

        if (isIndeterminate() && !checked) {
            // This is a special case where if we want the check state to be empty, and the
            // current check state is indeterminate, then we have to check it twice.
            getCheckboxInput().click();
            getPageUtils().waitForCondition(this::isChecked, PageUtils.DURATION_FAST);
        }

        getCheckboxInput().click();
        return this;
    }
}
