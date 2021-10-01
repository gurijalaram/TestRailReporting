package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.workflows.GenericWorkflow;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @FindBy(css = "input[name='email']")
    private WebElement email;
    @FindBy(css = "input[name='password']")
    private WebElement password;
    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private UserCredentials userCredentials;

    @Getter private static String userName;

    public LoginPage(WebDriver driver) {
        init(driver, true);
        userCredentials = UserUtil.getUser();
        userName = userCredentials.getUsername();
    }

    public void init(WebDriver driver, boolean loadNewPage) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        if (loadNewPage) {
            driver.get(PropertiesContext.get("${env}.ci-connect.ui_url"));
        }
        logger.info("CURRENTLY ON INSTANCE: " + PropertiesContext.get("${env}.ci-connect.ui_url"));
        PageFactory.initElements(driver, this);
    }

    /**
     * Login to CI Connect
     *
     * @param email        - user email address
     * @param userPassword - user password
     * @return new page object
     */
    public GenericWorkflow login(String email, String userPassword) {
        executeLogin(email, userPassword);
        return new GenericWorkflow(driver);
    }

    /**
     * Login to CI Connect with passed in user (from CSV file)
     *
     * @return new page object
     */
    public GenericWorkflow login() {
        executeLogin(this.userName, userCredentials.getPassword());
        return new GenericWorkflow(driver);
    }

    /**
     * Enter email address
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
     * @param userPassword - user password
     */
    private void enterPassword(String userPassword) {
        pageUtils.waitForElementToAppear(password);
        password.click();
        pageUtils.clearInput(password);
        password.sendKeys(userPassword);
    }

    /**
     * Submit login credentials
     */
    private void submitLogin() {
        pageUtils.waitForElementAndClick(submitButton);
    }

    /**
     * Execute login
     *
     * @param email        - user email
     * @param userPassword - user password
     * @return new page object
     */
    private GenericWorkflow executeLogin(String email, String userPassword) {
        enterEmail(email);
        enterPassword(userPassword);
        submitLogin();
        return new GenericWorkflow(driver);
    }
}