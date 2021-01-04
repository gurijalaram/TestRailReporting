package com.pageobjects.pages.compare;

import com.apriori.utils.PageUtils;

import com.pageobjects.common.ScenarioTablePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ScenarioNotesPage;
import com.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ComparePage extends LoadableComponent<ComparePage> {

    private final Logger logger = LoggerFactory.getLogger(ComparePage.class);

    @FindBy(css = "table.comparison-table-header-widget-table")
    private WebElement scenarioTable;

    @FindBy(css = "div.gwt-Label.comparison-table-header-part-number")
    private WebElement comparisonName;

    @FindBy(css = "textarea.gwt-TextArea.full-width")
    private WebElement descriptionText;

    @FindBy(css = "button.comparison-table-add-scenarios-button")
    private WebElement addScenariosButton;

    @FindBy(css = "[data-ap-comp='loadingComparisonData']")
    private WebElement loadingComparisonData;

    @FindBy(css = "div[data-ap-comp='scenarioTiles'] div.v-grid-scroller-horizontal")
    private WebElement horizontalScroller;

    @FindBy(css = "a.gwt-Anchor.comparison-table-header-scenario-state")
    private WebElement infoNotes;

    @FindBy(css = ".v-grid-scroller.v-grid-scroller-vertical")
    private WebElement componentScroller;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComparePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(scenarioTable);
        pageUtils.waitForSteadinessOfElement(By.xpath("//a[.='Info and Notes']"));
    }

    /**
     * Adds a new scenario
     *
     * @return new page object
     */
    public ScenarioTablePage addScenario() {
        pageUtils.waitForElementAndClick(addScenariosButton);
        return new ScenarioTablePage(driver);
    }

    /**
     * Gets the comparison description text
     *
     * @return the text as String
     */
    public String getDescriptionText() {
        By descriptionText = By.cssSelector("textarea.gwt-TextArea.full-width");
        pageUtils.waitForElementToAppear(descriptionText);
        return driver.findElement(descriptionText).getAttribute("value");
    }

    /**
     * Gets the comparison name
     *
     * @return string
     */
    public String getComparisonName() {
        pageUtils.waitForElementToAppear(comparisonName);
        return comparisonName.getText();
    }

    /**
     * Gets the locking status of the comparison
     *
     * @return true/false
     */
    public boolean isComparisonLockStatus(String lockStatus) {
        By lockedIcon = By.xpath(String.format("//div[@class='locked-status-icon fa fa-%s']", lockStatus));
        return pageUtils.waitForElementToAppear(lockedIcon).isDisplayed();
    }

    /**
     * @param workspace - the workspace
     * @return true/false
     */
    public boolean isComparisonWorkspace(String workspace) {
        By workspaceIcon = By.xpath(String.format("//div[contains(@class,'%s-workspace')]", workspace));
        return pageUtils.waitForElementToAppear(workspaceIcon).isDisplayed();
    }

    /**
     * Removes the scenario from the comparison view
     *
     * @param partName     - the part name
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ComparePage removeScenarioFromCompareView(String partName, String scenarioName) {
        By removeComparisonButton = By.xpath(String.format("//button[contains(@id,'rm_comp_btn_part_" + "%s" + "_" + "%s')]",
            partName.replace(" ", "_"), scenarioName.replace("-", "_")).toLowerCase());
        pageUtils.scrollToElement(removeComparisonButton, horizontalScroller, Constants.HORIZONTAL_SCROLL);
        pageUtils.waitForElementAndClick(removeComparisonButton);
        return this;
    }

    /**
     * Selects the basis button
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ComparePage setBasis(String partName, String scenarioName) {
        pageUtils.scrollToElement(findBasisButton(partName, scenarioName), horizontalScroller, Constants.HORIZONTAL_SCROLL);
        pageUtils.waitForElementAndClick(findBasisButton(partName, scenarioName));
        return this;
    }

    /**
     * Checks if the basis button exist
     *
     * @param scenarioName - the scenario name
     * @return true/false
     */
    public boolean isBasis(String partName, String scenarioName) {
        return pageUtils.invisibilityOfElements(driver.findElements(findBasisButton(partName, scenarioName)));
    }

    /**
     * Checks if the scenario is a basis
     *
     * @param partName     - the part name
     * @param scenarioName - the scenario name
     * @return true/false
     */
    public boolean isBasisButtonPresent(String partName, String scenarioName) {
        return pageUtils.isElementDisplayed(driver.findElement(findBasisButton(partName, scenarioName)));
    }

    /**
     * Gets list of scenarios in comparison view
     *
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return size of element as int
     */
    public boolean scenarioIsNotInComparisonView(String scenarioName, String partName) {
        By scenario = By.cssSelector(String.format("a[href*='#openFromSearch::sk,partState," + "%s" + "," + "%s" + "']", partName.toUpperCase(), scenarioName));
        return pageUtils.invisibilityOfElements(driver.findElements(scenario));
    }

    private By findBasisButton(String partName, String scenarioName) {
        return By.xpath(String.format("//div[@title='%s']/ancestor::th//a[contains(text(),'%s')]/ancestor::th//button[.='Basis']", partName.toUpperCase(), scenarioName));
    }

    /**
     * Opens scenarios from comparison view
     *
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @return new page object
     */
    public EvaluatePage openScenarioFromComparison(String partName, String scenarioName) {
        By scenarioNameLink = By.cssSelector(String.format("a[href*='#openFromSearch::sk,partState," + "%s" + "," + "%s" + "']", partName.toUpperCase(), scenarioName));
        driver.findElement(scenarioNameLink).click();
        return new EvaluatePage(driver);
    }

    /**
     * Clicks on Info&Notes link
     *
     * @return new page object
     */
    public ScenarioNotesPage selectInfoNotes() {
        pageUtils.waitForElementAndClick(infoNotes);
        return new ScenarioNotesPage(driver);
    }

    /**
     * Expands or collapses section of the comparison
     *
     * @param sectionName - name of the section
     * @return current page object
     */
    public ComparePage toggleSection(String sectionName) {
        By sectionDropdown = By.xpath(String.format("//div[.='%s']/ancestor::tr//label[@class='glyphicon']", sectionName));
        pageUtils.scrollToElement(sectionDropdown, componentScroller, Constants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(sectionDropdown);
        return this;
    }

    /**
     * Checks if info of appropriate section displayed
     *
     * @param columnName - name of column from section
     * @return true/false
     */
    public boolean isComparisonInfoDisplayed(String columnName) {
        By arrowInfoInput = By.xpath(String.format("//div[.='%s']", columnName));
        pageUtils.scrollToElement(arrowInfoInput, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.isElementDisplayed(arrowInfoInput);
    }

    /**
     * Checks if sections are collapsed
     *
     * @param sectionName - name of the section
     * @return attribute
     */
    public String isComparisonInfoNotDisplayed(String sectionName) {
        By sectionDropdown = By.xpath(String.format("//div[.='%s']/ancestor::tr//label[@class='glyphicon']/span", sectionName));
        return pageUtils.waitForElementToAppear(sectionDropdown).getAttribute("outerHTML");
    }

    /**
     * Checks the dfm risk score
     *
     * @return dfm risk score
     */
    public String getDfmRisk() {
        By dfmRisk = By.cssSelector("td.v-grid-cell .dfm-label");
        pageUtils.scrollToElement(dfmRisk, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(dfmRisk).getText();
    }

    /**
     * Gets warning count
     *
     * @return string
     */
    public String getCount(String label) {
        By count = By.xpath(String.format("//div[contains(text(),'%s')]/ancestor::tr//div[contains(@class,'comparison-table-ellipsis-value')]", label));
        pageUtils.scrollToElement(count, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(count).getAttribute("title");
    }
}
