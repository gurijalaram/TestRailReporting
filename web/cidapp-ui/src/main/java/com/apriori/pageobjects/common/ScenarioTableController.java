package com.apriori.pageobjects.common;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.PageUtils;
import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.utils.CssComponent;

import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import com.utils.SortOrderEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ScenarioTableController extends LoadableComponent<ScenarioTableController> {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioTableController.class);

    @FindBy(css = ".apriori-table")
    private WebElement componentTable;

    @FindBy(css = ".apriori-table.scenario-iteration-table .spinner-border")
    private List<WebElement> componentTableSpinner;

    @FindBy(css = ".apriori-table .table-head")
    private WebElement tableHeaders;

    @FindBy(css = ".table-head [data-testid='checkbox']")
    private WebElement selectAllCheckBox;

    @FindBy(css = "[id='qa-scenario-explorer-configure-button']")
    private WebElement configureButton;

    @FindBy(css = ".table-head [role='columnheader']")
    private List<WebElement> columnHeader;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ComponentTableActions componentTableActions;

    public ScenarioTableController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.componentTableActions = new ComponentTableActions(driver);
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
     * Get Component and Scenario names of first scenario in table
     *
     * @return The component and scenario names in a comma-separated String
     */
    public String getFirstScenarioDetails() {
        By firstComponentNameLocator = By.xpath("(//div[@class='table-body']/div)[1]//span[@data-testid='component-name']");
        By firstScenarioNameLocator = By.xpath("(//div[@class='table-body']/div)[1]//div[@data-header-id='scenarioName']//div[@data-testid='text-overflow']");
        pageUtils.waitForElementToAppear(firstScenarioNameLocator);
        String componentName = driver.findElement(firstComponentNameLocator).getText();
        String scenarioName = driver.findElement(firstScenarioNameLocator).getText();

        return componentName + "," + scenarioName;
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
     * Expands the subassembly
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController expandSubassembly(String componentName, String scenarioName) {
        moveToScenario(componentName, scenarioName);
        By byAssembly = with(By.cssSelector("[data-icon='circle-chevron-down']"))
            .toLeftOf(By.xpath(String.format("//span[text()='%s']/ancestor::div[@class='cell-text']", componentName.toUpperCase().trim())));
        pageUtils.waitForElementAndClick(byAssembly);
        return this;
    }

    /**
     * Collapses the subassembly
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController collapseSubassembly(String componentName, String scenarioName) {
        expandSubassembly(componentName, scenarioName);
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
        return By.xpath(String.format("//div[.='%s']/ancestor::div[@role='row']//span[contains(text(),'%s')]/ancestor::div[@class='sticky-columns']//div[@data-header-id='thumbnail']", scenarioName.trim(), componentName.toUpperCase().trim()));
    }

    /**
     * Checks if the component is present on the page by size == 0 or > 0
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return pageUtils.waitForElementsToAppear(byScenarioName(componentName, scenarioName)).size();
    }

    /**
     * Checks if the component is present on the page by scenario state
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public boolean getListOfScenariosWithStatus(String componentName, String scenarioName, ScenarioStateEnum scenarioState) {
        String stateIcon;
        switch (scenarioState) {
            case PROCESSING_FAILED:
                stateIcon = "circle-xmark";
                break;
            case NOT_COSTED:
                stateIcon = "circle-minus";
                break;
            case COST_COMPLETE:
                stateIcon = "check";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + scenarioState);
        }
        return pageUtils.isElementDisplayed(byScenarioNameWithStatus(componentName, scenarioName, stateIcon));
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
     * Get the State of the specified scenario
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of icon
     */
    public String getScenarioState(String componentName, String scenarioName) {
        return getByParentLocator(componentName, scenarioName)
            .findElement(By.cssSelector("svg[id*='scenario-state-icon-']"))
            .getAttribute("data-icon");
    }

    /**
     * Get the Created At value for a given scenario
     *
     * @param componentName - Name of the component
     * @param scenarioName  - Name of the scenario
     * @return LocalDateTime representation of Created At value
     */
    public LocalDateTime getCreatedAt(String componentName, String scenarioName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/y, h:m a", Locale.US);
        String dateTimeString = getByParentLocator(componentName, scenarioName)
            .findElement(By.cssSelector("[data-header-id='scenarioCreatedAt']"))
            .getText();
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Get the Published state of the specified scenario
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of icon (Public | Private)
     */
    public String getPublishedState(String componentName, String scenarioName) {
        return getByParentLocator(componentName, scenarioName)
            .findElement(By.cssSelector("div[data-header-id='scenarioPublished'] svg.scenario-workspace-icon"))
            .getAttribute("aria-label");
    }

    /**
     * Get the State of the specified scenario
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of icon
     */
    public Double getScenarioFullyBurdenedCost(String componentName, String scenarioName) {
        String cost = getByParentLocator(componentName, scenarioName)
            .findElement(By.cssSelector("div[data-header-id='analysisOfScenario.fullyBurdenedCost'] span"))
            .getText();
        return Double.parseDouble(cost.replaceAll("[^0-9?!\\.]", ""));
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
        By byScenario = By.xpath(String.format("//div[.='%s']/ancestor::div[@role='row']//a[@class]//span[text()='%s']", scenarioName.trim(), componentName.toUpperCase().trim()));
        return byScenario;
    }

    /**
     * Get the scenario with the state status
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param stateIcon     - state icon
     * @return - by
     */
    private By byScenarioNameWithStatus(String componentName, String scenarioName, String stateIcon) {
        By byScenario = By.xpath(String.format("//div[.='%s']/ancestor::div[@role='row']//a[@class]//span[text()='%s']/ancestor::div[@role='row']/child::div[@data-header-id='scenarioState']//*[@data-icon='%s']",
            scenarioName.trim(),
            componentName.toUpperCase().trim(),
            stateIcon));
        return byScenario;
    }

    /**
     * Gets the parent part of the element
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return webelement
     */
    private WebElement getByParentLocator(String componentName, String scenarioName) {
        return pageUtils.waitForElementToAppear(By.xpath(String.format("//div[.='%s']/parent::div//span[contains(text(),'%s')]/ancestor::div[@role='row']", scenarioName.trim(), componentName.toUpperCase().trim())));
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
     * Highlights the scenario in the table using the keyboard shift key
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ScenarioTableController shiftHighlightScenario(String componentName, String scenarioName) {
        Actions shiftHighlight = new Actions(driver);
        shiftHighlight.sendKeys(Keys.LEFT_SHIFT)
            .click(pageUtils.waitForElementToAppear(byComponentName(componentName, scenarioName)))
            .build()
            .perform();
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
    public ScenarioTableController clickScenarioCheckbox(String componentName, String scenarioName) {
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
        By scenario = By.xpath(String.format("//span[contains(text(),'%s')]/ancestor::div[@role='row']//div[.='%s']/ancestor::div[@role='row']//span[@data-testid='checkbox']",
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
        return Stream.of(tableHeaders.getAttribute("innerText").split("")).collect(Collectors.toList());
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
            pageUtils.clickOnOffScreenElement(driver.findElement(By.xpath(String.format("//div[.='%s']//span", column.getColumns()))));
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
        return driver.findElement(byColumn).getAttribute("data-icon");
    }

    /**
     * Check if table column already displayed and add if not
     *
     * @param columnToAdd - Name of column to be added
     * @return - The current page object
     */
    public ScenarioTableController addColumn(ColumnsEnum columnToAdd) {
        if (!getTableHeaders().contains(columnToAdd.toString())) {
            componentTableActions.configure(configureButton)
                .selectColumn(columnToAdd)
                .moveColumn(DirectionEnum.RIGHT)
                .selectColumn(columnToAdd)
                .moveColumn(DirectionEnum.UP)
                .moveColumn(DirectionEnum.UP)
                .submit(ExplorePage.class);
        }
        return this;
    }

    /**
     * Gets the background colour of the cell
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return string
     */
    public String getCellColour(String componentName, String scenarioName) {
        return Color.fromString(pageUtils.waitForElementToAppear(By.xpath(String.format("//div[.='%s']/ancestor::div[@role='row']//span[contains(text(),'%s')]/ancestor::div[@role='row']",
            scenarioName.trim(), componentName.toUpperCase().trim()))).getCssValue("background-color")).asHex();
    }

    /**
     * Checks component is in a required state
     *
     * @param componentInfo - the component info builder object
     * @param scenarioState - the scenario state to check for
     * @return current page object
     */
    public ScenarioTableController checkComponentState(ComponentInfoBuilder componentInfo, ScenarioStateEnum scenarioState) {
        new ScenariosUtil().getScenarioState(componentInfo, scenarioState);
        return this;
    }

    /**
     * Calls an api with GET verb
     *
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState, not_costed"
     * @param userCredentials - the user credentials
     * @return current page object
     */
    public ScenarioTableController getCssComponents(UserCredentials userCredentials, String... paramKeysValues) {
        new CssComponent().getComponentParts(userCredentials, paramKeysValues);
        return this;
    }

    /**
     * Gets the column data from a table
     *
     * @param column          - the column to query
     * @param scenarioId      - the scenario identity
     * @param userCredentials - the user credentials
     * @return string
     */
    public String getColumnData(ColumnsEnum column, String scenarioId, UserCredentials userCredentials) {
        int columnPosition = IntStream.range(0, columnHeader.size())
            .filter(o -> columnHeader.get(o).getAttribute("textContent").equals(column.getColumns()))
            .findFirst()
            .getAsInt();

        return pageUtils.waitForElementsToAppear(By.cssSelector(String.format("[role='row'] [data-row-id='%s']", scenarioId)))
            .stream()
            .map(o -> o.getAttribute("textContent"))
            .collect(Collectors.toList())
            .get(columnPosition);
    }
}
