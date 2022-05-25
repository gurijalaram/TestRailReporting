package com.apriori.pages.workflows;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.CICBasePage;
import com.apriori.pages.workflows.history.HistoryPage;
import com.apriori.pages.workflows.schedule.SchedulePage;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowHome extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowHome.class);

    @FindBy(css = "div#root_button-33 > button")
    private WebElement settingsBtn;

    @FindBy(css = "div#root_button-29 > button")
    private WebElement helpBtn;

    @FindBy(css = "div#root_button-30 > button")
    private WebElement userInfoDropdown;

    @FindBy(css = "div[title='View History']")
    private WebElement historyTab;

    @FindBy(css = "div[title='Schedule']")
    private WebElement scheduleTab;

    @FindBy(css = "#root_pagemashupcontainer-1_tabsv2-10 > div.tab-content > div.tabsv2-viewport > div > div > div.tabsv2-tab.enabled.selected > div > div")
    private WebElement selectedScheduleTab;

    @FindBy(css = "div.tw-status-msg-box")
    private WebElement statusMessagePopUpElement;

    @FindBy(css = "div.tw-status-msg-box > div.status-msg-container > div.status-msg > div[id='status-msg-text']")
    private WebElement statusMessageLbl;

    @FindBy(css = "div.tw-status-msg-box > div.close-sticky")
    private WebElement statusMessageCloseBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_label-18'] > span")
    private WebElement workflowLabel;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-30'] > span")
    private WebElement currentUser;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-31'] > span")
    private WebElement currentLoginID;

    @FindBy(css = "div[id^='CIC_UserDropDown_MU-BMController-'][id$='_label-32'] > span")
    private WebElement currentCompany;

    @FindBy(css = "div[id^='CIC_HelpDropDown_MU-BMController-'][id$='_link-41'] > a > span > span > span")
    private WebElement onlineHelpLink;


    public WorkflowHome(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
    }

    public WorkflowHome setTestData(WorkFlowData workFlowData) {
        this.workFlowData = workFlowData;
        return new WorkflowHome(driver);
    }

    /**
     * Click on the Users tab
     */
    public SchedulePage selectScheduleTab() {
        if (!pageUtils.isElementEnabled(selectedScheduleTab)) {
            pageUtils.waitForElementAndClick(scheduleTab);
        }
        return new SchedulePage(driver);
    }

    /**
     * Click on the Workflow tab
     *
     * @return
     */
    public HistoryPage selectViewHistoryTab() {
        pageUtils.waitForElementAndClick(historyTab);
        return new HistoryPage(driver);

    }

    public String getWorkFlowStatusMessage() {
        String message = StringUtils.EMPTY;
        try {
            pageUtils.waitForElementToAppear(statusMessagePopUpElement);
            message = statusMessageLbl.getText();
            pageUtils.waitForJavascriptLoadComplete();
        } catch (ElementNotInteractableException elementNotInteractableException) {
            logger.debug(elementNotInteractableException.getMessage());
        }
        return message;
    }

    /**
     * Get workflow page label text
     *
     * @return String
     */
    public String getWorkflowText() {
        pageUtils.waitForElementToAppear(workflowLabel);
        return workflowLabel.getText();
    }

    /**
     * Get current user
     *
     * @return String
     */
    public String getCurrentUser() {
        pageUtils.waitForElementToAppear(currentUser);
        return currentUser.getText();
    }

    /**
     * Get current user Login ID
     *
     * @return String
     */
    public String getLoginID() {
        return pageUtils.waitForElementToAppear(currentLoginID).getText();
    }

    /**
     * Get current user
     *
     * @return String
     */
    public String getCurrentCompany() {
        return pageUtils.waitForElementToAppear(currentCompany).getText();
    }

}