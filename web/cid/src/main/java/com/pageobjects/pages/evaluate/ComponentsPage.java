package com.pageobjects.pages.evaluate;

import com.apriori.utils.ColumnUtils;
import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.CommonConstants;

import com.pageobjects.common.ScenarioTablePage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    @FindBy(css = ".v-grid-header")
    private WebElement columnHeaders;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ScenarioTablePage scenarioTablePage;
    private ColumnUtils columnUtils;

    public ComponentsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.columnUtils = new ColumnUtils(driver);
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
        driver.navigate().refresh();
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
        return pageUtils.scrollToElement(subcomponent, componentScroller, CommonConstants.ARROW_DOWN);
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
        pageUtils.scrollToElement(assembly, componentScroller, CommonConstants.ARROW_DOWN);

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
    public ComponentsPage highlightSubcomponent(String scenarioName, String subcomponentName) {
        By subcomponent = By.xpath("//a[contains(@href,'#openFromSearch::sk,partState," + subcomponentName.toUpperCase() + "," + scenarioName + "')]/ancestor::tr");
        pageUtils.scrollToElement(subcomponent, componentScroller, CommonConstants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(subcomponent);
        return this;
    }

    /**
     * Checks the assembly thumbnail type
     *
     * @param scenarioName  - the scenario name
     * @param assemblyName  - the part name
     * @param thumbnailType - the thumbnail type
     * @return true/false
     */
    public boolean isAssemblyThumbnail(String scenarioName, String assemblyName, String thumbnailType) {
        By assembly = By.xpath("//a[contains(@href,'#openFromSearch::sk,assemblyState," + assemblyName.toUpperCase() + "," + scenarioName + "')]/ancestor::tr//div[@class='fa fa-cube assembly-thumbnail-icon']");
        return pageUtils.waitForElementToAppear(assembly).isDisplayed();
    }

    /**
     * Selects the table column button
     *
     * @return new page object
     */
    public ComponentTableColumnsPage openColumnsTable() {
        pageUtils.waitForElementToAppear(columnSelectorButton).click();
        return new ComponentTableColumnsPage(driver);
    }

    /**
     * Gets all column headers in the table
     *
     * @return column headers as string
     */
    public List<String> getColumnHeaderNames() {
        return Arrays.stream(columnHeaders.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Closes the panel
     *
     * @return Evaluate Page
     */
    public EvaluatePage closePanel() {
        pageUtils.waitForElementAndClick(closePanelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Gets the cell details
     *
     * @param component    - the assembly component
     * @param column - the column
     * @return string
     */
    public String getComponentCell(String component, String column) {
        String rowLocator = "//div[@data-ap-comp='assemblyComponentsGridArea']//div[.='" + component + "']/ancestor::tr[@class]";
        return columnUtils.columnDetails("assemblyComponentsGridArea", column, rowLocator);
    }

    /**
     * Gets table values by specified row index
     *
     * @param row - the row
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getTableValsByRow(String row) {
        Document evaluateComponentView = Jsoup.parse(driver.getPageSource());

        String baseCssSelector = "div[class='v-grid-tablewrapper'] > table > tbody > tr:nth-child(%s) > td";
        ArrayList<Element> elements;

        baseCssSelector = String.format(baseCssSelector, row);
        elements = evaluateComponentView.select(baseCssSelector);

        return elements.stream().filter(element -> !element.text().isEmpty() && element.text().contains(".")).map(element -> new BigDecimal(element.text())).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Switches to other tab
     */
    public void switchBackToTabOne() {
        pageUtils.switchBackToInitialTab();
    }
}