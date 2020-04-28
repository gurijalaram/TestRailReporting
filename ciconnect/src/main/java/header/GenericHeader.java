package header;

import com.apriori.utils.PageUtils;
import connectors.ConnectorList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import users.UserList;
import workflows.Schedule;

/**
 * @author kpatel
 */
public class GenericHeader extends PageHeader {

    private static final Logger logger = LoggerFactory.getLogger(GenericHeader.class);

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(1)")
    private WebElement workflowsMenuBtn;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(2)")
    private WebElement usersMenuBtn;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(3)")
    private WebElement connectorsMenuBtn;

    private WebDriver driver;
    private PageUtils pageUtils;

    public GenericHeader(WebDriver driver) {
        super(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
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