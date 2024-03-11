package com.apriori.cic.ui.pageobjects.workflows.schedule.notifications;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Email tab in Notification part during workflow creation or modification
 */
public class EmailTab extends NotificationsPart {

    private static final String EMAIL_TAB_ELEMENT = NOTIFICATIONS_TAB_ELEMENT + "//div[@tab-number='1']";

    public EmailTab(WebDriver driver) {
        super(driver);
    }

    public EmailTab selectEmailTemplate() {
        pageUtils.waitUntilDropdownOptionsLoaded(getEmailTemplateElement().findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(getEmailTemplateElement());
        this.selectValueFromDDL(workFlowData.getNotificationsData().getEmailTemplate());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    public EmailTab selectRecipient() {
        pageUtils.waitUntilDropdownOptionsLoaded(getEmailRecipientElement().findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(getEmailRecipientElement());
        this.selectValueFromDDL(workFlowData.getNotificationsData().getRecipientEmailType());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.setValueOfElement(getEmailRecipientTxtElement(), workFlowData.getNotificationsData().getRecipientEmailAddress());
        pageUtils.waitForJavascriptLoadComplete();
        return this;
    }

    public WebElement getEmailTemplateElement() {
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .below(By.xpath(EMAIL_TAB_ELEMENT + "//span[.='Template']")));
    }

    public WebElement getEmailRecipientElement() {
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .below(By.xpath(EMAIL_TAB_ELEMENT + "//span[.='Recipient']")));
    }

    public WebElement getEmailConfigCostRoundingElement() {
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .toRightOf(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='Cost Rounding']")));
    }

    public WebElement getEmailConfigAprioriCostElement() {
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .toRightOf(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='aPriori Cost']")));
    }

    public WebElement getEmailRecipientTxtElement() {
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//input[type='text']"))
            .toRightOf(getEmailRecipientElement()));
    }
}