package com.apriori.pageobjects.common;

import com.apriori.pageobjects.pages.explore.PreviewPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
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

    @FindBy(css = ".paginator .left")
    private WebElement paginatorDropdown;

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
        pageUtils.waitForElementToAppear(searchInput);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController clickSearch(String componentName) {
        search(componentName);
        pageUtils.waitForElementAndClick(searchIconButton);
        return new ScenarioTableController(driver);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController enterKeySearch(String componentName) {
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
        pageUtils.waitForElementToAppear(searchIconButton);
        pageUtils.clear(searchInput);
        searchInput.sendKeys(componentName.toUpperCase());
        return this;
    }

    /**
     * Open configure page
     *
     * @param element - the configure button
     * @return new page object
     */
    public ConfigurePage configure(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new ConfigurePage(driver);
    }

    /**
     * Open filters page
     *
     * @param element - the filter button
     * @return new page object
     */
    public FilterPage filter(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new FilterPage(driver);
    }

    /**
     * Opens the preview panel
     *
     * @param element - the preview button
     * @return new page object
     */
    public PreviewPage openPreviewPanel(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return new PreviewPage(driver);
    }

    /**
     * Close the preview panel
     *
     * @param element - the preview button
     * @return new page object
     */
    public ComponentTableActions closePreviewPanel(WebElement element) {
        pageUtils.waitForElementAndClick(element);
        return this;
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public ComponentTableActions setPagination() {
        pageUtils.waitForElementToBeClickable(paginatorDropdown).click();
        By paginator = By.xpath("//div[.='100']");
        pageUtils.waitForElementToAppear(paginator);
        pageUtils.waitForElementToBeClickable(paginator).click();
        return this;
    }
}
