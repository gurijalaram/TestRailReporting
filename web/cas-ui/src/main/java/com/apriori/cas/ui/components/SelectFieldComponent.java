package com.apriori.cas.ui.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the select field component in apriori-react-common
 *
 * This is just a wrapper to the actual underlying select field.
 */
public final class SelectFieldComponent extends CommonComponent {
    private SelectComponent selectComponent;

    /**
     * @inheritDoc
     */
    public SelectFieldComponent(WebDriver driver, WebElement root) {
        super(driver, root);

        WebElement element = this.getRoot().findElement(By.cssSelector(".apriori-select"));
        this.selectComponent = new SelectComponent(getDriver(), element);
    }

    /**
     * Gets the underlying select component for this field.
     *
     * @return The select component for this field.
     */
    public SelectComponent getSelect() {
        return this.selectComponent;
    }
}
