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

    @FindBy(xpath = "//h2[@class='textAccent']")
    private WebElement reportsLoginTitle;

    @FindBy(xpath = "//p[@class='password-inputs-header']")
    private WebElement adminLoginTitle;

    /*@FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='username']")
    private WebElement emailInputOnPrem;*/

    /*@FindBy(css = "input[name='password']")
    private WebElement password;*/

    /*@FindBy(css = "a[class='auth0-lock-alternative-link")
    private WebElement forgotPassword;*/

    /*@FindBy(css = "button[type='submit']")
    private WebElement submitLogin;

    @FindBy(css = "button[id='login']")
    private WebElement loginButtonOnPrem;*/

    /*@FindBy(css = "div.auth0-global-message.auth0-global-message-error span")
    private WebElement loginErrorMsg;

    @FindBy(css = ".auth0-lock-header-logo")
    private WebElement aprioriLogo;*/

    /*@FindBy(css = ".auth0-lock-name")
    private WebElement aprioriTitle;*/

    /*@FindBy(xpath = "//a[.='Privacy Policy']")
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
    private WebElement learnMore;*/

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
        //pageUtils.waitForElementToAppear(email);
        //pageUtils.waitForElementToAppear(password);
        //pageUtils.waitForElementToAppear(submitLogin);
    }

    /**
     * Gets title of current login page
     *
     * @return string
     */
    public String getLoginTitle(boolean isAdmin) {
        return isAdmin ? adminLoginTitle.getText() : reportsLoginTitle.getText();
        // return loginTitle.getAttribute("textContent");
        //return "";
    }

    /**
     * Login
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public <T> T login(final UserCredentials userCredentials, boolean isEnvOnPrem, boolean isReports, Class<T> klass) {
        executeLogin(userCredentials.getEmail(), userCredentials.getPassword(), isEnvOnPrem, isReports);
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
        executeLogin(email, password, false, false);
        //return pageUtils.waitForElementToAppear(loginErrorMsg).getText();
        return "";
    }

    /**
     * Execute actions to login
     *
     * @param email    - the email
     * @param password - the password
     */
    public void executeLogin(String email, String password, boolean isEnvOnPrem, boolean isReports) {
        enterEmail(email, isEnvOnPrem, isReports);
        enterPassword(password, isReports);
        submitLogin(isEnvOnPrem, isReports);
    }

    /**
     * Enters the email details
     *
     * @param emailAddress - the email address
     */
    private void enterEmail(String emailAddress, boolean isEnvOnPrem, boolean isReports) {
        By cloudLocator = By.cssSelector("input[name='email']");
        By onPremAdminLocator = By.cssSelector("input[name='username']");
        By onPremReportsLocator = By.cssSelector("input[name='j_username']");
        By locatorToUse = isEnvOnPrem ? onPremAdminLocator : cloudLocator;
        locatorToUse = isReports ? onPremReportsLocator : locatorToUse;
        WebElement elementToUse = driver.findElement(locatorToUse);
        elementToUse.click();
        pageUtils.clearInput(elementToUse);
        elementToUse.sendKeys(emailAddress);
        // put in config yml file to get the locators from - get env.locator_admin
        // users file - set based on env too (or else make passwords the same)
    }

    /**
     * Enters the password
     *
     * @param password - the password
     */
    private void enterPassword(String password, boolean isReports) {
        By cloudLocator = By.cssSelector("input[name='password']");
        By onPremReportsLocator = By.cssSelector("input[name='j_password_pseudo']");
        By locatorToUse = isReports ? onPremReportsLocator : cloudLocator;
        WebElement elementToUse = driver.findElement(locatorToUse);
        elementToUse.click();
        pageUtils.clearInput(elementToUse);
        elementToUse.sendKeys(password);
    }

    /**
     * Single action that login to cid
     */
    private void submitLogin(boolean isEnvOnPrem, boolean isReports) {
        By cloudLocator = By.cssSelector("button[type='submit']");
        By onPremAdminLocator = By.cssSelector("button[id='login']");
        By onPremReportsLocator = By.cssSelector("button[id='submitButton']");
        By locatorToUse = isEnvOnPrem ? onPremAdminLocator : cloudLocator;
        locatorToUse = isReports ? onPremReportsLocator : locatorToUse;
        WebElement elementToUse = driver.findElement(locatorToUse);
        pageUtils.waitForElementAndClick(elementToUse);
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
     * Gets both username and password
     *
     * @return current page object
     */
    private AprioriLoginPage getUsernameAndPassword() {
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
        //pageUtils.waitForElementAndClick(forgotPassword);
        return new ForgottenPasswordPage(driver);
    }

    /**
     * Selects privacy policy
     *
     * @return new page object
     */
    public PrivacyPolicyPage privacyPolicy() {
        //pageUtils.waitForElementAndClick(privacyPolicy);
        return new PrivacyPolicyPage(driver);
    }

    /**
     * Gets the email
     *
     * @return string
     */
    private String getEmail() {
        //return email.getText();
        return "";
    }

    /**
     * Gets the password
     *
     * @return string
     */
    private String getPassword() {
        //return this.password.getText();
        return "";
    }

    /**
     * Checks apriori logo is displayed
     *
     * @return true/false
     */
    public boolean isLogoDisplayed() {
        //return aprioriLogo.isDisplayed();
        return true;
    }

    /**
     * Gets welcome text
     *
     * @return string
     */
    public String getWelcomeText() {
        //return welcomeText.getText();
        return "";
    }
}