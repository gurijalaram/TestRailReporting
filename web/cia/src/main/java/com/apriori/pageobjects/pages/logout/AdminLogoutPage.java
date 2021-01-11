package com.apriori.pageobjects.pages.logout;

import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminLogoutPage extends AdminHeader {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminLogoutPage.class);

    @FindBy(css = "div[class='auth0-lock-header-welcome'] > div")
    private WebElement loginPageTitle;

    @FindBy(css = "button[type='submit'")
    private WebElement loginButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public AdminLogoutPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * Checks if login button is displayed and enabled
     * @return boolean
     */
    public boolean isLoginButtonDisplayedAndEnabled() {
        pageUtils.waitForElementToAppear(loginButton);
        return loginButton.isDisplayed() && loginButton.isEnabled();
    }
}
