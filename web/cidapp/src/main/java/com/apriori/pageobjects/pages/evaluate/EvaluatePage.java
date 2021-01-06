package com.apriori.pageobjects.pages.evaluate;

import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cfrith
 */

public class EvaluatePage extends EvaluateToolbar {

    private final Logger LOGGER = LoggerFactory.getLogger(EvaluatePage.class);

    @FindBy(css = ".left-panel.p-3")
    private WebElement leftPanel;

    @FindBy(css = ".webviewer-canvas")
    private WebElement viewerCanvas;

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

    @FindBy(xpath = "//label[.='Current Scenario']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement currentScenarioDropdown;

    @FindBy(xpath = "//label[.='Process Group']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement processGroupDropdown;

    @FindBy(xpath = "//label[.='VPE']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement vpeDropdown;

    @FindBy(xpath = "//label[.='Secondary Process']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement secondaryProcessDropdown;

    @FindBy(xpath = "//label[.='Material']/following-sibling::div//button")
    private WebElement materialsPencil;

    @FindBy(xpath = "//div[.='Material & Utilization']/following-sibling::div[.='details']")
    private WebElement materialsDetailsButton;

    @FindBy(xpath = "//div[.='Material']//input")
    private WebElement materialName;

    @FindBy(xpath = "//div[.='Design Guidance']/following-sibling::div[.='details']")
    private WebElement designGuidanceDetailsButton;

    @FindBy(xpath = "//div[.='Processes']/following-sibling::div[.='details']")
    private WebElement processesDetailsButton;

    @FindBy(xpath = "//div[.='Cost Results']/following-sibling::div[.='details']")
    private WebElement costDetailsButton;

    @FindBy(xpath = "//label[.='Secondary Processes']/following-sibling::div//button")
    private WebElement secondaryProcessesPencil;

    @FindBy(xpath = "//div[.='Inputs']/following-sibling::div[normalize-space()='more']")
    private WebElement inputDetailsButton;

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//label[.='Secondary Processes']/following-sibling::div//span")
    private List<WebElement> secondaryProcesses;

    @FindBy(xpath = "//label[.='VPE']/following-sibling::div//button")
    private List<WebElement> vpes;

    @FindBy(xpath = "//label[.='Process Group']/following-sibling::div//button")
    private List<WebElement> processGroups;

    private PageUtils pageUtils;
    private WebDriver driver;

    public EvaluatePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
        pageUtils.waitForElementAppear(leftPanel);
        pageUtils.waitForElementAppear(viewerCanvas);
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
        pageUtils.waitForElementAndClick(processGroupDropdown);
        By group = By.xpath(String.format("//button[.='%s']", processGroup));
        pageUtils.scrollWithJavaScript(driver.findElement(group), true).click();
        return this;
    }

    /**
     * Selects the vpe dropdown
     *
     * @param vpe - the vpe
     * @return current page object
     */
    public EvaluatePage selectVPE(String vpe) {
        pageUtils.waitForElementAndClick(vpeDropdown);
        By vp = By.xpath(String.format("//button[.='%s']", vpe));
        pageUtils.scrollWithJavaScript(driver.findElement(vp), true).click();
        return this;
    }

    /**
     * Selects the secondary process
     *
     * @param secondaryProcess - the secondary process
     * @return current page object
     */
    public EvaluatePage selectSecondaryProcess(String secondaryProcess) {
        pageUtils.waitForElementAndClick(secondaryProcessDropdown);
        By secProcess = By.xpath(String.format("//button[.='%s']", secondaryProcess));
        pageUtils.scrollWithJavaScript(driver.findElement(secProcess), true).click();
        return this;
    }

    /**
     * Enters the annual volume
     *
     * @param annualVolume - the annual volume
     * @return current page object
     */
    public EvaluatePage enterAnnualVolume(String annualVolume) {
        annualVolumeInput.clear();
        annualVolumeInput.sendKeys(Keys.DELETE);
        annualVolumeInput.sendKeys(annualVolume);
        return this;
    }

    /**
     * Enters the years of annual volume
     *
     * @param productionLife - the years
     * @return current page object
     */
    public EvaluatePage enterAnnualYears(String productionLife) {
        productionLifeInput.clear();
        productionLifeInput.sendKeys(Keys.DELETE);
        productionLifeInput.sendKeys(productionLife);
        return this;
    }

    /**
     * Checks material info is displayed
     *
     * @return true/false
     */
    public boolean isMaterialInfoDisplayed(String material) {
        By materialsInfo = By.xpath(String.format("//label[.='Material']/following-sibling::div//input[@value='%s']", material));
        return pageUtils.waitForElementToAppear(materialsInfo).isDisplayed();
    }

    /**
     * Opens the material selector table
     *
     * @return new page object
     */
    public MaterialSelectorPage openMaterialSelectorTable() {
        pageUtils.waitForElementAndClick(materialsPencil);
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
    public DesignGuidancePage openDesignGuidance() {
        pageUtils.waitForElementAndClick(designGuidanceDetailsButton);
        return new DesignGuidancePage(driver);
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
     * Opens the secondary processes page
     *
     * @return new page object
     */
    public SecondaryProcessesPage openSecondaryProcesses() {
        pageUtils.waitForElementAndClick(secondaryProcessesPencil);
        return new SecondaryProcessesPage(driver);
    }

    /**
     * Gets list of secondary processes
     *
     * @return list of string
     */
    public List<String> getSecondaryProcesses() {
        return secondaryProcesses.stream().map(WebElement::getText).collect(Collectors.toList());
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
     * Gets material details
     *
     * @param label - the label
     * @return double
     */
    public double getMaterialResult(String label) {
        By result = By.xpath(String.format("//div[@class='display-property vertical']//span[.='%s']/following-sibling::span[@class='property-value']", label));
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
     * Gets processes result
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
     * Gets cost result
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
     * Gets list of vpe's
     *
     * @return list as string
     */
    public List<String> getListOfVPEs() {
        return getDropdownsInList(vpes);
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
        return getDropdownsInList(processGroups);
    }

    private List<String> getDropdownsInList(List<WebElement> dropdownLists) {
        List<String> listOfDropdown = new ArrayList<>();

        for (WebElement dropdown : dropdownLists) {
            listOfDropdown.add(dropdown.getAttribute("textContent"));
        }
        return listOfDropdown;
    }

    /**
     * Get background colour
     * @param element - the element
     * @return hex code as string
     */
    public String getColour(String element) {
        WebElement elementColour = element.equalsIgnoreCase("Process Group") ? processGroupDropdown
            : element.equalsIgnoreCase("VPE") ? vpeDropdown
            : element.equalsIgnoreCase("Secondary Processes") ? secondaryProcessDropdown
            : element.equalsIgnoreCase("Annual Volume") ? annualVolumeInput
            : element.equalsIgnoreCase("Years") ? productionLifeInput
            : element.equalsIgnoreCase("Material") ? materialName
            : null;

        return Color.fromString(pageUtils.waitForElementAppear(elementColour).getCssValue("background-color")).asHex();
    }
}