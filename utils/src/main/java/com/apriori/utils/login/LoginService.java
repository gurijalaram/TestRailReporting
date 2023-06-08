package com.apriori.utils.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class LoginService extends LoadableComponent<LoginService> {
    private WebDriver driver;
    private String application;
    private LoginPage loginPage;
    private String url;
    private PageUtils pageUtils;

    /**
     * LoginService generic constructor
     *
     * @param driver - driver instance to use
     * @param application - application to log in to
     */
    public LoginService(WebDriver driver, String application) {
        this.driver = driver;
        this.application = application;
        this.loginPage = PropertiesContext.get("${env}").equals("onprem") ? new OnPremLoginPageImplementation(driver, application) : new CommonLoginPageImplementation(driver, application);

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

    }

    /**
     * Generic login method
     *
     * @param userCredentials - user to log in with
     * @param klass - class to return
     * @param <T> - specified class to return
     * @return - return instance of class specified
     */
    public <T> T login(final UserCredentials userCredentials, Class<T> klass) {
        return loginPage.performLogin(userCredentials, klass);
    }

    /**
     * Generic login method with username and password
     *
     * @param username - user to log in with
     * @param password - password to log in with
     * @param klass - class to return
     * @return - return instance of class specified
     * @param <T> - specified class to return
     */
    public <T> T loginUsernamePassword(String username, String password, Class<T> klass) {
        return loginPage.performLogin(username, password, klass);
    }

    /**
     * Generic login method that doesn't return anything
     *
     * @param username - user to log in with
     * @param password - password to log in with
     */
    public void loginNoReturn(String username, String password) {
        loginPage.performLoginVoid(username, password);
    }

    public <T> T forgottenPassword(Class<T> klass) {
        return loginPage.forgottenPassword(klass);
    }

    public void failedLoginEmptyFields() {
        loginPage.failedLoginEmptyFieldsNoReturn();
    }

    public <T> T submitEmailForgotPwd(String username, Class<T> klass) {
        return loginPage.submitEmailForgotPwd(username, klass);
    }

    /**
     * Gets title of current login page
     *
     * @return string
     */
    public String getLoginTitle() {
        return loginPage.getLoginTitle();
    }

    public String getLoginErrorMessage() {
        return loginPage.getLoginErrorMessage();
    }

    public boolean isLogoDisplayed() {
        return loginPage.isLogoDisplayed();
    }

    public String getWelcomeText() {
        return loginPage.getWelcomeText();
    }

    public <T> T privacyPolicy(Class<T> klass) {
        return loginPage.privacyPolicy(klass);
    }
}
