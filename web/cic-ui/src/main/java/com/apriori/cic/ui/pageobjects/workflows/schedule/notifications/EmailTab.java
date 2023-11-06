package com.apriori.cic.ui.pageobjects.workflows.schedule.notifications;

import com.apriori.cic.ui.utils.Constants;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Email tab in Notification part during workflow creation or modification
 */
public class EmailTab extends NotificationsPart {

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-171'] > div > div > span.placeholder")
    private WebElement emailTemplate;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-173'] > div > div > span.placeholder")
    private WebElement recipientDropdownElement;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_textbox-175'] > table > tbody > tr > td > input")
    private WebElement recipientEmailAddressTxtElement;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-173'] > div > div > span.ss-arrow > span.arrow-down")
    private WebElement emailDropdownArrowDownImg;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-173'] > div > div > span.ss-arrow > span.arrow-up")
    private WebElement emailDropdownArrowUpImg;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-180']")
    private WebElement emailConfigCostRoundingElement;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_DrowpdownWidget-181'] > div > div > span.placeholder")
    private WebElement emailConfigAprioriCostElement;


    public EmailTab(WebDriver driver) {
        super(driver);
    }

    public NotificationsPart setUpEmailConfiguration() {
        this.selectEmailTemplate();
        this.selectRecipient();
        return new NotificationsPart(this.driver);
    }

    public EmailTab selectEmailTemplate() {
        pageUtils.waitForElementAndClick(emailTemplate);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        this.waitUntilDropDownValuesAreLoaded(workFlowData.getNotificationsData().getEmailTemplate());
        this.selectValueFromDDL(workFlowData.getNotificationsData().getEmailTemplate());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return this;
    }

    public EmailTab selectRecipient() {
        pageUtils.waitForElementAndClick(recipientDropdownElement);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        this.selectValueFromDDL(workFlowData.getNotificationsData().getRecipientEmailType());
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        recipientEmailAddressTxtElement.sendKeys(workFlowData.getNotificationsData().getRecipientEmailAddress() + Keys.TAB);
        pageUtils.waitForJavascriptLoadComplete();
        return this;
    }

    public WebElement getEmailConfigCostRoundingElement() {
        return emailConfigCostRoundingElement;
    }

    public WebElement getEmailConfigAprioriCostElement() {
        return emailConfigAprioriCostElement;
    }
}