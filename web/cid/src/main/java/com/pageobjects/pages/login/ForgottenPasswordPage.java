package com.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ForgottenPasswordPage extends LoadableComponent<ForgottenPasswordPage> {

    private final Logger logger = LoggerFactory.getLogger(ForgottenPasswordPage.class);

    @FindBy(css = ".auth0-lock-name")
    private WebElement resetPasswordText;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = ".auth0-label-submit")
    private WebElement sendEmail;

    @FindBy(css = ".auth0-lock-back-button")
    private WebElement backButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ForgottenPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(resetPasswordText);
    }

    /**
     * Enters the email details
     *
     * @param emailAddress - the email address
     */
    public void enterEmail(String emailAddress) {
        pageUtils.clearInput(email);
        email.sendKeys(emailAddress);
    }

    /**
     * Clicks the link to send the email
     *
     * @return new page object
     */
    public CidLoginPage sendEmail() {
        sendEmail.click();
        return new CidLoginPage(driver);
    }

    /**
     * Get reset password text
     *
     * @return - string
     */
    public String getResetPassword() {
        return resetPasswordText.getText();
    }
}