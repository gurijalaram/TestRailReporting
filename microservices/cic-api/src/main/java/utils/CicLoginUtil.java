package utils;

import static org.junit.Assert.assertTrue;

import com.apriori.utils.PageUtils;
import com.apriori.utils.login.LoginService;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CicLoginUtil extends TestBase {

    private LoginService aprioriLoginService;
    private WebDriver driver;
    private PageUtils pageUtils;

    @FindBy(css = "span[title='Users']")
    private WebElement usersMenuBtn;

    public CicLoginUtil(WebDriver webdriver) {
        this.driver = webdriver;
        this.aprioriLoginService = new LoginService(driver, "ci-connect");
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Login to cid
     * @return new page object
     */
    public CicLoginUtil login(UserCredentials currentUser) {
        aprioriLoginService.loginNoReturn(currentUser.getEmail(), currentUser.getPassword());
        return this;
    }

    public CicLoginUtil navigateToUserMenu() {
        pageUtils.waitForElementsToNotAppear(By.cssSelector(".data-loading"));
        pageUtils.waitForElementAndClick(usersMenuBtn);
        return this;
    }

    public String getWebSession() {
        return driver.manage().getCookieNamed("JSESSIONID").getValue();
    }
}