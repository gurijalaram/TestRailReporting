package com.apriori.pageobjects.pages.compare;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.ScenarioTableController;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author nvalieieva
 */

public class ModifyComparisonPage extends LoadableComponent<ModifyComparisonPage> {

    private static final Logger logger = LoggerFactory.getLogger(ModifyComparisonPage.class);

    @FindBy(id = "qa-scenario-selector-table-filter-selector")
    private WebElement filterDropdown;

    @FindBy(id = "qa-scenario-selector-table-table-config-button")
    private WebElement configureButton;

    @FindBy(id = "qa-scenario-selector-table-filter-button")
    private WebElement filterButton;

    @FindBy(css = ".scenario-selector-table .action-buttons-container [type='submit']")
    private WebElement submitButton;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;
    private ModalDialogController modalDialogController;
    private ComponentTableActions componentTableActions;

    public ModifyComparisonPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);

    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(filterButton);
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public ModifyComparisonPage selectFilter(String filter) {
        pageUtils.typeAheadSelect(filterDropdown, "modal-body", filter);
        return this;
    }

    /**
     * Sorts the column
     *
     * @param column - the column
     * @param order  - the order
     * @return current page object
     */
    public ModifyComparisonPage sortColumn(ColumnsEnum column, SortOrderEnum order) {
        scenarioTableController.sortColumn(column, order);
        return this;
    }

    /**
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ModifyComparisonPage clickScenarioCheckbox(String componentName, String scenarioName) {
        scenarioTableController.clickScenarioCheckbox(componentName, scenarioName);
        return this;
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

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return scenarioTableController.getTableHeaders();
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
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return scenarioTableController.getListOfScenarios(componentName, scenarioName);
    }
}