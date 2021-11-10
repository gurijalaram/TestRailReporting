package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CisComponentTableActions extends LoadableComponent<CisScenarioTableController> {

    @FindBy(css = "input[name='search']")
    private WebElement searchInput;

    @FindBy(css = "button[type='submit']")
    private WebElement searchIconButton;

    @FindBy(xpath = "//div[.='100']")
    private WebElement paginator;

    @FindBy(css = ".paginator .left")
    private WebElement paginatorDropdown;

    private PageUtils pageUtils;
    private WebDriver driver;

    public CisComponentTableActions(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(searchInput);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return current page object
     */
    private CisComponentTableActions search(String componentName) {
        pageUtils.waitForElementToAppear(searchIconButton);
        pageUtils.clear(searchInput);
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
        pageUtils.waitForElementAndClick(searchIconButton);
        return new CisScenarioTableController(driver);
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public CisComponentTableActions setPagination() {
        pageUtils.waitForElementAndClick(paginatorDropdown);
        pageUtils.waitForElementToAppear(paginator);
        pageUtils.waitForElementAndClick(paginator);
        return this;
    }
}
