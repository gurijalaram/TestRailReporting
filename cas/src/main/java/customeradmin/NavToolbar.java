package customeradmin;

import com.apriori.utils.PageUtils;

import login.CasLoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavToolbar extends LoadableComponent<NavToolbar> {

    private final Logger logger = LoggerFactory.getLogger(NavToolbar.class);

    @FindBy(css = "[alt='aPriori Logo']")
    private WebElement aprioriLogo;

    @FindBy(css = "[class='not-link active btn btn-link']")
    private WebElement customersButton;

    @FindBy(xpath = "//button[contains(@class,'btn-secondary')][.='Help']")
    private WebElement helpDropdown;

    @FindBy(xpath = "//button[contains(@class,'dropdown-item')][.='Help']")
    private WebElement helpLink;

    @FindBy(xpath = "//button[contains(@class,'dropdown-item')][.='About']")
    private WebElement aboutLink;

    @FindBy(xpath = "//div[contains(@class,'user-dropdown dropdown show')]")
    private WebElement userDropdown;

    @FindBy(xpath = "//button[.='My Profile']")
    private WebElement myProfileLink;

    @FindBy(xpath = "//button[.='Terms of Use']")
    private WebElement termsLink;

    @FindBy(xpath = "//button[.='Logout']")
    private WebElement logoutLink;

    private WebDriver driver;
    private PageUtils pageUtils;

    public NavToolbar(WebDriver driver) {
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
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(aprioriLogo);
        pageUtils.waitForElementAppear(customersButton);
    }

    /**
     * Logout the user
     * @return new page object
     */
    public CasLoginPage logout() {
        pageUtils.waitForElementAndClick(userDropdown);
        pageUtils.waitForElementAndClick(logoutLink);
        return new CasLoginPage(driver);
    }
}
