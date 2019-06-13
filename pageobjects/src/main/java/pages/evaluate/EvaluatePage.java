package main.java.pages.evaluate;

import main.java.pages.explore.PrivateWorkspacePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private WebElement annualVolume;

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

    @FindBy(css = "a[data-ap-nav-viewport='showCostResultDetails']")
    private WebElement resultsDetails;

    @FindBy(css = "button.gwt-Button.btn.btn-primary")
    private WebElement dialogCostButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton']")
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
     * Cost the scenario
     * @return current page object
     */
    public EvaluatePage costScenario() {
        costButton.click();
        pageUtils.waitForElementToBeClickable(dialogCostButton).click();
        checkCostLabel(COST_UP_TO_DATE);
        return this;
    }

    /**
     * Cost the scenario
     * @param costText - the text in the cost label
     * @return current page object
     */
    public EvaluatePage costScenario(String costText) {
        costButton.click();
        pageUtils.waitForElementToBeClickable(dialogCostButton).click();
        checkCostLabel(costText);
        return this;
    }

    /**
     * Check the cost label text
     * @param costText - the cost label text
     * @return current page object
     */
    private EvaluatePage checkCostLabel(String costText) {
        pageUtils.waitForElementToAppear(costLabel).getText().equalsIgnoreCase(costText);
        return this;
    }

    /**
     * Publish the scenario
     * @return new page object
     */
    public PrivateWorkspacePage publishScenario() {
        publishButton.click();
        return new PrivateWorkspacePage(driver);
    }

    /**
     * Publish the scenario
     * @param status - the status dropdown
     * @param costMaturity - the cost maturity dropdown
     * @param assignee - the assignee
     * @return new page object
     */
    public PrivateWorkspacePage publishScenario(String status, String costMaturity, String assignee) {
        publishButton.click();
        new PublishPage(driver).selectStatus(status)
            .selectCostMaturity(costMaturity)
            .selectAssignee(assignee);
        return new PrivateWorkspacePage(driver);
    }

    /**
     * Selects the pg dropdown
     * @param processGroup - the process group
     * @return current page object
     */
    public EvaluatePage selectProcessGroup(String processGroup) {
        new Select(processGroupDropdown).selectByVisibleText(processGroup);
        return this;
    }

    /**
     * Selects the vpe dropdown
     * @param vpe - the vpe
     * @return current page object
     */
    public EvaluatePage selectVPE(String vpe) {
        new Select(vpeDropdown).selectByVisibleText(vpe);
        return this;
    }

    /**
     * Enters the annual volume
     * @param annVolume - the annual volume
     * @return current page object
     */
    public EvaluatePage enterAnnualVolume(String annVolume) {
        pageUtils.clearInput(annualVolume);
        annualVolume.sendKeys(annVolume);
        return this;
    }

    /**
     * Enters the years of annual volume
     * @param years - the years
     * @return current page object
     */
    public EvaluatePage enterAnnualYears(String years) {
        pageUtils.clearInput(annualVolumeYrs);
        annualVolumeYrs.sendKeys(years);
        return this;
    }
}
