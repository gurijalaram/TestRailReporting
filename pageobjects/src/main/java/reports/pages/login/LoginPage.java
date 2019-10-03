package reports.pages.login;

import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.constants.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends LoadableComponent<LoginPage> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static String loginPageURL = Constants.cirURL;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    private WebDriver driver;
    private PageUtils pageUtils;

    public LoginPage(WebDriver driver) {
        init(driver, "", true);
    }

    public LoginPage(WebDriver driver, String url) {
        init(driver, url, true);
    }

    public LoginPage(WebDriver driver, boolean loadNewPage) {
        init(driver, "", loadNewPage);
    }

    public void init(WebDriver driver, String url, boolean loadNewPage) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        if (url == null || url.isEmpty()) {
            url = loginPageURL;
        }
        if (loadNewPage) {
            driver.get(url);
        }
        logger.info("CURRENTLY ON INSTANCE: " + url);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }
}
