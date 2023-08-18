package com.apriori.pageobjects.common;

import com.apriori.PageUtils;
import com.apriori.pageobjects.explore.PreviewPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ComponentTableActions extends LoadableComponent<ComponentTableActions> {

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
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        //Don't really need to do anything here
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ScenarioTableController clickSearch(String componentName) {
        enterSearchText(componentName);
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
        enterSearchText(componentName);
        searchInput.sendKeys(Keys.ENTER);
        return new ScenarioTableController(driver);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return current page object
     */
    private ComponentTableActions enterSearchText(String componentName) {
        pageUtils.waitForElementToAppear(searchIconButton);
        pageUtils.clearValueOfElement(searchInput);
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
        pageUtils.waitForElementAndClick(paginatorDropdown);
        By paginator = By.xpath("//div[.='50']");
        pageUtils.waitForElementToAppear(paginator);
        pageUtils.waitForElementAndClick(paginator);
        return this;
    }
}
