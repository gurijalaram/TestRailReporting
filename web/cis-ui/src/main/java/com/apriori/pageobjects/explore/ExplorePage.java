package com.apriori.pageobjects.explore;

import com.apriori.common.CisComponentTableActions;
import com.apriori.common.CisScenarioTableController;
import com.apriori.navtoolbars.ExploreTabToolbar;

import com.utils.CisColumnsEnum;
import com.utils.CisSortOrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class ExplorePage extends ExploreTabToolbar {

    @FindBy(css = ".icon-button-group .background-animation")
    private WebElement enabledStartComparison;

    @FindBy(css = "div[id=qa-scenario-explorer-filter-selector]")
    private WebElement presetFilterDropdown;

    @FindBy(css = ".header-left .text-overflow")
    private WebElement presetFilter;

    private CisScenarioTableController scenarioTableController;
    private CisComponentTableActions componentTableActions;

    public ExplorePage(WebDriver driver) {
        super(driver);
        this.scenarioTableController = new CisScenarioTableController(driver);
        this.componentTableActions = new CisComponentTableActions(driver);
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
     * Search for a component
     *
     * @param componentName - the component name
     * @return current page object
     */
    public ExplorePage enterKeySearch(String componentName) {
        componentTableActions.enterKeySearch(componentName.toUpperCase());
        return this;
    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public ExplorePage selectPresetFilter(String filter) {
        getPageUtils().typeAheadSelect(presetFilterDropdown, filter);
        setPagination();
        return this;
    }

    /**
     * Search for component
     *
     * @param componentName - the component name
     * @return current page object
     */
    public ExplorePage clickSearch(String componentName) {
        componentTableActions.clickSearch(componentName);
        return this;
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
     * Selects the scenario by checkbox
     *
     * @param componentName - component name
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public ExplorePage selectScenarioTickBox(String componentName, String scenarioName) {
        scenarioTableController.selectScenario(componentName, scenarioName);
        return this;
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
     * Clicks on the first scenario
     *
     * @return current page object
     */
    public ExplorePage openFirstScenario() {
        scenarioTableController.openFirstScenario();
        return this;
    }

    /**
     * Check if the Start comparison button is enabled
     *
     * @return boolean
     */
    @Override
    public boolean isStartComparisonEnabled() {
        return getPageUtils().waitForElementToAppear(enabledStartComparison).isEnabled();
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
     * Sorts the column
     *
     * @param column - the column
     * @param order  - the order
     * @return current page object
     */
    public ExplorePage sortColumn(CisColumnsEnum column, CisSortOrderEnum order) {
        scenarioTableController.sortColumn(column, order);
        return this;
    }

    /**
     * Gets the sort order
     *
     * @param column - the column
     * @return string
     */
    public String getSortOrder(CisColumnsEnum column) {
        return scenarioTableController.getSortOrder(column);
    }

    /**
     * Gets the Preset filter type
     *
     * @return String
     */
    public String getPresetFilterType() {
        return getPageUtils().waitForElementToAppear(presetFilter).getAttribute("textContent");
    }
}
