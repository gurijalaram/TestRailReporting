package com.apriori.cid.ui.pageobjects.bulkanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cid.ui.pageobjects.common.ComponentTableActions;
import com.apriori.cid.ui.pageobjects.common.ConfigurePage;
import com.apriori.cid.ui.pageobjects.common.FilterPage;
import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.cid.ui.pageobjects.common.ScenarioTableController;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class NewFromScenarioExplorePage extends LoadableComponent<NewFromScenarioExplorePage> {

    @FindBy(css = "div h5")
    private WebElement componentHeader;

    @FindBy(css = "input[name='bulkAnalysisName']")
    private WebElement inputBulkAnalysisName;

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

    @FindBy(css = "[id='qa-scenario-list-configure-button']")
    private WebElement configureButton;

    @FindBy(id = "qa-scenario-list-filter-button")
    private WebElement filterButton;

    @FindBy(id = "qa-scenario-selector-table-filter-selector")
    private WebElement filterDropdown;

    @FindBy(css = "[id='qa-scenario-list-filter-selector'] input")
    private WebElement filterInput;

    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;

    @FindBy(xpath = "//h2[.='Source Model']/..//button[.='Submit']")
    private WebElement submitButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;
    private ComponentTableActions componentTableActions;
    private ModalDialogController modalDialogController;

    public NewFromScenarioExplorePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals("New From Scenario", pageUtils.waitForElementToAppear(componentHeader).getAttribute("textContent"), "New From Scenario page was not displayed");
    }

    /**
     * Input bulk analysis name
     *
     * @param name - the name of the bulk analysis
     * @return page object
     */
    public NewFromScenarioExplorePage enterBulkAnalysisName(String name) {
        pageUtils.clearValueOfElement(inputBulkAnalysisName);
        inputBulkAnalysisName.sendKeys(name);
        return this;
    }

    /**
     * Sets pagination to by default
     *
     * @return current page object
     */
    public NewFromScenarioExplorePage setPagination() {
        componentTableActions.setPagination();
        return this;
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public NewFromScenarioExplorePage selectFilter(String filter) {
        pageUtils.typeAheadSelect(filterDropdown, "qa-scenario-selector-table-filter-selector", filter);
        setPagination();
        return this;
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public NewFromScenarioExplorePage highlightScenario(String componentName, String scenarioName) {
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
        return driver.findElements(getByScenario(componentName, scenarioName)).size();
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    private By getByScenario(String componentName, String scenarioName) {
        return By.xpath(String.format("//a[.='%s']/ancestor::div[@role='row']//span[normalize-space(.)='%s']", scenarioName.trim(), componentName.toUpperCase().trim()));
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
     * Open filters page
     *
     * @return new page object
     */
    public FilterPage filter() {
        return componentTableActions.filter(filterButton);
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public NewFromScenarioExplorePage enterKeySearch(String componentName) {
        componentTableActions.enterKeySearch(componentName);
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public NewFromScenarioExplorePage clickSearch(String componentName) {
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
    public NewFromScenarioExplorePage sortColumn(ColumnsEnum column, SortOrderEnum order) {
        scenarioTableController.sortColumn(column, order);
        return this;
    }

    /**
     * Checks if the scenario is clickable
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return by
     */
    public boolean isScenarioClickable(String componentName, String scenarioName) {
        return !pageUtils.waitForElementToAppear(scenarioTableController.byComponentRow(componentName, scenarioName)).getAttribute("class").contains("disabled");
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
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
