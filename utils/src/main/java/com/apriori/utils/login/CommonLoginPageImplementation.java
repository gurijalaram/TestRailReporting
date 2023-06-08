package com.apriori.utils.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CommonLoginPageImplementation extends LoadableComponent<CommonLoginPageImplementation> implements LoginPage {

    @FindBy(css = ".auth0-lock-name")
    private WebElement cloudLoginTitle;

    @FindBy(css = "input[name='email']")
    private WebElement emailInputCloud;

    @FindBy(css = "input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "a[class='auth0-lock-alternative-link")
    private WebElement forgotPassword;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButtonCloud;

    /*@FindBy(css = "div.auth0-global-message.auth0-global-message-error span")
    private WebElement loginErrorMsg;*/
    // above element not found for obvious reasons when error not present and when error present, issue?

    @FindBy(css = ".auth0-lock-header-logo")
    private WebElement aprioriLogo;

    @FindBy(xpath = "//a[.='Privacy Policy']")
    private WebElement privacyPolicy;

    @FindBy(css = ".welcome-message")
    private WebElement welcomeText;

    private WebDriver driver;
    private PageUtils pageUtils;
    private String url;
    private String application;

    public CommonLoginPageImplementation(WebDriver driver, String application) {
        init(driver, application);
    }

    public CommonLoginPageImplementation(WebDriver driver) {
        init(driver, "");
    }

    public void init(WebDriver driver, String application) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));

        this.application = application;
        url = application == null || application.isEmpty() ? PropertiesContext.get("cloud.ui_url") : PropertiesContext.get("" + application + ".ui_url");

        driver.get(url);

        log.info("CURRENTLY ON INSTANCE: " + url);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    public <T> T performLogin(final UserCredentials userCredentials, Class<T> klass) {
        executeLogin(userCredentials.getEmail(), userCredentials.getPassword());
        return PageFactory.initElements(driver, klass);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Execute actions to login
     *
     * @param email    - the email
     * @param password - the password
     */
    public void executeLogin(String email, String password) {
        enterEmail(email, emailInputCloud);
        enterPassword(password, passwordInput);
        submitLogin(loginButtonCloud);
    }

    /**
     * Enters the email details
     *
     * @param emailAddress - the email address
     */
    public void enterEmail(String emailAddress, WebElement webElementToUse) {
        pageUtils.waitForElementToBeClickable(webElementToUse);
        webElementToUse.click();
        pageUtils.clearInput(webElementToUse);
        webElementToUse.sendKeys(emailAddress);
    }

    /**
     * Enters the password
     *
     * @param password - the password
     */
    private void enterPassword(String password, WebElement webElementToUse) {
        webElementToUse.click();
        pageUtils.clearInput(webElementToUse);
        webElementToUse.sendKeys(password);
    }

    /**
     * Single action that login to cid
     */
    private void submitLogin(WebElement webElementToUse) {
        pageUtils.waitForElementAndClick(webElementToUse);
    }

    /**
     * Failed login to cid
     *
     * @param email    - the email
     * @param password - the password
     * @return the current page object
     */
    public String failedLoginAs(String email, String password) {
        executeLogin(email, password);
        //return pageUtils.waitForElementToAppear(loginErrorMsg).getText();
        return "pageUtils.waitForElementToAppear(loginErrorMsg).getText()";
    }

    /**
     * Gets the login error message
     *
     * @return login error message
     */
    public String getLoginErrorMessage() {
        //return loginErrorMsg.getText();
        return "";
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
     * Checks apriori logo is displayed
     *
     * @return true/false
     */
    public boolean isLogoDisplayed() {
        return aprioriLogo.isDisplayed();
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
     * Gets title of current login page
     *
     * @return string
     */
    public String getLoginTitle() {
        pageUtils.waitForElementToAppear(cloudLoginTitle);
        pageUtils.waitForElementToBeClickable(cloudLoginTitle);
        return cloudLoginTitle.getText();
    }

    /**
     * Submits email and hits login
     *
     * @param email - email to input
     */
    public void submitEmailForgotPwd(String email) {
        enterEmail(email, emailInputCloud);
        submitLogin(loginButtonCloud);
    }
}
