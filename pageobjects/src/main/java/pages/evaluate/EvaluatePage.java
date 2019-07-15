package main.java.pages.evaluate;

import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.materialutilization.MaterialCompositionPage;
import main.java.pages.evaluate.materialutilization.MaterialPage;
import main.java.pages.evaluate.process.ProcessPage;
import main.java.pages.explore.ExplorePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EvaluatePage extends LoadableComponent<EvaluatePage> {

    private final Logger logger = LoggerFactory.getLogger(EvaluatePage.class);

    @FindBy(css = "div.main-viewport")
    private WebElement leftPanel;

    @FindBy(css = "div[data-ap-comp='part-viewer-panel'] canvas")
    private WebElement viewerCanvas;

    @FindBy(css = "ul.viewer-controls-toolbar")
    private WebElement controlToolbars;

    @FindBy(css = "button[data-ap-comp='editScenarioButton']")
    private WebElement editButton;

    @FindBy(css = "button[data-ap-comp='revertScenarioButton']")
    private WebElement revertButton;

    @FindBy(css = "button[data-ap-comp='deleteScenarioButton']")
    private WebElement deleteButton;

    @FindBy(css = "button[data-ap-comp='costButton']")
    private WebElement costButton;

    @FindBy(css = ".bottom .popover-content .gwt-HTML")
    private WebElement costLabelPopover;

    @FindBy(css = "li[data-ap-comp='costButton']")
    private WebElement costLabel;

    @FindBy(css = "a[data-ap-comp='updateInfoLink']")
    private WebElement infoNotes;

    @FindBy(css = "span.gwt-InlineHTML.glyphicon.glyphicon-chevron-right")
    private WebElement infoNotesChevron;

    @FindBy(css = "div.btn-group.bootstrap-select.form-control.open")
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

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement dialogCostButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton'] span")
    private WebElement publishButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private static final String COST_UP_TO_DATE = "Cost up to\n" + "Date";

    public EvaluatePage(WebDriver driver) {
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
     * Cost the scenario. Enter 'null' if the cost label is expected to be default label
     *
     * @param costText - the text for the cost label
     * @return current page object
     */
    public EvaluatePage costScenario(String costText) {
        pageUtils.waitForElementToBeClickable(costButton).click();
        pageUtils.waitForElementToAppear(dialogCostButton).click();
        costText = costText == "Success" ? COST_UP_TO_DATE : costText;
        checkCostLabel(costText);
        return this;
    }

    /**
     * Checks the text in the cost label
     *
     * @param costText - the cost label text
     * @return true or false
     */
    private boolean checkCostLabel(String costText) {
        pageUtils.waitForElementToAppear(costLabelPopover);
        return pageUtils.waitForElementToAppear(costLabel).getText().equalsIgnoreCase(costText);
    }

    /**
     * Publish the scenario
     *
     * @return new page object
     */
    public ExplorePage publishScenario() {
        pageUtils.waitForElementToAppear(viewerCanvas);
        pageUtils.waitForElementToBeClickable(publishButton).click();
        new PublishPage(driver).selectPublishButton();
        return new ExplorePage(driver);
    }

    /**
     * Publish the scenario
     *
     * @param status       - the status dropdown
     * @param costMaturity - the cost maturity dropdown
     * @param assignee     - the assignee
     * @return new page object
     */
    public ExplorePage publishScenario(String status, String costMaturity, String assignee) {
        pageUtils.waitForElementToBeClickable(publishButton).click();
        new PublishPage(driver).selectStatus(status)
            .selectCostMaturity(costMaturity)
            .selectAssignee(assignee)
            .selectPublishButton();
        return new ExplorePage(driver);
    }

    /**
     * Selects the pg dropdown
     *
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage selectProcessGroup(String processGroup) {
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
    public ProcessPage openProcessDetails() {
        pageUtils.waitForElementToAppear(processDetails).click();
        return new ProcessPage(driver);
    }

    /**
     * Gets the process routing details
     *
     * @return the details as string
     */
    public String getProcessRoutingDetails() {
        pageUtils.checkElementsNotVisibleByBoolean(processRoutingState);
        return processRoutingName.getAttribute("title");
    }

    /**
     * Opens the design guidance dialog
     *
     * @return new page object
     */
    public DesignGuidancePage openDesignGuidance() {
        pageUtils.waitForElementToAppear(guidanceDetails).click();
        return new DesignGuidancePage(driver);
    }

    /**
     * Opens the secondary process dialog
     *
     * @return new page object
     */
    public SecondaryProcessPage openSecondaryProcess() {
        pageUtils.waitForElementToAppear(secondaryProcessButton).click();
        return new SecondaryProcessPage(driver);
    }

    /**
     * Opens the material composition dialog
     *
     * @return new page object
     */
    public MaterialPage openMaterialComposition() {
        pageUtils.waitForElementToAppear(materialsDetails).click();
        return new MaterialPage(driver);
    }

    /**
     * Opens the material composition table
     *
     * @return new page object
     */
    public MaterialCompositionPage openMaterialCompositionTable() {
        pageUtils.waitForElementToBeClickable(materialsButton).click();
        return new MaterialCompositionPage(driver);
    }
}