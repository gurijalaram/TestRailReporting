package com.apriori.utils.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
        pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
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
    public void enterEmail(String emailAddress) {
        pageUtils.clearValueOfElement(email);
        email.sendKeys(emailAddress);
    }

    /**
     * Clicks the link to send the email
     *
     * @return new page object
     */
    public ForgottenPasswordPage sendEmail() {
        pageUtils.waitForElementAndClick(sendEmail);
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
