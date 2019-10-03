package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.header.EvaluateHeader;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialCompositionPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.evaluate.process.secondaryprocess.SecondaryProcessPage;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class EvaluatePage extends EvaluateHeader {

    private final Logger logger = LoggerFactory.getLogger(EvaluatePage.class);

    @FindBy(css = "div.main-viewport")
    private WebElement leftPanel;

    @FindBy(css = "div[data-ap-comp='part-viewer-panel'] canvas")
    private WebElement viewerCanvas;

    @FindBy(css = "ul.viewer-controls-toolbar")
    private WebElement controlToolbars;

    @FindBy(css = "a[data-ap-comp='updateInfoLink']")
    private WebElement infoNotes;

    @FindBy(css = "th[data-ap-comp='comparisonSectionVisibilityButton']")
    private WebElement referenceChevron;

    @FindBy(css = ".gwt-InlineHTML.glyphicon.glyphicon-chevron-left")
    private WebElement chevron;

    @FindBy(css = "[data-ap-comp='scenarioKey'] .pull-left .filter-option-text")
    private WebElement scenarioDropdown;

    @FindBy(css = "a[data-ap-nav-viewport='showInputDetails']")
    private WebElement moreInputs;

    @FindBy(css = "select[data-ap-field='processGroupSelection']")
    private WebElement processGroupDropdown;

    @FindBy(css = "select[data-ap-field='processGroupSelection'] option")
    private List<WebElement> processGroupList;

    @FindBy(css = "select[data-ap-field='primaryVpeName']")
    private WebElement vpeDropdown;

    @FindBy(css = "select[data-ap-field='primaryVpeName'] option")
    private List<WebElement> vpeList;

    @FindBy(css = "button[data-ap-comp='secondaryTreatmentsButton']")
    private WebElement secondaryProcessButton;

    @FindBy(css = "input[data-ap-field='annualVolume']")
    private WebElement annVolume;

    @FindBy(css = "input[data-ap-field='productionLife']")
    private WebElement annualVolumeYrs;

    @FindBy(css = "a[data-ap-nav-viewport='showMaterialDetails']")
    private WebElement materialsDetails;

    @FindBy(css = "input[data-ap-field='materialNameOverride']")
    private WebElement materialsInfo;

    @FindBy(css = "button[data-ap-comp='materialSelectionButton']")
    private WebElement materialsButton;

    @FindBy(css = "a[data-ap-nav-viewport='showDesignGuidanceDetails']")
    private WebElement guidanceDetails;

    @FindBy(css = "a[data-ap-nav-viewport='showCycleTimeDetails']")
    private WebElement processDetails;

    @FindBy(css = "label[data-ap-field='processRoutingName'] div")
    private WebElement processRoutingName;

    @FindBy(css = "a[data-ap-nav-viewport='showCostResultDetails']")
    private WebElement resultsDetails;

    @FindBy(css = "td[data-ap-field='failuresWarningsCount']")
    private WebElement warningsCount;

    @FindBy(css = "td[data-ap-field='dtcMessagesCount']")
    private WebElement guidanceIssuesCount;

    @FindBy(css = "td[data-ap-field='gcdWithTolerancesCount']")
    private WebElement gcdTolerancesCount;

    @FindBy(css = "td[data-ap-field='cycleTime']")
    private WebElement cycleTimeCount;

    @FindBy(css = "td[data-ap-field='materialCost']")
    private WebElement materialCost;

    @FindBy(css = "td[data-ap-field='totalCost']")
    private WebElement pPartCost;

    @FindBy(css = "td[data-ap-field='fullyBurdenedCost']")
    private WebElement burdenedCost;

    @FindBy(css = "td[data-ap-field='capitalInvestment']")
    private WebElement capitalInvestments;

    @FindBy(css = ".color-failure")
    public WebElement failedCostedIcon;

    private WebDriver driver;
    private PageUtils pageUtils;

    public EvaluatePage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(leftPanel);
        pageUtils.waitForElementToAppear(viewerCanvas, 1);
        pageUtils.waitForElementToAppear(controlToolbars);
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage selectProcessGroup(String processGroup) {
        pageUtils.waitForElementToBeClickable(processGroupDropdown);
        pageUtils.selectDropdownOption(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * Selects the vpe dropdown
     *
     * @param vpe - the vpe
     * @return current page object
     */
    public EvaluatePage selectVPE(String vpe) {
        pageUtils.selectDropdownOption(vpeDropdown, vpe);
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public EvaluatePage enterAnnualVolume(String annualVolume) {
        annVolume.clear();
        annVolume.sendKeys(annualVolume);
        return this;
    }

    /**
     * Enters the years of annual volume
     *
     * @param years - the years
     * @return current page object
     */
    public EvaluatePage enterAnnualYears(String years) {
        annualVolumeYrs.clear();
        annualVolumeYrs.sendKeys(years);
        return this;
    }

    /**
     * Opens the process tab
     *
     * @return new page object
     */
    public ProcessRoutingPage openProcessDetails() {
        pageUtils.waitForElementAndClick(processDetails);
        return new ProcessRoutingPage(driver);
    }

    /**
     * Checks the process routing details
     *
     * @return the details as string
     */
    public Boolean isProcessRoutingDetails(String text) {
        pageUtils.waitForElementToAppear(processRoutingName);
        return pageUtils.checkElementAttribute(processRoutingName, "title", text);
    }

    /**
     * Opens the design guidance dialog
     *
     * @return new page object
     */
    public DesignGuidancePage openDesignGuidance() {
        pageUtils.waitForElementAndClick(guidanceDetails);
        return new DesignGuidancePage(driver);
    }

    /**
     * Opens the secondary process dialog
     *
     * @return new page object
     */
    public SecondaryProcessPage openSecondaryProcess() {
        pageUtils.waitForElementAndClick(secondaryProcessButton);
        return new SecondaryProcessPage(driver);
    }

    /**
     * Opens the material composition dialog
     *
     * @return new page object
     */
    public MaterialPage openMaterialComposition() {
        pageUtils.waitForElementAndClick(materialsDetails);
        return new MaterialPage(driver);
    }

    /**
     * Opens the material composition table
     *
     * @return new page object
     */
    public MaterialCompositionPage openMaterialCompositionTable() {
        pageUtils.waitForElementAndClick(materialsButton);
        return new MaterialCompositionPage(driver);
    }

    /**
     * Opens the reference compare page
     *
     * @return new page object
     */
    public ReferenceComparePage openReferenceCompare() {
        if (!pageUtils.isElementDisplayed(chevron)) {
            pageUtils.waitForElementAndClick(referenceChevron);
        }
        return new ReferenceComparePage(driver);
    }

    /**
     * Gets the scenario name
     *
     * @return current page object
     */
    public Boolean getCurrentScenarioName(String text) {
        return pageUtils.checkElementContains(scenarioDropdown, text);
    }

    /**
     * Checks the selected process group details
     *
     * @return group details as string
     */
    public Boolean isProcessGroupSelected(String text) {
        return pageUtils.checkElementFirstOption(processGroupDropdown, text);
    }

    /**
     * Gets list of vpe's
     *
     * @return list as string
     */
    public List<String> getListOfVPEs() {
        return vpeList.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Gets list of process groups
     *
     * @return list as string
     */
    public List<String> getListOfProcessGroups() {
        return processGroupList.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Checks material info
     *
     * @return material info as string
     */
    public Boolean isMaterialInfo(String text) {
        return pageUtils.checkElementAttribute(materialsInfo, "value", text);
    }

    /**
     * Selects info and notes link
     *
     * @return new page object
     */
    public ScenarioNotesPage selectInfoNotes() {
        pageUtils.waitForElementToAppear(infoNotes).click();
        return new ScenarioNotesPage(driver);
    }

    /**
     * @return
     */
    public String getWarningsCount() {
        return pageUtils.waitForElementToAppear(warningsCount).getText();
    }

    /**
     * Gets guidance issues count
     *
     * @return string
     */
    public String getGuidanceIssuesCount() {
        return pageUtils.waitForElementToAppear(guidanceIssuesCount).getText();
    }

    /**
     * Gets gcd tolerances count
     *
     * @return string
     */
    public String getGcdTolerancesCount() {
        return pageUtils.waitForElementToAppear(gcdTolerancesCount).getText();
    }

    /**
     * Gets cycle time count
     *
     * @return string
     */
    public String getCycleTimeCount() {
        return pageUtils.waitForElementToAppear(cycleTimeCount).getText();
    }

    /**
     * Gets the material cost
     *
     * @return string
     */
    public String getMaterialCost() {
        return pageUtils.waitForElementToAppear(materialCost).getText();
    }

    /**
     * Gets the piece part cost
     *
     * @return string
     */
    public String getPartCost() {
        return pageUtils.waitForElementToAppear(pPartCost).getText();
    }

    /**
     * Gets the fully burdened cost
     *
     * @param text
     * @return string
     */
    public Boolean getBurdenedCost(String text) {
        return pageUtils.checkElementContains(burdenedCost, text);
    }

    /**
     * Gets the capital investment
     *
     * @return string
     */
    public String getCapitalInvestment() {
        return pageUtils.waitForElementToAppear(capitalInvestments).getText();
    }
}