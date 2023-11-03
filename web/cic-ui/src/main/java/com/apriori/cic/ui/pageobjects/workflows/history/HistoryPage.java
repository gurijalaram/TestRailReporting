package com.apriori.cic.ui.pageobjects.workflows.history;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.ui.enums.ViewHistoryListHeaders;
import com.apriori.cic.ui.pageobjects.CICBasePage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * iew History Page
 */
@Slf4j
public class HistoryPage extends CICBasePage {

    @FindBy(css = "div#root_pagemashupcontainer-1_textbox-76 input")
    private WebElement searchJobName;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-81 button")
    private WebElement searchBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_dataexport-99 button")
    private WebElement exportBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-62 button")
    private WebElement refreshButton;

    @FindBy(css = "div[tab-number='2'] div[class$='cic-table'] table.obj")
    private WebElement historyJobListTable;

    @FindBy(css = "div[tab-number='2'] div[class$='cic-table'] table.hdr")
    private WebElement historyJobListHeaders;

    @FindBy(xpath = "//button[.='View Details']")
    private WebElement viewDetailsButton;

    @FindBy(xpath = "//div/span[.='Job Details']")
    private WebElement jobDetailsPopTitle;

    public HistoryPage(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"), 5);
        waitUntilRowsLoaded();
        pageUtils.waitForElementToAppear(searchJobName);
        pageUtils.waitForElementToAppear(searchBtn);
    }

    /**
     * Search work flow in History page and track the job status
     *
     * @param workflowName
     * @return
     */
    public Boolean searchAndTrackWorkFlowStatus(String workflowName) {
        searchWorkflow(workflowName);
        return trackJobStatus(workflowName);
    }

    /**
     * Search work flow in History page and track the job status
     *
     * @param workflowName
     * @return
     */
    public HistoryPage searchAndSelectWorkflowByID(String workflowName) {
        searchWorkflow(workflowName);
        selectWorkflow(workflowName);
        return this;
    }

