package pageobjects.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.evaluate.materialutilization.MaterialUtilizationPage;
import pageobjects.navtoolbars.EvaluateToolbar;

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
    private WebElement materialsButton;

    @FindBy(xpath = "//div[.='Material & Utilization']/../div[.='details']")
    private WebElement materialsDetailsButton;

    @FindBy(xpath = "//div[.='Cost Results']/../div[.='details']")
    private WebElement costDetailsButton;


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
     * Opens the material selector table
     *
     * @return new page object
     */
    public MaterialSelectorPage openMaterialSelectorTable() {
        pageUtils.waitForElementAndClick(materialsButton);
        return new MaterialSelectorPage(driver);
    }

    /**
     * Opens the Cost Result Panel
     *
     * @return new page object
     */
    public CostDetailsPage openCostDetails() {
        pageUtils.waitForElementAndClick(costDetailsButton);
        return new CostDetailsPage(driver);
    }

    /**
     * Gets material info
     *
     * @param material - the material
     * @return material info as string
     */
    public String getMaterialInfo(String material) {
        By materialsInfo = By.xpath(String.format("//label[.='Material']/..//input[@value='%s']", material));
        return pageUtils.waitForElementToAppear(materialsInfo).getText();
    }

    /**
     * Opens the material composition dialog
     *
     * @return new page object
     */
    public MaterialUtilizationPage openMaterialUtilization() {
        pageUtils.waitForElementAndClick(materialsDetailsButton);
        return new MaterialUtilizationPage(driver);
    }
}