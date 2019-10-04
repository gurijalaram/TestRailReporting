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
import reports.pages.homepage.HomePage;

public class LoginPage extends LoadableComponent<LoginPage> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static String loginPageURL = Constants.cirURL;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "a[name='password']")
    private WebElement password;

    @FindBy(css = "input[href='javascript:void(0)']")
    private WebElement forgotPassword;

    @FindBy(css = "button[type='submit'")
    private WebElement submitButton;

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

    /**
     * Enter email details
     *
     * @param emailAddress - user email address
     */
    private void enterEmail(String emailAddress){
        email.click();
        pageUtils.clearInput(email);
        email.sendKeys(emailAddress);
    }

    /**
     * Enter password
     *
     * @param password - user password
     */
    private void enterPassword(String password){
        this.password.click();
        pageUtils.clearInput(this.password);
        this.password.sendKeys(password);
    }

    /**
     * Single action to submit login credentials
     */
    private void submitLogin(){
        submitButton.click();
    }

    /**
     * Execute actions to login
     *
     * @param email - user email
     * @param password - user password
     */
    private void executeLogin(String email, String password){
        enterEmail(email);
        enterPassword(password);
        submitLogin();
    }

    /**
     * Login to CI Report
     *
     * @param email - user email
     * @param password - user password
     * @return new page object
     */
    public HomePage login(String email, String password){
        executeLogin(email, password);
        return new HomePage(driver);
    }
}
