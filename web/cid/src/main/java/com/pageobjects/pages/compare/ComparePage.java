package com.pageobjects.pages.compare;

import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import com.pageobjects.common.ScenarioTablePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ScenarioNotesPage;
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
        return descriptionText.getText();
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

    public boolean isComparisonPublicWorkspace(String workspace) {
        By workspaceIcon = By.xpath(String.format("//div[@class='workspace-status-icon fa fa-users %s-workspace']", workspace));
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
     * @param partName     the part name
     * @return size of element as int
     */
    public boolean scenarioIsNotInComparisonView(String scenarioName, String partName) {
        By scenario = By.cssSelector(String.format("a[href*='#openFromSearch::sk,partState," + "%s" + "," + "%s" + "']", partName.toUpperCase(), scenarioName));
        return pageUtils.invisibilityOfElements(driver.findElements(scenario));
    }

    private By findBasisButton(String partName, String scenarioName) {
        return By.xpath(String.format("//div[@title='%s']/ancestor::th//a[contains(text(),'%s')]/ancestor::th//button[.='Basis']", partName.toUpperCase(), scenarioName));
    }

    public EvaluatePage openScenarioFromComparison(String partName, String scenarioName) {
        By scenarioNameLink = By.cssSelector(String.format("a[href*='#openFromSearch::sk,partState," + "%s" + "," + "%s" + "']", partName.toUpperCase(), scenarioName));
        driver.findElement(scenarioNameLink).click();
        return new EvaluatePage(driver);
    }

    public ScenarioNotesPage selectInfoNotes() {
        pageUtils.waitForElementAndClick(infoNotes);
        return new ScenarioNotesPage(driver);
    }

    public ComparePage selectInfoAndInputs() {
        By infoAndInputsSection = By.xpath("//div[contains(text(),'Info & Inputs')]");
        pageUtils.waitForElementAndClick(infoAndInputsSection);
        return this;
    }

    public ComparePage selectMaterialAndUtilization() {
        By materialAndUtilizationSection = By.xpath("//div[contains(text(),'Material & Utilization')]");
        pageUtils.scrollToElement(materialAndUtilizationSection, componentScroller, Constants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(materialAndUtilizationSection);
        return this;
    }

    public ComparePage selectDesignGuidanceSection() {
        By designGuidanceSection = By.xpath("//div[contains(text(),'Design Guidance')]");
        pageUtils.scrollToElement(designGuidanceSection, componentScroller, Constants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(designGuidanceSection);
        return this;
    }

    public ComparePage selectProcess() {
        By processSection = By.xpath("//span[@class='fa fa-caret-right']/ancestor::div[.='Process']");
        pageUtils.scrollToElement(processSection, componentScroller, Constants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(processSection);
        return this;
    }

    public ComparePage selectProcess2() {
        By processSection = By.xpath("//span[@class='fa fa-caret-down']/ancestor::div[.='Process']");
        pageUtils.scrollToElement(processSection, componentScroller, Constants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(processSection);
        return this;
    }

    public ComparePage selectCostResults() {
        By costResultsSection = By.xpath("//div[contains(text(),'Cost Results')]");
        pageUtils.scrollToElement(costResultsSection, componentScroller, Constants.ARROW_DOWN);
        pageUtils.waitForElementAndClick(costResultsSection);
        return this;
    }

    public boolean isInfoInputsSectionCollapsed(String arrowDirection) {
        By arrowInfoInput = By.xpath(String.format("//div[contains(text(),'Info & Inputs')]/ancestor::tr//span[@class = 'fa fa-caret-%s']", arrowDirection));
        return pageUtils.waitForElementToAppear(arrowInfoInput).isDisplayed();
    }

    public boolean isMaterialUtilizationCollapsed(String arrowDirection) {
        By arrowMaterialUtilization = By.xpath(String.format("//div[contains(text(),'Material & Utilization')]/ancestor::tr//span[@class = 'fa fa-caret-%s']", arrowDirection));
        pageUtils.scrollToElement(arrowMaterialUtilization, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(arrowMaterialUtilization).isDisplayed();
    }

    public boolean isDesignGuidanceCollapsed(String arrowDirection) {
        By arrowDesignGuidance = By.xpath(String.format("//div[contains(text(),'Design Guidance')]/ancestor::tr//span[@class = 'fa fa-caret-%s']", arrowDirection));
        pageUtils.scrollToElement(arrowDesignGuidance, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(arrowDesignGuidance).isDisplayed();
    }

    public boolean isProcessCollapsed(String arrowDirection) {
        By arrowProcess = By.xpath(String.format("//div[contains(text(),'Process')]/ancestor::tr//span[@class = 'fa fa-caret-%s']", arrowDirection));
        pageUtils.scrollToElement(arrowProcess, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(arrowProcess).isDisplayed();
    }

    public boolean isCostResultCollapsed(String arrowDirection) {
        By arrowCostResult = By.xpath(String.format("//div[contains(text(),'Cost Results')]/ancestor::tr//span[@class = 'fa fa-caret-%s']", arrowDirection));
        pageUtils.scrollToElement(arrowCostResult, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(arrowCostResult).isDisplayed();
    }

    public String getDfmRisk() {
        By dfmRisk = By.cssSelector("td.v-grid-cell .dfm-label");
        pageUtils.scrollToElement(dfmRisk, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(dfmRisk).getText();
    }

    public String getWarningsCount() {
        By warnings = By.xpath("//div[contains(text(),'Warnings')]/ancestor::tr//div[@class = 'gwt-Label comparison-table-row-value-cell-table-formula-value comparison-table-ellipsis-value']");
        pageUtils.scrollToElement(warnings, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(warnings).getAttribute("title");
    }

    public String getGuidanceIssuesCount() {
        By guidanceIssues = By.xpath("//div[contains(text(),'Guidance Issues')]/ancestor::tr//div[@class = 'gwt-Label comparison-table-row-value-cell-table-formula-value comparison-table-ellipsis-value']");
        pageUtils.scrollToElement(guidanceIssues, componentScroller, Constants.ARROW_DOWN);
        return pageUtils.waitForElementToAppear(guidanceIssues).getAttribute("title");
    }


}
