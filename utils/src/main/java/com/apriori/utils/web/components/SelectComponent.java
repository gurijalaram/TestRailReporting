package com.apriori.utils.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the common select component in apriori-react-common.
 */
public final class SelectComponent extends CommonComponent{
    private WebElement valueInput;

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

        valueInput = this.getRoot().findElement(By.cssSelector("input"));

        if (valueInput == null) {
            throw new Error("The main input control for a select component was not found.");
        }
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
        this.valueInput.sendKeys(Keys.TAB);
        return this;
    }

    /**
     * Clears the value and tabs out of the component.
     *
     * @return This object.
     */
    public SelectComponent clear() {
        // This method works regardless of whether the clear indicator is displayed or not.
        getRoot().click();
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
        getRoot().click();
        return new SelectPopMenuComponent(this);
    }
}
