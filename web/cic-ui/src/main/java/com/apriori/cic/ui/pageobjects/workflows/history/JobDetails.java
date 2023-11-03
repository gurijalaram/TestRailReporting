package com.apriori.cic.ui.pageobjects.workflows.history;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.ui.enums.JobDetailsListHeaders;
import com.apriori.cic.ui.pageobjects.CICBasePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Navigate to job details page from view history page
 * #### This class will be used in future when there are test cases like to view details of workflow job.####
 */
@Slf4j
public class JobDetails extends CICBasePage {

    @FindBy(xpath = "//div/span[.='Job Details']")
    private WebElement jobDetailsTitleElement;

    @FindBy(xpath = "//div[contains(@id, 'popup_container')]//span[.='Components Processed:']")
    private WebElement componentProcessedLbl;

    @FindBy(xpath = "//span[@class='label-text textsize-xlarge']")
    private WebElement fieldLblElement;

    @FindBy(css = "div[id*='popup_container'] div[class$='cic-table'] table.obj")
    private WebElement jobDetailsListTable;

    //div[@class='dhtmlxMenu_material_SubLevelArea_Polygon ']//table//tr

    @FindBy(css = "div[class='dhtmlxMenu_material_SubLevelArea_Polygon '] table")
    private WebElement jobDetailsHeadersPopupTable;

    @FindBy(css = "div[id*='popup_container'] div[class$='cic-table'] table.hdr")
    private WebElement jobDetailsListHeadersTable;

    @FindBy(xpath = "//div[contains(@id, 'popup_container')]//span[.='Export']")
    private WebElement exportBtn;

    @FindBy(css = "div[class='widget-content widget-button singleImageOnly iconOnly'] button")
    private WebElement jobDetailsCloseButton;

    private WebElement selectedPartRow;

    public JobDetails(WebDriver driver) {
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
        pageUtils.waitForElementAppear(jobDetailsTitleElement);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
    }

    /**
     * get export button web element
     *
     * @return WebElement
     */
    public WebElement getExportBtn() {
        return exportBtn;
    }

    /**
     * get StartedAT webelement
     *
     * @return WebElement
     */
    public WebElement getStartedAtElement() {
        return driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']"))
            .below(driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']"))
                .toRightOf(By.xpath("//div[contains(@id, 'popup_container')]//span[.='Workflow Name:']")))));
    }

    /**
     * get ID element
     *
     * @return WebElement
     */
    public WebElement getIdElement() {
        return driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']")).toLeftOf(By.xpath("//div[contains(@id, 'popup_container')]//span[.='Job Status:']")));
    }

    /**
     * get workflow name element
     *
     * @return WebElement
     */
    public WebElement getWorkflowNameElement() {
        return driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']")).toRightOf(By.xpath("//div[contains(@id, 'popup_container')]//span[.='Workflow Name:']")));
    }

    /**
     * getComponentProcessedElement
     *
     * @return WebElement
     */
    public WebElement getComponentProcessedElement() {
        return driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']"))
            .toRightOf(By.xpath("//div[contains(@id, 'popup_container')]//span[.='Components Processed:']")));
    }

    /**
     * getComponentFailed Element
     *
     * @return webElement
     */
    public WebElement getComponentFailedElement() {
        return driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']"))
            .below(driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']"))
                .toRightOf(By.xpath("//div[contains(@id, 'popup_container')]//span[.='Components Processed:']")))));
    }

    /**
     * get Job Status Element
     *
     * @return WebElement
     */
    public WebElement getJobStatusElement() {
        return driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']"))
            .above(driver.findElement(with(By.xpath("//span[@class='label-text textsize-xlarge']"))
                .toRightOf(By.xpath("//div[contains(@id, 'popup_container')]//span[.='Components Processed:']")))));
    }

    /**
     * check for header displaying job details columns list
     *
     * @param jobDetailsListHeaders - JobDetailsListHeaders enum
     * @return Boolean
     */
    public Boolean isHeaderExists(JobDetailsListHeaders jobDetailsListHeaders) {
        return (tableUtils.getColumnHeader(jobDetailsListHeadersTable, jobDetailsListHeaders.getColumnName()) == null) ? false : true;
    }

    /**
     * get matching value from Job Details table
     *
     * @param headerName column name
     * @return string from matching row and above column
     */
    public String getPartItemValue(JobDetailsListHeaders headerName) {
        Integer columnIndex = tableUtils.getColumnIndx(jobDetailsListHeadersTable, headerName.getColumnName());
        return selectedPartRow.findElements(By.tagName("td")).get(columnIndex).getText();
    }

    /**
     * Select and get the matching value in specific column
     *
     * @param rowValue   - cellValue
     * @param headerName - Column
     * @return current class object
     */
    public JobDetails selectPartRow(String rowValue, JobDetailsListHeaders headerName) {
        Integer columnIndex = tableUtils.getColumnIndx(jobDetailsListHeadersTable, headerName.getColumnName());
        selectedPartRow = tableUtils.getRowByColumnIndex(jobDetailsListTable, rowValue, columnIndex);
        if (selectedPartRow == null) {
            throw new RuntimeException("PART DETAILS NOT FOUND IN JOB DETAILS LIST!!!");
        }
        return this;
    }

    /**
     * right click on job details column header table
     *
     * @return current class object
     */
    public JobDetails rightClickOnHeader() {
        pageUtils.rightClick(jobDetailsListHeadersTable);
        return this;
    }

    /**
     * Select column name in pop up list
     *
     * @param headerName - column name
     * @return current class object
     */
    public JobDetails clickHeaderCheckboxFromPopList(JobDetailsListHeaders headerName) {
        pageUtils.waitForElementAppear(jobDetailsHeadersPopupTable);
        WebElement element = tableUtils.findTableItemByName(jobDetailsHeadersPopupTable, headerName.getColumnName(), 1);
        if (element == null) {
            throw new RuntimeException("View Job Details pop up did not displayed!!");
        }
        pageUtils.waitForElementAndClick(element);
        pageUtils.waitForElementAndClick(getStartedAtElement());
        return this;
    }

    /**
     * Click the View Details button
     *
     * @return JobDetails
     */
    public HistoryPage clickCloseButton() {
        pageUtils.waitForElementAndClick(jobDetailsCloseButton);
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        return new HistoryPage(driver);
    }
}
