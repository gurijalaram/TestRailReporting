package pageobjects.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluatePage extends LoadableComponent<EvaluatePage> {

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

    @FindBy(xpath = "//div[contains(text(),'Render')]")
    private WebElement renderButton;

    @FindBy(css = "button[data-ap-comp='solidViewerToolbarButton'][class='selected']")
    private WebElement renderSelectedButton;

    @FindBy(css = "input[name='annualVolume']")
    private WebElement annualVolumeInput;

    @FindBy(css = "input[name='productionLife']")
    private WebElement productionLifeInput;

    @FindBy(xpath = "//label[.='Current Scenario']/..//button[@class='dropdown-toggle btn btn-secondary']")
    private WebElement currentScenarioDropdown;

    @FindBy(xpath = "//label[.='Process Group']/..//button[@class='dropdown-toggle btn btn-secondary']")
    private WebElement processGroupDropdown;

    @FindBy(xpath = "//label[.='VPE']/..//button[@class='dropdown-toggle btn btn-secondary']")
    private WebElement vpeDropdown;

    @FindBy(xpath = "//label[.='Secondary Process']/..//button[@class='dropdown-toggle btn btn-secondary']")
    private WebElement secondaryProcessDropdown;

    private PageUtils pageUtils;
    private WebDriver driver;

    public EvaluatePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(leftPanel);
        pageUtils.waitForElementAppear(viewerCanvas);
        pageUtils.waitForElementAndClick(renderButton);
        pageUtils.waitForElementAppear(renderSelectedButton);
    }

    /**
     * Selects the current scenario
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public EvaluatePage selectCurrentScenario(String scenarioName) {
        pageUtils.selectDropdownOption(currentScenarioDropdown, scenarioName);
        return this;
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
     * Selects the secondary process
     * @param secondaryProcess - the secondary process
     * @return current page object
     */
    public EvaluatePage selectSecondaryProcess(String secondaryProcess) {
        pageUtils.selectDropdownOption(secondaryProcessDropdown, secondaryProcess);
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
}