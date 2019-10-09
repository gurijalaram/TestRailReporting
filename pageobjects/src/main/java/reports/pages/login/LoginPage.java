package reports.pages.login;

import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.constants.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.pages.homepage.HomePage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginPage extends LoadableComponent<LoginPage> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static String loginPageURL = Constants.cirURL;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "a[href='javascript:void(0)']")
    private WebElement forgotPassword;

    @FindBy(css = "button[type='submit'")
    private WebElement submitButton;

    @FindBy(css = "span[class='animated fadeInUp']")
    private WebElement loginMsg;

    @FindBy(css = "div.auth0-lock-error-msg")
    private WebElement inputErrorMsg;

    @FindBy(css = "a[href='https://www.apriori.com/sso-instructions-page']")
    private WebElement helpLink;

    @FindBy(css = "a[href='https://www.apriori.com/privacy-policy']")
    private WebElement privacyPolicyLink;

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
        pageUtils.waitForElementToAppear(email);
        email.click();
        pageUtils.clearInput(email);
        email.sendKeys(emailAddress);
    }

    /**
     * Enter password
     *
     * @param password - user password
     */
    private void enterPassword(String password) {
        pageUtils.waitForElementToAppear(this.password);
        this.password.click();
        pageUtils.clearInput(this.password);
        this.password.sendKeys(password);
    }

    /**
     * Single action to submit login credentials
     */
    private void submitLogin() {
        submitButton.click();
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
    public HomePage login(String email, String password) {
        executeLogin(email, password);
        return new HomePage(driver);
    }

    /**
     * Failed login to CI Report
     *
     * @param email    - user email
     * @param password - user password
     * @return current page object
     */
    public LoginPage failedLogin(String email, String password) {
        executeLogin(email, password);
        return new LoginPage(driver, false);
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
    public LoginPage clickForgotPassword() {
        pageUtils.waitForElementToAppear(forgotPassword);
        forgotPassword.click();
        return new LoginPage(driver, false);
    }

    /**
     * Submit email address alone for password recovery
     *
     * @param email - user email
     */
    public LoginPage submitEmail(String email) {
        enterEmail(email);
        submitLogin();
        return new LoginPage(driver, false);
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

    /**
     * Get help link URL
     *
     * @return String help link URL
     */
    public String getHelpURL() {
        pageUtils.waitForElementToAppear(helpLink);
        return helpLink.getAttribute("href");
    }

    /**
     * Get privacy policy URL
     *
     * @return String privacy policy URL
     */
    public String getPrivacyPolicyURL() {
        pageUtils.waitForElementToAppear(privacyPolicyLink);
        return privacyPolicyLink.getAttribute("href");
    }

    /**
     * Get link response code
     *
     * @param linkURL - URL of link
     * @return String response code
     */
    public int getLinkRespCode(String linkURL) throws IOException {
        HttpURLConnection huc = (HttpURLConnection)(new URL(linkURL).openConnection());
        huc.setRequestMethod("HEAD");
        huc.connect();
        return huc.getResponseCode();
    }
}
