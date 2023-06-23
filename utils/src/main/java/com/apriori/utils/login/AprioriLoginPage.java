package com.apriori.utils.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
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

    @FindBy(css = ".auth0-lock-name")
    private WebElement cloudLoginTitle;

    @FindBy(xpath = "//h2[@class='textAccent']")
    private WebElement reportsLoginTitle;

    @FindBy(xpath = "//p[@class='password-inputs-header']")
    private WebElement adminLoginTitle;

    @FindBy(css = "input[name='email']")
    private WebElement emailInputCloud;

    @FindBy(css = "input[name='username']")
    private WebElement emailInputOnPremAdmin;

    @FindBy(css = "input[name='j_username']")
    private WebElement emailInputOnPremReports;

    @FindBy(css = "input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "input[name='j_password_pseudo']")
    private WebElement passwordInputReportsOnPrem;

    @FindBy(css = "a[class='auth0-lock-alternative-link")
    private WebElement forgotPassword;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButtonCloud;

    @FindBy(css = "button[id='login']")
    private WebElement loginButtonOnPremAdmin;

    @FindBy(css = "button[id='submitButton']")
    private WebElement loginButtonOnPremReports;

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

    public AprioriLoginPage(WebDriver driver, String application) {
        init(driver, application);
    }

    public AprioriLoginPage(WebDriver driver) {
        init(driver, "");
    }

    public void init(WebDriver driver, String application) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));

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
        pageUtils.waitForElementToAppear(passwordInput);
    }

    /**
     * Gets title of current login page
     *
     * @return string
     */
    public String getLoginTitle() {
        boolean isAdmin = Thread.currentThread().getStackTrace()[5].getFileName().contains("Admin");
        WebElement elementToUse = isAdmin
            ? driver.findElement(By.xpath(PropertiesContext.get("${env}.admin.login_title_locator")))
            : driver.findElement(By.xpath(PropertiesContext.get("${env}.reports.login_title_locator")));
        return elementToUse.getText();
    }

    /**
     * Login
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public <T> T login(final UserCredentials userCredentials, Class<T> klass) {
        executeLogin(userCredentials.getEmail(), userCredentials.getPassword());
        return PageFactory.initElements(driver, klass);
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
        return pageUtils.waitForElementToAppear(loginErrorMsg).getText();
    }

    public void submitEmailForgotPwd(String email) {
        enterEmail(email, emailInputCloud);
        submitLogin(loginButtonCloud);
    }

    /**
     * Execute actions to login
     *
     * @param email    - the email
     * @param password - the password
     */
    public void executeLogin(String email, String password) {
        boolean isReports = driver.getCurrentUrl().contains("jasperserver");
        WebElement emailInputToUse = driver.findElement(By.xpath(PropertiesContext.get("${env}.reports.username_locator")));
        WebElement loginButtonToUse = isReports ? driver.findElement(By.xpath(PropertiesContext.get("${env}.reports.login_locator")))
            : driver.findElement(By.xpath(PropertiesContext.get("${env}.admin.login_locator")));
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
     * Single action that login to cid
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
     * Gets both username and password
     *
     * @return current page object
     */
    private AprioriLoginPage getUsernameAndPassword() {
        getEmailInputCloud();
        getPasswordCloud();
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
    private String getEmailInputCloud() {
        return emailInputCloud.getText();
    }

    /**
     * Gets the password
     *
     * @return string
     */
    private String getPasswordCloud() {
        return this.passwordInput.getText();
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
}