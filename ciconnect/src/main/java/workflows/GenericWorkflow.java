package workflows;

import com.apriori.utils.PageUtils;

import header.GenericHeader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericWorkflow extends GenericHeader {

    private final Logger logger = LoggerFactory.getLogger(GenericWorkflow.class);

    @FindBy(xpath = "//div[contains(@class,'tabsv2-tab') and contains(@tab-number,'1')]")
    private WebElement scheduleTab;

    @FindBy(xpath = "//div[contains(@class,'tabsv2-tab') and contains(@tab-number,'2')]")
    private WebElement scheduleHistoryTab;

    @FindBy(css = "div#root_pagemashupcontainer-1_label-18-bounding-box span")
    private WebElement workflowLabel;

    private WebDriver driver;
    private PageUtils pageUtils;

    public GenericWorkflow(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(scheduleTab);
    }

    /**
     * clicks on schedule tab
     * @return
     */
    public Schedule clickScheduleTab() {
        scheduleTab.click();
        return new Schedule(driver);
    }

    /**
     * clicks on view history tab
     * @return
     */
    public History clickScheduleHistoryTab() {
        scheduleHistoryTab.click();
        return new History(driver);
    }

    public String getWorkflowText() {

        return workflowLabel.getText();
    }
}
