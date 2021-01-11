package com.apriori.workflows;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kpatel
 */
public class Schedule extends LoadableComponent<Schedule> {

    private final Logger LOGGER = LoggerFactory.getLogger(Schedule.class);

    @FindBy(xpath = "//span[.='New']")
    private WebElement newWorkflowBtn;

    @FindBy(xpath = "//span[.='Edit']")
    private WebElement editWorkflow;

    @FindBy(xpath = "//span[.='Delete']")
    private WebElement deleteWorkflow;

    @FindBy(xpath = "//span[.='Invoke']")
    private WebElement invokeWorkflow;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-38 > button")
    private WebElement refreshScheduleList;

    @FindBy(css = "div.objbox tr")
    private List<WebElement> listOfSchedule;

    @FindBy(css = "div.xhdr tr:nth-of-type(1)")
    private WebElement scheduleName;

    @FindBy(css = "div.xhdr tr:nth-of-type(2)")
    private WebElement scheduleDescription;

    @FindBy(css = "div.xhdr tr:nth-of-type(3)")
    private WebElement scheduleLastModifiedBy;

    @FindBy(css = "div.xhdr tr:nth-of-type(4)")
    private WebElement scheduleCron;

    @FindBy(css = "div.xhdr tr:nth-of-type(5)")
    private WebElement scheduleConnectorType;

    @FindBy(css = "div.xhdr tr:nth-of-type(6)")
    private WebElement scheduleEnabled;

    @FindBy(css = "div.xhdr tr:nth-of-type(1)")
    private WebElement scheduleLocked;

    @FindBy(xpath = "//div[contains(@class,'tabsv2-tab') and contains(@tab-number,'1')]")
    private WebElement scheduleTab;

    @FindBy(css = "img[src='../Common/thingworx/widgets/gridadvanced/imgs/dhxgrid_material/ar_right_dis.gif']")
    private WebElement disabledNextBtn;

    @FindBy(css = "img[src='../Common/thingworx/widgets/gridadvanced/imgs/dhxgrid_material/ar_right.gif']")
    private WebElement enabledNextBtn;

    @FindBy(xpath = "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-bottom-container']//img[contains(@src, 'ar_right') and not(contains(@src, 'abs'))]")
    private WebElement nextBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-of-type(4)")
    private WebElement numRowsText;

    @FindBy(xpath = "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container']//div[contains(text(), ' rows per page')]")
    private WebElement rowsPerPageText;

    @FindBy(css = "div#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-of-type(1)")
    private WebElement firstPageBtn;

    @FindBy(css = "div#confirmButtons > a > span")
    private WebElement confirmDeleteBtn;

    @FindBy(css = "div#confirmBox > div:nth-of-type(2) > p")
    private WebElement confirmDeletionText;

    private WebDriver driver;
    private PageUtils pageUtils;

    public Schedule(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(newWorkflowBtn);
        pageUtils.waitForElementToAppear(scheduleTab);
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
    public NewEditWorkflow clickNewWorkflowBtn() {
        newWorkflowBtn.click();
        return new NewEditWorkflow(driver);
    }

    /**
     * Click workflow with specified name on current page of table
     *
     * @param workflowName - name of workflow to click
     */
    public void clickWorkflowIfOnPage(String workflowName) {
        List<WebElement> workflowNamesElements = driver.findElements(By.cssSelector(
            "div[id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced'] > div:nth-of-type(2) > table > tbody > tr > td:nth-of-type(1)"));
        for (WebElement workflowNamesElement : workflowNamesElements) {
            if (workflowNamesElement.getText().equals(workflowName)) {
                pageUtils.waitForElementAndClick(pageUtils.scrollWithJavaScript(driver.findElement(By.xpath(String.format(
                    "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced']//td[.='%s']", workflowName))), true));
            }
        }
    }


    /**
     * Select workflow in table
     *
     * @param workflowName - name of workflow to select
     * @return new Schedule page object
     */
    public Schedule selectWorkflow(String workflowName) {
        String nextBtnImgSrc = nextBtn.getAttribute("src");

        clickWorkflowIfOnPage(workflowName);

        while (!nextBtnImgSrc.contains("dis")) {
            pageUtils.waitForElementAndClick(nextBtn);

            clickWorkflowIfOnPage(workflowName);
            nextBtnImgSrc = nextBtn.getAttribute("src");
        }
        return new Schedule(driver);
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
    public Schedule clickDeleteBtn() {
        pageUtils.waitForElementAndClick(deleteWorkflow);
        return new Schedule(driver);
    }

    /**
     * Click confirm delete button
     *
     * @return new Schedule page object
     */
    public Schedule clickConfirmDeleteBtn() {
        pageUtils.waitForElementAndClick(confirmDeleteBtn);
        return new Schedule(driver);
    }

    /**
     * Click refresh button
     *
     * @return new Schedule page object
     */
    public Schedule clickRefreshBtn() {
        pageUtils.waitForElementAndClick(refreshScheduleList);
        return new Schedule(driver);
    }

    /**
     * Wait until paginator displays expected row count
     *
     * @param expectedRows - integer value expected rows
     */
    public void waitForExpectedRowCount(int expectedRows) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath(String.format(
            "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container']//div[contains(text(), 'of %s')]", expectedRows)))));
    }

    /**
     * Check whether a workflow is present in Schedule list
     *
     * @param workflowName - name of workflow to check for
     * @return - boolean
     */
    public boolean isWorkflowInTable(String workflowName) {
        String nextBtnImgSrc = nextBtn.getAttribute("src");

        List<WebElement> workflowNamesElements = driver.findElements(By.cssSelector(
            "div[id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced'] > div:nth-of-type(2) > table > tbody > tr > td:nth-of-type(1)"));
        for (WebElement workflowNamesElement : workflowNamesElements) {
            if (workflowNamesElement.getText().equals(workflowName)) {
                return true;
            }
        }

        while (!nextBtnImgSrc.contains("dis")) {
            pageUtils.waitForElementAndClick(nextBtn);

            workflowNamesElements = driver.findElements(By.cssSelector(
                "div[id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced'] > div:nth-of-type(2) > table > tbody > tr > td:nth-of-type(1)"));
            for (WebElement workflowNamesElement : workflowNamesElements) {
                if (workflowNamesElement.getText().equals(workflowName)) {
                    return true;
                }
            }
            nextBtnImgSrc = nextBtn.getAttribute("src");
        }
        return false;
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
}
