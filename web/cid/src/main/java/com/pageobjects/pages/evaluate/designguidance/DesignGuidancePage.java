package com.pageobjects.pages.evaluate.designguidance;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.pageobjects.pages.evaluate.designguidance.tolerances.TolerancePage;
import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class DesignGuidancePage extends EvaluatePanelToolbar {

    private final Logger logger = LoggerFactory.getLogger(DesignGuidancePage.class);

    @FindBy(css = ".panel.panel-details")
    private WebElement panelDetails;

    @FindBy(css = "a[href='#failuresAndWarningsTab']")
    private WebElement failuresWarningsTab;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement questionButton;

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

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    @FindBy(xpath = "//div[contains(text(), 'Hole Issue')]/ancestor::td/following-sibling::td")
    private WebElement holeIssueValue;

    @FindBy(xpath = "(//td[contains(@class, 'numeric-column')])[1]")
    private WebElement draftIssueValue;

    @FindBy(xpath = "(//td[contains(@class, 'numeric-column')])[3]")
    private WebElement radiusIssueValue;

    private WebDriver driver;
    private PageUtils pageUtils;

    public DesignGuidancePage(WebDriver driver) {
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
        pageUtils.waitForElementToBeClickable(investigationTab);
        pageUtils.waitForElementToAppear(panelDetails);
    }

    /**
     * Opens the guidance tab
     *
     * @return new page object
     */
    public GuidancePage openGuidanceTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(guidanceTab);
        return new GuidancePage(driver);
    }

    /**
     * Opens the failures and warnings tab
     *
     * @return new page object
     */
    public FailuresWarningsPage openFailuresWarningsTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(failuresWarningsTab);
        return new FailuresWarningsPage(driver);
    }

    /**
     * Opens the investigation tab
     *
     * @return new page object
     */
    public InvestigationPage openInvestigationTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(investigationTab);
        return new InvestigationPage(driver);
    }

    /**
     * Opens the tolerances tab
     *
     * @return new page object
     */
    public TolerancePage openTolerancesTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(tolerancesTab);
        return new TolerancePage(driver);
    }

    /**
     * Opens the geometry tab
     *
     * @return new page object
     */
    public GeometryPage openGeometryTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(geometryTab);
        return new GeometryPage(driver);
    }

    /**
     * Gets the Hole Issue value
     *
     * @return String - Hole Issue value
     */
    public String getHoleIssueValue() {
        pageUtils.waitForElementToAppear(holeIssueValue);
        return holeIssueValue.getText();
    }

    /**
     * Gets DTC Issue Value Draft or Radius
     *
     * @param issueName - String of value title
     * @return String value
     */
    public String getDtcIssueValue(String issueName) {
        By locator = By.xpath(String.format(
            "//div[@title='%s Issue']/ancestor::tr[2]/td[contains(@class, 'numeric-column')]",
            issueName)
        );
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }
}
