package com.apriori.pages.workflows.history;

import com.apriori.enums.WorkflowJobState;
import com.apriori.pages.CICBasePage;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.concurrent.TimeUnit;

/**
 * iew History Page
 */
public class HistoryPage extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(HistoryPage.class);

    @FindBy(css = "div#root_pagemashupcontainer-1_textbox-76 input")
    private WebElement searchJobName;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-81 button")
    private WebElement searchBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_dataexport-99 button")
    private WebElement exportBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-92 button")
    private WebElement viewDetailsBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-62 button")
    private WebElement refreshButton;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_gridadvanced-85-grid-advanced'] > div.objbox > table")
    private WebElement historyJobListTable;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_gridadvanced-85-grid-advanced'] > div.xhdr > table")
    private WebElement historyJobListHeader;

    public HistoryPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(searchJobName);
        pageUtils.waitForElementToAppear(searchBtn);
    }

    /**
     * Search work flow in History page and track the job status
     * @param workflowName
     * @return
     */
    public Boolean searchAndTrackWorkFlowStatus(String workflowName) {
        try {
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            pageUtils.waitForElementToAppear(searchJobName);
            searchJobName.sendKeys(workflowName);
            pageUtils.waitForElementAndClick(searchBtn);
            pageUtils.waitFor(Constants.DEFAULT_WAIT);
            tableUtils.selectRowByName(historyJobListTable, workflowName, 1);

        } catch (StaleElementReferenceException staleElementReferenceException) {
            pageUtils.waitForElementAndClick(searchBtn);
            tableUtils.selectRowByName(historyJobListTable, workflowName, 1);
        }

        return trackJobStatus(workflowName);
    }

    /**
     * Track the job status by workflow
     * @param workflowName
     * @return boolean - true or false (true - Job is in finished state)
     */
    private Boolean trackJobStatus(String workflowName) {
        long initialTime = System.currentTimeMillis() / 1000;
        String jobStatus = StringUtils.EMPTY;
        do {
            WebElement tableRow = tableUtils.findTableItemByName(historyJobListTable, workflowName, 1);
            jobStatus = tableUtils.getItemNameFromTable(tableRow, 6).getText();
            logger.info(String.format("WorkFlowName  >>%s<< ::: Job Status  >>%s<<", workflowName, jobStatus));
            try {
                pageUtils.waitForElementAndClick(refreshButton);
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!jobStatus.equals(WorkflowJobState.FINISHED.getJobState())
            || jobStatus.equals(WorkflowJobState.BATCH_COSTING_FAILED.getJobState())
            || jobStatus.equals(WorkflowJobState.CANCELLED.getJobState())
            || jobStatus.equals(WorkflowJobState.ERRORED.getJobState())
            || jobStatus.equals(WorkflowJobState.QUERY_IN_PROGRESS.getJobState())
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return (jobStatus.equals(WorkflowJobState.FINISHED.getJobState())) ? true : false;
    }
}
