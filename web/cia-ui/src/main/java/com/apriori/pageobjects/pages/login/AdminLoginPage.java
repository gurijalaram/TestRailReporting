package com.apriori.pageobjects.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.pageobjects.pages.homepage.AdminHomePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.users.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class AdminLoginPage extends AdminHeader {

    private static final Logger logger = LoggerFactory.getLogger(AdminLoginPage.class);
    private static String loginPageURL = PropertiesContext.get("${env}.admins.ui_url");

    @FindBy(css = "input[name='username']")
    private WebElement emailInput;

    @FindBy(css = "input[name='password']")
    private WebElement passwordInput;

    @FindBy(css = "a[href='javascript:void(0)']")
    private WebElement forgotPassword;

    @FindBy(css = "button[id='login']")
    private WebElement loginButton;

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
    private AprioriLoginPage aprioriLoginPage;

    public AdminLoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "admin");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CIA login page was not displayed", aprioriLoginPage.getPageTitle().contains("CI Design APRIORI-INTERNAL"));
    }

    /**
     * Login to CIA
     *
     * @param emailAddress - user email address
     */
    private void enterEmail(String emailAddress) {
        pageUtils.waitForElementAndClick(emailInput);
        pageUtils.clearInput(emailInput);
        emailInput.sendKeys(emailAddress);
    }

    /**
     * Enter password
     *
     * @param password - user password
     */
    private void enterPassword(String password) {
        pageUtils.waitForElementAndClick(passwordInput);
        pageUtils.clearInput(this.passwordInput);
        this.passwordInput.sendKeys(password);
    }

    /**
     * Single action to submit login credentials
     */
    private void submitLogin() {
        loginButton.click();
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
    public AdminHomePage login(final UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, AdminHomePage.class);
    }
}
