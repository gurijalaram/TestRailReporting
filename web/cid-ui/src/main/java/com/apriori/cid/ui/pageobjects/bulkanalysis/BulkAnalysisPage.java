package com.apriori.cid.ui.pageobjects.bulkanalysis;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cid.ui.pageobjects.common.ComponentTableActions;
import com.apriori.cid.ui.pageobjects.common.ScenarioTableController;
import com.apriori.cid.ui.pageobjects.navtoolbars.BulkAnalysisToolbar;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BulkAnalysisPage extends BulkAnalysisToolbar {

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;

    @FindBy(xpath = "//div[@data-testid = 'table-body']/div")
    private List<WebElement> listOfWorksheets;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;
    private ComponentTableActions componentTableActions;

    public BulkAnalysisPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(scenarioCount);
    }

    /**
     * Checks worksheets are present in the bulk analysis page
     *
     * @return boolean
     */
    public int getListOfWorksheets() {
        return pageUtils.waitForElementsToAppear(listOfWorksheets).size();
    }

    /**
     * Opens the scenario
     *
     * @param worksheetName - the name of the worksheet
     * @return a new page object
     */
    public WorksheetsExplorePage openWorksheet(String worksheetName) {
        By byWorksheet = By.xpath(String.format("//div[contains(.,'%s')][@data-testid = 'text-overflow']", worksheetName));
        pageUtils.waitForElementAndClick(byWorksheet);
        return new WorksheetsExplorePage(driver);
    }

    /**
     * Checks scenario count is displayed
     *
     * @return visibility of button
     */
    public boolean isScenarioCountPresent() {
        return scenarioCount.isDisplayed();
    }

    /**
     * Gets the count of scenarios found
     *
     * @return string
     */
    public String getComponentsFound() {
        return pageUtils.waitForElementToAppear(scenarioCount).getText();
    }

    /**
     * Get the Created At value for a given scenario
     *
     * @param componentName - Name of the component
     * @param scenarioName  - Name of the scenario
     * @return LocalDateTime representation of Created At value
     */
    public LocalDateTime getCreatedAt(String componentName, String scenarioName) {
        return scenarioTableController.getCreatedAt(componentName, scenarioName);
    }

    /**
     * Selects all scenarios on the page
     *
     * @return current page object
     */
    public BulkAnalysisPage selectAllScenarios() {
        scenarioTableController.selectAllScenarios();
        return this;
    }

    /**
     * check if element exists in the DOM
     *
     * @return boolean
     */
    public boolean isFilterTablePresent() {
        return pageUtils.isElementPresent(By.xpath("//h5[contains(.,'Scenario Filter')][@class = 'modal-title']"));
    }

    /**
     * Get Component and Scenario names of first scenario in table
     *
     * @return The component and scenario names in a comma-separated String
     */
    public String getFirstScenarioDetails() {
        return scenarioTableController.getFirstScenarioDetails();
    }

    /**
     * Highlights the worksheet in the table
     *
     * @param worksheetName - name of the part
     * @return current page object
     */
    public BulkAnalysisPage highlightWorksheet(String worksheetName) {
        By byWorksheet = with(By.cssSelector(".checkbox-cell")).toLeftOf(By.cssSelector(String.format("div[aria-label='%s']", worksheetName)));
        pageUtils.waitForElementAndClick(byWorksheet);
        return this;
    }

    /**
     * Check if worksheet is present on UI
     *
     * @param worksheetName - name of the worksheet
     * @return true/false
     */
    public boolean isWorksheetPresent(String worksheetName) {
        By byWorksheet = By.cssSelector(String.format("div[aria-label='%s']", worksheetName));
        return pageUtils.isElementDisplayed(byWorksheet);
    }

    /**
     * Checks the scenario is not displayed
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public boolean isScenarioNotDisplayed(String componentName, String scenarioName) {
        return scenarioTableController.isScenarioNotDisplayed(componentName, scenarioName);
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return scenarioTableController.getListOfScenarios(componentName, scenarioName);
    }

    /**
     * Multi-highlight scenarios
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public BulkAnalysisPage multiHighlightScenarios(String... componentScenarioName) {
        scenarioTableController.multiHighlightScenario(componentScenarioName);
        return this;
    }

    /**
     * Multi-select scenario
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public BulkAnalysisPage multiSelectScenarios(String... componentScenarioName) {
        scenarioTableController.multiSelectScenario(componentScenarioName);
        return this;
    }

    /**
     * Highlights the scenario in the table using the keyboard control key
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public BulkAnalysisPage controlHighlightScenario(String componentName, String scenarioName) {
        scenarioTableController.controlHighlightScenario(componentName, scenarioName);
        return this;
    }

    /**
     * Highlights the scenario in the table using the keyboard shift key
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public BulkAnalysisPage shiftHighlightScenario(String componentName, String scenarioName) {
        scenarioTableController.shiftHighlightScenario(componentName, scenarioName);
        return this;
    }

    /**
     * Gets the icon in the row
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return list of string
     */
    public List<String> getRowDetails(String componentName, String scenarioName) {
        return scenarioTableController.getRowDetails(componentName, scenarioName);
    }

    /**
     * Gets no scenarios message
     *
     * @return string
     */
    public String getScenarioMessage() {
        return pageUtils.waitForElementToAppear(noScenariosMessage).getText();
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return scenarioTableController.getTableHeaders();
    }

    /**
     * Gets the Published state of a given scenario
     *
     * @param componentName - The component name to be checked
     * @param scenarioName  - The scenario name to be checked
     * @return String representation of published column
     */
    public String getPublishedState(String componentName, String scenarioName) {
        return scenarioTableController.getPublishedState(componentName, scenarioName);
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public BulkAnalysisPage setPagination() {
        componentTableActions.setPagination();
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public BulkAnalysisPage enterKeySearch(String componentName) {
        componentTableActions.enterKeySearch(componentName.toUpperCase());
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public BulkAnalysisPage clickSearch(String componentName) {
        componentTableActions.clickSearch(componentName);
        return this;
    }

    /**
     * Sorts the column
     *
     * @param column - the column
     * @param order  - the order
     * @return current page object
     */
    public BulkAnalysisPage sortColumn(ColumnsEnum column, SortOrderEnum order) {
        scenarioTableController.sortColumn(column, order);
        pageUtils.waitForElementToAppear(scenarioCount);
        return this;
    }

    /**
     * Gets sort order
     *
     * @param column - the column
     * @return string
     */
    public String getSortOrder(ColumnsEnum column) {
        return scenarioTableController.getSortOrder(column);
    }

    /**
     * Gets all scenario Component Names from Explorer Table
     *
     * @return - list of all scenario Component Names
     */
    public List<String> getAllScenarioComponentName() {
        List<WebElement> rows =
            pageUtils.waitForElementsToAppear(By.xpath("//div[contains(@class,'table-cell')][contains(@data-header-id,'componentDisplayName')]"));
        List<String> componentNames = rows.stream().map(WebElement::getText).collect(Collectors.toList());
        componentNames.remove("Component Name");
        return componentNames;
    }

    /**
     * assert if element exists in the DOM
     *
     * @return boolean
     */
    public boolean isElementDisplayed(String searchedText, String className) {

        String xpath = "//div[contains(.,'".concat(searchedText).concat("')][@class = '").concat(className).concat("']");
        WebElement element = pageUtils.waitForElementToAppear(By.xpath(xpath));
        return element.isDisplayed();
    }

    /**
     * Gets the background colour of the cell
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return current page object
     */
    public String getCellColour(String componentName, String scenarioName) {
        return scenarioTableController.getCellColour(componentName, scenarioName);
    }

    /**
     * Gets the column data from a table
     *
     * @param column          - the column
     * @param scenarioId      - the scenario identity
     * @param userCredentials - the user credentials
     * @return string
     */
    public String getColumnData(ColumnsEnum column, String scenarioId, UserCredentials userCredentials) {
        return scenarioTableController.getColumnData(column, scenarioId, userCredentials);
    }
}
