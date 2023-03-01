package com.apriori.pageobjects.pages.homepage;

import com.apriori.pageobjects.header.AdminPageHeader;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class AdminHomePage extends AdminPageHeader {

    @FindBy(css = "div[class='devices']")
    private WebElement reportsWelcomeText;

    @FindBy(xpath = "//h2[contains(text(), 'Domains')]")
    private WebElement domainOption;

    @FindBy(xpath = "//div[contains(text(), 'Welcome to')]")
    private WebElement adminHomePageWelcomeText;

    private WebDriver driver;
    private PageUtils pageUtils;

    public AdminHomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.isElementDisplayed(adminHomePageWelcomeText);
        pageUtils.isElementEnabled(adminHomePageWelcomeText);
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
     * Gets count of open tabs
     *
     * @return int
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Gets url to check
     *
     * @return String
     */
    public String getUrlToCheck() {
        return PropertiesContext.get("base_url");
    }

    /**
     * Wait for element to appear
     */
    public void waitForReportsLogoutDisplayedToAppear() {
        pageUtils.windowHandler(1);
        pageUtils.waitForElementToAppear(domainOption);
    }

    /**
     * Checks if Reports Logout button element is displayed
     *
     * @return boolean
     */
    public boolean isReportsWelcomeTextDisplayed() {
        return pageUtils.isElementDisplayed(domainOption);
    }

    /**
     * Checks if Reports Logout button element is enabled
     *
     * @return boolean
     */
    public boolean isReportsWelcomeTextEnabled() {
        return pageUtils.isElementEnabled(domainOption);
    }
}
