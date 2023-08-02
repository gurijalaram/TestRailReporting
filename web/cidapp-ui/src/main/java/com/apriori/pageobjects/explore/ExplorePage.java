package com.apriori.pageobjects.explore;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.PageUtils;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.navtoolbars.ExploreToolbar;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.utils.CssComponent;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class ExplorePage extends ExploreToolbar {

    private static final Logger logger = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

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

    public ExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(scenarioCount);
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
     * Get Currently selected Filter
     *
     * @return The current filter name as a String
     */
    public String getCurrentFilter() {
        return currentFilter.getText();
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
    public ExplorePage selectAllScenarios() {
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
    public ExplorePage highlightScenario(String componentName, String scenarioName) {
        scenarioTableController.highlightScenario(componentName, scenarioName);
        return this;
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
    public ExplorePage multiHighlightScenarios(String... componentScenarioName) {
        scenarioTableController.multiHighlightScenario(componentScenarioName);
        return this;
    }

    /**
     * Multi-select scenario
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public ExplorePage multiSelectScenarios(String... componentScenarioName) {
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
    public ExplorePage controlHighlightScenario(String componentName, String scenarioName) {
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
    public ExplorePage shiftHighlightScenario(String componentName, String scenarioName) {
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
     * Gets the processing failed state
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param currentUser   -  current user
     * @return - String
     */
    public String getScenarioState(String componentName, String scenarioName, UserCredentials currentUser, ScenarioStateEnum stateEnum) {
        List<ScenarioItem> itemResponse = new CssComponent().getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName,
            SCENARIO_STATE_EQ.getKey() + stateEnum.getState());

        return itemResponse.stream().filter(item ->
            item.getScenarioState().equalsIgnoreCase(stateEnum.getState())).findFirst().get().getScenarioState();
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
    public ExplorePage setPagination() {
        componentTableActions.setPagination();
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ExplorePage enterKeySearch(String componentName) {
        componentTableActions.enterKeySearch(componentName.toUpperCase());
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public ExplorePage clickSearch(String componentName) {
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
    public ExplorePage sortColumn(ColumnsEnum column, SortOrderEnum order) {
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
     * Checks component is in a required state
     *
     * @param componentInfo - the component info builder object
     * @param scenarioState - the scenario state to check for
     * @return current page object
     */
    public ExplorePage checkComponentStateRefresh(ComponentInfoBuilder componentInfo, ScenarioStateEnum scenarioState) {
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
    public ExplorePage getCssComponents(UserCredentials userCredentials, String... paramKeysValues) {
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
    public ExplorePage addColumn(ColumnsEnum columnToAdd) {
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
