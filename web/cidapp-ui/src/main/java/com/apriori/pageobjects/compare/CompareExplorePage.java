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

    @FindBy(id = "qa-scenario-list-filter-button")
    private WebElement filterButton;

    @FindBy(id = "qa-scenario-list-filter-selector")
    private WebElement filterDropdown;

    @FindBy(css = "div[data-testid='search-field'] input")
    private WebElement filterInput;

    @FindBy(id = "qa-comparison-explorer-configure-button")
    private WebElement configureButton;

    @FindBy(css = "div[role='status']")
    private WebElement loadingSpinner;


    @FindBy(css = "div.no-content.medium-no-content")
    private WebElement noScenariosMessage;

    @FindBy(css = ".comparison-row-link")
    private List<WebElement> comparisonNames;

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
        pageUtils.waitForElementToAppear(tableFiltersDiv);

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
     * Select single scenario
     *
     * @param comparisonName - Name of comparison to be selected
     *
     * @return - This page object
     */
    public CompareExplorePage selectComparison(String comparisonName) {
        findComparisonCheckbox(comparisonName).click();
        return this;
    }

    /**
     * Multi-select scenario
     *
     * @param comparisonNames - component name and method name
     * @return current page object
     */
    public CompareExplorePage multiSelectComparisons(String... comparisonNames) {
        Arrays.stream(comparisonNames).forEach(comparisonName -> findComparisonCheckbox(comparisonName).click());
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
        WebElement comparison = driver.findElement(By.xpath(String.format(scenarioLocator, comparisonName)));
        pageUtils.scrollWithJavaScript(comparison, true);
        pageUtils.mouseMove(comparison);
    }

    private WebElement findComparisonCheckbox(String comparisonName) {
        By comparison = By.xpath(
            String.format("//div[.='%s']/ancestor::div[@data-header-id='comparisonName']/ancestor::div[@role='row']//span[@data-testid='checkbox']",
            comparisonName));
        pageUtils.waitForElementToAppear(comparison);
        return pageUtils.scrollWithJavaScript(driver.findElement(comparison), true);
    }
}
