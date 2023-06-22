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
public class OnPremLoginPageImplementation extends LoadableComponent<OnPremLoginPageImplementation> implements LoginPage {

    @FindBy(css = "input[name='username']")
    private WebElement emailInputOnPremAdmin;

    @FindBy(css = "input[name='j_username']")
    private WebElement emailInputOnPremReports;

    @FindBy(css = "input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "button[id='login']")
    private WebElement loginButtonOnPremAdmin;

    @FindBy(css = "button[id='submitButton']")
    private WebElement loginButtonOnPremReports;

    @FindBy(css = "div.auth0-global-message.auth0-global-message-error span")
    private WebElement loginErrorMsg;

    @FindBy(css = ".auth0-lock-header-logo")
    private WebElement aprioriLogo;

    @FindBy(css = "a[class='auth0-lock-alternative-link")
    private WebElement forgotPassword;

    @FindBy(css = ".welcome-message")
    private WebElement welcomeText;

    @FindBy(xpath = "//a[.='Privacy Policy']")
    private WebElement privacyPolicy;

    @FindBy(css = ".auth0-lock-name")
    private WebElement cloudLoginTitle;

    private WebDriver driver;
    private PageUtils pageUtils;
    private String url;
    private String application;

    public OnPremLoginPageImplementation(WebDriver driver, String application) {
        init(driver, application);
    }

    public OnPremLoginPageImplementation(WebDriver driver) {
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
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Generic login method
     *
     * @param userCredentials - users
     * @param klass - class to return an instance of
     * @param <T> - generic class to return an instance of
     * @return instance of class
     */
    @Override
    public <T> T performLogin(final UserCredentials userCredentials, Class<T> klass) {
        executeLogin(userCredentials.getEmail(), userCredentials.getPassword());
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Generic login method
     *
     * @param username - username to login with
     * @param password - password to login with
     * @param klass - class to return an instance of
     * @param <T> - generic class to return an instance of
     * @return instance of class
     */
    @Override
    public <T> T performLogin(String username, String password, Class<T> klass) {
        executeLogin(username, password);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Generic login method
     *
     * @param username - username to login with
     * @param password - password to login with
     */
    @Override
    public void performLoginVoid(String username, String password) {
        executeLogin(username, password);
    }

    /**
     * Failed login with empty fields and no return
     */
    @Override
    public void failedLoginEmptyFieldsNoReturn() {
        executeLogin("", "");
    }

    /**
     * Submits email but no password
     *
     * @param username - username to login with
     * @param klass - class to return an instance of
     * @param <T> - generic of class to return an instance of
     * @return - instance of class
     */
    @Override
    public <T> T submitEmailForgotPwd(String username, Class<T> klass) {
        executeLogin(username, "");
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Gets login title
     *
     * @return String
     */
    @Override
    public String getLoginTitle() {
        pageUtils.waitForElementToAppear(cloudLoginTitle);
        pageUtils.waitForElementToBeClickable(cloudLoginTitle);
        return cloudLoginTitle.getText();
    }

    /**
     * Clicks forgot password
     *
     * @param klass - class to return an instance of
     * @param <T> - generic of class to return an instance of
     * @return instance of class specified
     */
    @Override
    public <T> T forgottenPassword(Class<T> klass) {
        pageUtils.waitForElementAndClick(forgotPassword);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Gets welcome text
     *
     * @return string
     */
    @Override
    public String getWelcomeText() {
        return welcomeText.getText();
    }

    /**
     * Selects privacy policy
     *
     * @return new page object
     */
    @Override
    public <T> T privacyPolicy(Class<T> klass) {
        pageUtils.waitForElementAndClick(privacyPolicy);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Execute actions to login
     *
     * @param email    - the email
     * @param password - the password
     */
    public void executeLogin(String email, String password) {
        boolean isAdmin = application.equals("admin");
        WebElement emailInputToUse = isAdmin ? emailInputOnPremAdmin : emailInputOnPremReports;
        WebElement loginButtonToUse = isAdmin ? loginButtonOnPremAdmin : loginButtonOnPremReports;
        enterEmail(email, emailInputToUse);
        enterPassword(password, passwordInput);
        submitLogin(loginButtonToUse);
    }

    /**
     * Enters the email details
     *
     * @param emailAddress - the email address
     */
    public void enterEmail(String emailAddress, WebElement webElementToUse) {
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
     * Single action that clicks login button that is specified
     */
    private void submitLogin(WebElement webElementToUse) {
        pageUtils.waitForElementAndClick(webElementToUse);
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
     * Checks apriori logo is displayed
     *
     * @return true/false
     */
    public boolean isLogoDisplayed() {
        return aprioriLogo.isDisplayed();
    }
}
