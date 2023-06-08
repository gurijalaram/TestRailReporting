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
        boolean isReports = application.equals("admin");
        WebElement emailInputToUse = isReports ? emailInputOnPremReports : emailInputOnPremAdmin;
        WebElement loginButtonToUse = isReports ? loginButtonOnPremReports : loginButtonOnPremAdmin;
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
}
