package com.apriori.cas.ui.components;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents the search field component.
 */
public final class SearchFieldComponent extends CommonComponent {
    private static final By BY_INPUT = By.tagName("input");
    private static final By BY_SEARCH_BUTTON = By.className("search-field-button");

    /**
     * @inheritDoc
     */
    public SearchFieldComponent(final WebDriver driver, final WebElement root) {
        super(driver, root);
    }

    /**
     * Gets the input for the search text.
     *
     * @return The input for the search text.
     */
    private WebElement getSearchInput() {
        return getPageUtils().waitForElementToAppear(BY_INPUT, PageUtils.DURATION_FAST, getRoot());
    }

    /**
     * Gets the search button.
     *
     * @return The search button.
     */
    private WebElement getSearchFieldButton() {
        return getPageUtils().waitForElementToAppear(BY_SEARCH_BUTTON, PageUtils.DURATION_FAST, getRoot());
    }

    /**
     * Gets the placeholder for the search input.
     *
     * @return The search placeholder.
     */
    public String getPlaceholder() {
        return getSearchInput().getAttribute("placeholder");
    }

    /**
     * Enters text into the search field and runs the search.
     *
     * @param text The text to search by.
     *
     * @return This component.
     */
    public SearchFieldComponent search(final String text) {
        WebElement search = getSearchInput();
        getPageUtils().scrollWithJavaScript(search, true);
        getPageUtils().setValueOfElement(search, text, Keys.ENTER);
        return this;
    }

    /**
     * Enters text into the search field and runs the search by clicking the search button.
     *
     * @param text The text to search by.
     *
     * @return This component
     */
    public SearchFieldComponent clickSearch(final String text) {
        WebElement search = getSearchInput();
        getPageUtils().scrollWithJavaScript(search, true);
        getPageUtils().setValueOfElement(search, text);
        WebElement button = getSearchFieldButton();
        button.click();
        return this;
    }
}
