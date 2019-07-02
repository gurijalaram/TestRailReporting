package main.java.pages.evaluate.designguidance;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuidancePage extends LoadableComponent<GuidancePage> {

    private final Logger logger = LoggerFactory.getLogger(GuidancePage.class);

    @FindBy(css = "div[data-ap-comp='guidanceIssuesTable']")
    private WebElement guidanceTable;

    @FindBy(css = "div[data-ap-comp='guidanceIssuesTable'] div.v-grid-scroller-vertical")
    private WebElement guidanceTableScroller;

    @FindBy(css = "div[data-ap-comp='guidanceIssuesDetailsTable'] div.v-grid-scroller-vertical")
    private WebElement detailsTableScroller;

    @FindBy(css = "div[data-ap-comp='guidanceIssuesMessage']")
    private WebElement gcdMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private DesignGuidancePage designGuidancePage;

    public GuidancePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(guidanceTable);
    }

    /**
     * Selects both issue type and gcd details
     * @param issueType - the issue type
     * @param gcd - the gcd
     * @return current page object
     */
    public GuidancePage selectIssueTypeAndGCD(String issueType, String gcd) {
        selectIssueType(issueType).click();
        selectGCD(gcd).click();
        return this;
    }

    /**
     * Selects the issue type
     * @param issueType - the issue type
     * @return issue type as webelement
     */
    private WebElement selectIssueType(String issueType) {
        By issue = By.xpath("//div[@data-ap-comp='guidanceIssuesTable']//div[contains(text(),'" + issueType + "')]");
        return pageUtils.scrollToElement(issue, guidanceTableScroller);
    }

    // TODO: 17/06/2019 ciene - implement functionality to properly scroll the issue type and select dropdown caret.  this functionality will be used for geometry and secondary processes

    /**
     * Selects the gcd
     * @param gcdType - the gcd
     * @return gcd as a webelement
     */
    private WebElement selectGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='guidanceIssuesDetailsTable']//td[contains(text(),'" + gcdType + "')]");
        return pageUtils.scrollToElement(gcd, detailsTableScroller);
    }

    /**
     * Selects the entire row of the gcd
     * @param gcdType - the gcd
     * @return gcd as a webelement
     */
    public WebElement selectGCDRow(String gcdType) {
        By gcdRow = By.xpath("//div[@data-ap-comp='guidanceIssuesDetailsTable']//td[contains(text(),'" + gcdType + "')]/ancestor::tr");
        return pageUtils.scrollToElement(gcdRow,detailsTableScroller);
    }

    /**
     * Gets the displayed guidance message
     * @return guidance message
     */
    public String getGuidanceMessage() {
        return pageUtils.waitForElementToAppear(gcdMessage).getText();
    }
}