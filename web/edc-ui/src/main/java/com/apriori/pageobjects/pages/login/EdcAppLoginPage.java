package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.users.UserCredentials;

import com.utils.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdcAppLoginPage extends LoadableComponent<EdcAppLoginPage> {

    private static final Logger logger = LoggerFactory.getLogger(EdcAppLoginPage.class);
    private static String loginPageUrl = Constants.getDefaultUrl();

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "button[type='submit']")
    private WebElement submitLogin;



    private WebDriver driver;
    private PageUtils pageUtils;

    public EdcAppLoginPage(WebDriver driver) {
        init(driver, "", true);
    }

    public EdcAppLoginPage(WebDriver driver, String url) {
        init(driver, url, true);
    }

    public EdcAppLoginPage(WebDriver driver, boolean loadNewPage) {
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
        pageUtils.waitForElementAppear(email);
        pageUtils.waitForElementAppear(password);
        pageUtils.waitForElementAppear(submitLogin);
    }

    public ElectronicsDataCollectionPage login(final UserCredentials userCredentials) {
        executeLogin(userCredentials.getUsername(), userCredentials.getPassword());
        return new ElectronicsDataCollectionPage(driver);
    }

    private void executeLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        submitLogin();
    }

    private void enterEmail(String emailAddress) {
        email.click();
        pageUtils.clearInput(email);
        email.sendKeys(emailAddress);
    }

    private void enterPassword(String password) {
        this.password.click();
        pageUtils.clearInput(this.password);
        this.password.sendKeys(password);
    }

    private void submitLogin() {
        submitLogin.click();
    }
}
