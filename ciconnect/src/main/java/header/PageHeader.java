package header;

import com.apriori.utils.PageUtils;

import connectors.ConnectorList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import users.UserList;
import workflows.Schedule;

/**
 * @author kpatel
 */
public class PageHeader extends LoadableComponent<PageHeader> {

    private static final Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @FindBy(css = "div#root_button-33 > button")
    private WebElement settingsBtn;

    @FindBy(css = "div#root_button-29 > button")
    private WebElement helpBtn;

    @FindBy(css = "div#root_button-30 > button")
    private WebElement userInfoDropdown;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(1)")
    private WebElement workflowsMenuBtn;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(2)")
    private WebElement usersMenuBtn;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(3)")
    private WebElement connectorsMenuBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_label-18-bounding-box span")
    private WebElement workflowLabel;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PageHeader(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(settingsBtn);
        pageUtils.waitForElementToAppear(userInfoDropdown);
    }

    /**
     * click Workflows tab
     * @return new generic schedule page object from workflow tab
     */
    public Schedule clickWorkflowMenu() {
        workflowsMenuBtn.click();
        return new Schedule(driver);
    }

    /**
     * click Users Tab
     * @return new user list page object from Users tab
     */
    public UserList clickUsersMenu() {
        usersMenuBtn.click();
        return new UserList(driver);
    }

    /**
     * click Connectors tab
     * @return new connectors list page object from Connectors Tab
     */
    public ConnectorList clickConnectorsMenu() {
        connectorsMenuBtn.click();
        return new ConnectorList(driver);
    }
}