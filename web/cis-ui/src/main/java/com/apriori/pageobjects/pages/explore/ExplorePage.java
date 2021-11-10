package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.common.CisComponentTableActions;
import com.apriori.pageobjects.common.CisScenarioTableController;
import com.apriori.pageobjects.navtoolbars.ExploreTabToolbar;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ExplorePage extends ExploreTabToolbar {

    @FindBy(css = ".icon-button-group .background-animation")
    private WebElement enabledStartComparison;

    @FindBy(css = "[data-icon='filter']")
    private WebElement filter;

    private PageUtils pageUtils;
    private WebDriver driver;
    private CisScenarioTableController scenarioTableController;
    private CisComponentTableActions componentTableActions;

    public ExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new CisScenarioTableController(driver);
        this.componentTableActions = new CisComponentTableActions(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
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
     * Check if the Start comparison button is enabled
     *
     * @return boolean
     */
    public boolean isStartComparisonEnabled() {
        return pageUtils.waitForElementToAppear(enabledStartComparison).isEnabled();
    }
}
