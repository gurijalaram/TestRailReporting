package main.java.pages;

import main.java.constants.Constants;
import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public PrivateWorkspacePage login(String email, String password) {
        executeLogin(email, password);
        return new PrivateWorkspacePage(driver);
    }

    public LoginPage failedLoginAs(String email, String password) {
        executeLogin(email, password);
        pageUtils.waitForElementToAppear(loginErrorMsg);
        return new LoginPage(driver, false);
    }

    private void executeLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        submitLogin();
    }

    private void enterEmail(String emailaddress) {
        email.click();
        pageUtils.clearInput(email);
        email.sendKeys(emailaddress);
    }

    private void enterPassword(String passsword) {
        password.click();
        pageUtils.clearInput(password);
        password.sendKeys(passsword);
    }

    private void submitLogin() {
        submitLogin.click();
    }

    public String getLoginErrorMessage() {
        return loginErrorMsg.getText();
    }
}
