package main.java.pages.evaluate.designguidance;

import main.java.pages.evaluate.designguidance.investigation.InvestigationPage;
import main.java.pages.evaluate.designguidance.tolerances.TolerancePage;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesignGuidancePage extends LoadableComponent<DesignGuidancePage> {

    private final Logger logger = LoggerFactory.getLogger(DesignGuidancePage.class);

    @FindBy(css = ".panel.panel-details")
    private WebElement panelDetails;

    @FindBy(css = "a[href='#failuresAndWarningsTab']")
    private WebElement failuresTab;

    @FindBy(css = "button[data-ap-comp='expandPanelButton']")
    private WebElement chevronButton;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement questionButton;

    @FindBy(css = ".details-viewport-part .glyphicon-remove")
    private WebElement closeButton;

    @FindBy(css = "a[href='#guidanceTab")
    private WebElement guidanceTab;

    @FindBy(css = "a[href='#investigationTab")
    private WebElement investigationTab;

    @FindBy(css = "a[href='#tolerancesTab")
    private WebElement tolerancesTab;

    @FindBy(css = "a[href='#geometryTab")
    private WebElement geometryTab;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public DesignGuidancePage(WebDriver driver) {
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
        pageUtils.waitForElementToBeClickable(failuresTab);
        pageUtils.waitForElementToAppear(panelDetails);
    }

    /**
     * Opens the guidance tab
     * @return new page object
     */
    public GuidancePage openGuidanceTab() {
        pageUtils.waitForElementToBeClickable(guidanceTab).click();
        return new GuidancePage(driver);
    }

    /**
     * Opens the investigation tab
     * @return new page object
     */
    public InvestigationPage openInvestigationTab() {
        investigationTab.click();
        return new InvestigationPage(driver);
    }

    /**
     * Opens the tolerances tab
     * @return new page object
     */
    public TolerancePage openTolerancesTab() {
        tolerancesTab.click();
        return new TolerancePage(driver);
    }

    /**
     * Opens the geometry tab
     * @return new page object
     */
    public GeometryPage openGeometryTab() {
        geometryTab.click();
        return new GeometryPage(driver);
    }

    /**
     * Closes the design guidance
     * @return current page object
     */
    public DesignGuidancePage closeDesignGuidance() {
        pageUtils.javaScriptClick(closePanelButton);
        return this;
    }
}
