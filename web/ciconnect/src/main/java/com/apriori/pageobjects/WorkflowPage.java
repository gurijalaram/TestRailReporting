package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
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
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.xhdr > table > tbody > tr:nth-child(2) > td:nth-child(1)")
    private WebElement firstColumn;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody > tr:nth-child(2) > td:nth-child(3)")
    private WebElement firstRowFirstColumn;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.xhdr > table")
    private WebElement workflowListHeaders;
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
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(11) > div")
    private WebElement pageSizeLabel25;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(13) > div")
    private WebElement pageSizeLabel5;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(1) > img")
    private WebElement paginatorBeginning;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(2) > img")
    private WebElement paginatorPrevious;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(10) > img")
    private WebElement paginatorNext;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(11) > img")
    private WebElement paginatorLast;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(3) > div")
    private WebElement rowRangeLabel;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(4)")
    private WebElement rowTotal;
    @FindBy(css = "#runtime > div:nth-child(30) > table > tbody > tr.tr_btn > td.td_btn_txt > div > span")
    private WebElement pageMaxDropDownSelection;
    @FindBy(css = "#runtime > div:nth-child(30) > table")
    private WebElement pageMaxSizeTable;

    private String row = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody > " +
            "tr:nth-child({ROWNUMBER})";

    private WebDriver driver;
    private PageUtils pageUtils;
    TableUtils tableUtils;
    UIUtils uiUtils;

    public enum Field {
        NAME,
        LAST_MODIFIED
    }

    public WorkflowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        pageUtils = new PageUtils(driver);
        tableUtils = new TableUtils(driver);
        uiUtils = new UIUtils();
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    /**
     *Workflow List Headers
     *
     * Display Name (User defined workflow name)
     * Description (User defined workflow description)
     * Last Modified By (Name of the user who last made changes)
     * Schedule (A CRON string translated into colloquial language)
     * Connector (The name of the associated connector)
     * Enabled (tick-box indicating whether workflow is enabled or disabled)
     * Locked (tick-box indicating whether a Workflow is locked for editing due to job in progress or not)
     *
     * @return expected array of headers
     */
    public String[] getExpectedWorkflowHeaders() {
        String[] expectedHeaders = {
            "Name",
            "Description",
            "Last Modified",
            "Schedule",
            "Connector",
            "Enabled",
            "Locked"
        };
        return expectedHeaders;
    }

    /**
     * Return the worfkflow list's headers
     */
    public List<String> getWorkflowListHeaders() {
        return tableUtils.getTableHeaders(workflowList);
    }

    /**
     *
     * @return the number of workflows displayed the schedule page
     */
    public int getNumberOfDisplayedWorkflows() {
        return tableUtils.numberOfRows(workflowList);
    }

    /**
     *
     * @return Page Max Size Table
     */
    public WebElement getPageMaxSizeTable() {
        return pageMaxSizeTable;
    }

    /**
     * Get the current page row range
     * @return Row range
     */
    public String getRowRange() {
        pageUtils.waitForElementAppear(rowRangeLabel);
        return rowRangeLabel.getText();
    }

    /**
     * Get the number of workflows in the list
     * @return Row total
     */
    public String getRowTotal() {
        pageUtils.waitForElementAppear(rowTotal);
        return rowTotal.getText();
    }

    /**
     * Goto the beginning of the list
     */
    public void pageToTheBeginning() {
        pageUtils.waitForElementAndClick(paginatorBeginning);
    }

    /**
     * Get the first workflows name
     *
     * @return name
     */
    public String getFirstWorkflowName() {
        pageUtils.waitForElementAndClick(firstRowFirstColumn);
        return firstRowFirstColumn.getText();
    }

    /**
     * click on the first column of the header row
     */
    public void clickOnFirstColumn() {
        pageUtils.waitForElementAndClick(firstColumn);
    }

    /**
     * Goto the end of the list
     */
    public WorkflowPage pageToTheEnd() {
        pageUtils.waitForElementAndClick(paginatorLast);
        return this;
    }

    /**
     * Goto the previous page
     */
    public WorkflowPage pageBack() {
        pageUtils.waitForElementAndClick(paginatorPrevious);
        return this;
    }

    /**
     * Goto the next page
     */
    public WorkflowPage pageNext() {
        pageUtils.waitForElementAndClick(paginatorNext);
        return this;
    }

    /**
     * Get the workflow list page size
     *
     * @param setMaxSize The current max page size
     * @return Page size
     */
    public Integer getPageSize(int setMaxSize) {
        WebElement pageSizeLabel = getPageSizeLabel(setMaxSize);
        pageUtils.waitForElementAppear(pageSizeLabel);
        String[] labelSplit = pageSizeLabel.getText().split("rows");
        return Integer.parseInt(labelSplit[0].trim());
    }

    /**
     * Gets the page size label for the requested size
     *
     * @param size Number of rows per page
     * @return page size element
     */
    public WebElement getPageSizeLabel(int size) {
        switch (size) {
            case 5:
                return pageSizeLabel5;
            case 25:
                return pageSizeLabel25;
            default:
                return null;
        }
    }

    /**
     * Refreshes the workflow page to reload the the DOM. This prevents stale element errors
     *
     */
    public void refreshPage() {
        clickUserTab();
        clickWorkflowTab();
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
        refreshPage();
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
     * @param field The field to retrieve the value for
     * @return Workflow name
     */
    public String getRowValue(Integer rowNumber, Field field) {
        rowNumber = rowNumber + 1;
        String cssRow = row.replace("{ROWNUMBER}", rowNumber.toString());
        WebElement workflowRow = driver.findElement(By.cssSelector(cssRow));
        List<WebElement> columns = workflowRow.findElements(By.tagName("td"));

        switch (field) {
            case NAME:
                return columns.get(0).getText();
            case LAST_MODIFIED:
                return columns.get(2).getText();
            default:
                return null;
        }

    }

    /**
     * Determine if the workflow list is exists and is enabled on the schedule tab
     *
     * @return True if the workflow list is enabled
     */
    public boolean workflowListExists() {
        return pageUtils.isElementEnabled(workflowList);
    }

    /**
     * Open maximum page size drop down
     *
     * @param size number of items visible on a page
     */
    public void openMaxPageDropDown(int size) {
        WebElement pageSizeLabel = null;
        switch (size) {
            case 5:
                pageSizeLabel = pageSizeLabel5;
                break;
            case 25:
                pageSizeLabel = pageSizeLabel25;
                break;
            default:
                logger.debug("Invalid size selection: " + size);
        }

        pageUtils.waitForElementAndClick(pageSizeLabel);
    }

    /**
     * Click on the maximum page size dropdown
     */
    public void clickOnMaxSizeDropDown() {
        pageUtils.waitForElementAndClick(pageMaxDropDownSelection);
    }
}
