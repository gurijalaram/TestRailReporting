package com.pageobjects.pages.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;

import com.pageobjects.pages.explore.ExplorePage;
import com.utils.Constants;
import io.qameta.allure.Step;
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

public class CidLoginPage extends LoadableComponent<CidLoginPage> {

    private static final Logger logger = LoggerFactory.getLogger(CidLoginPage.class);
    private static String loginPageUrl = PropertiesContext.getStr("{env}.base_url");

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

    public CidLoginPage(WebDriver driver) {
        init(driver, "", true);
    }

    public CidLoginPage(WebDriver driver, String url) {
        init(driver, url, true);
    }

    public CidLoginPage(WebDriver driver, boolean loadNewPage) {
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
    @Deprecated
    public ExplorePage login(String email, String password) {
        executeLogin(email, password);
        return new ExplorePage(driver);
    }

    /**
     * Login to cid
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public ExplorePage login(final UserCredentials userCredentials) {
        executeLogin(userCredentials.getUsername(), userCredentials.getPassword());
        return new ExplorePage(driver);
    }

    /**
     * Failed login to cid
     *
     * @param email    - the email
     * @param password - the password
     * @return the current page object
     */
    public CidLoginPage failedLoginAs(String email, String password) {
        executeLogin(email, password);
        pageUtils.waitForElementToAppear(loginErrorMsg);
        return new CidLoginPage(driver, false);
    }

    /**
     * Execute actions to login
     *
     * @param email    - the email
     * @param password - the password
     */
    @Step("Verify sign with uname {0} and password {1}")
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
    public CidLoginPage getUsernameAndPassword() {
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
     *
     * @return true/false
     */
    public boolean isLogoDisplayed() {
        return aprioriLogo.isDisplayed();
    }

    /**
     * Checks the environment title
     *
     * @return true/false
     */
    public boolean isEnvironmentDisplayed() {
        return aprioriTitle.isDisplayed();
    }

    /**
     * Gets marketing text
     *
     * @return string
     */
    public String getMarketingText() {
        return marketingText.getText();
    }

    /**
     * Gets welcome text
     *
     * @return string
     */
    public String getWelcomeText() {
        return welcomeText.getText();
    }

    /**
     * Selects learn more
     *
     * @return new page object
     */
    public LearnMorePage learnMore() {
        pageUtils.waitForElementAndClick(learnMore);
        return new LearnMorePage(driver);
    }

    /**
     * Selects forgotten password
     *
     * @return new page object
     */
    public ForgottenPasswordPage forgottenPassword() {
        pageUtils.waitForElementAndClick(forgotPassword);
        return new ForgottenPasswordPage(driver);
    }

    /**
     * Selects privacy policy
     *
     * @return new page object
     */
    public PrivacyPolicyPage privacyPolicy() {
        pageUtils.waitForElementAndClick(privacyPolicy);
        return new PrivacyPolicyPage(driver);
    }

    /**
     * Select helps link
     *
     * @return new page object
     */
    public HelpPage help() {
        pageUtils.waitForElementAndClick(helpLink);
        return new HelpPage(driver);
    }
}