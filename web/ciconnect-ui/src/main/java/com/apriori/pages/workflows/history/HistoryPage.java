package com.apriori.pages.workflows.history;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.pages.CICBasePage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Constants;

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
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     *
     * @param workflowName
     * @return
     */
    public Boolean searchAndTrackWorkFlowStatus(String workflowName) {
        searchWorkflow(workflowName);
        return trackJobStatus(workflowName);
    }

    /**
     * Track the job status by workflow
     *
     * @param workflowName
     * @return boolean - true or false (true - Job is in finished state)
     */
    private Boolean trackJobStatus(String workflowName) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(15);
        List<String> jobStatusList = Arrays.asList(new String[]{"Finished", "Batch Costing Failed", "Errored", "Cancelled"});
        String finalJobStatus = StringUtils.EMPTY;
        WebElement tableRow;
        tableRow = tableUtils.findTableItemByName(historyJobListTable, workflowName, 1);
        finalJobStatus = tableUtils.getItemNameFromTable(tableRow, 6).getText();
        while (!jobStatusList.contains(finalJobStatus)) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                return false;
            }
            try {
                pageUtils.waitForElementAndClick(refreshButton);
                TimeUnit.SECONDS.sleep(30);
                finalJobStatus = tableUtils.getItemNameFromTable(tableUtils.findTableItemByName(historyJobListTable, workflowName, 1), 6).getText();
                log.info(String.format("WorkFlowName  >>%s<< ::: Job Status  >>%s<<", workflowName, finalJobStatus));
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
    public Boolean isScheduledWorkflowInvoked(String workflowName) {
        try {
            pageUtils.waitFor(150000);
            pageUtils.waitForElementToAppear(searchJobName);
            searchJobName.clear();
            searchJobName.sendKeys(workflowName);
            pageUtils.waitForElementAndClick(searchBtn);
            pageUtils.waitFor(Constants.DEFAULT_WAIT);

            return (tableUtils.getRowCount(historyJobListTable) > 0) ? true : false;

        } catch (StaleElementReferenceException staleElementReferenceException) {
            pageUtils.waitForElementAndClick(searchBtn);
            return (tableUtils.getRowCount(historyJobListTable) > 0) ? true : false;
        }
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
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
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
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
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
}
