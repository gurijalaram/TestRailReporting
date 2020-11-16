package com.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class ReferenceComparePage extends LoadableComponent<ReferenceComparePage> {

    private final Logger logger = LoggerFactory.getLogger(ReferenceComparePage.class);

    @FindBy(css = "[data-ap-comp='baselineScenarioSelection']")
    private WebElement filterDropdown;

    @FindBy(css = "div[data-ap-region='inputsTile']")
    private WebElement inputsTile;

    @FindBy(css = "div[data-ap-region='materialTile']")
    private WebElement materialUtilizationTile;

    @FindBy(css = "div[data-ap-region='costDriversGuidanceTile']")
    private WebElement designGuidanceTile;

    @FindBy(css = "div[data-ap-region='cycleTimeTile']")
    private WebElement processTile;

    @FindBy(css = "div[data-ap-region='costResultsTile']")
    private WebElement costResultsTile;

    @FindBy(css = "[data-ap-field='processGroupSelection.baseline']")
    private WebElement processGroupBaseline;

    @FindBy(css = "[data-ap-field='primaryVpeName.baseline']")
    private WebElement vpeBaseline;

    @FindBy(css = "[data-ap-field='baselineUserOverridesCount']")
    private WebElement secondaryProcessesBaseline;

    @FindBy(css = "[data-ap-field='annualVolume.baseline']")
    private WebElement annualVolumeBaseline;

    @FindBy(css = "[data-ap-field='productionLife.baseline']")
    private WebElement productionLifeBaseline;

    @FindBy(css = "[data-ap-field='materialNameOverride.baseline']")
    private WebElement materialBaseline;

    @FindBy(css = "[data-ap-field='finishMass.baseline']")
    private WebElement finishMassBaseline;

    @FindBy(css = "[data-ap-field='utilization.baseline']")
    private WebElement utilizationBaseline;

    @FindBy(css = "[data-ap-field='failuresWarningsCount.baseline']")
    private WebElement failuresWarningsCountBaseline;

    @FindBy(css = "[data-ap-field='dtcMessagesCount.baseline']")
    private WebElement guidanceIssuesBaseline;

    @FindBy(css = "[data-ap-field='gcdWithTolerancesCount.baseline']")
    private WebElement gcdWithTolerancesCountBaseline;

    @FindBy(css = "[data-ap-field='cycleTime.baseline']")
    private WebElement cycleTimeBaseline;

    @FindBy(css = "[data-ap-field='materialCost.baseline']")
    private WebElement materialCostBaseline;

    @FindBy(css = "[data-ap-field='totalCost.baseline']")
    private WebElement piecePartCostBaseline;

    @FindBy(css = "[data-ap-field='fullyBurdenedCost.baseline']")
    private WebElement fullyBurdenedCostBaseline;

    @FindBy(css = "[data-ap-field='capitalInvestment.baseline']")
    private WebElement capitalInvestmentBaseline;

    @FindBy(css = "[data-ap-field='capitalInvestment.indicator']")
    private WebElement capitalInvestmentIndicator;

    @FindBy(css = "[data-ap-field='fullyBurdenedCost.indicator']")
    private WebElement fullyBurdenedCostIndicator;

    @FindBy(css = "[data-ap-field='totalCost.indicator']")
    private WebElement totalCostIndicator;

    @FindBy(css = "[data-ap-field='materialCost.indicator']")
    private WebElement materialCostIndicator;

    @FindBy(css = "th[data-ap-comp='comparisonSectionVisibilityButton']")
    private WebElement referenceChevron;

    @FindBy(css = ".gwt-InlineHTML.glyphicon.glyphicon-chevron-left")
    private WebElement chevron;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ReferenceComparePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(filterDropdown);
    }

    /**
     * Selects the dropdown
     *
     * @return current page object
     */
    public ReferenceComparePage selectDropdown() {
        pageUtils.waitForElementAndClick(filterDropdown);
        return this;
    }

    /**
     * Selects the scenario in the dropdown
     *
     * @param workspaceType - the workspace
     * @param scenarioName  - the scenario
     * @return current page object
     */
    public ReferenceComparePage selectDropdownScenario(String workspaceType, String scenarioName) {
        List<WebElement> scenario = driver.findElements(By.xpath("//a[contains(@data-normalized-text,'" + workspaceType.toLowerCase() + "-workspace')]//span[contains(text(),'" + scenarioName + "')]"));
        scenario.stream().filter(dropdown -> dropdown.getText().equalsIgnoreCase(scenarioName)).forEach(WebElement::click);
        return this;
    }

    /**
     * Gets input tile info
     *
     * @return input as string
     */
    public List<String> getInputsTile() {
        return Arrays.stream(inputsTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets material utilization info
     *
     * @return material utilization as string
     */
    public List<String> getMaterialsUtilizationTile() {
        return Arrays.stream(materialUtilizationTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets design guidance info
     *
     * @return design guidance as string
     */
    public List<String> getDesignGuidanceTile() {
        return Arrays.stream(designGuidanceTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets process tile info
     *
     * @return process tile as string
     */
    public List<String> getProcessesTile() {
        return Arrays.stream(processTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets cost result info
     *
     * @return cost result as string
     */
    public List<String> getCostResultsTile() {
        return Arrays.stream(costResultsTile.getText().split("\n")).collect(Collectors.toList());
    }

    /**
     * Gets reference panel process group
     *
     * @return as string
     */
    public String getReferenceProcessGroup() {
        By groupBase = By.cssSelector("[data-ap-field='processGroupSelection.baseline']");
        return getAttribute(groupBase, "innerText");
    }

    /**
     * Gets reference panel VPE
     *
     * @return as string
     */
    public String getReferenceVPE() {
        By vpeBase = By.cssSelector("[data-ap-field='primaryVpeName.baseline']");
        return getAttribute(vpeBase, "innerText");
    }

    /**
     * Gets reference panel Secondary Processes
     *
     * @return as string
     */
    public String getReferenceSecondaryProcesses() {
        By processBase = By.cssSelector("[data-ap-field='baselineUserOverridesCount']");
        return getAttribute(processBase, "innerText");
    }

    /**
     * Gets reference panel Annual Volume
     *
     * @return as string
     */
    public String getReferenceAnnualVolume() {
        By annualBase = By.cssSelector("[data-ap-field='annualVolume.baseline']");
        return getAttribute(annualBase, "innerText");
    }

    /**
     * Gets reference panel Production Life
     *
     * @return as string
     */
    public String getReferenceProductionLife() {
        By productionBase = By.cssSelector("[data-ap-field='productionLife.baseline']");
        return getAttribute(productionBase, "innerText");
    }

    /**
     * Gets reference panel Material
     *
     * @return as string
     */
    public String getReferenceMaterial() {
        By materialBase = By.cssSelector("[data-ap-field='materialNameOverride.baseline']");
        return getAttribute(materialBase, "innerText");
    }

    /**
     * Gets reference panel Mass
     *
     * @return as string
     */
    public String getReferenceFinishMass() {
        By finishMass = By.cssSelector("[data-ap-field='finishMass.baseline']");
        return getAttribute(finishMass, "innerText");
    }

    /**
     * Gets reference Utilization
     *
     * @return double
     */
    public double getReferenceUtilization() {
        By utilizationBase = By.cssSelector("[data-ap-field='utilization.baseline']");
        return Double.parseDouble(getAttribute(utilizationBase, "innerText"));
    }

    /**
     * Gets Material Cost Delta
     *
     * @return as string
     */
    public String getMaterialCostDelta() {
        By materialIndicator = By.cssSelector("[data-ap-field='materialCost.indicator']");
        return getAttribute(materialIndicator, "innerHTML");
    }

    /**
     * Gets Piece Part Cost Delta
     *
     * @return as string
     */
    public String getPiecePartCostDelta() {
        By partCostIndicator = By.cssSelector("[data-ap-field='totalCost.indicator']");
        return getAttribute(partCostIndicator, "innerHTML");
    }

    /**
     * Gets Fully Burdened Cost
     *
     * @return as string
     */
    public String getFullyBurdenedCostDelta() {
        By fullyBurdenedCost = By.cssSelector("[data-ap-field='fullyBurdenedCost.indicator']");
        return getAttribute(fullyBurdenedCost, "innerHTML");
    }

    /**
     * Gets Total Capital Investments
     *
     * @return as string
     */
    public String getTotalCapitalInvestmentsDelta() {
        By capitalInvestment = By.cssSelector("[data-ap-field='capitalInvestment.indicator']");
        return getAttribute(capitalInvestment, "innerHTML");
    }

    /**
     * Gets element attribute
     *
     * @param locator - the locator
     * @return string
     */
    private String getAttribute(By locator, String attribute) {
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute(attribute);
    }

    /**
     * Close the reference compare page
     *
     * @return new page object
     */
    public EvaluatePage collapseReferencePanel() {
        if (pageUtils.isElementDisplayed(chevron)) {
            pageUtils.waitForElementAndClick(referenceChevron);
        }
        return new EvaluatePage(driver);
    }
}