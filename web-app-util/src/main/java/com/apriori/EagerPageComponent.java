package com.apriori;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;

/**
 * Represents a loadable component that immediately loads the page content in the constructor.
 *
 * @param <T> The component self type
 */
public abstract class EagerPageComponent<T extends LoadableComponent<T>> extends LoadableComponent<T> {
    private final WebDriver driver;
    private final PageUtils pageUtils;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     * @param logger The logger for the page.
     */
    public EagerPageComponent(final WebDriver driver, final Logger logger) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    /**
     * Does nothing in this instance.
     */
    @Override
    protected void load() {
        // Don't really need to do anything for this.
    }

    /**
     * Gets the global web driver.
     *
     * @return The global web driver.
     */
    public final WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Gets the utilities for this page.
     *
     * @return The utilities for this page.
     */
    protected final PageUtils getPageUtils() {
        return pageUtils;
    }
}

