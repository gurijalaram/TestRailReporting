package workflows;

import com.apriori.utils.PageUtils;

import cicuserguide.CicUserGuide;
import connectors.ConnectorList;
import header.PageHeader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import users.UserList;

public class GenericWorkflow extends LoadableComponent<GenericWorkflow> {

    private final Logger logger = LoggerFactory.getLogger(GenericWorkflow.class);

    @FindBy(xpath = "//div[contains(@class,'tabsv2-tab') and contains(@tab-number,'1')]")
    private WebElement scheduleTab;

    @FindBy(xpath = "//div[contains(@class,'tabsv2-tab') and contains(@tab-number,'2')]")
    private WebElement scheduleHistoryTab;

    @FindBy(css = "div#root_pagemashupcontainer-1_label-18-bounding-box span")
    private WebElement workflowLabel;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PageHeader pageHeader;

    public GenericWorkflow(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.pageHeader = new PageHeader(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(scheduleTab);
        pageUtils.waitForElementToAppear(workflowLabel);
    }

    public UserList clickUsersMenu(){
        return pageHeader.clickUsersMenu();
    }

    public ConnectorList clickConnectorsMenu(){
        return pageHeader.clickConnectorsMenu();
    }

    /**
     * clicks on schedule tab
     * @return new Schedule page
     */
    public Schedule clickScheduleTab() {
        scheduleTab.click();
        return new Schedule(driver);
    }

    /**
     * clicks on view history tab
     * @return new History page
     */
    public History clickScheduleHistoryTab() {
        scheduleHistoryTab.click();
        return new History(driver);
    }

    public String getWorkflowText() {
        return workflowLabel.getText();
    }

    public PageHeader expandUserInfoDropdown() {
        pageHeader.expandUserInfoDropdown();
        return new PageHeader(driver);
    }

    public CicUserGuide navigateToCicUserGuide() {
        pageHeader.navigateToCicUserGuide();
        return new CicUserGuide(driver);
    }

}
