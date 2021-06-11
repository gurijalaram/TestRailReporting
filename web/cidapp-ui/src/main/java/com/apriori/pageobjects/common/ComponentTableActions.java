package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentTableActions extends LoadableComponent<ComponentTableActions> {

    private static final Logger logger = LoggerFactory.getLogger(ComponentTableActions.class);

    @FindBy(css = "input[name='search']")
    private WebElement searchInput;

    @FindBy(css = "button[type='submit']")
    private WebElement searchIconButton;

    @FindBy(id = "qa-sub-component-detail-filter-button")
    private WebElement filterButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ComponentTableActions(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(searchInput);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController clickSearch(String componentName) {
        search(componentName.toUpperCase());
        searchIconButton.click();
        return new ScenarioTableController(driver);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController enterKeySearch(String componentName) {
        search(componentName.toUpperCase());
        searchInput.sendKeys(Keys.ENTER);
        return new ScenarioTableController(driver);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return current page object
     */
    private ComponentTableActions search(String componentName) {
        pageUtils.waitForElementToAppear(searchIconButton);
        searchInput.clear();
        searchInput.sendKeys(componentName);
        return this;
    }

    /**
     * Open configure page
     *
     * @param element - the configure button
     *
     * @return new page object
     */
    public ConfigurePage configure(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new ConfigurePage(driver);
    }

    /**
     * Open filters page
     *
     * @return new page object
     */
    public FilterPage filter(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new FilterPage(driver);
    }
}
