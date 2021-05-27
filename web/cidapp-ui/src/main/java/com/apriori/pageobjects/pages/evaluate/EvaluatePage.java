package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.common.CustomAttributesInputsController;
import com.apriori.pageobjects.common.PrimaryInputsController;
import com.apriori.pageobjects.common.SecondaryInputsController;
import com.apriori.pageobjects.common.StatusIcon;
import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.StatusIconEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.Color;
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

public class EvaluatePage extends EvaluateToolbar {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatePage.class);

    @FindBy(css = ".costing-inputs .spinner-border")
    private List<WebElement> panelLoaders;

    @FindBy(css = ".webviewer-canvas")
    private WebElement viewerCanvas;

    @FindBy(css = ".scenario-state-preview [data-icon='cog']")
    private List<WebElement> cogIcon;

    @FindBy(css = "svg[data-icon='home']")
    private WebElement homeButton;

    @FindBy(css = "svg[data-icon='expand-arrows-alt']")
    private WebElement zoomButton;

    @FindBy(css = "svg[data-icon='list-alt']")
    private WebElement analysisButton;

    @FindBy(css = "svg[data-icon='cube']")
    private WebElement cubeButton;

    @FindBy(css = "svg[data-icon='eye']")
    private WebElement viewButton;

    @FindBy(css = "svg[data-icon='border-none']")
    private WebElement sectioningButton;

    @FindBy(css = "input[name='annualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = "input[name='productionLife']")
    private WebElement productionLifeInput;

    @FindBy(id = "qa-scenario-select-field")
    private WebElement currentScenarioDropdown;

    @FindBy(css = "div[id='qa-process-group-select-field'] [data-icon='chevron-down']")
    private WebElement processGroupDropdown;

    @FindBy(css = "div[id='qa-process-group-select-field'] input")
    private WebElement processGroupInput;

    @FindBy(css = "div[id='qa-digital-factory-select-field'] [data-icon='chevron-down']")
    private WebElement digitalFactoryDropdown;

    @FindBy(css = "div[id='qa-digital-factory-select-field'] input")
    private WebElement digitalFactoryInput;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field'] .pill-box")
    private WebElement secondaryProcessBox;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field'] .badge-pill")
    private List<WebElement> secondaryProcesses;

    @FindBy(css = "div[id='qa-secondary-process-modal-select-field'] .input-group-append")
    private WebElement secondaryProcessesPencil;

    @FindBy(css = "div[id='qa-material-modal-select-field'] .input-group-append")
    private WebElement materialsPencil;

    @FindBy(css = ".material-summary-card.card .pill-text")
    private WebElement materialsDetailsButton;

    @FindBy(css = ".material-summary-card.card input")
    private WebElement materialName;

    @FindBy(css = ".design-guidance-summary-card.card .pill-text")
    private WebElement designGuidanceDetailsButton;

    @FindBy(css = ".process-summary-card.card .pill-text")
    private WebElement processesDetailsButton;

    @FindBy(css = ".cost-result-summary-card.card .pill-text")
    private WebElement costDetailsButton;

    @FindBy(css = ".production-info-summary-card.card .pill.action-button")
    private WebElement inputDetailsButton;

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(css = "[id='qa-digital-factory-select-field']")
    private WebElement digitalFactoryList;

    @FindBy(css = "[id='qa-process-group-select-field']")
    private WebElement processGroupList;

    @FindBy(css = ".sub-components-summary.card .pill-text")
    private WebElement componentsDetailsButton;

    @FindBy(id = "qa-scenario-select-field")
    @CacheLookup
    private WebElement scenarioName;

    private PageUtils pageUtils;
    private WebDriver driver;
    private PrimaryInputsController primaryInputsController;
    private SecondaryInputsController secondaryInputsController;
    private CustomAttributesInputsController customAttributesInputsController;
    private StatusIcon statusIcon;

    public EvaluatePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.primaryInputsController = new PrimaryInputsController(driver);
        this.secondaryInputsController = new SecondaryInputsController(driver);
        this.customAttributesInputsController = new CustomAttributesInputsController(driver);
        this.statusIcon = new StatusIcon(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        pageUtils.invisibilityOfElements(panelLoaders);
        pageUtils.invisibilityOfElements(cogIcon);
    }

    /**
     * Selects the current scenario
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public EvaluatePage selectCurrentScenario(String scenarioName) {
        pageUtils.waitForElementAndClick(currentScenarioDropdown);
        By currentScenario = By.xpath(String.format("//button[.='%s']", scenarioName));
        pageUtils.waitForElementAndClick(currentScenario);
        return this;
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage selectProcessGroup(String processGroup) {
        primaryInputsController.selectProcessGroup(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * Inputs the pg
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage inputProcessGroup(String processGroup) {
        primaryInputsController.typeAheadProcessGroup(processGroupInput, processGroup);
        return this;
    }

    /**
     * Selects the vpe dropdown
     *
     * @param digitalFactory - the vpe
     * @return current page object
     */
    public EvaluatePage selectDigitalFactory(String digitalFactory) {
        primaryInputsController.selectDigitalFactory(digitalFactoryDropdown, digitalFactory);
        return this;
    }

    /**
     * Inputs the vpe dropdown
     *
     * @param digitalFactory - the vpe
     * @return current page object
     */
    public EvaluatePage inputDigitalFactory(String digitalFactory) {
        primaryInputsController.typeAheadDigitalFactory(digitalFactoryInput, digitalFactory);
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public EvaluatePage enterAnnualVolume(String annualVolume) {
        primaryInputsController.enterAnnualVolume(annualVolumeInput, annualVolume);
        return this;
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLife - the years
     * @return current page object
     */
    public EvaluatePage enterAnnualYears(String productionLife) {
        primaryInputsController.enterAnnualYears(productionLifeInput, productionLife);
        return this;
    }

    /**
     * Checks material info is displayed
     *
     * @return true/false
     */
    public boolean isMaterialInfoDisplayed(String material) {
        By materialsInfo = By.cssSelector(String.format("div[id='qa-material-modal-select-field'] input[value='%s']", material));
        return pageUtils.waitForElementToAppear(materialsInfo).isDisplayed();
    }

    /**
     * Opens the material selector table
     *
     * @return new page object
     */
    public MaterialSelectorPage openMaterialSelectorTable() {
        primaryInputsController.openMaterialSelectorTable(materialsPencil);
        return new MaterialSelectorPage(driver);
    }

    /**
     * Opens the input panel
     *
     * @return new page object
     */
    public MoreInputsPage openInputDetails() {
        pageUtils.waitForElementAndClick(inputDetailsButton);
        return new MoreInputsPage(driver);
    }

    /**
     * Opens the cost result panel
     *
     * @return new page object
     */
    public CostDetailsPage openCostDetails() {
        pageUtils.waitForElementAndClick(costDetailsButton);
        return new CostDetailsPage(driver);
    }

    /**
     * Opens the material utilization panel
     *
     * @return new page object
     */
    public MaterialUtilizationPage openMaterialUtilization() {
        pageUtils.waitForElementAndClick(materialsDetailsButton);
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Opens the design guidance panel
     *
     * @return new page object
     */
    public GuidanceIssuesPage openDesignGuidance() {
        pageUtils.waitForElementAndClick(designGuidanceDetailsButton);
        return new GuidanceIssuesPage(driver);
    }

    /**
     * Opens the processes panel
     *
     * @return new page object
     */
    public ProcessesPage openProcesses() {
        pageUtils.waitForElementAndClick(processesDetailsButton);
        return new ProcessesPage(driver);
    }

    /**
     * Opens the components panel
     *
     * @return new page object
     */
    public ComponentsListPage openComponents() {
        if (!pageUtils.isElementDisplayed(componentsDetailsButton)) {
            return new ComponentsListPage(driver);
        }
        pageUtils.waitForElementAndClick(componentsDetailsButton);
        return new ComponentsListPage(driver);
    }

    /**
     * Opens the secondary processes page
     *
     * @return new page object
     */
    public SecondaryProcessesPage openSecondaryProcesses() {
        primaryInputsController.openSecondaryProcesses(secondaryProcessesPencil);
        return new SecondaryProcessesPage(driver);
    }

    /**
     * Gets list of secondary processes
     *
     * @return list of string
     */
    public List<String> getSecondaryProcesses() {
        return primaryInputsController.getSecondaryProcesses(secondaryProcesses);
    }

    /**
     * Gets the process routing details
     *
     * @return string
     */
    public String getProcessRoutingDetails() {
        By processRouting = By.cssSelector("div[class='routing-name']");
        pageUtils.waitForElementToAppear(processRouting);
        return driver.findElement(processRouting).getAttribute("textContent");
    }

    /**
     * Gets material details - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getMaterialResult(String label) {
        By result = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        pageUtils.waitForElementToAppear(result);
        return Double.parseDouble(driver.findElement(result).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the value of specified material
     *
     * @param label - the label
     * @return string
     */
    public String isMaterial(String label) {
        By result = By.xpath(String.format("//span[.='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(result).getAttribute("textContent");
    }

    /**
     * Gets guidance result
     *
     * @param label - the label
     * @return string
     */
    public String getGuidanceResult(String label) {
        By guidanceResult = By.xpath(String.format("//div[@class='design-guidance']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return pageUtils.waitForElementToAppear(guidanceResult).getAttribute("textContent");
    }

    /**
     * Checks the value of specified guidance
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isGuidanceResultDisplayed(String label, String value) {
        By guidanceResult = By.xpath(String.format("//div[@class='design-guidance']//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(guidanceResult);
        return driver.findElement(guidanceResult).isDisplayed();
    }

    /**
     * Gets processes result - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getProcessesResult(String label) {
        By processResult = By.xpath(String.format("//div[@class='process-summary']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(processResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the value of specified process
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isProcessResultDisplayed(String label, String value) {
        By processResult = By.xpath(String.format("//div[@class='process-summary']//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(processResult);
        return driver.findElement(processResult).isDisplayed();
    }

    /**
     * Gets cost result - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public double getCostResults(String label) {
        By costResult = By.xpath(String.format("//div[@class='cost-result-summary']//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(costResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the value of specified cost
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isCostResultDisplayed(String label, String value) {
        By costResult = By.xpath(String.format("//div[@class='cost-result-summary']//span[.='%s']/following-sibling::span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(costResult);
        return driver.findElement(costResult).isDisplayed();
    }

    /**
     * Checks the dfm risk score
     *
     * @return true/false
     */
    public boolean isDfmRisk(String riskFactor) {
        By risk = By.xpath(String.format("//span[.='DFM Risk']/following-sibling::span[.='%s']", riskFactor));
        return pageUtils.waitForElementToAppear(risk).isDisplayed();
    }

    /**
     * Checks the dfm risk icon
     *
     * @param riskFactor - risk
     * @return boolean
     */
    public boolean isDfmRiskIcon(String riskFactor) {
        String risk = riskFactor.equalsIgnoreCase("Low") ? "var(--success)"
            : riskFactor.equalsIgnoreCase("Medium") ? "var(--info)"
            : riskFactor.equalsIgnoreCase("High") ? "var(--warning)"
            : riskFactor.equalsIgnoreCase("Critical") ? "var(--danger)"
            : riskFactor.equalsIgnoreCase("Unknown") ? "var(--secondary-light)"
            : null;

        By riskIcon = By.cssSelector(String.format("circle[stroke='%s']", risk));
        return pageUtils.waitForElementToAppear(riskIcon).isDisplayed();
    }

    /**
     * Gets list of process groups
     *
     * @return list as string
     */
    public List<String> getListOfProcessGroups() {
        pageUtils.waitForElementAndClick(processGroupList);
        return Arrays.stream(processGroupList.getText().split("\n")).filter(x -> !x.equalsIgnoreCase("Process Group")).collect(Collectors.toList());
    }

    /**
     * Gets list of digital factory
     *
     * @return list as string
     */
    public List<String> getListOfDigitalFactory() {
        pageUtils.waitForElementAndClick(digitalFactoryList);
        return Arrays.stream(digitalFactoryList.getText().split("\n")).filter(x -> !x.equalsIgnoreCase("Digital Factory")).collect(Collectors.toList());
    }

    /**
     * Get background colour
     *
     * @param element - the element
     * @return hex code as string
     */
    public String getColour(String element) {
        WebElement elementColour = element.equalsIgnoreCase("Process Group") ? processGroupDropdown
            : element.equalsIgnoreCase("VPE") ? digitalFactoryDropdown
            : element.equalsIgnoreCase("Secondary Processes") ? secondaryProcessBox
            : element.equalsIgnoreCase("Annual Volume") ? annualVolumeInput
            : element.equalsIgnoreCase("Years") ? productionLifeInput
            : element.equalsIgnoreCase("Material") ? materialName
            : null;

        return Color.fromString(pageUtils.waitForElementAppear(elementColour).getCssValue("background-color")).asHex();
    }

    /**
     * Gets the scenario name
     *
     * @return current page object
     */
    public String getCurrentScenarioName() {
        return pageUtils.waitForElementToAppear(scenarioName).getAttribute("textContent");
    }

    /**
     * Gets component result - result is returned as a double with strings and special characters parsed
     *
     * @param label - the label
     * @return double
     */
    public String getComponentResults(String label) {
        By componentResult = By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
        return pageUtils.waitForElementToAppear(componentResult).getAttribute("textContent");
    }

    /**
     * Checks icon is displayed
     *
     * @param icon - the icon
     * @return true/false
     */
    public boolean isIconDisplayed(StatusIconEnum icon) {
        return statusIcon.isIconDisplayed(icon);
    }
}