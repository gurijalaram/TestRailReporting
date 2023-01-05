package com.apriori.security;

import com.apriori.customer.CustomerWorkspacePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public final class SecurityPage extends CustomerWorkspacePage {
    @FindBy(css = ".apriori-card.card .card-header")
    private WebElement cardHeader;

    @FindBy(css = ".btn.btn-light.customer-security-reset-mfa")
    private WebElement resetMfaAllUsersButton;

    @FindBy(xpath = "//button[@class='mr-2 btn btn-secondary'][.='Cancel']")
    private WebElement confirmResetCancelButton;

    @FindBy(xpath = "//button[@class='btn btn-primary'][.='OK']")
    private WebElement confirmResetOkButton;

    @FindBy(xpath = "//div[@class='Toastify__toast Toastify__toast--success']/div[@role='alert']")
    private WebElement modalSuccessMessage;

    public SecurityPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementAppear(cardHeader);
    }

    /**
     * Gets the list of fields names
     *
     * @return list of fields names
     */
    public List<String> getFieldName() {
        List<WebElement> fieldName = getDriver().findElements(By.xpath("//label[@class='display-property-item-name form-label m-0']"));
        return fieldName.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Clicks on Reset MFA for All Users button
     *
     * @return this object
     */
    public SecurityPage clickResetMfaButton() {
        getPageUtils().waitForElementAndClick(resetMfaAllUsersButton);
        return this;
    }

    /**
     * Clicks on Cancel button of confirmation reset action
     *
     * @return this object
     */
    public SecurityPage clickCancelConfirmReset() {
        getPageUtils().waitForElementAndClick(confirmResetCancelButton);
        return this;
    }

    /**
     * Clicks on Ok button of confirmation reset action
     *
     * @return this object
     */
    public SecurityPage clickOkConfirmReset() {
        getPageUtils().waitForElementAndClick(confirmResetOkButton);
        return this;
    }

    /**
     * Gets the text of notification message
     *
     * @return string
     */
    public String getSuccessTextMessage() {
        return getPageUtils().waitForElementToAppear(modalSuccessMessage).getAttribute("textContent");
    }
}