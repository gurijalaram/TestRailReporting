package workflows;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kpatel
 */
public class Schedule extends LoadableComponent<Schedule> {

    private final Logger logger = LoggerFactory.getLogger(Schedule.class);

    @FindBy(css = "div#root_pagemashupcontainer-1_button-35 > button")
    private WebElement newWorkflowBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-36 > button")
    private WebElement editWorkflow;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-37 > button")
    private WebElement deleteWorkflow;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-38 > button")
    private WebElement invokeWorkflow;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-97 > button")
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

    @FindBy(css = "div#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-of-type(4)")
    private WebElement numRowsText;

    @FindBy(xpath = "//div[@id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container']//div[contains(text(), ' rows per page')]")
    private WebElement rowsPerPageText;

    @FindBy(css = "div#root_pagemashupcontainer-1_gridadvanced-46-grid-advanced-paging-container > div > div:nth-of-type(1)")
    private WebElement firstPageBtn;

    private WebDriver driver;
    private PageUtils pageUtils;

    public Schedule(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * Select workflow in table
     *
     * @param workflowName  - name of workflow to select
     * @return new Schedule page object
     */
    public Schedule selectWorkflow(String workflowName) {
        ArrayList<String> workflowNames = new ArrayList<String>();
        int numRows = getNumberOfRows();
        int rowsPerPage = getRowsPerPage();
        int nextClicksAllPages = numRows / rowsPerPage;

        for (int i = 0; i < nextClicksAllPages; i++) {
            List<WebElement> workflowNamesElements = driver.findElements(By.cssSelector(
                "div[id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced'] > div:nth-of-type(2) > table > tbody > tr > td:nth-of-type(1)"));
            for (WebElement workflowNamesElement : workflowNamesElements) {
                workflowNames.add(workflowNamesElement.getText());
            }
            pageUtils.waitForElementAndClick(enabledNextBtn);
        }

        int workflowIndex = workflowNames.indexOf(workflowName);
        int nextClicks = workflowIndex / rowsPerPage;

        pageUtils.waitForElementAndClick(firstPageBtn);

        for (int i = 0; i < nextClicks; i++) {
            pageUtils.waitForElementAndClick(enabledNextBtn);
        }

        int rowOnPage = workflowIndex % rowsPerPage;
        WebElement elementToClick = driver.findElement(By.cssSelector(String.format(
            "div[id='root_pagemashupcontainer-1_gridadvanced-46-grid-advanced'] > div:nth-of-type(2) > table > tbody > tr:nth-of-type(%s) > td:nth-of-type(1)", rowOnPage)));

        pageUtils.waitForElementAndClick(elementToClick);

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
     * @return numRowsPerPage   -
     */
    public int getRowsPerPage() {
        pageUtils.waitForElementToAppear(rowsPerPageText);
        String rowsPerPage = rowsPerPageText.getText();
        int numRowsPerPage = Integer.parseInt(rowsPerPage.substring(0, rowsPerPage.length() - 14));
        return numRowsPerPage;
    }

    public Schedule clickDeleteBtn() {
        pageUtils.waitForElementAndClick(deleteWorkflow);
        return new Schedule(driver);
    }
}
