package com.apriori.utils.web.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents a paginator that can be used to change pages.
 */
public class PaginatorComponent extends CommonComponent {
    /**
     * Initializes a new instance of this object.
     *
     * @param driver The overall global web driver that is querying different pages.
     * @param root   The root element to attach for this component.
     */
    public PaginatorComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }
}
