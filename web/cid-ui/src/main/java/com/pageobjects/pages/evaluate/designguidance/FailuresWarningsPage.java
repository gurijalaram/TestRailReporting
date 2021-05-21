package com.pageobjects.pages.evaluate.designguidance;

import com.apriori.utils.ColumnUtils;
import com.apriori.utils.PageUtils;

import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailuresWarningsPage extends EvaluatePanelToolbar {

    private static final Logger logger = LoggerFactory.getLogger(FailuresWarningsPage.class);

    @FindBy(css = "div[data-ap-comp='uncostedFeaturesInfo']")
    private WebElement failuresInfo;

    @FindBy(css = "div[data-ap-comp='uncostedMessage']")
    private WebElement uncostedMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ColumnUtils columnUtils;

    public FailuresWarningsPage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(failuresInfo);
    }

    /**
     * Selects both issue type and gcd details
     *
     * @param gcd - the gcd
     * @return current page object
     */
    public FailuresWarningsPage selectIssueTypeAndGCD(String issueType, String gcd) {
        findIssueType(issueType).click();
        findGCD(gcd).click();
        return this;
    }

    /**
     * Selects the issue type
     *
     * @param issueType - the issue type
     * @return
     */
    public WebElement findIssueType(String issueType) {
        By issue = By.xpath("//div[@data-ap-comp='uncostedFeaturesInfo']//td[contains(text(),'" + issueType + "')]");
        return pageUtils.waitForElementToAppear(issue);
    }

    /**
     * Selects the gcd
     *
     * @param gcdType - the gcd
     * @return gcd as a webelement
     */
    private WebElement findGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='uncostedFeaturesInfo']//td[contains(text(),'" + gcdType + "')]/ancestor::tr");
        return pageUtils.waitForElementToAppear(gcd);
    }

    /**
     * Gets the displayed reason for failure
     *
     * @return failure message
     */
    public String getUncostedMessage() {
        return pageUtils.waitForElementToAppear(uncostedMessage).getText();
    }

    /**
     * Gets the cell details
     *
     * @param issueType - tolerance type
     * @param column    - the column
     * @return string
     */
    public String getFailuresCell(String issueType, String column) {
        String rowLocator = "//div[@data-ap-comp='uncostedFeaturesInfo']//td[contains(text(),'" + issueType + "')]/ancestor::tr";
        return columnUtils.columnDetails("uncostedFeaturesInfo", column, rowLocator);
    }
}

