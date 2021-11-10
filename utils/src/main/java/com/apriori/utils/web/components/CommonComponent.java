package com.apriori.utils.web.components;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents a common component in a web application that is used throughout
 * the system.
 *
 * These components are built by attaching the root of a WebElement on the page
 * to them.  Note that any find parameters in the class are restricted through the
 * root element.
 */
public abstract class CommonComponent {
    private WebDriver driver;
    private WebElement root;
    private PageUtils pageUtils;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root The root element to attach for this component.
     *
     * @exception java.lang.Error Occurs if there is a problem loading child components.
     */
    protected CommonComponent(WebDriver driver, WebElement root) {
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
        return root;
    }

    /**
     * Gets the utilities for this page.
     *
     * The page utils for a common component are lazy loaded and
     * only required when requested for the first time.
     *
     * @return The utilities for this page.
     */
    protected final PageUtils getPageUtils() {
        this.pageUtils = this.pageUtils == null ? new PageUtils(getDriver()) : this.pageUtils;
        return this.pageUtils;
    }
}
