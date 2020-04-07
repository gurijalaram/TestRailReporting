package com.apriori.pageobjects.reports.pages.homepage;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    @FindBy(css = "input[name='j_username']")
    private WebElement email;

    @FindBy(css = "h2[class='textAccent']")
    private WebElement reportsHomePageWelcomeText;

    private PageUtils pageUtils;
    private HelpPage helpPage;
    private WebDriver driver;

    public HomePage(WebDriver driver) {
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

    }

    @FindBy(css = "button[aria-label='Create Data Sources']")
    private WebElement createButton;

    /**
     * Check if Create button is displayed
     *
     * @return Visibility of button
     */
    public boolean isCreateButtonDisplayed() {
        pageUtils.waitForElementToAppear(createButton);
        return createButton.isDisplayed();
    }

    /**
     * Gets current URL of new tab
     *
     * @return String
     */
    public String getCurrentUrl() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets url to check
     *
     * @return String
     */
    public String getUrlToCheck() {
        return pageUtils.getLocalUrlToCheck();
    }

    /**
     * Gets count of open tabs
     *
     * @return int
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Wait for element to appear
     */
    public void waitForReportsLogoutDisplayedToAppear() {
        pageUtils.windowHandler();
        isLoadedNow();
        //pageUtils.waitForElementToAppear(reportsLogoutOption);
    }

    /**
     * Checks if Reports Logout button element is displayed
     *
     * @return boolean
     */
    public boolean isReportsLogoutDisplayed() {
        return pageUtils.isElementDisplayed(reportsHomePageWelcomeText);
    }

    /**
     * Checks if Reports Logout button element is enabled
     *
     * @return boolean
     */
    public boolean isReportsLogoutEnabled() {
        return pageUtils.isElementEnabled(reportsHomePageWelcomeText);
    }

    private void isLoadedNow() {
        //pageUtils.waitForElementToAppear(reportsLogoutOption);
        pageUtils.windowHandler();
        pageUtils.waitForElementToAppear(email);
    }
}