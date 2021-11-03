package com.apriori.utils.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the select field component in apriori-react-common
 *
 * This is just a wrapper to the actual underlying select field.
 */
public final class SelectFieldComponent extends CommonComponent<SelectFieldComponent> {
    private SelectComponent selectComponent;

    public SelectFieldComponent(WebDriver driver, WebElement root) {
        super(driver, root);
        this.get();
    }

    public SelectComponent getSelect() {
        return this.selectComponent;
    }

    @Override
    protected void load() {
        WebElement element = this.getRoot().findElement(By.cssSelector(".apriori-select"));
        this.selectComponent = new SelectComponent(getDriver(), element);
    }

    @Override
    protected void isLoaded() throws Error {
        if (selectComponent == null) {
            throw new Error("The underlying select component in a select field is missing.");
        }
    }
}
