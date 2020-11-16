package com.apriori.pageobjects.pages.logout;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(com.pageobjects.pages.logout.LogoutPage.class);

    @FindBy(css = "div[class='auth0-lock-header-welcome'] > div")
    private WebElement loginPageTitle;

    private WebDriver driver;
    private PageUtils pageUtils;

    public LogoutPage(WebDriver driver) {
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

    /**
     * Gets isDisplayed value for header
     *
     * @return boolean - isDisplayed
     */
    public boolean isHeaderDisplayed() {
        pageUtils.waitForElementToAppear(loginPageTitle);
        return pageUtils.isElementDisplayed(loginPageTitle);
    }

    /**
     * Gets isEnabled value for header
     *
     * @return boolean - isEnabled
     */
    public boolean isHeaderEnabled() {
        pageUtils.waitForElementToAppear(loginPageTitle);
        return pageUtils.isElementEnabled(loginPageTitle);
    }

    /**
     * Gets header text
     *
     * @return String - header text
     */
    public String getHeaderText() {
        pageUtils.waitForElementToAppear(loginPageTitle);
        return loginPageTitle.getText();
    }
}
