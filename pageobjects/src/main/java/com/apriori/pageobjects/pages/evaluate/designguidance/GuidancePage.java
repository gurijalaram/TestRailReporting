package com.apriori.pageobjects.pages.evaluate.designguidance;

import com.apriori.pageobjects.utils.ColumnUtils;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

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
    private ColumnUtils columnUtils;

    public GuidancePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.columnUtils = new ColumnUtils(driver);
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
     *
     * @param issueTypeDropdown - the issue type parent
     * @param issueType - the issue type
     * @param gcd               - the gcd
     * @return current page object
     */
    public GuidancePage selectIssueTypeAndGCD(String issueTypeDropdown, String issueType, String gcd) {
        collapseIssueDropdown();
        selectIssue(issueTypeDropdown);
        findIssueType(issueType).click();
        findGCD(gcd).click();
        pageUtils.scrollUp(guidanceTableScroller, 2);
        return this;
    }

    /**
     * Collapses the dropdowns so the correct can be selected
     *
     * @return current page object
     */
    private GuidancePage collapseIssueDropdown() {
        By dropdown = By.cssSelector(".fa.fa-caret-down");
        pageUtils.scrollToElement(dropdown, guidanceTableScroller);
        pageUtils.waitForElementToAppear(dropdown).click();
        return this;
    }

    /**
     * Selects the issue type dropdown
     *
     * @param issueTypeDropdown - the issue type dropdown
     * @return issue type as webelement
     */
    private GuidancePage selectIssue(String issueTypeDropdown) {

        String[] parents = issueTypeDropdown.split(",");

        for (String parent : parents) {
            By issue = By.xpath("//div[@data-ap-comp='guidanceIssuesTable']//div[contains(text(),'" + parent.trim() + "')]/ancestor::tr//span[@class]");
            pageUtils.scrollToElement(issue, guidanceTableScroller);

            if (driver.findElement(issue).getAttribute("class").contains("right")) {
                driver.findElement(issue).click();
            }
        }
        return this;
    }

    /**
     * Selects the issue type
     *
     * @param issueType - the issue type
     * @return
     */
    private WebElement findIssueType(String issueType) {
        By issue = By.xpath("//div[@data-ap-comp='guidanceIssuesTable']//div[contains(text(),'" + issueType.trim() + "')]");
        return pageUtils.scrollToElement(issue, guidanceTableScroller);
    }

    /**
     * Selects the gcd
     *
     * @param gcdType - the gcd
     * @return gcd as a webelement
     */
    private WebElement findGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='guidanceIssuesDetailsTable']//td[contains(text(),'" + gcdType + "')]/ancestor::tr");
        return pageUtils.scrollToElement(gcd, detailsTableScroller);
    }

    /**
     * Gets the displayed guidance message
     *
     * @return guidance message
     */
    public String getGuidanceMessage() {
        return pageUtils.waitForElementToAppear(gcdMessage).getText();
    }

    /**
     * Finds the issue type
     * @param issueTypeDropdown - the issue type parent
     * @param issueTypeDropdown - the issue type
     * @return current page object
     */
    public GuidancePage findIssueType(String issueTypeDropdown, String issueType) {
        collapseIssueDropdown();
        selectIssue(issueTypeDropdown);
        findIssueType(issueType);
        return this;
    }

    /**
     * Gets the cell details
     *
     * @param issueType         - tolerance type
     * @param column            - the column
     * @return string
     */
    public String getGuidanceCell(String issueType, String column) {
        String rowLocator = "//div[@data-ap-comp='guidanceIssuesTable']//div[contains(text(),'" + issueType + "')]/ancestor::tr[@class]";
        return columnUtils.columnDetails("guidanceIssuesTable", column, rowLocator);
    }
}