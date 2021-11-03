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

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root The root element to attach for this component.
     */
    public CommonComponent(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.root = root;
    }

    /**
     * Gets the web driver for this component.
     *
     * @return The web driver for this component.
     */
    protected WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Gets the root element that contains this component.
     *
     * @return The root element that contains this component.
     */
    protected WebElement getRoot() {
        return this.root;
    }
}
