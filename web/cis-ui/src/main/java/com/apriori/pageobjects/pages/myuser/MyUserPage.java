package com.apriori.pageobjects.pages.myuser;

import com.apriori.EagerPageComponent;
import com.apriori.PageUtils;
import com.apriori.pageobjects.pages.help.ZendeskSignInPage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.settings.UserPreferencePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

@Slf4j
public class MyUserPage extends EagerPageComponent<MyUserPage> {

    @FindBy(xpath = "//button[.='My Profile']")
    private WebElement myProfileButton;

    @FindBy(xpath = "//button[.='Terms of Use']")
    private WebElement termsOfUseButton;

    @FindBy(xpath = "//button[.='Logout']")
    private WebElement logoutButton;

    @FindBy(xpath = "//li[.='Settings']")
    private WebElement settingsButton;

    @FindBy(xpath = "//li[.='Terms of Use']")
    private WebElement termsOfUseButtonButton;

    @FindBy(xpath = "//li[.='Support']")
    private WebElement supportButton;

    @FindBy(xpath = "//li[.='Log Out']")
    private WebElement logOutButton;

    @FindBy(xpath = "//div[@data-testid='avatar']")
    private WebElement btnUserIcon;

    private PageUtils pageUtils;


    public MyUserPage(WebDriver driver) {
        this(driver, log);
    }

    public MyUserPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        PageFactory.initElements(driver, this);
        this.pageUtils = new PageUtils(driver);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Selects the User dropdown and go to Logout
     *
     * @retun new page object
     */
    public CisLoginPage logout() {
        getPageUtils().waitForElementAndClick(logoutButton);
        return new CisLoginPage(getDriver());
    }

    /**
     * Click on My Profile
     *
     * @return new page object
     */
    public MyProfilePage selectMyProfile() {
        getPageUtils().waitForElementAndClick(myProfileButton);
        return new MyProfilePage(getDriver());
    }

    /**
     * Click Terms of Use
     *
     * @return new page object
     */
    public TermsOfUsePage selectTermsOfUse() {
        getPageUtils().waitForElementAndClick(termsOfUseButton);
        return new TermsOfUsePage(getDriver());
    }

    /**
     * Checks if settings option displayed
     *
     * @return true/false
     */
    public boolean isSettingsOptionDisplayed() {
        return pageUtils.isElementDisplayed(settingsButton);
    }

    /**
     * Checks if terms of use option displayed
     *
     * @return true/false
     */
    public boolean isTermsOfUseOptionDisplayed() {
        return pageUtils.isElementDisplayed(termsOfUseButtonButton);
    }

    /**
     * Checks if support option displayed
     *
     * @return true/false
     */
    public boolean  isSupportOptionDisplayed() {
        return pageUtils.isElementDisplayed(supportButton);
    }

    /**
     * Checks if logout option displayed
     *
     * @return true/false
     */
    public boolean isLogoutOptionDisplayed() {
        return pageUtils.isElementDisplayed(logOutButton);
    }

    /**
     * Click Logout
     *
     * @return current page object
     */
    public MyUserPage clickLogOut() {
        getPageUtils().waitForElementAndClick(logOutButton);
        return new MyUserPage(getDriver());
    }

    /**
     * Click Terms Of Use
     *
     * @return current page object
     */
    public TermsOfUsePage clickTermsOfUse() {
        getPageUtils().waitForElementAndClick(termsOfUseButtonButton);
        return new TermsOfUsePage(getDriver());
    }

    /**
     * Click Support
     *
     * @return new page object
     */
    public ZendeskSignInPage clickSupport() {
        getPageUtils().waitForElementAndClick(supportButton);
        return new ZendeskSignInPage(getDriver());
    }

    /**
     * Gets logged username
     *
     * @return String
     */
    public String getLoggedUsername() {
        return getPageUtils().waitForElementToAppear(btnUserIcon).getText();
    }

    /**
     * Click Settings
     *
     * @return new page object
     */
    public UserPreferencePage clickSettings() {
        getPageUtils().waitForElementAndClick(settingsButton);
        return new UserPreferencePage(getDriver());
    }

    /**
     * select user preference
     *
     * @return new page object
     */
    public UserPreferencePage selectUserPreference() {
        getPageUtils().waitForElementAndClick(btnUserIcon);
        getPageUtils().waitForElementAndClick(settingsButton);
        return new UserPreferencePage(getDriver());
    }
}
