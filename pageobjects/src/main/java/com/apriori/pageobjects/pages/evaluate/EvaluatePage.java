package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.header.EvaluateHeader;
import com.apriori.pageobjects.pages.evaluate.analysis.AnalysisPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.inputs.MoreInputsPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialCompositionPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.evaluate.process.secondaryprocess.SecondaryProcessPage;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cfrith and bhegan
 */

public class EvaluatePage extends EvaluateHeader {

    private final Logger logger = LoggerFactory.getLogger(EvaluatePage.class);

    private Map<String, String> columnSelectorMap = new HashMap<>();

    @FindBy(css = "thead[data-ap-comp='scenarioKey'] label[data-ap-field='masterName']")
    private WebElement partName;

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

    @FindBy(css = "[data-ap-field='processRoutingName'] div")
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

    @FindBy(css = "td[data-ap-field='compTotal.fullyBurdenedCost']")
    private WebElement componentsCost;

    @FindBy(css = "td[data-ap-field='assemblyProcTotal.fullyBurdenedCost']")
    private WebElement assemblyProcessCost;

    @FindBy(css = "td[data-ap-field='totalCost']")
    private WebElement pPartCost;

    @FindBy(css = "td[data-ap-field='fullyBurdenedCost']")
    private WebElement burdenedCost;

    @FindBy(css = "td[data-ap-field='capitalInvestment']")
    private WebElement capitalInvestments;

    @FindBy(css = "li[data-ap-comp='analysis-button']")
    private WebElement analysisButton;

    @FindBy(css = ".color-failure")
    private WebElement failedCostIcon;

    @FindBy(css = ".locked-status-icon")
    private WebElement lockedStatusIcon;

    @FindBy(css = ".cad-connection-status-icon")
    private WebElement cadConnectedIcon;

    @FindBy(css = "a[data-ap-nav-viewport='showAssemblyComponentsDetails']")
    private WebElement componentsDetails;

    @FindBy(css = ".components-refresh-btn")
    private WebElement refreshComponents;

    @FindBy(css = "td[data-ap-field='totalComponents']")
    private WebElement totalComponents;

    @FindBy(css = "td[data-ap-field='uniqueComponents']")
    private WebElement uniqueComponents;

    @FindBy(css = "td[data-ap-field='uncostedComponentsCount']")
    private WebElement uncostedComponents;

    @FindBy(css = "td[data-ap-field='finishMass']")
    private WebElement finishMass;

    @FindBy(css = "td[data-ap-field='targetFinishMass']")
    private WebElement targetMass;

    @FindBy(css = "[data-ap-field='userOverridesCount']")
    private WebElement secondaryProcesses;

    private WebDriver driver;
    private PageUtils pageUtils;

