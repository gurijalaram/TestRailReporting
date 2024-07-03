package com.apriori.cid.ui.pageobjects.bulkanalysis;

import com.apriori.cid.ui.pageobjects.common.ComponentTableActions;
import com.apriori.cid.ui.pageobjects.common.ConfigurePage;
import com.apriori.cid.ui.pageobjects.common.FilterPage;
import com.apriori.cid.ui.pageobjects.common.ScenarioTableController;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.PreviewPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.BulkAnalysisToolbar;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ScenarioStateEnum;
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

/**
 * @author cfrith
 */

@Slf4j
public class WorksheetsExplorePage extends BulkAnalysisToolbar {

    @FindBy(css = "[id='qa-scenario-explorer-configure-button']")
    private WebElement configureButton;

    @FindBy(css = "[id='qa-scenario-explorer-filter-button'] button")
    private WebElement filterButton;

    @FindBy(css = "[id='qa-sub-component-detail-filter-button'] button")
    private WebElement filterButtonOnTableView;

    @FindBy(css = "[id='qa-scenario-explorer-preview-button'] button")
    private WebElement previewButton;

    @FindBy(id = "qa-scenario-explorer-filter-selector")
    private WebElement filterDropdown;

    @FindBy(css = "[id='qa-scenario-list-filter-selector'] input")
    private WebElement filterInput;

    @FindBy(css = "[id='qa-scenario-explorer-filter-selector']")
    private WebElement currentFilter;

    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;
    private ComponentTableActions componentTableActions;

    public WorksheetsExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(setInputsButton);
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public ExplorePage selectFilter(String filter) {
        pageUtils.typeAheadSelect(filterDropdown, "qa-scenario-explorer-filter-selector", filter);
        setPagination();
        return new ExplorePage(driver);
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
    public WorksheetsExplorePage selectAllScenarios() {
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
     * Opens the scenario
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return a new page object
     */
    public EvaluatePage openScenario(String componentName, String scenarioName) {
        scenarioTableController.openScenario(componentName, scenarioName);
        return new EvaluatePage(driver);
    }

    /**
     * Opens the first scenario
     *
     * @return a new page object
     */
    public EvaluatePage openFirstScenario() {
        scenarioTableController.openFirstScenario();
        return new EvaluatePage(driver);
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
     * Highlights the scenario in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public WorksheetsExplorePage highlightScenario(String componentName, String scenarioName) {
        scenarioTableController.highlightScenario(componentName, scenarioName);
        return this;
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
     * Gets the number of elements with state present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return boolean
     */
    public boolean getListOfScenariosWithStatus(String componentName, String scenarioName, ScenarioStateEnum scenarioState) {
        return scenarioTableController.getListOfScenariosWithStatus(componentName, scenarioName, scenarioState);
    }

    /**
     * Open configure page
     *
     * @return new page object
     */
    public ConfigurePage configure() {
        return componentTableActions.configure(configureButton);
    }

    /**
     * Multi-highlight scenarios
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public WorksheetsExplorePage multiHighlightScenarios(String... componentScenarioName) {
        scenarioTableController.multiHighlightScenario(componentScenarioName);
        return this;
    }

    /**
     * Multi-select scenario
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public WorksheetsExplorePage multiSelectScenarios(String... componentScenarioName) {
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
    public WorksheetsExplorePage controlHighlightScenario(String componentName, String scenarioName) {
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
    public WorksheetsExplorePage shiftHighlightScenario(String componentName, String scenarioName) {
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
     * Gets the data-icon value for the State icon
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return String representation of state icon
     */
    public String getScenarioState(String componentName, String scenarioName) {
        return scenarioTableController.getScenarioState(componentName, scenarioName);
    }

    /**
     * Open filters page
     *
     * @return new page object
     */
    public FilterPage filter() {
        return componentTableActions.filter(filterButton);
    }

    /**
     * Open filters page on Table View
     *
     * @return new page object
     */
    public FilterPage filterOnTableView() {
        return componentTableActions.filter(filterButtonOnTableView);
    }

    /**
     * Opens the preview panel
     *
     * @return new page object
     */
    public PreviewPage openPreviewPanel() {
        return componentTableActions.openPreviewPanel(previewButton);
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
    public WorksheetsExplorePage setPagination() {
        componentTableActions.setPagination();
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public WorksheetsExplorePage enterKeySearch(String componentName) {
        componentTableActions.enterKeySearch(componentName.toUpperCase());
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public WorksheetsExplorePage clickSearch(String componentName) {
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
    public WorksheetsExplorePage sortColumn(ColumnsEnum column, SortOrderEnum order) {
        scenarioTableController.sortColumn(column, order);
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
     * Checks component is in a required state
     *
     * @param componentInfo - the component info builder object
     * @param scenarioState - the scenario state to check for
     * @return current page object
     */
    public WorksheetsExplorePage checkComponentStateRefresh(ComponentInfoBuilder componentInfo, ScenarioStateEnum scenarioState) {
        scenarioTableController.checkComponentState(componentInfo, scenarioState);
        refresh();
        return this;
    }

    /**
     * Calls an api with GET verb
     *
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState, not_costed"
     * @param userCredentials - the user credentials
     * @return current page object
     */
    public WorksheetsExplorePage getCssComponents(UserCredentials userCredentials, String... paramKeysValues) {
        scenarioTableController.getCssComponents(userCredentials, paramKeysValues);
        return this;
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
     * Check if table column already displayed and add if not
     *
     * @param columnToAdd - Name of column to be added
     * @return - The current page object
     */
    public WorksheetsExplorePage addColumn(ColumnsEnum columnToAdd) {
        scenarioTableController.addColumn(columnToAdd);
        return this;
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
