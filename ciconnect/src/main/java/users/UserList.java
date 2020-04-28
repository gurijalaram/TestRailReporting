package users;

import com.apriori.utils.PageUtils;
import header.GenericHeader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kpatel
 */
public class UserList extends GenericHeader {

    private final Logger logger = LoggerFactory.getLogger(UserList.class);

    @FindBy(css = "div.objbox tr")
    private List<WebElement> userListTable;

    @FindBy(css = "div.xhdr td:nth-of-type(1)")
    private WebElement usernameHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(2)")
    private WebElement emailHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(3)")
    private WebElement fullNameHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(4)")
    private WebElement departmentHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(5)")
    private WebElement locationHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(6)")
    private WebElement timezoneHeader;

    private WebDriver driver;
    private PageUtils pageUtils;

    public UserList(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(usernameHeader);
    }
}
