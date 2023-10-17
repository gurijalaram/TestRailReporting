package com.apriori.pageobjects.workflows.schedule;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.enums.SortedOrderType;
import com.apriori.enums.WorkflowListColumns;
import com.apriori.features.WorkFlowFeatures;
import com.apriori.pageobjects.CICBasePage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.pageobjects.workflows.schedule.details.DetailsPart;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SchedulePage extends CICBasePage {
    private static final Logger logger = LoggerFactory.getLogger(SchedulePage.class);

    @FindBy(xpath = "//button//span[.='New']")
    private WebElement newWorkflowBtn;
    @FindBy(xpath = "//button//span[.='Edit']")
    private WebElement editWorkflowButton;
    @FindBy(xpath = "//button//span[.='Delete']")
    private WebElement deleteWorkflowButton;
    @FindBy(xpath = "//button//span[.='Invoke']")
    private WebElement invokeWorkflowBtn;
    @FindBy(css = "div[class='tabsv2-actual-tab-contents'][tab-number='1'] div[class$='cic-table'] table.obj")
    private WebElement workflowList;
    @FindBy(css = "div[class='tabsv2-actual-tab-contents'][tab-number='1'] div[class$='cic-table'] table.hdr")
    private WebElement workflowHeaders;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced > div.xhdr > table > tbody > tr:nth-child(2) > td:nth-child(1)")
    private WebElement firstColumn;

    @FindBy(css = "#confirmBox")
    private WebElement confirmAlertBox;

    @FindBy(css = "#confirmButtons > a.button.btn.blue > span")
    private WebElement confirmAlertBoxDeleteBtn;

    @FindBy(css = "#confirmButtons > a.button.btn.gray > span")
    private WebElement confirmAlertBoxCancelBtn;


    @FindBy(css = "div[class^='tabsv2-tab'][tab-value='schedule']")
    private WebElement scheduleTab;

    @FindBy(xpath = "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-bottom-container']//img[contains(@src, 'ar_right') and not(contains(@src, 'abs'))]")
    private WebElement nextBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-of-type(4)")
    private WebElement numRowsText;

    @FindBy(xpath = "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container']//div[contains(text(), ' rows per page')]")
    private WebElement rowsPerPageText;

    @FindBy(css = "div#confirmButtons > a > span")
    private WebElement confirmDeleteBtn;

    @FindBy(css = "div#confirmBox > div:nth-of-type(2) > p")
    private WebElement confirmDeletionText;

    public SchedulePage(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(scheduleTab);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"), 5);
        waitUntilRowsLoaded();
    }

    /**
     * Get new workflow button text
     *
     * @return String
     */
    public String getNewWorkflowBtnText() {
        return newWorkflowBtn.getText();
    }

    /**
     * Click New Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public WorkFlowFeatures clickNewWorkflowBtn() {
        pageUtils.waitForElementAndClick(newWorkflowBtn);
        return new WorkFlowFeatures(this.driver);
    }

    /**
     * Click New Workflow button
     *
     * @return DetailsPart page object
     */
    public DetailsPart clickNewButton() {
        pageUtils.waitForElementAndClick(newWorkflowBtn);
        return new DetailsPart(this.driver);
    }

    /**
     * Click the Invoke button
     */
    public void clickInvokeButton() {
        pageUtils.waitForElementAndClick(invokeWorkflowBtn);
    }

    /**
     * Click the Delete button
     */
    public SchedulePage clickDeleteButton() {
        pageUtils.waitForElementAndClick(deleteWorkflowButton);
        return new SchedulePage(this.driver);
    }

    /**
     * Click the Edit button
     */
    public WorkFlowFeatures clickEditButton() {
        pageUtils.waitForElementAndClick(editWorkflowButton);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return new WorkFlowFeatures(this.driver);
    }

    /**
     * Click Edit Workflow button
     *
     * @return DetailsPart page object
     */
    public DetailsPart clickEditWorkflowBtn() {
        pageUtils.waitForElementAndClick(editWorkflowButton);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementToAppear(workflowPopUpTitleElement);
        pageUtils.waitForElementToAppear(getNameTextFieldElement());
        return new DetailsPart(this.driver);
    }


    public SchedulePage clickConfirmAlertBoxDelete() {
        pageUtils.waitForElementToAppear(confirmAlertBox);
        pageUtils.waitForElementAndClick(confirmAlertBoxDeleteBtn);
        return this;
    }

    public WorkflowHome clickConfirmAlertBoxCancel() {
        pageUtils.waitForElementToAppear(confirmAlertBox);
        pageUtils.waitForElementAndClick(confirmAlertBoxCancelBtn);
        return new WorkflowHome(driver);
    }

    /**
     * Select workflow in table
     *
     * @param workflowName - name of workflow to select
     * @return new Schedule page object
     */
    public SchedulePage selectWorkflow(String workflowName) {
        pageUtils.waitForElementAppear(workflowList);
        tableUtils.selectRowByName(workflowList, workflowName);
        pageUtils.waitForElementToBeClickable(invokeWorkflowBtn);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return new SchedulePage(driver);
    }

    /**
     * select workflow name in schedule page and click invoke button
     *
     * @param worflowName - work flow name
     * @return WorkflowHome
     */
    public WorkflowHome selectWorkflowAndInvoke(String worflowName) {
        this.selectWorkflow(worflowName);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        pageUtils.waitForElementToBeClickable(invokeWorkflowBtn);
        pageUtils.waitForElementAndClick(invokeWorkflowBtn);
        return new WorkflowHome(driver);
    }

    /**
     * Get the row element of workflow name
     * Get the target row value for column from the same selected row.
     *
     * @param workflowName
     * @param targetColumnIndx
     * @return WebElement
     */
    public WebElement getItemFromWorkflowList(String workflowName, WorkflowListColumns targetColumnIndx) {
        WebElement tableRow = tableUtils.findTableItemByName(workflowList, workflowName, WorkflowListColumns.Name.getColumnID());
        return tableUtils.getItemNameFromTable(tableRow, targetColumnIndx.getColumnID());
    }

    /**
     * Get workflow locked status element from list of workflow in schedule tab
     *
     * @param workflowName     - workflow name to be selected from the table
     * @param targetColumnIndx - enum value of targetted column name
     * @return WebElement
     */
    public WebElement getWorkflowLockedStatus(String workflowName, WorkflowListColumns targetColumnIndx) {
        return getItemFromWorkflowList(workflowName, targetColumnIndx).findElement(By.cssSelector("input[type='CHECKBOX']"));
    }

    /**
     * Check if a workflow exists in the Schedule Workflow list. The search is by workflow name
     *
     * @param name Workflow name to check for
     * @return True if the workflow exists
     */
    public Boolean isWorkflowExists(String name) {
        pageUtils.waitForElementToBeClickable(workflowList);
        try {
            return tableUtils.itemExistsInTable(workflowList, name);
        } catch (StaleElementReferenceException staleElementReferenceException) {
            return tableUtils.itemExistsInTable(workflowList, name);
        }
    }

    /**
     * validates workflow is in sorted order by workflow name
     *
     * @param columnName  - WorkflowListColumns enum
     * @param sortedBy    - SortedOrderType (Ascending or descending)
     * @param columnValue - workflow name
     * @return - true or false
     */
    public Boolean isWorkflowListIsSorted(WorkflowListColumns columnName, SortedOrderType sortedBy, String columnValue) {
        Boolean isInSortedOrder = false;
        WebElement webElement;
        if (columnName.toString().equals("Name")) {
            webElement = tableUtils.getColumnHeader(workflowHeaders, columnName.toString());
            isInSortedOrder = sortedBy(webElement, sortedBy, columnValue);
        }
        return isInSortedOrder;
    }

    /**
     * click column name on schedule page workflow list for sorting
     *
     * @param webElement  - target webelement to click in the list
     * @param sortType    - Ascending or descending
     * @param columnValue - workflow name
     * @return true or false
     */
    private Boolean sortedBy(WebElement webElement, SortedOrderType sortType, String columnValue) {
        Boolean isInSortedOrder = false;
        switch (sortType.toString()) {
            case "DESCENDING":
                pageUtils.waitForElementAndClick(webElement);
                pageUtils.waitForElementToBeClickable(workflowList);
                if (!webElement.getAttribute("class").equals("dhxgrid_sort_desc_col")) {
                    pageUtils.waitForElementAndClick(webElement);
                    pageUtils.waitForElementToBeClickable(workflowList);
                }
                isInSortedOrder = isWorkflowExists(columnValue);
                break;
            default:
                pageUtils.waitForElementAndClick(webElement);
                pageUtils.waitForElementToBeClickable(workflowList);
                isInSortedOrder = webElement.getAttribute("class").equals("dhxgrid_sort_asc_col") ? isWorkflowExists(columnValue) : false;
        }
        return isInSortedOrder;
    }

    /**
     * Get total number of rows in table
     *
     * @return numRows  - number of rows in table
     */
    public int getNumberOfRows() {
        pageUtils.waitForElementToAppear(numRowsText);
        int numRows = Integer.parseInt((numRowsText.getText()).substring(3));
        return numRows;
    }

    /**
     * Get number of rows per page
     *
     * @return numRowsPerPage   - Integer value number of rows per page
     */
    public int getRowsPerPage() {
        pageUtils.waitForElementToAppear(rowsPerPageText);
        String rowsPerPage = rowsPerPageText.getText();
        int numRowsPerPage = Integer.parseInt(rowsPerPage.substring(0, rowsPerPage.length() - 14));
        return numRowsPerPage;
    }

    /**
     * Click delete button
     *
     * @return new Schedule page object
     */
    public SchedulePage clickDeleteBtn() {
        pageUtils.waitForElementAndClick(deleteWorkflowButton);
        return new SchedulePage(driver);
    }

    /**
     * Click confirm delete button
     *
     * @return new Schedule page object
     */
    public SchedulePage clickConfirmDeleteBtn() {
        pageUtils.waitForElementAndClick(confirmDeleteBtn);
        return new SchedulePage(driver);
    }

    /**
     * Click refresh button
     *
     * @return new Schedule page object
     */
    public SchedulePage clickRefreshBtn() {
        pageUtils.waitForElementAndClick(getScheduleTabRefreshElement());
        return new SchedulePage(driver);
    }

    /**
     * Wait until paginator displays expected row count
     *
     * @param expectedRows - integer value expected rows
     */
    public void waitForExpectedRowCount(int expectedRows) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath(String.format(
            "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container']//div[contains(text(), 'of %s')]", expectedRows)))));
    }

    /**
     * Get delete confirmation text
     *
     * @return String - delete confirmation text
     */
    public String getDeleteConfirmationText() {
        pageUtils.waitForElementToAppear(confirmDeletionText);
        return confirmDeletionText.getText();
    }

    /**
     * Delete workflow
     *
     * @return String - boolean
     */
    public boolean deleteWorkFlow(String workflowName) {
        boolean isWorkflowDeleted = false;
        if (isWorkflowExists(workflowName)) {
            this.selectWorkflow(workflowName).clickDeleteButton().clickConfirmAlertBoxDelete();
            isWorkflowDeleted = true;
        } else {
            this.isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.ASCENDING, workFlowData.getWorkflowName());
            if (isWorkflowExists(workflowName)) {
                this.selectWorkflow(workflowName).clickDeleteButton().clickConfirmAlertBoxDelete();
                isWorkflowDeleted = true;
            }
        }
        return isWorkflowDeleted;
    }

    /**
     * wait until all the rows are loaded in standard mappings
     */
    @SneakyThrows
    private void waitUntilRowsLoaded() {
        int retries = 0;
        int maxRetries = 12;
        Exception ex = null;

        while (retries < maxRetries) {
            if (getScheduleWorkflowList().size() >= 1) {
                log.info("workflows are loaded!!");
                break;
            }
            TimeUnit.SECONDS.sleep(DEFAULT_WAIT_TIME);
            retries++;
            if (retries == maxRetries) {
                throw new RuntimeException(String.format("Workflows are not loaded !! : %s", ex.getMessage()));
            }
        }
    }

    /**
     * Getter for EDIT Workflow button to verify the state
     *
     * @return WebElement
     */
    public WebElement getEditWorkflowButton() {
        return editWorkflowButton.findElement(By.xpath(".."));
    }

    /**
     * Getter for DELETE Workflow button to verify the state
     *
     * @return WebElement
     */
    public WebElement getDeleteWorkflowButton() {
        return deleteWorkflowButton;
    }

    private WebElement getScheduleTabRefreshElement() {
        return driver.findElement(with(By.xpath("//span[@class='widget-button-text']")).toLeftOf(invokeWorkflowBtn));
    }

    /**
     * get the list of workflow rows
     *
     * @return list of workflow rows
     */
    public List<WebElement> getScheduleWorkflowList() {
        return driver.findElements(By.cssSelector("div[class='tabsv2-actual-tab-contents'][tab-number='1'] div[class$='cic-table'] table.obj"));
    }
}
