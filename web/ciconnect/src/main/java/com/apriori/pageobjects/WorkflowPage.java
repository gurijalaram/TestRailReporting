package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Constants;
import utils.TableUtils;
import utils.UIUtils;

import java.util.List;

public class WorkflowPage {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowPage.class);

    @FindBy(css = "img[title='Users']")
    private WebElement userTab;
    @FindBy(css = "img[title='Workflows']")
    private WebElement workflowTab;
    @FindBy(css = "img[title='Connectors']")
    private WebElement connectorTab;
    @FindBy(css = "div[title='View History']")
    private WebElement historyTab;
    @FindBy(css = "div[title='Schedule']")
    private WebElement scheduleTab;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody")
    private WebElement workflowList;
    @FindBy(css = "#root_pagemashupcontainer-1_button-35 > button")
    private WebElement newWorkflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-36 > button")
    private WebElement editWorkflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-37 > button")
    private WebElement deleteWorkflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-97 > button")
    private WebElement invokeWorflowButton;
    @FindBy(css = "#root_pagemashupcontainer-1_button-38 > button")
    private WebElement refreshButton;
    @FindBy(css = "#root_pagemashupcontainer-1_tabsv2-10 > div.tab-content > div.tabsv2-viewport > div > div > div:nth-child(2) > div")
    private WebElement viewHistoryTab;

    private String row = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody > " +
            "tr:nth-child({ROWNUMBER})";

    private WebDriver driver;
    private PageUtils pageUtils;
    TableUtils tableUtils;
    UIUtils uiUtils;

    public WorkflowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        pageUtils = PageUtils.getInstance(driver);
        tableUtils = new TableUtils(driver);
        uiUtils = new UIUtils();
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    /**
     * Refreshes the workflow page to reload the the DOM. This prevents stale element errors
     *
     * @param workflowPage Worflowpage object
     */
    public void refeshPage(WorkflowPage workflowPage) {
        workflowPage.clickUserTab();
        workflowPage.clickWorkflowTab();
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
    }

    /**
     * Click on the Users tab
     */
    public void clickUserTab() {
        pageUtils.waitForElementAndClick(userTab);
    }

    /**
     * Click on the Workflow tab
     */
    public void clickWorkflowTab() {
        pageUtils.waitForElementAndClick(workflowTab);
    }

    /**
     * Click on the refresh workflow list button
     */
    public void refreshWorkflowTable() {
        pageUtils.waitForElementAndClick(refreshButton);
    }

    /**
     * Checks the enabled state of a button on the Workflow Schedule page
     *
     * @param button The button to check for
     * @return True if the buton is enabled
     */
    public Boolean getButtonState(String button) {
        switch (button.toUpperCase()) {
            case "NEW":
                pageUtils.waitForElementToAppear(newWorkflowButton);
                return newWorkflowButton.isEnabled();
            case "EDIT":
                pageUtils.waitForElementToAppear(editWorkflowButton);
                return editWorkflowButton.isEnabled();
            case "DELETE":
                pageUtils.waitForElementToAppear(deleteWorkflowButton);
                return deleteWorkflowButton.isEnabled();
            case "INVOKE":
                pageUtils.waitForElementToAppear(invokeWorflowButton);
                return invokeWorflowButton.isEnabled();
            default:
                return null;
        }
    }

    /**
     * Click the Invoke button
     */
    public void clickInvokeButton() {
        pageUtils.waitForElementAndClick(invokeWorflowButton);
    }

    /**
     * Click the Delete button
     */
    public void clickDeleteButton() {
        pageUtils.waitForElementAndClick(deleteWorkflowButton);
    }

    /**
     * Click the Edit button
     */
    public void clickEditButton() {
        pageUtils.waitForElementAndClick(editWorkflowButton);
    }

    /**
     * Select a workflow by name in the Schedule Workflow list
     *
     * @param name Name of the workflow to select
     */
    public void selectWorkflow(String name) {
        pageUtils.waitForElementAppear(workflowList);
        tableUtils.selectRowByName(workflowList, name);
    }

    /**
     * Check if a workflow exists in the Schedule Workflow list. The search is by workflow name
     *
     * @param name Workflow name to check for
     * @return True if the workflow exists
     */
    public Boolean workflowExists(String name) {
        pageUtils.waitForElementToBeClickable(workflowList);
        return tableUtils.itemExistsInTable(workflowList, name);
    }

    /**
     * Get the New Workflow popup
     */
    public void newWorkflow() {
        pageUtils.waitForElementAndClick(newWorkflowButton);
    }

    /**
     * Get the Edit Workflow popup
     */
    public void editWorkflow() {
        pageUtils.waitForElementAndClick(editWorkflowButton);
    }

    /**
     * Selects he first workflow in the Schedule Workflow list
     */
    public void clickOnFirstWorkflowRow() {
        tableUtils.selectRowByIndex(workflowList, 1);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
    }

    /**
     * Retrieve the name of the workflow in a specified row
     *
     * @param rowNumber The row numer to get the workflow name
     * @return Workflow name
     */
    public String getRowName(Integer rowNumber) {
        rowNumber = rowNumber + 1;
        String cssRow = row.replace("{ROWNUMBER}", rowNumber.toString());
        WebElement workflowRow = driver.findElement(By.cssSelector(cssRow));
        List<WebElement> columns = workflowRow.findElements(By.tagName("td"));
        return columns.get(0).getText();
    }
}
