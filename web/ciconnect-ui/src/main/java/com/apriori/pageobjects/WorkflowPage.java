package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
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

    @FindBy(css = "span[title='Users']")
    private WebElement userTab;
    @FindBy(css = "span[title='Workflows']")
    private WebElement workflowTab;
    @FindBy(css = "span[title='Connectors']")
    private WebElement connectorTab;
    @FindBy(css = "div[title='View History']")
    private WebElement historyTab;
    @FindBy(css = "div[title='Schedule']")
    private WebElement scheduleTab;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody")
    private WebElement workflowList;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.xhdr > table > tbody > tr:nth-child(2)")
    private WebElement workflowHeaders;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.xhdr > table > tbody > tr:nth-child(2) > td:nth-child(1)")
    private WebElement firstColumn;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.objbox > table > tbody > " +
            "tr:nth-child(2) > td:nth-child(1)")
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
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div.dhx_toolbar_btn > div")
    private WebElement pageSizeButton;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div")
    private WebElement pageSizeLabel;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child" +
            "(3) > div")
    private WebElement pageSizeLabel25;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(3) > div")

    private WebElement pageSizeLabel5;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(1) > img")
    private WebElement paginatorBeginning;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(2) > img")
    private WebElement paginatorPrevious;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(8)")
    private WebElement paginatorNext;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(9) > img")
    private WebElement paginatorLast;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(3) > div")
    private WebElement rowRangeLabel;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child(4)")
    private WebElement rowTotal;
    @FindBy(css = "#runtime > div:nth-child(40) > table > tbody > tr.tr_btn > td.td_btn_txt > div > span")
    private WebElement pageMaxDropDownSelection5;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-child" +
            "(12)")
    private WebElement pageMaxDropDownSelection25;
    @FindBy(css = "#runtime > div:nth-child(30) > table")
    private WebElement pageMaxSizeTable;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div")
    private WebElement paginationBar;
    @FindBy(className = "buttons_cont")
    private WebElement maxPageSizeTable;

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
            "Last Modified By",
            "Schedule",
            "Connector",
            "Enabled",
            "Locked"
        };
        return expectedHeaders;
    }

    /**
     * Get the number of workflow rows in the workflow table
     *
     * @return number of rows
     */
    public Integer getRowCount() {
        return tableUtils.getRowCount(workflowList) - 1;
    }

    /**
     * Sort the workflow schedule list
     *
     * @param columnHeader The column to sort by
     * @return WorkflowPage Object
     */
    public WorkflowPage sortBy(String columnHeader) {
        tableUtils.getColumnHeader(workflowHeaders, columnHeader).click();
        return this;
    }

    /**
     * Return the worfkflow list's headers
     */
    public List<String> getWorkflowListHeaders() {
        return tableUtils.getTableHeaders(workflowHeaders);
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
     * Return the workflow row index
     *
     * @param workflowName
     * @return
     */
    public Integer getWorkflowIndex(String workflowName) {
        return tableUtils.getTableItemIndex(workflowList, workflowName);
    }

    /**
     * Get the workflow list page size
     *
     * @param setMaxSize The current max page size
     * @return Page size
     */
    public Integer getPageSize(int setMaxSize) {
        WebElement pageSizeLbl = getPageSizeLabel(setMaxSize);
        pageUtils.waitForElementToBeClickable(pageSizeLabel);
        String[] labelSplit = pageSizeLbl.getText().split("Rows");

        String[] range = labelSplit[1].split("-");
        return Integer.parseInt(range[1].trim());
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
     * Gets the max page size selection
     *
     * @param size Maximum number of rows per page
     * @return page size element
     */
    public WebElement getMaxSizeSelection(int size) {
        switch (size) {
            case 5:
                return pageMaxDropDownSelection5;
            case 25:
                return pageMaxDropDownSelection25;
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
     * Select a workflow by connector name in the Schedule Workflow list
     *
     */
    public WebElement selectWorkflowByConnector() {
        pageUtils.waitForElementAppear(workflowList);
        return tableUtils.selectRowByConnector(workflowList);
    }

    /**
     * Check if a workflow exists in the Schedule Workflow list. The search is by workflow name
     *
     * @param name Workflow name to check for
     * @return True if the workflow exists
     */
    public Boolean workflowExists(String name) {
        pageUtils.waitForElementToBeClickable(workflowList);
        try {
            sortBy("Last Modified By");
        } catch (ElementClickInterceptedException elementClickInterceptedException) {
            refreshPage();
            sortBy("Last Modified By");

        }

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
        rowNumber = rowNumber + 2;
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
     * @return Workflowpage object
     */
    public WorkflowPage openMaxPageDropDown(int currentSize) {
        WebElement pageSizeLbl = getPageSizeLabel(currentSize);
        pageUtils.waitForElementAndClick(pageSizeLbl);
        return this;
    }

    /**
     * Click on the maximum page size dropdown
     *
     * @param currentSize Current maximum number of workflows displayed
     * @param selectedSize Maximum number of workflows displayed on a page
     * @return Workflowpage object
     */
    public WorkflowPage clickOnMaxSizeDropDown(int currentSize, int selectedSize) {
        findPaginationElement(currentSize);

        pageUtils.waitForElementToBeClickable(maxPageSizeTable);
        List<WebElement> rows = maxPageSizeTable.findElements(By.cssSelector("tr"));
        for (WebElement maxSizeSelection : rows) {
            String text = maxSizeSelection.findElement(By.className("btn_sel_text"))
                    .getText();
            if (text.equalsIgnoreCase(String.format("%d rows per page", selectedSize))) {
                pageUtils.waitForElementAndClick(maxSizeSelection);
                break;
            }
        }


        return this;
    }

    /**
     * Finds the maximum page size button and clicks on it
     *
     * @param size Current maximum rows per page setting
     * @return Workflow page
     */
    public WorkflowPage findPaginationElement(int size) {
        List<WebElement> paginationFields = paginationBar.findElements(By.cssSelector("div"));
        for (WebElement field : paginationFields) {
            if (field.getText().equalsIgnoreCase(String.format("%d rows per page", size))) {
                pageUtils.waitForElementAndClick(field);
                break;
            }
        }

        return  this;
    }
}
