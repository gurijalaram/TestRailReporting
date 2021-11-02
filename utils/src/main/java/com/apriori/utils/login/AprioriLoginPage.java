package com.apriori.utils.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class AprioriLoginPage extends LoadableComponent<AprioriLoginPage> {

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "a[class='auth0-lock-alternative-link")
    private WebElement forgotPassword;

    @FindBy(css = "button[type='submit']")
    private WebElement submitLogin;

    @FindBy(css = "div.auth0-global-message.auth0-global-message-error span")
    private WebElement loginErrorMsg;

    @FindBy(css = ".auth0-lock-header-logo")
    private WebElement aprioriLogo;

    @FindBy(css = ".auth0-lock-name")
    private WebElement aprioriTitle;

    @FindBy(xpath = "//a[.='Privacy Policy']")
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
    private String url;

    public AprioriLoginPage(WebDriver driver) {
        init(driver, true);
    }

    public AprioriLoginPage(WebDriver driver, boolean loadNewPage) {
        init(driver, loadNewPage);
    }

    public void init(WebDriver driver,  boolean loadNewPage) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));

        String application = System.getProperty("user.dir").split("web\\\\")[1].split("-ui")[0];

        url = application == null || application.isEmpty() ? PropertiesContext.get("${env}.cloud.ui_url") : PropertiesContext.get("${env}." + application + ".ui_url");

        if (loadNewPage) {
            driver.get(url);
        }

        log.info("CURRENTLY ON INSTANCE: " + application);
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
    public <T> T login(String email, String password, Class<T> klass) {
        executeLogin(email, password);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Login to cid
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public <T> T login(final UserCredentials userCredentials, Class<T> klass) {
        executeLogin(userCredentials.getUsername(), userCredentials.getPassword());
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Failed login to cid
     *
     * @param email    - the email
     * @param password - the password
     * @return the current page object
     */
    public AprioriLoginPage failedLoginAs(String email, String password) {
        executeLogin(email, password);
        pageUtils.waitForElementToAppear(loginErrorMsg);
        return new AprioriLoginPage(driver, false);
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
    public AprioriLoginPage getUsernameAndPassword() {
        getEmail();
        getPassword();
        return this;
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
}

