package com.apriori.pageobjects.pages.evaluate.designguidance;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailuresPage extends LoadableComponent<FailuresPage> {

    private final Logger logger = LoggerFactory.getLogger(FailuresPage.class);

    @FindBy(css = "div[data-ap-comp='uncostedFeaturesInfo']")
    private WebElement failuresInfo;

    @FindBy(css = "div[data-ap-comp='uncostedMessage']")
    private WebElement uncostedMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private DesignGuidancePage designGuidancePage;

    public FailuresPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(failuresInfo);
    }

    /**
     * Selects both issue type and gcd details
     *
     * @param gcd               - the gcd
     * @return current page object
     */
    public FailuresPage selectIssueTypeAndGCD(String issueType, String gcd) {
        selectIssueType(issueType).click();
        selectGCD(gcd).click();
        return this;
    }

    /**
     * Selects the issue type
     *
     * @param issueType - the issue type
     * @return
     */
    private WebElement selectIssueType(String issueType) {
        By issue = By.xpath("//div[@data-ap-comp='uncostedFeaturesInfo']//td[contains(text(),'" + issueType + "')]");
        return pageUtils.waitForElementToAppear(issue);
    }

    /**
     * Selects the gcd
     *
     * @param gcdType - the gcd
     * @return gcd as a webelement
     */
    private WebElement selectGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='uncostedFeaturesInfo']//td[contains(text(),'" + gcdType + "')]/ancestor::tr");
        return pageUtils.waitForElementToAppear(gcd);
    }

    /**
     * Gets the displayed reason for failure message
     *
     * @return uncosted message
     */
    public String getUncostedMessage() {
        return pageUtils.waitForElementToAppear(uncostedMessage).getText();
    }
}

