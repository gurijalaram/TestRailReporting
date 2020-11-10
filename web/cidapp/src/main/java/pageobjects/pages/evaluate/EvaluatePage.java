package pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.navtoolbars.EvaluateToolbar;
import pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;

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

    @FindBy(xpath = "//label[.='Current Scenario']/..//div[contains(@class,'apriori-select form-control')]")
    private WebElement currentScenarioDropdown;

    @FindBy(xpath = "//label[.='Process Group']/..//div[contains(@class,'apriori-select form-control')]")
    private WebElement processGroupDropdown;

    @FindBy(xpath = "//label[.='VPE']/..//div[contains(@class,'apriori-select form-control')]")
    private WebElement vpeDropdown;

    @FindBy(xpath = "//label[.='Secondary Process']/..//div[contains(@class,'apriori-select form-control')]")
    private WebElement secondaryProcessDropdown;

    @FindBy(xpath = "//label[.='Material']/..//button")
    private WebElement materialsPencil;

    @FindBy(xpath = "//div[.='Material & Utilization']/../div[.='details']")
    private WebElement materialsDetailsButton;

    @FindBy(xpath = "//div[.='Design Guidance']/../div[.='details']")
    private WebElement designGuidanceDetailsButton;

    @FindBy(xpath = "//div[.='Processes']/../div[.='details']")
    private WebElement processesDetailsButton;

    @FindBy(xpath = "//div[.='Cost Results']/../div[.='details']")
    private WebElement costDetailsButton;

    @FindBy(xpath = "//label[.='Secondary Processes']/..//button")
    private WebElement secondaryProcessesPencil;

    @FindBy(xpath = "//div[.='Inputs']/../div[normalize-space()='more']")
    private WebElement inputDetailsButton;

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//label[.='Secondary Processes']//..//div//span[@class]")
    private List<WebElement> secondaryProcesses;

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
        By materialsInfo = By.xpath(String.format("//label[.='Material']/..//input[@value='%s']", material));
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
     * Gets finish mass
     * @return double
     */
    public double getMaterialResult(String resultType) {
        return Double.parseDouble(getMaterialDetails(resultType).replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the value of finish mass
     * @param resultType - the type of result
     * @param value - the value
     * @return true/false
     */
    public boolean isMaterialResult(String resultType, String value) {
        By result = By.xpath(String.format("//span[.='%s']/..//span[.='%s']", resultType, value));
        pageUtils.waitForElementToAppear(result);
        return driver.findElement(result).isDisplayed();
    }


    /**
     * Gets the finish mass details
     * @param resultType  - the type of result
     * @return string
     */
    private String getMaterialDetails(String resultType) {
        By result = By.xpath(String.format("//span[.='%s']/..//span[@class='property-value']", resultType));
        pageUtils.waitForElementToAppear(result);
        return driver.findElement(result).getAttribute("textContent");
    }

//    /**
//     * Gets the value of finish mass
//     *
//     * @return string
//     */
//    public double getUtilization() {
//        By utilization = By.xpath("//span[.='Utilization']/..//span[@class='property-value']");
//        pageUtils.waitForElementToAppear(utilization);
//        return Double.parseDouble(driver.findElement(utilization).getAttribute("textContent"));
//    }

    /**
     * Gets guidance result
     *
     * @param result - the result
     * @return string
     */
    public String getGuidanceResult(String result) {
        By guidanceResult = By.xpath(String.format("//div[@class='design-guidance']//span[.='%s']/..//span[@class='property-value']", result));
        return pageUtils.waitForElementToAppear(guidanceResult).getAttribute("textContent");
    }

    /**
     * Gets processes result
     *
     * @param result - the result
     * @return string
     */
    public String getProcessesResult(String result) {
        By processResult = By.xpath(String.format("//div[@class='process-summary']//span[.='%s']/..//span[@class='property-value']", result));
        return pageUtils.waitForElementToAppear(processResult).getAttribute("textContent");
    }

    /**
     * Gets cost result
     *
     * @param result - the result
     * @return string
     */
    public String getCostResult(String result) {
        By costResult = By.xpath(String.format("//div[@class='cost-result-summary']//span[.='%s']/..//span[@class='property-value']", result));
        return pageUtils.waitForElementToAppear(costResult).getAttribute("textContent");
    }
}