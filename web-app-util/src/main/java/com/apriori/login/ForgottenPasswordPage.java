package com.apriori.login;

import com.apriori.EagerPageComponent;
import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author cfrith
 */

@Slf4j
public class ForgottenPasswordPage extends EagerPageComponent<ForgottenPasswordPage> {

    @FindBy(css = ".auth0-lock-name")
    private WebElement resetPasswordText;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = ".auth0-label-submit")
    private WebElement sendEmail;

    @FindBy(css = ".auth0-lock-back-button")
    private WebElement backButton;

    private PageUtils pageUtils;

    public ForgottenPasswordPage(WebDriver driver) {
        super(driver, log);
        log.debug(getPageUtils().currentlyOnPage(this.getClass().getSimpleName()));
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(backButton);
    }

    /**
     * Enters the email details
     *
     * @param emailAddress - the email address
     */
    public ForgottenPasswordPage enterEmail(String emailAddress) {
        getPageUtils().clearValueOfElement(email);
        email.sendKeys(emailAddress);
        return this;
    }

    /**
     * Clicks the link to send the email
     *
     * @return new page object
     */
    public ForgottenPasswordPage sendEmail() {
        getPageUtils().waitForElementAndClick(sendEmail);
        return this;
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
