package com.apriori.pageobjects.pages.login;

import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.PageUtils;
import com.apriori.login.LoginService;
import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReportsLoginPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(ReportsLoginPage.class);
    private static final String loginPageURL = PropertiesContext.get("${env}.reports.ui_url");

    @FindBy(css = "input[name='j_username']")
    private WebElement email;

    @FindBy(css = "input[name='j_password_pseudo']")
    private WebElement password;

    @FindBy(css = "a.auth0-lock-alternative-link")
    private WebElement forgotPassword;

    @FindBy(css = "button[id='submitButton']")
    private WebElement loginButton;

    @FindBy(css = "span[class='animated fadeInUp']")
    private WebElement loginMsg;

    @FindBy(xpath = "//div[contains(@class, 'email')]/div[@class='auth0-lock-error-msg']")
    private WebElement emailInputErrorMsg;

    @FindBy(xpath = "//div[contains(@class, 'password')]/div[@class='auth0-lock-error-msg']")
    private WebElement passwordInputErrorMsg;

    @FindBy(css = "a[href='https://www.apriori.com/sso-instructions-page']")
    private WebElement helpButton;

    @FindBy(css = "a[href='https://www.apriori.com/privacy-policy']")
    private WebElement privacyPolicyButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private LoginService loginService;
    private UserCredentials userCredentials = UserUtil.getUserOnPrem();

    @FindBy(xpath = "//input[@type='email']")
    private WebElement emailInput;

    public ReportsLoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.loginService = new LoginService(this.driver, "reports");
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertThat("CIR login page was not displayed", loginService.getLoginTitle().contains(PropertiesContext.get("${env}.reports.welcome_page_text")));
    }

    /**
     * Login to CIR
     *
     * @return new page object
     */
    public ReportsPageHeader login() {
        return loginService.login(userCredentials, ReportsPageHeader.class);
    }

    /**
     * Failed login to CI Report
     *
     * @param user - user to use for login
     * @param password - password to input
     * @return instance of ReportsLoginPage
     */
    public ReportsLoginPage failedLogin(UserCredentials user, String password) {
        String username = PropertiesContext.get("${env}").equals("onprem") ? user.getUsername() : user.getEmail();
        return loginService.loginUsernamePassword(username, password, ReportsLoginPage.class);
    }

    public ReportsLoginPage invalidEmailFailedLogin(String email, String password) {
        loginService.loginNoReturn(email, password);
        pageUtils.waitForElementToAppear(By.xpath("//div[@class='auth0-lock-error-invalid-hint']"));
        pageUtils.waitForElementToBeClickable(By.xpath("//div[@class='auth0-lock-error-invalid-hint']"));
        return new ReportsLoginPage(driver);
    }

    /**
     * Failed login with empty fields
     *
     * @return instance of ReportsLoginPage
     */
    public ReportsLoginPage failedLoginEmptyFields() {
        loginService.failedLoginEmptyFields();
        return this;
    }

    /**
     * Get login message text
     *
     * @return String
     */
    public String getLoginMessage() {
        By loginMessageLocator = By.xpath("//*[contains(@class, 'error')]");
        pageUtils.waitForElementToAppear(loginMessageLocator);
        pageUtils.waitForElementToBeClickable(loginMessageLocator);
        List<WebElement> errorElements = driver.findElements(loginMessageLocator);
        String errorText = "";
        if (PropertiesContext.get("${env}").equals("onprem") && errorElements.size() > 1) {
            for (WebElement element : errorElements) {
                errorText = errorText.concat(element.getText());
            }
        } else {
            errorText = errorElements.get(0).getText();
        }
        return errorText;
    }

    public String getInvalidEmailMessage() {
        By invalidEmailLocator = By.xpath("//div[@class='auth0-lock-error-invalid-hint']");
        pageUtils.waitForElementToAppear(invalidEmailLocator);
        pageUtils.waitForElementToBeClickable(invalidEmailLocator);
        return driver.findElement(invalidEmailLocator).getText();
    }

    /**
     * Click forgot password link
     */
    public ReportsLoginPage clickForgotPassword() {
        pageUtils.waitForElementAndClick(forgotPassword);
        return this;
    }

    /**
     * Submit email address alone for password recovery
     *
     * @param email - user email
     */
    public ReportsLoginPage submitEmail(String email) {
        return loginService.submitEmailForgotPwd(email, ReportsLoginPage.class);
    }

    /**
     * Get input error message
     *
     * @return String
     */
    public String getEmailOrPwdInputErrorMsg(boolean isEmail) {
        WebElement elementToUse = isEmail ? emailInputErrorMsg : passwordInputErrorMsg;
        pageUtils.waitForElementToAppear(elementToUse);
        return elementToUse.getText();
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