    public EvaluatePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseColumnSelectorMap();
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(leftPanel);
        pageUtils.waitForElementAppear(viewerCanvas);
        pageUtils.waitForElementAppear(controlToolbars);
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
     * Gets the partname
     *
     * @return string
     */
    public String getPartName() {
        return pageUtils.waitForElementToAppear(partName).getText();
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
     * Opens the Cost Result Panel
     *
     * @return new page object
     */
    public CostDetailsPage openCostDetails() {
        pageUtils.waitForElementAndClick(resultsDetails);
        return new CostDetailsPage(driver);
    }

    /**
     * Checks the process routing details
     *
     * @return the details as string
     */
    public boolean isProcessRoutingDetails(String text) {
        pageUtils.scrollWithJavaScript(processRoutingName,true);
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
     * Opens the more inputs dialog
     *
     * @return new page object
     */
    public MoreInputsPage openMoreInputs() {
        pageUtils.waitForElementAndClick(moreInputs);
        return new MoreInputsPage(driver);
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
    public boolean getCurrentScenarioName(String text) {
        return pageUtils.checkElementContains(scenarioDropdown, text);
    }

    /**
     * Checks the selected process group details
     *
     * @return group details as string
     */
    public boolean isProcessGroupSelected(String text) {
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
    public boolean isMaterialInfo(String text) {
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
     * Gets warning count
     *
     * @return string
     */
    public boolean getWarningsCount(String count) {
        return pageUtils.checkElementAttribute(warningsCount, "outerText", count);
    }

    /**
     * Gets guidance issues count
     *
     * @return string
     */
    public boolean getGuidanceIssuesCount(String count) {
        return pageUtils.checkElementAttribute(guidanceIssuesCount, "outerText", count);
    }

    /**
     * Gets gcd tolerances count
     *
     * @return string
     */
    public boolean getGcdTolerancesCount(String count) {
        return pageUtils.checkElementAttribute(gcdTolerancesCount, "outerText", count);
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
     * Gets the components cost
     *
     * @return string
     */
    public String getComponentsCost() {
        return pageUtils.waitForElementToAppear(componentsCost).getText();
    }

    /**
     * Gets the assembly process cost
     *
     * @return string
     */
    public String getAssemblyProcessCost() {
        return pageUtils.waitForElementToAppear(assemblyProcessCost).getText();
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
    public boolean getBurdenedCost(String text) {
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

    /**
     * Checks if the failed icon is present
     *
     * @return string
     */
    public boolean isFailedIconPresent() {
        return failedCostIcon.isDisplayed();
    }

    /**
     * Selects the analysis button
     *
     * @return new page object
     */
    public AnalysisPage selectAnalysis() {
        pageUtils.waitForElementAndClick(analysisButton);
        return new AnalysisPage(driver);
    }

    /**
     * Gets the locked status
     *
     * @return current page object
     */
    public boolean isLockedStatus(String status) {
        return pageUtils.checkElementAttribute(lockedStatusIcon, "title", status);
    }

    /**
     * Gets the CAD Connection status
     *
     * @return current page object
     */
    public boolean isCADConnectionStatus(String status) {
        return pageUtils.checkElementAttribute(cadConnectedIcon, "title", status);
    }

    /**
     * Gets the selected process group
     *
     * @return string
     */
    public boolean getSelectedProcessGroup(String text) {
        return pageUtils.checkElementFirstOption(processGroupDropdown, text);
    }

    /**
     * Gets the selected VPE
     *
     * @return string
     */
    public boolean getSelectedVPE(String text) {
        return pageUtils.checkElementFirstOption(vpeDropdown, text);
    }

    /**
     * Checks the input value is correct
     *
     * @return true/false
     */
    public boolean getAnnualVolume(String text) {
        return pageUtils.checkElementAttribute(annVolume, "value", text);
    }

    /**
     * Checks the input value is correct
     *
     * @return true/false
     */
    public boolean getProductionLife(String text) {
        return pageUtils.checkElementAttribute(annualVolumeYrs, "value", text);
    }

    /**
     * Opens the components page
     *
     * @return new page object
     */
    public ComponentsPage openComponentsTable() {
        pageUtils.waitForElementAndClick(componentsDetails);
        return new ComponentsPage(driver);
    }

    /**
     * Refreshes the component count
     *
     * @return current page object
     */
    public EvaluatePage refreshComponents() {
        pageUtils.waitForElementAndClick(refreshComponents);
        return this;
    }

    /**
     * Checks the value of the total components
     *
     * @param value - the value
     * @return true/false
     */
    public boolean isTotalComponents(String value) {
        return pageUtils.checkElementAttribute(totalComponents, "innerText", value);
    }

    /**
     * Checks the value of unique components
     *
     * @param value - the value
     * @return true/false
     */
    public boolean isUniqueComponents(String value) {
        return pageUtils.checkElementAttribute(uniqueComponents, "innerText", value);
    }

    /**
     * Checks the uncosted unique value
     *
     * @param value - the value
     * @return true/false
     */
    public boolean isUncostedUnique(String value) {
        return pageUtils.checkElementAttribute(uncostedComponents, "innerText", value);
    }

    /**
     * Checks the value of finish mass
     *
     * @param value - the value
     * @return true/false
     */
    public boolean isFinishMass(String value) {
        return pageUtils.checkElementAttribute(finishMass, "innerText", value);
    }

    /**
     * Checks the value of target mass
     *
     * @param value - the value
     * @return true/false
     */
    public boolean isTargetMass(String value) {
        return pageUtils.checkElementAttribute(targetMass, "innerText", value);
    }

    /**
     * Checks the value of secondary processes
     *
     * @param value - the value
     * @return true/false
     */
    public boolean isSecondaryProcesses(String value) {
        return pageUtils.checkElementAttribute(secondaryProcesses, "value", value);
    }

    /**
     * Close the reference compare page
     *
     * @return new page object
     */
    public EvaluatePage collapseReferenceCompare() {
        if (pageUtils.isElementDisplayed(chevron)) {
            pageUtils.waitForElementAndClick(referenceChevron);
        }
        return this;
    }

    /**
     * Checks the uncosted unique value
     *
     * @return true/false
     */
    public boolean isReferencePanelExpanded() {
        return pageUtils.isElementDisplayed(chevron);
    }

    /**
     * Gets table values by specified row index
     * @param row
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getTableValsByRow(String row) {
        ArrayList<BigDecimal> valsToReturn = new ArrayList<BigDecimal>();
        Document evaluateComponentView = Jsoup.parse(driver.getPageSource());

        String baseCssSelector = "div[class='v-grid-tablewrapper'] > table > tbody > tr:nth-child(%s) > td";
        ArrayList<Element> elements = new ArrayList<>();

        baseCssSelector = String.format(baseCssSelector, row);
        elements = evaluateComponentView.select(baseCssSelector);

        for (Element element : elements) {
            if (!element.text().isEmpty() && element.text().contains(".")) {
                valsToReturn.add(new BigDecimal(element.text()));
            }
        }

        return valsToReturn;
    }

    /**
     * Switches to other tab
     */
    public void switchBackToTabOne() {
        pageUtils.switchBackToInitialTab();
    }

    private void initialiseColumnSelectorMap() {
        columnSelectorMap.put("Cycle Time (s)", "5");
        columnSelectorMap.put("Per Part Cost (USD)", "6");
        columnSelectorMap.put("Fully Burdened Cost (USD)", "7");
        columnSelectorMap.put("Capital Investment (USD)", "8");
    }
}