    /**
     * Track the job status by workflow
     *
     * @param workflowName
     * @return boolean - true or false (true - Job is in finished state)
     */
    private Boolean trackJobStatus(String workflowName) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(15);
        List<String> jobStatusList = Arrays.asList(new String[]{"Finished", "Failed", "Errored", "Cancelled"});
        String finalJobStatus = StringUtils.EMPTY;
        WebElement tableRow;
        tableRow = tableUtils.findTableItemByName(historyJobListTable, workflowName, tableUtils.getColumnIndx(historyJobListHeaders, ViewHistoryListHeaders.WORKFLOW_NAME.getColumnName()));
        finalJobStatus = tableUtils.getItemNameFromTable(tableRow, tableUtils.getColumnIndx(historyJobListHeaders, ViewHistoryListHeaders.JOB_STATUS.getColumnName()))
            .getText();
        while (!jobStatusList.contains(finalJobStatus)) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                return false;
            }
            try {
                pageUtils.waitForElementAndClick(refreshButton);
                TimeUnit.SECONDS.sleep(WAIT_TIME);
                finalJobStatus = tableUtils.getItemNameFromTable(tableUtils.findTableItemByName(historyJobListTable, workflowName,
                            tableUtils.getColumnIndx(historyJobListHeaders, ViewHistoryListHeaders.WORKFLOW_NAME.getColumnName())),
                        tableUtils.getColumnIndx(historyJobListHeaders, ViewHistoryListHeaders.JOB_STATUS.getColumnName()))
                    .getText();
                log.debug(String.format("WorkFlowName  >>%s<< ::: Job Status  >>%s<<", workflowName, finalJobStatus));
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }

    /**
     * Check if a workflow exists in the Schedule Workflow list. The search is by workflow name
     *
     * @param workflowName Workflow name to check for
     * @return True if the workflow exists
     */
    @SneakyThrows
    public Boolean isScheduledWorkflowInvoked(String workflowName) {
        Boolean isWorkflowInvoked = false;
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(3);
        try {
            pageUtils.waitForElementToAppear(searchJobName);
            searchJobName.clear();
            searchJobName.sendKeys(workflowName);
            pageUtils.waitForElementAndClick(searchBtn);
            pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
            isWorkflowInvoked = (tableUtils.getRowCount(historyJobListTable) > 0) ? true : false;
            while (!isWorkflowInvoked) {
                if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                    return isWorkflowInvoked;
                }
                TimeUnit.SECONDS.sleep(WAIT_TIME);
                pageUtils.waitForElementAndClick(searchBtn);
                pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
                isWorkflowInvoked = (tableUtils.getRowCount(historyJobListTable) > 0) ? true : false;
            }
        } catch (StaleElementReferenceException staleElementReferenceException) {
            pageUtils.waitForElementAndClick(searchBtn);
            isWorkflowInvoked = (tableUtils.getRowCount(historyJobListTable) > 0) ? true : false;
        }
        return isWorkflowInvoked;
    }

    /**
     * Search work flow in History page and track the job status
     *
     * @param workflowName
     * @return
     */
    @SneakyThrows
    public HistoryPage searchWorkflow(String workflowName) {
        pageUtils.waitForElementToAppear(searchJobName);
        pageUtils.clearValueOfElement(searchJobName);
        searchJobName.sendKeys(workflowName);
        pageUtils.waitForElementAndClick(searchBtn);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"), 5);
        pageUtils.waitForElementToAppear(historyJobListTable);
        pageUtils.waitForJavascriptLoadComplete();
        tableUtils.waitForSteadinessOfElement(historyJobListTable, workflowName, 1);
        return this;
    }

    /**
     * Click the Cancel button
     */
    public HistoryPage clickCancelButton() {
        pageUtils.waitForElementAndClick(getCancelButtonElement());
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"), 5);
        new ModalDialog(driver).clickConfirmButton();
        return this;
    }

    /**
     * verify Cancel Button element in workflow history tab
     *
     * @return WebElement
     */
    public Boolean isCancelButtonEnabled() {
        return pageUtils.isElementEnabled(getCancelButtonElement());
    }

    /**
     * get workflow status details from history table list
     *
     * @return String
     */
    public String getWorkflowStatusDetails(String workflowName) {
        WebElement tableRow = tableUtils.findTableItemByName(historyJobListTable, workflowName, 1);
        return tableUtils.getItemNameFromTable(tableRow, 7).getText();
    }

    /**
     * get Cancel Button element in workflow history tab
     *
     * @return WebElement
     */
    private WebElement getCancelButtonElement() {
        return driver.findElement(with(By.xpath("//button[.='Cancel']")).toRightOf(By.xpath("//button/span[text()='Export']")));
    }

    /**
     * Get worflow started at date from history
     *
     * @param workflowName workflowName
     * @return String
     */
    public String getWorkflowStartedAt(String workflowName) {
        WebElement tableRow = tableUtils.findTableItemByName(historyJobListTable, workflowName, tableUtils.getColumnIndx(historyJobListHeaders, ViewHistoryListHeaders.WORKFLOW_NAME.getColumnName()));
        return tableUtils.getItemNameFromTable(tableRow, tableUtils.getColumnIndx(historyJobListHeaders, ViewHistoryListHeaders.STARTED_AT.getColumnName())).getText();
    }

    /**
     * Click the View Details button
     *
     * @return JobDetails
     */
    public JobDetails clickViewDetailsButton() {
        pageUtils.waitForElementAndClick(viewDetailsButton);
        pageUtils.waitForElementAppear(jobDetailsPopTitle);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"), 5);
        return new JobDetails(driver);
    }

    /**
     * Select workflow in table
     *
     * @param workflowName - name of workflow to select
     * @return new Schedule page object
     */
    public HistoryPage selectWorkflow(String workflowName) {
        pageUtils.waitForElementAppear(historyJobListTable);
        tableUtils.findTableItemByName(historyJobListTable, workflowName, tableUtils.getColumnIndx(historyJobListHeaders, ViewHistoryListHeaders.WORKFLOW_NAME.getColumnName())).click();
        return this;
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
            if (getHistoryWorkflowList().size() >= 1) {
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
     * get the list of workflow rows
     *
     * @return list of workflow rows
     */
    public List<WebElement> getHistoryWorkflowList() {
        return driver.findElements(By.cssSelector("div[class='tabsv2-actual-tab-contents'][tab-number='2'] div[class$='cic-table'] table.obj"));
    }
}
