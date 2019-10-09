package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author cfrith
 */

public class LoginPage extends LoadableComponent<LoginPage> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static String loginPageUrl = Constants.cidURL;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "a[href='javascript:void(0)")
    private WebElement forgotPassword;

    @FindBy(css = "button[type='submit']")
    private WebElement submitLogin;

    @FindBy(css = "div.auth0-global-message.auth0-global-message-error span")
    private WebElement loginErrorMsg;

    @FindBy(css = ".auth0-lock-header-logo")
    private WebElement aprioriLogo;

    @FindBy(css = ".auth0-lock-name")
    private WebElement aprioriTitle;

    @FindBy(xpath = "//a[contains(text(),'Privacy Policy')]")
    private WebElement privacyPolicy;

    @FindBy(xpath = "//a[contains(text(),'Help')]")
    private WebElement helpLink;

    @FindBy(css = ".marketing-info")
    private WebElement marketingText;

    @FindBy(css = ".welcome-message")
    private WebElement welcomeText;

    @FindBy(css = ".legal-text")
    private WebElement legalText;

    @FindBy(xpath = "//a[@class='white-link']")
    private WebElement learnMore;

    private WebDriver driver;
    private PageUtils pageUtils;

    public LoginPage(WebDriver driver) {
        init(driver, "", true);
    }

    public LoginPage(WebDriver driver, String url) {
        init(driver, url, true);
    }

    public LoginPage(WebDriver driver, boolean loadNewPage) {
        init(driver, "", loadNewPage);
    }

    public void init(WebDriver driver, String url, boolean loadNewPage) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        if (url == null || url.isEmpty()) {
            url = loginPageUrl;
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
        pageUtils.waitForElementToAppear(email);
        pageUtils.waitForElementToAppear(password);
        pageUtils.waitForElementToAppear(submitLogin);
    }

    /**
     * Login to cid
     *
     * @param email    - the email
     * @param password - the password
     * @return new page object
     */
    public ExplorePage login(String email, String password) {
        executeLogin(email, password);
        return new ExplorePage(driver);
    }

    /**
     * Failed login to cid
     *
     * @param email    - the email
     * @param password - the password
     * @return the current page object
     */
    public LoginPage failedLoginAs(String email, String password) {
        executeLogin(email, password);
        pageUtils.waitForElementToAppear(loginErrorMsg);
        return new LoginPage(driver, false);
    }

    /**
     * Execute actions to login
     *
     * @param email    - the email
     * @param password - the password
     */
    private void executeLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        submitLogin();
    }

    /**
     * Enters the email details
     *
     * @param emailAddress - the email address
     */
    private void enterEmail(String emailAddress) {
        email.click();
        pageUtils.clearInput(email);
        email.sendKeys(emailAddress);
    }

    /**
     * Enters the password
     *
     * @param password - the password
     */
    private void enterPassword(String password) {
        this.password.click();
        pageUtils.clearInput(this.password);
        this.password.sendKeys(password);
    }

    /**
     * Single action that login to cid
     */
    private void submitLogin() {
        submitLogin.click();
    }

    /**
     * Gets the login error message
     *
     * @return login error message
     */
    public String getLoginErrorMessage() {
        return loginErrorMsg.getText();
    }

    /**
     * Gets both username and password
     *
     * @return current page object
     */
    public LoginPage getUsernameAndPassword() {
        getEmail();
        getPassword();
        return this;
    }

    /**
     * Gets the email
     *
     * @return string
     */
    private String getEmail() {
        return email.getText();
    }

    /**
     * Gets the password
     *
     * @return string
     */
    private String getPassword() {
        return this.password.getText();
    }

    /**
     * Checks apriori logo is displayed
     * @return true/false
     */
    public Boolean isLogoDisplayed() {
        return aprioriLogo.isDisplayed();
    }

    /**
     * Checks the environment title
     * @return true/false
     */
    public Boolean isEnvironmentDisplayed() {
        return aprioriTitle.isDisplayed();
    }

    /**
     * Gets marketing text
     * @return string
     */
    public String getMarketingText() {
        return marketingText.getText();
    }

    /**
     * Gets welcome text
     * @return string
     */
    public String getWelcomeText() {
        return welcomeText.getText();
    }

    /**
     * Selects learn more
     * @return new page object
     */
    public LearnMorePage learnMore() {
        pageUtils.waitForElementAndClick(learnMore);
        return new LearnMorePage(driver);
    }

    /**
     * Get learn more link URL
     *
     * @return string
     */
    public String getLearnMoreURL() {
        return getURLs(learnMore);
    }

    /**
     * Selects forgotten password
     * @return new page object
     */
    public ForgottenPasswordPage forgottenPassword() {
        pageUtils.waitForElementAndClick(forgotPassword);
        return new ForgottenPasswordPage(driver);
    }

    /**
     * Selects privacy policy
     * @return new page object
     */
    public PrivacyPolicyPage privacyPolicy() {
        pageUtils.waitForElementAndClick(privacyPolicy);
        return new PrivacyPolicyPage(driver);
    }

    /**
     * Get privacy policy link URL
     *
     * @return String
     */
    public String getPrivacyPolicyURL() {
        return getURLs(privacyPolicy);
    }

    /**
     * Select helps link
     * @return new page object
     */
    public HelpPage help() {
        pageUtils.waitForElementAndClick(helpLink);
        return new HelpPage(driver);
    }

    /**
     * Get help link URL
     *
     * @return String
     */
    public String getHelpURL() {
        return getURLs(helpLink);
    }

    public String getURLs(WebElement url) {
        return pageUtils.waitForElementToAppear(url).getAttribute("href");
    }

    /**
     * Get link response code
     *
     * @param linkURL - URL of link
     * @return String response code
     */
    public int getResponseCode(String linkURL) throws IOException {
        return pageUtils.urlRespCode(linkURL);
    }
}