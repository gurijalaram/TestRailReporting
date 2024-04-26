package com.apriori.cic.ui.pageobjects.workflows.schedule.notifications;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.ui.enums.EmailTemplateEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Email tab in Notification part during workflow creation or modification
 */
public class EmailTab extends NotificationsPart {

    private static final String EMAIL_TAB_ELEMENT = NOTIFICATIONS_TAB_ELEMENT + "//div[@tab-number='1']";

    @FindBy(xpath = EMAIL_TAB_ELEMENT + "//input[contains(@placeholder, 'Recipient email address')]")
    protected WebElement recipientEmailAddressTxtElement;

    public EmailTab(WebDriver driver) {
        super(driver);
    }

    /**
     * Select Report email Template
     *
     * @param reportsEnum ReportsEnum
     * @return current class object
     */
    public EmailTab selectEmailTemplate(EmailTemplateEnum emailTemplateEnum) {
        pageUtils.waitUntilDropdownOptionsLoaded(getEmailTemplateElement().findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(getEmailTemplateElement());
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, emailTemplateEnum.getEmailTemplate())));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    public EmailTab selectEmailTemplate() {
        pageUtils.waitUntilDropdownOptionsLoaded(getEmailTemplateElement().findElement(By.tagName("select")));
        pageUtils.waitForElementEnabled(notificationNextButton);
        pageUtils.waitForElementAndClick(getEmailTemplateElement());
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, workFlowData.getNotificationsData().getEmailTemplate())));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * Select Email Recipient Field Type
     *
     * @param recipientFieldType - EmailRecipientType enum
     * @param emailAddress       - email address
     * @return current class object
     */
    public EmailTab selectRecipient(EmailRecipientType recipientFieldType, String emailAddress) {
        pageUtils.waitForElementAndClick(getEmailRecipientElement());
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, recipientFieldType.getEmailRecipientType())));
        pageUtils.waitForElementAppear(recipientEmailAddressTxtElement);
        pageUtils.setValueOfElement(recipientEmailAddressTxtElement, emailAddress);
        if (pageUtils.isElementDisplayed(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'ss-open')]"))) {
            pageUtils.waitForElementAndClick(getEmailRecipientElement());
        }
        pageUtils.waitForJavascriptLoadComplete();
        return this;
    }

    public EmailTab selectRecipient() {
        pageUtils.waitForElementAndClick(getEmailRecipientElement());
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, workFlowData.getNotificationsData().getRecipientEmailType())));
        pageUtils.waitForElementAppear(recipientEmailAddressTxtElement);
        pageUtils.setValueOfElement(recipientEmailAddressTxtElement, workFlowData.getNotificationsData().getRecipientEmailAddress());
        if (pageUtils.isElementDisplayed(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'ss-open')]"))) {
            pageUtils.waitForElementAndClick(getEmailRecipientElement());
        }
        pageUtils.waitForJavascriptLoadComplete();
        return this;
    }

    public WebElement getEmailTemplateElement() {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .below(By.xpath(EMAIL_TAB_ELEMENT + "//span[.='Template']")));
    }

    public WebElement getEmailRecipientElement() {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//span[.='Recipient']")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .below(By.xpath(EMAIL_TAB_ELEMENT + "//span[.='Recipient']")));
    }

    public WebElement getEmailConfigCostRoundingElement() {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='Cost Rounding']")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .toRightOf(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='Cost Rounding']")));
    }

    public WebElement getEmailConfigAprioriCostElement() {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='aPriori Cost']")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .toRightOf(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='aPriori Cost']")));
    }

    public WebElement getEmailRecipientTxtElement() {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//input[@type='text']")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//input[@type='text']"))
            .toRightOf(getEmailRecipientElement()));
    }

}