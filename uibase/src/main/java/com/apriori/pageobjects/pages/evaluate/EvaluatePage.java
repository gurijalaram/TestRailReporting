package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.common.ScenarioTablePage;
import com.apriori.pageobjects.pages.evaluate.analysis.AnalysisPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.inputs.MoreInputsPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.evaluate.process.secondaryprocess.SecondaryProcessPage;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.toolbars.EvaluateHeader;
import com.apriori.utils.PageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
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

    @FindBy(css = "td[data-ap-field='cycleTime']")
    private WebElement cycleTimeCount;

    @FindBy(css = "td[data-ap-field='materialCost']")
    private WebElement materialCost;

    @FindBy(css = "td[data-ap-field='compTotal.fullyBurdenedCost']")
    private WebElement componentsCost;

    @FindBy(css = "td[data-ap-field='assemblyProcTotal.fullyBurdenedCost']")
    private WebElement assemblyProcessCost;

    @FindBy(css = "td[data-ap-field='totalCost']")
    private WebElement partCost;

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

    @FindBy(css = "[data-ap-field='dfmRisk']")
    private WebElement dfmRisk;

    @FindBy(css = "[data-ap-comp='dfmRiskIcon']")
    private WebElement dfmRiskIcon;

    @FindBy(css = "[data-ap-region='sourceAndUtilizationTile']")
    private WebElement sourceAndUtilTile;

    @FindBy(css = "[data-ap-field='sourceModelKeyPartName']")
    private WebElement sourceModelPartName;

    @FindBy(css = "[data-ap-comp='selectSourceLink']")
    private WebElement selectSourceButton;

    @FindBy(css = "[data-ap-field='sourceModelKeyScenarioName']")
    private WebElement sourceModelScenarioName;

    @FindBy(css = "[data-ap-field='materialName']")
    private WebElement sourceMaterial;

    @FindBy(css = "[data-ap-field='utilization']")
    private WebElement utilizationPercentage;

    @FindBy(css = "[data-ap-comp='twoModelProdInfo'] [data-ap-field='utilization']")
    private WebElement twoModelUtilPercentage;

    @FindBy(xpath = "//div[contains(text(),'Render')]")
    private WebElement renderButton;

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
     * Gets the process routing details
     *
     * @return the details as string
     */
    public String getProcessRoutingDetails() {
        pageUtils.scrollWithJavaScript(processRoutingName, true);
        By processRouting = By.cssSelector("div[data-ap-field='processRoutingName'] div");
        pageUtils.waitForElementToAppear(processRouting);
        return driver.findElement(processRouting).getAttribute("title");
    }

    /**
     * Opens the design guidance dialog
     *
     * @return new page object
     */
    public DesignGuidancePage openDesignGuidance() {
        waitRenderSelected();
        pageUtils.waitForElementAndClick(guidanceDetails);
        return new DesignGuidancePage(driver);
    }

    /**
     * Waits for the render to be selected
     */
    private void waitRenderSelected() {
        pageUtils.waitForElementAndClick(renderButton);
        pageUtils.waitForElementToAppear(By.cssSelector("button[data-ap-comp='solidViewerToolbarButton'][class='selected']"));
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
    public MaterialUtilizationPage openMaterialUtilization() {
        pageUtils.waitForElementAndClick(materialsDetails);
        return new MaterialUtilizationPage(driver);
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
        return pageUtils.textPresentInElement(scenarioDropdown, text);
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
     * Gets material info
     *
     * @return material info as string
     */
    public String getMaterialInfo() {
        By materialsInfo = By.cssSelector("input[data-ap-field='materialNameOverride']");
        pageUtils.waitForElementToAppear(materialsInfo);
        return driver.findElement(materialsInfo).getAttribute("value");
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
    public String getWarningsCount() {
        By warnings = By.cssSelector("td[data-ap-field='failuresWarningsCount']");
        pageUtils.waitForElementToAppear(warnings);
        return driver.findElement(warnings).getAttribute("outerText");
    }

    /**
     * Gets guidance issues count
     *
     * @return string
     */
    public String getGuidanceIssuesCount() {
        By guidanceIssues = By.cssSelector("td[data-ap-field='dtcMessagesCount']");
        pageUtils.waitForElementToAppear(guidanceIssues);
        return driver.findElement(guidanceIssues).getAttribute("outerText");
    }

    /**
     * Gets gcd tolerances count
     *
     * @return string
     */
    public String getGcdTolerancesCount() {
        By gcdTolerancesCount = By.cssSelector("td[data-ap-field='gcdWithTolerancesCount']");
        pageUtils.waitForElementToAppear(gcdTolerancesCount);
        return driver.findElement(gcdTolerancesCount).getAttribute("outerText");
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
     * @return double
     */
    public double getMaterialCost() {
        pageUtils.waitForElementToAppear(materialCost);
        return Double.parseDouble(materialCost.getText());
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
     * @return double
     */
    public double getPartCost() {
        pageUtils.waitForElementToAppear(partCost);
        return Double.parseDouble(partCost.getText());
    }

    /**
     * Gets the fully burdened cost
     *
     * @return double
     */
    public double getBurdenedCost() {
        pageUtils.waitForElementToAppear(burdenedCost);
        return Double.parseDouble(burdenedCost.getText());
    }

    /**
     * Returns fully burdened cost value
     * @return BigDecimal - Fully Burdened Cost (rounded down - thus ROUND_FLOOR)
     */
    public BigDecimal getBurdenedCostValue() {
        return new BigDecimal(
                burdenedCost.getText()
                        .replace(",", ""))
                .setScale(2, BigDecimal.ROUND_FLOOR);
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
     * @return string
     */
    public String getLockedStatus() {
        By lockedStatus = By.cssSelector(".locked-status-icon");
        pageUtils.waitForElementToAppear(lockedStatus);
        return driver.findElement(lockedStatus).getAttribute("title");
    }

    /**
     * Gets the CAD Connection status
     *
     * @return string
     */
    public String isCADConnectionStatus() {
        By cadConnection = By.cssSelector(".cad-connection-status-icon");
        pageUtils.waitForElementToAppear(cadConnection);
        return driver.findElement(cadConnection).getAttribute("title");
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
     * Gets the input value
     *
     * @return true/false
     */
    public String getAnnualVolume() {
        By annualVolume = By.cssSelector("input[data-ap-field='annualVolume']");
        pageUtils.waitForElementToAppear(annualVolume);
        return driver.findElement(annualVolume).getAttribute("value");
    }

    /**
     * Get the input value is correct
     *
     * @return true/false
     */
    public String getProductionLife() {
        By productionLife = By.cssSelector("input[data-ap-field='productionLife']");
        pageUtils.waitForElementToAppear(productionLife);
        return driver.findElement(productionLife).getAttribute("value");
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
     * Gets the value of the total components
     *
     * @return string
     */
    public String getTotalComponents() {
        By totalComponents = By.cssSelector("td[data-ap-field='totalComponents']");
        pageUtils.waitForElementToAppear(totalComponents);
        return driver.findElement(totalComponents).getAttribute("innerText");
    }

    /**
     * Gets the value of unique components
     *
     * @return string
     */
    public String getUniqueComponents() {
        By uniqueComponents = By.cssSelector("td[data-ap-field='uniqueComponents']");
        pageUtils.waitForElementToAppear(uniqueComponents);
        return driver.findElement(uniqueComponents).getAttribute("innerText");
    }

    /**
     * Gets the uncosted unique value
     *
     * @return string
     */
    public String getUncostedUnique() {
        By uncostedComponents = By.cssSelector("td[data-ap-field='uncostedComponentsCount']");
        pageUtils.waitForElementToAppear(uncostedComponents);
        return driver.findElement(uncostedComponents).getAttribute("innerText");
    }

    /**
     * Gets the value of finish mass
     *
     * @return double
     */
    public double getFinishMass() {
        By finishMass = By.cssSelector("td[data-ap-field='finishMass']");
        pageUtils.waitForElementToAppear(finishMass);
        return Double.parseDouble(driver.findElement(finishMass).getAttribute("innerText"));
    }

    /**
     * Gets the value of target mass
     *
     * @return string
     */
    public String getTargetMass() {
        By targetMass = By.cssSelector("td[data-ap-field='targetFinishMass']");
        pageUtils.waitForElementToAppear(targetMass);
        return driver.findElement(targetMass).getAttribute("innerText");
    }

    /**
     * Gets the value of secondary processes
     *
     * @return string
     */
    public String getSecondaryProcesses() {
        By secondaryProcess = By.cssSelector("[data-ap-field='userOverridesCount']");
        pageUtils.waitForElementToAppear(secondaryProcess);
        return driver.findElement(secondaryProcess).getAttribute("value");
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
     *
     * @param row
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getTableValsByRow(String row) {
        Document evaluateComponentView = Jsoup.parse(driver.getPageSource());

        String baseCssSelector = "div[class='v-grid-tablewrapper'] > table > tbody > tr:nth-child(%s) > td";
        ArrayList<Element> elements;

        baseCssSelector = String.format(baseCssSelector, row);
        elements = evaluateComponentView.select(baseCssSelector);

        return elements.stream().filter(element -> !element.text().isEmpty() && element.text().contains(".")).map(element -> new BigDecimal(element.text())).collect(Collectors.toCollection(ArrayList::new));
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

    /**
     * Checks the dfm risk score
     *
     * @return dfm risk score
     */
    public boolean isDfmRisk(String risk) {
        By dfmRisk = By.xpath(String.format("//td[.='%s']", risk));
        return pageUtils.waitForElementToAppear(dfmRisk).isDisplayed();
    }

    /**
     * Checks if DFM Risk Icon is displayed
     *
     * @return Risk Level
     */
    public boolean isDFMRiskIconDisplayed() {
        return pageUtils.isElementDisplayed(dfmRiskIcon);

    }

    /**
     * Checks the dfm risk Icon
     *
     * @return Risk Level
     */
    public boolean isDFMRiskIcon(String icon) {
        By dfmRiskIcon = By.xpath(String.format("//span[contains(@class,'%s')]", icon));
        return pageUtils.waitForElementToAppear(dfmRiskIcon).isDisplayed();
    }

    /**
     * Opens the Source Scenario
     *
     * @return new page object
     */
    public EvaluatePage openSourceScenario() {
        pageUtils.waitForElementAndClick(sourceModelScenarioName);
        return new EvaluatePage(driver);
    }

    /**
     * Checks source part name
     *
     * @return part name
     */
    public String getSourcePartName() {
        pageUtils.waitForElementAppear(sourceModelPartName);
        return sourceModelPartName.getText();
    }

    /**
     * Checks source Scenario name
     *
     * @return source model scenario name
     */
    public String getSourceScenarioName() {
        pageUtils.waitForElementAppear(sourceModelScenarioName);
        return sourceModelScenarioName.getAttribute("title");
    }

    /**
     * Checks source Scenario Material
     *
     * @return source material
     */
    public String getSourceMaterial() {
        pageUtils.waitForElementAppear(sourceMaterial);
        return sourceMaterial.getText();
    }

    /**
     * Opens the Scenario Selection Page
     *
     * @return new page object
     */
    public ScenarioTablePage selectSourcePart() {
        pageUtils.waitForElementToAppear(selectSourceButton);
        pageUtils.waitForElementAndClick(selectSourceButton);
        return new ScenarioTablePage(driver);
    }

    /**
     * Get Two Model Utilization Percentage
     *
     * @return Utilization Percentage
     */
    public double getTwoModelUtilizationPercentage() {
        return getUtilPercentage(twoModelUtilPercentage);
    }


    /**
     * Get Utilization Percentage
     *
     * @return Utilization Percentage
     */
    public double getUtilizationPercentage() {
        return getUtilPercentage(utilizationPercentage);
    }

    private double getUtilPercentage(WebElement utilPercentage) {
        pageUtils.waitForElementAppear(utilPercentage);
        return Double.parseDouble(utilPercentage.getText());
    }
}
