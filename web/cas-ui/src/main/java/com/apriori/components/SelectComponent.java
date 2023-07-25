package com.apriori.components;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the common select component in apriori-react-common.
 */
public final class SelectComponent extends CommonComponent {
    private static By BY_VALUE_INPUT = By.tagName("input");

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver
     * @param root The root element.
     *
     * @exception java.lang.Error Occurs if the underlying value input cannot be found.
     */
    public SelectComponent(final WebDriver driver, final WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the underlying value input for this component.
     *
     * @return The value input for this component.
     */
    private WebElement getValueInput() {
        return getPageUtils().waitForElementToAppear(BY_VALUE_INPUT, PageUtils.DURATION_INSTANT, getRoot());
    }

    /**
     * Gets the selected value.
     *
     * @return The string text of the selected value.
     */
    public String getSelected() {
        return getRoot().getText();
    }

    /**
     * Selects a value from the drop-down.
     *
     * This is a shortcut to open().select(value) or clear().
     *
     * @param value The value to select.  If this is null
     *              then the value is cleared.
     *
     * @return This object.
     */
    public SelectComponent select(String value) {
        if (value == null) {
            return this.clear();
        }

        this.open().select(value);
        this.getValueInput().sendKeys(Keys.TAB);
        return this;
    }

    /**
     * Clears the value and tabs out of the component.
     *
     * @return This object.
     */
    public SelectComponent clear() {
        // This method works regardless of whether the clear indicator is displayed or not.
        getPageUtils().waitForElementAndClick(getRoot());

        WebElement valueInput = getValueInput();
        // This actual clears the value if there is one.
        valueInput.sendKeys(Keys.DELETE);
        // Close the drop-down.
        valueInput.sendKeys(Keys.ESCAPE);
        // Finalize the value.
        valueInput.sendKeys(Keys.TAB);
        return this;
    }

    /**
     * Opens the drop-down and returns the menu.
     *
     * @return The drop-down menu with the given items.
     */
    public SelectPopMenuComponent open() {
        getPageUtils().scrollWithJavaScript(getRoot(), true);
        getRoot().click();
        return new SelectPopMenuComponent(this);
    }
}
