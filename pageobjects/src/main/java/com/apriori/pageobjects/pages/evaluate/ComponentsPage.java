package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.pages.explore.TableColumnsPage;
import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ComponentsPage extends LoadableComponent<ComponentsPage> {

    private final Logger logger = LoggerFactory.getLogger(ComponentsPage.class);

    @FindBy(css = ".panel.panel-details")
    private WebElement panelDetails;

    @FindBy(css = "div[data-ap-comp='assemblyComponentsTable'] .v-grid-row-focused")
    private WebElement componentsTableRow;

    @FindBy(css = "select[data-ap-field='tableViewFilter']")
    private WebElement filterDropdown;

    @FindBy(css = "div[data-ap-comp='assemblyComponentsTable'] div.v-grid-scroller-vertical")
    private WebElement componentScroller;

    @FindBy(css = "button[data-ap-nav-dialog='showTableViewEditor']")
    private WebElement columnsButton;

    @FindBy(css = "button[data-ap-scope='assemblyComponentsTableViewSelection']")
    private WebElement columnSelectorButton;

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
        pageUtils.waitForElementToAppear(componentsTableRow);
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
     * @param subcomponentName - name of the part
     * @param scenarioName     - scenario name
     * @return a new page object
     */
    public EvaluatePage openSubcomponent(String scenarioName, String subcomponentName) {
        pageUtils.waitForElementAndClick(findSubcomponent(scenarioName, subcomponentName));
        return new EvaluatePage(driver);
    }

    /**
     * Find specific element in the table
     *
     * @param subcomponentName - the subcomponent name
     * @param scenarioName     - scenario name
     * @return the part as webelement
     */
    public WebElement findSubcomponent(String scenarioName, String subcomponentName) {
        By subcomponent = By.cssSelector("a[href*='#openFromSearch::sk,partState," + subcomponentName.toUpperCase() + "," + scenarioName + "']");
        return pageUtils.scrollToElement(subcomponent, componentScroller, Constants.ARROW_DOWN);
    }

    /**
     * Expands assembly dropdown
     *
     * @param scenarioName - the scenario name
     * @param assemblyName - the assembly name
     * @return current page object
     */
    public ComponentsPage expandAssembly(String scenarioName, String assemblyName) {
        By assembly = By.xpath("//a[contains(@href,'#openFromSearch::sk,assemblyState," + assemblyName.toUpperCase() + "," + scenarioName + "')]/ancestor::td//span[@class]");
        pageUtils.scrollToElement(assembly, componentScroller, Constants.ARROW_DOWN);

        if (driver.findElement(assembly).getAttribute("outerHTML").contains("right")) {
            driver.findElement(assembly).click();
        }
        return this;
    }

    /**
     * Highlights the assembly in the table
     *
     * @param scenarioName     - scenario name
     * @param subcomponentName - subcomponent name
     */
    public void selectSubcomponent(String scenarioName, String subcomponentName) {
        By subcomponent = By.xpath("//a[contains(@href,'#openFromSearch::sk,partState," + subcomponentName.toUpperCase() + "," + scenarioName + "')]/ancestor::td");
        pageUtils.scrollToElement(subcomponent, componentScroller, Constants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(subcomponent);
    }

    /**
     * Checks the assembly thumbnail type
     *
     * @param scenarioName - the scenario name
     * @param assemblyName         - the part name
     * @param thumbnailType    - the thumbnail type
     * @return true/false
     */
    public boolean isAssemblyThumbnail(String scenarioName, String assemblyName, String thumbnailType) {
        By assembly = By.xpath("//a[contains(@href,'#openFromSearch::sk,assemblyState," + assemblyName.toUpperCase() + "," + scenarioName + "')]/ancestor::tr//div[@class='fa fa-cube assembly-thumbnail-icon']");
        return pageUtils.checkElementAttribute(driver.findElement(assembly), "title", thumbnailType);
    }

    /**
     * Selects the table column button
     *
     * @return new page object
     */
    public TableColumnsPage openColumnsTable() {
        pageUtils.waitForElementToAppear(columnSelectorButton).click();
        return new TableColumnsPage(driver);
    }
}