package com.apriori.common;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class CisComponentTableActions extends EagerPageComponent<CisComponentTableActions> {

    @FindBy(css = "input[name='search']")
    private WebElement searchInput;

    @FindBy(css = "button[type='submit']")
    private WebElement searchIconButton;

    @FindBy(xpath = "//div[.='100']")
    private WebElement paginator;

    @FindBy(css = ".paginator .left")
    private WebElement paginatorDropdown;

    public CisComponentTableActions(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(searchInput);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return current page object
     */
    private CisComponentTableActions search(String componentName) {
        getPageUtils().waitForElementToAppear(searchIconButton);
        getPageUtils().clearValueOfElement(searchInput);
        searchInput.sendKeys(componentName.toUpperCase());
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public CisScenarioTableController clickSearch(String componentName) {
        search(componentName);
        getPageUtils().waitForElementAndClick(searchIconButton);
        return new CisScenarioTableController(getDriver());
    }

    /**
     * Search for a component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public CisScenarioTableController enterKeySearch(String componentName) {
        search(componentName);
        searchInput.sendKeys(Keys.ENTER);
        return new CisScenarioTableController(getDriver());
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public CisComponentTableActions setPagination() {
        getPageUtils().waitForElementAndClick(paginatorDropdown);
        getPageUtils().waitForElementToAppear(paginator);
        getPageUtils().waitForElementAndClick(paginator);
        return this;
    }
}
