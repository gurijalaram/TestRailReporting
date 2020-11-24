package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import com.pageobjects.pages.login.PrivacyPolicyPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReportsLoginPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(ReportsLoginPage.class);
    private static String loginPageURL = CommonConstants.cirURL;

    @FindBy(css = "input[name='j_username']")
    private WebElement email;

    @FindBy(css = "input[name='j_password_pseudo']")
    private WebElement password;

    @FindBy(css = "a[href='javascript:void(0)']")
    private WebElement forgotPassword;

    @FindBy(css = "button[id='submitButton']")
    private WebElement submitButton;

    @FindBy(css = "span[class='animated fadeInUp']")
    private WebElement loginMsg;

    @FindBy(css = "div.auth0-lock-error-msg")
    private WebElement inputErrorMsg;

    @FindBy(css = "a[href='https://www.apriori.com/sso-instructions-page']")
    private WebElement helpButton;

    @FindBy(css = "a[href='https://www.apriori.com/privacy-policy']")
    private WebElement privacyPolicyButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ReportsLoginPage(WebDriver driver) {
        super(driver);
        init(driver, "", true);
    }

    public ReportsLoginPage(WebDriver driver, String url) {
        super(driver);
        init(driver, url, true);
    }

    public ReportsLoginPage(WebDriver driver, boolean loadNewPage) {
        super(driver);
        init(driver, "", loadNewPage);
    }

    public void init(WebDriver driver, String url, boolean loadNewPage) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        if (url == null || url.isEmpty()) {
            url = loginPageURL;
        }
        if (loadNewPage) {
            driver.get(url);
        }
        logger.info("CURRENTLY ON INSTANCE: " + url);
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
     * Enter email details
     *
     * @param emailAddress - user email address
     */
    private void enterEmail(String emailAddress) {
        pageUtils.waitForElementAndClick(email);
        pageUtils.clearInput(email);
        email.sendKeys(emailAddress);
    }

    /**
     * Enter password
     *
     * @param password - user password
     */
    private void enterPassword(String password) {
        pageUtils.waitForElementAndClick(this.password);
        pageUtils.clearInput(this.password);
        this.password.sendKeys(password);
    }

    /**
     * Single action to submit login credentials
     */
    private void submitLogin() {
        pageUtils.waitForElementAndClick(submitButton);
    }

    /**
     * Execute actions to login
     *
     * @param email    - user email
     * @param password - user password
     */
    private void executeLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        submitLogin();
    }

    /**
     * Login to CI Report
     *
     * @param email    - user email
     * @param password - user password
     * @return new page object
     */
    @Deprecated
    public ReportsPageHeader login(String email, String password) {
        executeLogin(email, password);
        return new ReportsPageHeader(driver);
    }

    /**
     * Login to CI Report with passed in user (from CSV file)
     *
     * @return new page object
     */
    public ReportsPageHeader login(UserCredentials userCredentials) {
        executeLogin(userCredentials.getUsername(), userCredentials.getPassword());
        return new ReportsPageHeader(driver);
    }

    /**
     * Login to CI Report with passed in user (from Jenkins)
     *
     * @return new page object
     */
    public ReportsPageHeader login() {
        UserCredentials userCredentials;

        if (CommonConstants.PROP_USER_NAME != null && CommonConstants.PROP_USER_PASSWORD != null) {
            userCredentials = new UserCredentials(CommonConstants.PROP_USER_NAME, CommonConstants.PROP_USER_PASSWORD);
        } else {
            userCredentials = UserUtil.getUser();
        }

        executeLogin(userCredentials.getUsername(), userCredentials.getPassword());
        return new ReportsPageHeader(driver);
    }

    /**
     * Failed login to CI Report
     *
     * @param email    - user email
     * @param password - user password
     * @return current page object
     */
    public ReportsLoginPage failedLogin(String email, String password) {
        executeLogin(email, password);
        return new ReportsLoginPage(driver, false);
    }

    /**
     * Get login message text
     *
     * @return String
     */
    public String getLoginMessage() {
        pageUtils.waitForElementToAppear(loginMsg);
        return loginMsg.getText();
    }

    /**
     * Click forgot password link
     */
    public ReportsLoginPage clickForgotPassword() {
        pageUtils.waitForElementToAppear(forgotPassword);
        forgotPassword.click();
        return new ReportsLoginPage(driver, false);
    }

    /**
     * Submit email address alone for password recovery
     *
     * @param email - user email
     */
    public ReportsLoginPage submitEmail(String email) {
        enterEmail(email);
        submitLogin();
        return new ReportsLoginPage(driver, false);
    }

    /**
     * Get input error message
     *
     * @return String
     */
    public String getInputErrorMsg() {
        pageUtils.waitForElementToAppear(inputErrorMsg);
        return inputErrorMsg.getText();
    }

    public String getInputErrorMessagesLocalInstall() {
        List<WebElement> errors = driver.findElements(By.cssSelector("p[class='errorMessage']"));
        return String.format("%s %s", errors.get(0).getText(), errors.get(1).getText());
    }

    /**
     * Wait for privacy policy link visibility
     *
     * @return current page object
     */
    public ReportsLoginPage waitForPrivacyPolicyLinkVisibility() {
        pageUtils.waitForElementToAppear(privacyPolicyButton);
        return this;
    }

    /**
     * Clicks Privacy Policy link
     *
     * @return Privacy Policy page object
     */
    public PrivacyPolicyPage goToPrivacyPolicy() {
        privacyPolicyButton.click();
        return new PrivacyPolicyPage(driver);
    }
}