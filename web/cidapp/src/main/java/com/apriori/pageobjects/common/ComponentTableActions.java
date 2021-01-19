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

    private final Logger LOGGER = LoggerFactory.getLogger(ComponentTableActions.class);

    @FindBy(css = "input[name='search']")
    private WebElement searchInput;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[.='Filters']")
    private WebElement filtersButton;

    @FindBy(xpath = "//button[.='Configure']")
    private WebElement configureButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ComponentTableActions(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(searchInput);
        pageUtils.waitForElementAppear(configureButton);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController clickSearch(String componentName) {
        search(componentName);
        submitButton.click();
        return new ScenarioTableController(driver);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController enterSearch(String componentName) {
        search(componentName);
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
        pageUtils.waitForElementToAppear(submitButton);
        searchInput.clear();
        searchInput.sendKeys(componentName);
        return this;
    }

    /**
     * Open configure page
     *
     * @return new page object
     */
    public ConfigurePage configure() {
        pageUtils.waitForElementAndClick(configureButton);
        return new ConfigurePage(driver);
    }

    /**
     * Open filters page
     *
     * @return new page object
     */
    public FiltersPage filters() {
        pageUtils.waitForElementAndClick(filtersButton);
        return new FiltersPage(driver);
    }
}
