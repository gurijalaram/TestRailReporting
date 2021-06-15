package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import utils.ColumnsEnum;
import utils.SortOrderEnum;

/**
 * @author cfrith
 */

@Slf4j
public class SourceModelExplorePage extends LoadableComponent<SourceModelExplorePage> {

    @FindBy(css = "div h5")
    private WebElement componentHeader;

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

    @FindBy(css = "[id='qa-scenario-list-configure-button']")
    private WebElement configureButton;

    @FindBy(id = "qa-scenario-list-filter-button")
    private WebElement filterButton;

    @FindBy(id = "qa-scenario-list-filter-selector")
    private WebElement filterDropdown;

    @FindBy(css = "[id='qa-scenario-list-filter-selector'] input")
    private WebElement filterInput;

    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;
    private ComponentTableActions componentTableActions;
    private ModalDialogController modalDialogController;

    public SourceModelExplorePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(componentHeader);
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public SourceModelExplorePage inputFilter(String filter) {
        pageUtils.typeAheadInput(filterDropdown, filterInput, filter);
        return this;
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public SourceModelExplorePage highlightScenario(String componentName, String scenarioName) {
        pageUtils.waitForElementAndClick(getByScenario(componentName, scenarioName));
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
        return By.xpath(String.format("//div[@class='scenario-selector-table']//div[.='%s']/ancestor::div//span[normalize-space(.)='%s']", scenarioName.trim(), componentName.toUpperCase().trim()));
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
    public SourceModelExplorePage enterKeySearch(String componentName) {
        componentTableActions.enterKeySearch(componentName);
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return new page object
     */
    public SourceModelExplorePage clickSearch(String componentName) {
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
    public SourceModelExplorePage sortColumn(ColumnsEnum column, SortOrderEnum order) {
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
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
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