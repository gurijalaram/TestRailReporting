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

    public static final String PARENT_ELEMENT = "div[id*='root_pagemashupcontainer-1_navigation-']";

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_tabsv2-154'] > div.tab-content > div.tabsv2-viewport > div > div > div[class^='tabsv2-tab'][tab-value='email']")
    protected WebElement emailTab;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_tabsv2-154'] > div.tab-content > div.tabsv2-viewport > div > div > div[class^='tabsv2-tab'][tab-value='attach-email-report']")
    protected WebElement attachReportTab;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_tabsv2-154'] > div.tab-content > div.tabsv2-viewport > div > div > div[class^='tabsv2-tab'][tab-value='email-filter']")
    protected WebElement filterTab;

    @FindBy(css = "div[class='BMCollectionViewCellWrapper'] div[class='BMCollectionViewCell BMCollectionViewCellHoverable']")
    protected WebElement ciReportConfigurationRowElement;

    @FindBy(css = PARENT_ELEMENT + "[id$='-popup_button-43'] > button")
    protected WebElement notificationNextButton;

    protected String reportConfigRootElementsRowsCss = "div[class='BMCollectionViewCellWrapper'] div[class='BMCollectionViewCell BMCollectionViewCellHoverable'][id^='CIC_ReportConfigurationCell_MU-']";

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
