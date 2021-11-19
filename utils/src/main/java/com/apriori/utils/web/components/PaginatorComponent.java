package com.apriori.utils.web.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents a paginator that can be used to change pages.
 */
public class PaginatorComponent extends CommonComponent {
    private static By BY_PAGE_SIZE = By.className("apriori-select");

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root   The root element to attach for this component.
     */
    public PaginatorComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the page size drop-down.
     *
     * @return The page size drop-down.
     */
    public SelectComponent getPageSize() {
        WebElement pageSizeDrop = getPageUtils().waitForElementToAppear(BY_PAGE_SIZE);
        return new SelectComponent(getDriver(), pageSizeDrop);
    }
}
