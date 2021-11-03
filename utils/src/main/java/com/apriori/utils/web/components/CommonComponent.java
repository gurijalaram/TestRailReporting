package com.apriori.utils.web.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * Represents a common component in a web application that is used throughout
 * the system.
 *
 * These components are built by attaching the root of a WebElement on the page
 * to them.
 */
public abstract class CommonComponent<T extends LoadableComponent<T>> extends LoadableComponent<T> {
    private WebDriver driver;
    private WebElement root;

    public CommonComponent(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.root = root;
    }

    protected WebDriver getDriver(){
        return this.driver;
    }

    protected WebElement getRoot() {
        return this.root;
    }
}
