package com.apriori.pageobjects.compare;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ComponentTableActions;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.common.ScenarioTableController;
import com.apriori.pageobjects.navtoolbars.CompareToolbar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class CompareExplorePage extends CompareToolbar {

    private static final Logger logger = LoggerFactory.getLogger(CompareExplorePage.class);

    @FindBy(css = "div[class='card-header'] .left")
    private WebElement tableFiltersDiv;

    @FindBy(css = "div h5")
    private WebElement componentHeader;

    @FindBy(css = "div[role='status']")
    private WebElement loadingSpinner;

    @FindBy(id = "qa-sub-header-refresh-view-button")
    private WebElement refreshButton;

    @FindBy(id = "qa-comparison-explorer-configure-button")
    private WebElement configureButton;

    @FindBy(id = "qa-scenario-list-filter-button")
    private WebElement filterButton;

    @FindBy(id = "qa-scenario-list-preview-button")
    private WebElement previewButton;

    @FindBy(id = "qa-scenario-list-filter-selector")
    private WebElement filterDropdown;

    @FindBy(css = "div[data-testid='search-field'] input")
    private WebElement filterInput;

    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;

    @FindBy(css = "placeholder...")
    private WebElement submitButton;

    // ToDo:- Rethink name
    @FindBy(css = "div[class='card-header'] .left")
    private WebElement comparisonCount;

    @FindBy(css = ".comparison-row-link")
    private List<WebElement> comparisonNames;

//    private String scenarioLocator = "//div[aria-label='%s']/..";
    private String scenarioLocator = "//div[.='%s']/ancestor::div[@data-header-id='comparisonName']//a";
    private PageUtils pageUtils;
    private WebDriver driver;
    private ScenarioTableController scenarioTableController;
    private ComponentTableActions componentTableActions;
    private ModalDialogController modalDialogController;

    public CompareExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.scenarioTableController = new ScenarioTableController(driver);
        this.componentTableActions = new ComponentTableActions(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementNotVisible(loadingSpinner, 2);
        pageUtils.waitForElementToAppear(comparisonCount);

    }

    /**
     * Uses type ahead to input the filter
     *
     * @param filter - the filter
     * @return current page object
     */
    public CompareExplorePage selectFilter(String filter) {
        pageUtils.typeAheadSelect(filterDropdown, "modal-body", filter);
        return this;
    }

    /**
     * Check Comparison Name Filter display state
     *
     * @return Boolean of Comparison Name filter display state
     */
    public Boolean isComparisonNameFilterDisplayed() {
        return filterInput.isDisplayed();
    }

    /**
     * Opens the scenario
     *
     * @param comparisonName - name of the part
     * @return a new page object
     */
    public ComparePage openComparison(String comparisonName) {
        By comparisonSelector = By.xpath(String.format(scenarioLocator, comparisonName));
        pageUtils.waitForElementToAppear(comparisonSelector);
        moveToComparison(comparisonName);
        pageUtils.waitForElementAndClick(comparisonSelector);
        return new ComparePage(driver);
    }

    /**
     * Click the refresh button
     *
     * @return New copy of current page object
     */
    public CompareExplorePage clickRefresh() {
        pageUtils.waitForElementAndClick(refreshButton);
        return new CompareExplorePage(driver);
    }

    /**
     * Highlights the scenario in the table
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return current page object
     */
    public CompareExplorePage highlightScenario(String componentName, String scenarioName) {
        scenarioTableController.highlightScenario(componentName, scenarioName);
        return this;
    }

    /**
     * Gets the number of elements present on the page
     *
     * @return List of all Comparison Names
     */
    public List<String> getListOfComparisons()
    {
        return comparisonNames.stream().map(WebElement::getText).collect(Collectors.toList());
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
    public CompareExplorePage multiHighlightScenarios(String... componentScenarioName) {
        scenarioTableController.multiHighlightScenario(componentScenarioName);
        return this;
    }

    /**
     * Multi-select scenario
     *
     * @param componentScenarioName - component name and method name
     * @return current page object
     */
    public CompareExplorePage multiSelectScenarios(String... componentScenarioName) {
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
    public CompareExplorePage controlHighlightScenario(String componentName, String scenarioName) {
        scenarioTableController.controlHighlightScenario(componentName, scenarioName);
        return this;
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
     * Hovers over the Comparison
     *
     * @param comparisonName - component name
     */
    private void moveToComparison(String comparisonName) {
        WebElement comparison = driver.findElement(By.xpath(String.format(scenarioLocator, comparisonName)));            ;
        pageUtils.scrollWithJavaScript(comparison, true);
        pageUtils.mouseMove(comparison);
    }
}
