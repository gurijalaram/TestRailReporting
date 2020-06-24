package workflows;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public String getNewWorkflowBtnText(){
        return newWorkflowBtn.getText();
    }

    public NewEditWorkflow clickNewWorkflowBtn() {
        newWorkflowBtn.click();
        return new NewEditWorkflow(driver);
    }




}
