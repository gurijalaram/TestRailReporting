package com.apriori.cic.ui.pageobjects.workflows.schedule.notifications;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MonitoringTab extends NotificationsPart {

    public MonitoringTab(WebDriver driver) {
        super(driver);
    }


    /**
     * select Custom multi field in write fields rows
     *
     * @param customValues
     * @return MonitoringTab
     */
    public MonitoringTab selectMultiSelectField(String... customValues) {
        for (String multiValue : customValues) {
            pageUtils.waitUntilDropdownOptionsLoaded(getNotificationTriggerElement().findElement(By.tagName("select")));
            pageUtils.waitForElementAndClick(getNotificationTriggerElement());
            this.selectValueFromDDL(multiValue);
            if (pageUtils.isElementDisplayed(By.xpath("//div[@tab-number='4']//div[@tab-number='4']//div[contains(@class, 'ss-multi-selected ss-open')]"))) {
                pageUtils.waitForElementAndClick(getNotificationTriggerElement());
            }
            pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        }
        return this;
    }

    /**
     * enter Recipient email address
     *
     * @param emailAddresses - email address
     * @return MonitoringTab
     */
    public MonitoringTab enterRecipient(String... emailAddresses) {
        for (String emailAddress : emailAddresses) {
            pageUtils.setValueOfElement(getRecipientElement(), emailAddress);
        }
        return this;
    }

    /**
     * get Notification Trigger dropdow Element
     *
     * @return WebElement
     */
    private WebElement getNotificationTriggerElement() {
        return driver.findElement(with(By.xpath("//div[contains(@class, 'dropdown')]")).below(By.xpath("//span[.='Notification Trigger:']")));
    }

    /**
     * get recipient email address textbox webelement
     *
     * @return WebElement
     */
    private WebElement getRecipientElement() {
        return driver.findElement(with(By.xpath("//input")).below(By.xpath("//span[.='Recipient:']")));
    }

}
