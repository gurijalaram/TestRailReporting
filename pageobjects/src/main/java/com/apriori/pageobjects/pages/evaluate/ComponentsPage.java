package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.pages.explore.TableColumnsPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentsPage extends LoadableComponent<ComponentsPage> {

    private final Logger logger = LoggerFactory.getLogger(ComponentsPage.class);

    @FindBy(css = ".panel.panel-details")
    private WebElement panelDetails;

    @FindBy(css = "select[data-ap-field='tableViewFilter']")
    private WebElement filterDropdown;

    @FindBy(css = "div[data-ap-comp='assemblyComponentsTable'] div.v-grid-scroller-vertical")
    private WebElement componentScroller;

    @FindBy(css = "button[data-ap-nav-dialog='showTableViewEditor']")
    private WebElement columnsButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComponentsPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(panelDetails);
    }

    /**
     * Selects the filter dropdown
     *
     * @param view - the view
     * @return current page object
     */
    public ComponentsPage selectComponentsView(String view) {
        pageUtils.selectDropdownOption(filterDropdown, view);
        return this;
    }

    /**
     * Opens the part
     *
     * @param partName     - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openAssemblyPart(String scenarioName, String partName) {
        pageUtils.waitForElementToAppear(findAssembly(scenarioName, partName));
        findAssembly(scenarioName, partName).click();
        return new EvaluatePage(driver);
    }

    /**
     * Find specific element in the table
     *
     * @param partName     - name of the assembly
     * @param scenarioName - scenario name
     * @return the part as webelement
     */
    public WebElement findAssembly(String scenarioName, String partName) {
        By scenario = By.cssSelector("a[href*='#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElement(scenario, componentScroller);
    }

    /**
     * Highlights the assembly in the table
     *
     * @param scenarioName - scenario name
     * @param partName     - name of the assembly
     */
    public void highlightAssembly(String scenarioName, String partName) {
        By scenario = By.xpath("//a[contains(@href,'#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::td");
        pageUtils.scrollToElement(scenario, componentScroller);
        pageUtils.waitForElementAndClick(scenario);
    }

    /**
     * Checks the assembly thumbnail type
     * @param scenarioName - the scenario name
     * @param partName - the part name
     * @param thumbnailType - the thumbnail type
     * @return true/false
     */
    public Boolean isAssemblyThumbnail(String scenarioName, String partName, String thumbnailType) {
        By scenario = By.xpath("//a[contains(@href,'#openFromSearch::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]/ancestor::tr//div[@class='fa fa-cube assembly-thumbnail-icon']");
        return pageUtils.checkElementAttribute(driver.findElement(scenario), "title", thumbnailType);
    }

    /**
     * Selects the table column button
     *
     * @return new page object
     */
    public TableColumnsPage openColumnsTable() {
        pageUtils.waitForElementToAppear(columnsButton).click();
        return new TableColumnsPage(driver);
    }
}