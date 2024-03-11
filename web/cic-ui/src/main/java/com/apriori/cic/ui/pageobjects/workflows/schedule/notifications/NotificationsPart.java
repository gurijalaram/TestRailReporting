package com.apriori.cic.ui.pageobjects.workflows.schedule.notifications;

import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.cic.ui.utils.Constants;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Notifications tab during workflow creation or modification
 * Navigate to Attach Report tab or Email Tab
 */
@Slf4j
public class NotificationsPart extends CICBasePage {

    protected static final String NOTIFICATIONS_TAB_ELEMENT = "//div[@tab-number='4']";

    @FindBy(xpath = NOTIFICATIONS_TAB_ELEMENT + "//div[@title='Email']")
    protected WebElement emailTab;

    @FindBy(xpath = NOTIFICATIONS_TAB_ELEMENT + "//div[@title='Attach Report']")
    protected WebElement attachReportTab;

    @FindBy(xpath = NOTIFICATIONS_TAB_ELEMENT + "//div[@title='Filter']")
    protected WebElement filterTab;

    @FindBy(xpath = NOTIFICATIONS_TAB_ELEMENT + "//div[@title='Monitoring']")
    protected WebElement monitoringTab;

    @FindBy(xpath = NOTIFICATIONS_TAB_ELEMENT + "//button[.='Next']")
    protected WebElement notificationNextButton;

    @FindBy(xpath = NOTIFICATIONS_TAB_ELEMENT + "//div[@tab-number='4']//span[.='Job Monitoring Notification Configuration:']")
    protected WebElement monitoringTabConfigElement;

    public NotificationsPart(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigate to Email Tab in notifications part
     *
     * @return Email Tab
     */
    public EmailTab selectEmailTab() {
        if (!this.emailTab.isEnabled()) {
            pageUtils.waitForElementAndClick(emailTab);
        }
        return new EmailTab(driver);
    }

    /**
     * Navigate to Filter Tab in notifications part
     *
     * @return Email Tab
     */
    public FilterTab selectFilterTab() {
        if (this.filterTab.isEnabled()) {
            pageUtils.waitForElementAndClick(filterTab);
        }
        return new FilterTab(driver);
    }

    /**
     * Navigate to AttachReport tab in notification part
     *
     * @return AttachReportTab object
     */
    public AttachReportTab selectAttachReport() {
        if (this.attachReportTab.isDisplayed()) {
            pageUtils.waitForElementAndClick(attachReportTab);
        }
        return new AttachReportTab(driver);
    }

    /**
     * Navigate to Monitoring Tab in notifications part
     *
     * @return Monitoring  Tab
     */
    public MonitoringTab selectMonitoringTab() {
        if (this.monitoringTab.isEnabled()) {
            pageUtils.waitForElementAndClick(monitoringTab);
            pageUtils.waitForElementToAppear(monitoringTabConfigElement);
        }
        return new MonitoringTab(driver);
    }


    /**
     * Click Previous Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public PublishResultsPart clickCINotificationNextBtn() {
        pageUtils.waitForElementToBeClickable(notificationNextButton);
        pageUtils.waitForElementAndClick(notificationNextButton);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return new PublishResultsPart(this.driver);
    }

    /**
     * Getter for Notifications Part Next Button
     *
     * @return WebElement
     */
    public WebElement getNotificationNextButton() {
        return notificationNextButton;
    }

    /**
     * Getter for email tab element
     *
     * @return emailTab Webelement
     */
    public WebElement getEmailTab() {
        return emailTab;
    }

    /**
     * getter for Attach report tab element
     *
     * @return WebElement
     */
    public WebElement getAttachReportTab() {
        return attachReportTab;
    }

    /**
     * getter for Filter tab element
     *
     * @return WebElement
     */
    public WebElement getFilterTab() {
        return filterTab;
    }
}
