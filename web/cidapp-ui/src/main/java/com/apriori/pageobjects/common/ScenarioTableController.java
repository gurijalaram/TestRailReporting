package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScenarioTableController extends LoadableComponent<ScenarioTableController> {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioTableController.class);

    @FindBy(css = ".apriori-table.scenario-iteration-table")
    private WebElement componentTable;

    @FindBy(css = ".apriori-table.scenario-iteration-table .spinner-border")
    private List<WebElement> componentTableSpinner;

    @FindBy(css = ".apriori-table .table-head")
    private WebElement tableHeaders;

    @FindBy(css = ".table-head .checkbox-icon")
    private WebElement selectAllCheckBox;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ScenarioTableController(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(componentTable);
        pageUtils.waitForElementsToNotAppear(componentTableSpinner);
    }

    /**
     * Selects all scenarios on the page
     *
     * @return current page object
     */
    public ScenarioTableController selectAllScenarios() {
        pageUtils.waitForElementAndClick(selectAllCheckBox);
        return this;
    }

    /**
     * Opens the scenario
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController openScenario(String componentName, String scenarioName) {
        moveToScenario(componentName, scenarioName);
        pageUtils.waitForElementAndClick(byScenarioName(componentName, scenarioName));
        return this;
    }

    /**
     * Opens the first scenario
     *
     * @return a new page object
     */
    public ScenarioTableController openFirstScenario() {
        By scenarioLocator = By.xpath("(//div[@class='table-body']/div)[1]//div[@class='scenario-thumbnail small']");
        pageUtils.waitForElementAndClick(scenarioLocator);
        return this;
    }

    /**
     * Hovers over the scenario
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    private ScenarioTableController moveToScenario(String componentName, String scenarioName) {
        pageUtils.scrollWithJavaScript(elementScenarioName(componentName, scenarioName), true);
        pageUtils.mouseMove(elementScenarioName(componentName, scenarioName));
        return this;
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController highlightScenario(String componentName, String scenarioName) {
        moveToScenario(componentName, scenarioName);
        pageUtils.waitForElementAndClick(byComponentName(componentName, scenarioName));
        return this;
    }

    /**
     * Finds the scenario by component name
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return by
     */
    private By byComponentName(String componentName, String scenarioName) {
        return By.xpath(String.format("//div[.='%s']/ancestor::div[@role='row']//span[contains(text(),'%s')]", scenarioName.trim(), componentName.toUpperCase().trim()));
    }

    /**
     * Checks if the component is present on the page by size == 0 or > 0
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return pageUtils.waitForElementsToAppear(byScenarioName(componentName, scenarioName));
    }


    /**
     * Gets the info in the row
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return list of string
     */
    public List<String> getRowDetails(String componentName, String scenarioName) {
        return Stream.of(getRowText(componentName, scenarioName), getRowIcon(componentName, scenarioName), getDfmRisk(componentName, scenarioName))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    /**
     * Gets the cell in the row
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return list of string
     */
    private List<String> getRowText(String componentName, String scenarioName) {
        return getByParentLocator(componentName, scenarioName)
            .findElements(By.cssSelector("[class='cell-text']"))
            .stream()
            .map(x -> x.getAttribute("textContent"))
            .filter(text -> !text.equals(""))
            .collect(Collectors.toList());
    }

    /**
     * Gets the icon in the row
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return list of string
     */
    private List<String> getRowIcon(String componentName, String scenarioName) {
        return getByParentLocator(componentName, scenarioName)
            .findElements(By.cssSelector("svg"))
            .stream()
            .map(x -> x.getAttribute("data-icon"))
            .collect(Collectors.toList());
    }

    /**
     * Gets the dfm risk icon in the row
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return list of string
     */
    private List<String> getDfmRisk(String componentName, String scenarioName) {
        return getByParentLocator(componentName, scenarioName)
            .findElements(By.cssSelector("svg circle"))
            .stream()
            .map(x -> x.getAttribute("stroke"))
            .collect(Collectors.toList());
    }

    /**
     * Gets the scenario 'webelement' locator
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return webelement
     */
    private WebElement elementScenarioName(String componentName, String scenarioName) {
        return pageUtils.waitForElementToAppear(byScenarioName(componentName, scenarioName));
    }

    /**
     * Gets the scenario 'by' locator
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return by
     */
    private By byScenarioName(String componentName, String scenarioName) {
        return By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//a[@class='scenario-display-name']//div[.='%s']", componentName.toUpperCase().trim(), scenarioName.trim()));
    }

    /**
     * Gets the parent part of the element
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return webelement
     */
    private WebElement getByParentLocator(String componentName, String scenarioName) {
        return driver.findElement(By.xpath(String.format("//div[.='%s']/following-sibling::div[.='%s']/parent::div", componentName.toUpperCase().trim(), scenarioName.trim())));
    }

    /**
     * Highlights the scenario in the table using the keyboard control key
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController controlHighlightScenario(String componentName, String scenarioName) {
        Actions controlHighlight = new Actions(driver);
        controlHighlight.keyDown(Keys.CONTROL)
            .click(pageUtils.waitForElementToAppear(byComponentName(componentName, scenarioName)))
            .build()
            .perform();
        return this;
    }

    /**
     * Multi-highlight scenario
     * This method takes any number of arguments as string. A combination of component and scenario name needs to passed in the argument eg. {"PISTON, Initial", "Television, AutoScenario101"}
     *
     * @param componentAndScenarioName - component name and method name
     * @return current page object
     */
    public ScenarioTableController multiHighlightScenario(String... componentAndScenarioName) {
        Actions multiHighlight = new Actions(driver);

        Arrays.stream(componentAndScenarioName).map(x -> x.split(",")).collect(Collectors.toList())
            .forEach(componentScenario -> multiHighlight.keyDown(Keys.CONTROL)
                .click(pageUtils.waitForElementToAppear(byComponentName(componentScenario[0], componentScenario[1])))
                .build()
                .perform());
        return this;
    }

    /**
     * Multi-select scenario
     * This method takes any number of arguments as string. A combination of component and scenario name needs to passed in the argument eg. {"PISTON, Initial", "Television, AutoScenario101"}
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ScenarioTableController multiSelectScenario(String... componentScenarioName) {
        Arrays.stream(componentScenarioName).forEach(componentScenario -> {
            String[] scenario = componentScenario.split(",");
            findScenarioCheckbox(scenario[0], scenario[1]).click();
        });
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController selectScenario(String componentName, String scenarioName) {
        findScenarioCheckbox(componentName, scenarioName).click();
        return this;
    }

    /**
     * Find scenario checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return webelement
     */
    private WebElement findScenarioCheckbox(String componentName, String scenarioName) {
        By scenario = By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//div[.='%s']/ancestor::div[@role='row']//div[@class='checkbox-icon']",
            componentName.toUpperCase().trim(), scenarioName.trim()));
        pageUtils.waitForElementToAppear(scenario);
        return pageUtils.scrollWithJavaScript(driver.findElement(scenario), true);
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return Stream.of(tableHeaders.getAttribute("innerText").split("\n")).collect(Collectors.toList());
    }

    /**
     * Sorts the column in order
     *
     * @param column - the column
     * @param order  - the order
     * @return current page object
     */
    public ScenarioTableController sortColumn(ColumnsEnum column, SortOrderEnum order) {
        while (!getSortOrder(column).equals(order.getOrder())) {
            pageUtils.waitForElementAndClick(By.xpath(String.format("//div[.='%s']//span", column.getColumns())));
        }
        return this;
    }

    /**
     * Gets sort order
     *
     * @param column - the column
     * @return string
     */
    public String getSortOrder(ColumnsEnum column) {
        By byColumn = By.xpath(String.format("//div[.='%s']//div[@class]//*[local-name()='svg']", column.getColumns()));
        return pageUtils.waitForElementToAppear(byColumn).getAttribute("data-icon");
    }
}
