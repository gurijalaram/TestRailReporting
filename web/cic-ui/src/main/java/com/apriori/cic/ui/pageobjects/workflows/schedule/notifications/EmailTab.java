package com.apriori.cic.ui.pageobjects.workflows.schedule.notifications;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.ui.enums.EmailConfigSummaryEnum;
import com.apriori.cic.ui.enums.EmailTemplateEnum;
import com.apriori.cic.ui.enums.FieldState;
import com.apriori.shared.util.enums.PropertyEnum;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

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
     * @param emailTemplateEnum EmailTemplateEnum
     * @return current class object
     */
    public EmailTab selectEmailTemplate(EmailTemplateEnum emailTemplateEnum) {
        pageUtils.waitUntilDropdownOptionsLoaded(getEmailTabElement("Template").findElement(By.tagName("select")));
        pageUtils.waitForElementAndClick(getEmailTabElement("Template"));
        WebElement webElement = driver.findElement(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, emailTemplateEnum.getEmailTemplate())));
        pageUtils.moveAndClick(webElement);
        pageUtils.waitForElementNotEnabled(notificationNextButton, 1);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    public EmailTab selectEmailTemplate() {
        pageUtils.waitUntilDropdownOptionsLoaded(getEmailTabElement("Template").findElement(By.tagName("select")));
        pageUtils.waitForElementEnabled(notificationNextButton);
        pageUtils.waitForElementAndClick(getEmailTabElement("Template"));
        WebElement webElement = driver.findElement(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, workFlowData.getNotificationsData().getEmailTemplate())));
        pageUtils.moveAndClick(webElement);
        pageUtils.waitForElementNotEnabled(notificationNextButton, 1);
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
        pageUtils.waitForElementAndClick(getEmailTabFieldElement("Recipient"));
        pageUtils.moveAndClick(driver.findElement(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, recipientFieldType.getEmailRecipientType()))));
        pageUtils.waitForElementAppear(recipientEmailAddressTxtElement);
        pageUtils.setValueOfElement(recipientEmailAddressTxtElement, emailAddress);
        if (pageUtils.isElementDisplayed(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'ss-open')]"))) {
            pageUtils.waitForElementAndClick(getEmailTabFieldElement("Recipient"));
        }
        pageUtils.waitForJavascriptLoadComplete();
        return this;
    }

    public EmailTab selectRecipient() {
        pageUtils.waitForElementAndClick(getEmailTabFieldElement("Recipient"));
        pageUtils.moveAndClick(driver.findElement(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, workFlowData.getNotificationsData().getRecipientEmailType()))));
        pageUtils.waitForElementAppear(recipientEmailAddressTxtElement);
        pageUtils.setValueOfElement(recipientEmailAddressTxtElement, workFlowData.getNotificationsData().getRecipientEmailAddress());
        if (pageUtils.isElementDisplayed(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'ss-open')]"))) {
            pageUtils.moveAndClick(getEmailTabFieldElement("Recipient"));
        }
        pageUtils.waitForJavascriptLoadComplete();
        return this;
    }

    /**
     * select cost rounding
     *
     * @return EmailTab object
     */
    public EmailTab selectCostRounding(FieldState fieldState) {
        pageUtils.waitForElementAndClick(getEmailTabTxtElement("Cost Rounding"));
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, fieldState)));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * select apriori cost type field
     *
     * @param aprioriCostType - AprioriCostType enum
     * @return current class object
     */
    public EmailTab selectAprioriCost(PropertyEnum aprioriCostType) {
        pageUtils.waitForElementAndClick(getEmailTabTxtElement("aPriori Cost"));
        pageUtils.waitForElementAndClick(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, aprioriCostType.getProperty())));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * select cost rounding
     *
     * @return EmailTab object
     */
    public EmailTab selectScenarioLink(PlmTypeAttributes plmTypeAttributes) {
        pageUtils.waitForElementAndClick(getEmailConfigScenarioLinkElement());
        pageUtils.moveAndClick(driver.findElement(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, plmTypeAttributes.getCicGuiField()))));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * select cost rounding
     *
     * @return EmailTab object
     */
    public EmailTab selectField(EmailConfigSummaryEnum emailConfigSummaryField, PlmTypeAttributes plmTypeAttributes) {
        pageUtils.waitForElementAndClick(getEmailConfigFieldElement(emailConfigSummaryField));
        pageUtils.waitForElementAppear(searchTextboxElement).sendKeys(plmTypeAttributes.getCicGuiField());
        pageUtils.moveAndClick(driver.findElement(By.xpath(String.format(OPTIONS_CONTAINS_TEXT, plmTypeAttributes.getCicGuiField()))));
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return this;
    }

    /**
     * get Email Config scenario link element
     *
     * @return WebElement
     */
    public WebElement getEmailConfigScenarioLinkElement() {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='Cost Rounding']")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .toRightOf(getEmailConfigTextElement(EmailConfigSummaryEnum.SCENARIO_LINK)));
    }

    /**
     * get Email Config Field Element based on the parameter
     *
     * @param emailConfigSummaryEnum - EmailConfigSummaryEnum
     * @return WebElement
     */
    public WebElement getEmailConfigFieldElement(EmailConfigSummaryEnum emailConfigSummaryEnum) {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//input[@value='Cost Rounding']")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .toRightOf(getEmailConfigTextElement(emailConfigSummaryEnum)));
    }

    /**
     * get Email Config custom text element
     *
     * @param configSummaryTemplateField - EmailConfigSummaryEnum
     * @return WebElement
     */
    private WebElement getEmailConfigTextElement(EmailConfigSummaryEnum configSummaryTemplateField) {
        List<WebElement> templateFieldElements = driver.findElements(with(By.xpath(EMAIL_TAB_ELEMENT + "//input[@type='text'] [@disabled='disabled']")));
        return templateFieldElements.stream()
            .filter(webElement -> webElement.getAttribute("value").equals(configSummaryTemplateField.getEmailConfigSummaryField()))
            .findFirst().get();
    }

    /**
     * get Email tab txt field element
     *
     * @param txtFieldName - txt field name
     * @return WebElement
     */
    public WebElement getEmailTabTxtElement(String txtFieldName) {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + String.format("//input[@value='%s']", txtFieldName))));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .toRightOf(By.xpath(EMAIL_TAB_ELEMENT + String.format("//input[@value='%s']", txtFieldName))));
    }

    /**
     * Get Element in Email Tab based field name
     *
     * @param elementTxt - Field text
     * @return WebElement
     */
    private WebElement getEmailTabElement(String elementTxt) {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]")));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .below(By.xpath(EMAIL_TAB_ELEMENT + String.format("//span[.='%s']", elementTxt))));
    }

    /**
     * get Email tab field element
     *
     * @param fieldName - Field Name
     * @return WebElement
     */
    private WebElement getEmailTabFieldElement(String fieldName) {
        pageUtils.waitForElementAppear(driver.findElement(By.xpath(EMAIL_TAB_ELEMENT + String.format("//span[.='%s']", fieldName))));
        return driver.findElement(with(By.xpath(EMAIL_TAB_ELEMENT + "//div[contains(@class, 'dropdown')]"))
            .below(By.xpath(EMAIL_TAB_ELEMENT + String.format("//span[.='%s']", fieldName))));
    }

}