package utils;

import static org.junit.Assert.assertTrue;

import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CicLoginUtil extends TestBase {

    private AprioriLoginPage aprioriLoginPage;
    private WebDriver driver;
    private PageUtils pageUtils;

    @FindBy(css = "ul#root_menu-19 > li:nth-of-type(2)")
    private WebElement usersMenuBtn;

    public CicLoginUtil(WebDriver webdriver) {
        this.driver = webdriver;
        this.aprioriLoginPage = new AprioriLoginPage(driver, "ci-connect");
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Login to cid
     * @return new page object
     */
    public CicLoginUtil login(UserCredentials currentUser) {
        aprioriLoginPage.executeLogin(currentUser.getEmail(), currentUser.getPassword());
        return this;
    }

    public CicLoginUtil navigateToUserMenu() {
        assertTrue("CIC login page was not displayed", aprioriLoginPage.getLoginTitle().contains("Cost Insight Connect"));
        pageUtils.waitForElementAndClick(usersMenuBtn);
        return this;
    }

    public String getWebSession() {
        return driver.manage().getCookieNamed("JSESSIONID").getValue();
    }
}