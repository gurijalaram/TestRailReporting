package main.java.pages.evaluate;

import main.java.header.EvaluateHeader;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.materialutilization.MaterialCompositionPage;
import main.java.pages.evaluate.materialutilization.MaterialPage;
import main.java.pages.evaluate.process.ProcessPage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    @FindBy(css = "span.gwt-InlineHTML.glyphicon.glyphicon-chevron-right")
    private WebElement infoNotesChevron;

    @FindBy(css = ".pull-left .filter-option-text")
    private WebElement scenarioDropdown;

    @FindBy(css = "a[data-ap-nav-viewport='showInputDetails']")
    private WebElement moreInputs;

    @FindBy(css = "select[data-ap-field='processGroupSelection']")
    private WebElement processGroupDropdown;

    @FindBy(css = "select[data-ap-field='primaryVpeName']")
    private WebElement vpeDropdown;

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

    @FindBy(css = "label[data-ap-field='processRoutingName'] div")
    private WebElement processRoutingName;

    @FindBy(css = "label.dirty")
    private List<WebElement> processRoutingState;

    @FindBy(css = "a[data-ap-nav-viewport='showCostResultDetails']")
    private WebElement resultsDetails;

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
        pageUtils.waitForElementToAppear(viewerCanvas);
        pageUtils.waitForElementToAppear(controlToolbars);
    }

    /**
     * Selects the pg dropdown
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage selectProcessGroup(String processGroup) {
        pageUtils.selectDropdownOption(processGroupDropdown, processGroup);
        return this;
    }

    /**
     * Selects the vpe dropdown
     * @param vpe - the vpe
     * @return current page object
     */
    public EvaluatePage selectVPE(String vpe) {
        pageUtils.selectDropdownOption(vpeDropdown, vpe);
        return this;
    }

    /**
     * Enters the annual volume
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
     * @return new page object
     */
    public ProcessPage openProcessDetails() {
        pageUtils.waitForElementToAppear(processDetails).click();
        return new ProcessPage(driver);
    }

    /**
     * Gets the process routing details
     * @return the details as string
     */
    public boolean getProcessRoutingDetails(String text) {
        return pageUtils.checkElementContains(processRoutingName, text);
    }

    /**
     * Opens the design guidance dialog
     * @return new page object
     */
    public DesignGuidancePage openDesignGuidance() {
        pageUtils.waitForElementToAppear(guidanceDetails).click();
        return new DesignGuidancePage(driver);
    }

    /**
     * Opens the secondary process dialog
     * @return new page object
     */
    public SecondaryProcessPage openSecondaryProcess() {
        pageUtils.waitForElementToAppear(secondaryProcessButton).click();
        return new SecondaryProcessPage(driver);
    }

    /**
     * Opens the material composition dialog
     * @return new page object
     */
    public MaterialPage openMaterialComposition() {
        pageUtils.waitForElementToAppear(materialsDetails).click();
        return new MaterialPage(driver);
    }

    /**
     * Opens the material composition table
     * @return new page object
     */
    public MaterialCompositionPage openMaterialCompositionTable() {
        pageUtils.waitForElementToBeClickable(materialsButton).click();
        return new MaterialCompositionPage(driver);
    }

    /**
     * Gets the scenario name
     * @return current page object
     */
    public EvaluatePage getCurrentScenarioName() {
        scenarioDropdown.getText();
        return this;
    }

    /**
     * Gets the process group details
     * @return group details as string
     */
    public String getProcessGroup() {
        return processGroupDropdown.getText();
    }

    /**
     * Selects the revert button
     * @return new page object
     */
    public RevertPage revert() {
        pageUtils.waitForElementToAppear(revertButton).click();
        return new RevertPage(driver);
    }
}