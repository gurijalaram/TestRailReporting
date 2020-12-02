package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.navtoolbars.ExploreToolbar;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ExplorePage extends ExploreToolbar {

    private final Logger LOGGER = LoggerFactory.getLogger(ExplorePage.class);

    @FindBy(xpath = "//button[.='Filters']")
    private WebElement filtersButton;

    @FindBy(css = "button[class='dropdown-toggle btn btn-primary']")
    private WebElement paginatorDropdown;

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement scenarioCount;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;

    public ExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(filtersButton);
    }

    /**
     * Checks filter button is displayed
     *
     * @return visibility of button
     */
    public boolean isFilterButtonPresent() {
        return filtersButton.isDisplayed();
    }

    /**
     * Gets the count of scenarios found
     *
     * @return string
     */
    public String getScenariosFound() {
        return pageUtils.waitForElementAppear(scenarioCount).getText();
    }

    /**
     * Opens the scenario
     *
     * @param componentName     - name of the part
     * @param scenarioName - scenario name
     * @return a new page object
     */
    public EvaluatePage openScenario(String componentName, String scenarioName) {
        scenarioTableController.openScenario(componentName, scenarioName);
        return new EvaluatePage(driver);
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName     - name of the part
     * @param scenarioName - scenario name
     * @return current page object
     */
    public ExplorePage highlightScenario(String componentName, String scenarioName) {
        scenarioTableController.highlightScenario(componentName, scenarioName);
        return this;
    }
}
