package login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflows.GenericWorkflow;

public class LoginPage extends LoadableComponent<LoginPage> {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static String loginPageURL = Constants.cicURL;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    protected String url;

    public LoginPage(WebDriver driver) {
        init(driver, "", true);
    }

    public void init(WebDriver driver, String url, boolean loadNewPage) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        if (url == null || url.isEmpty()) {
            url = "https://" + loginPageURL;
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

    public GenericWorkflow login(WebDriver driver) {
        if (url == null || url.isEmpty()) {
            url = "https://" + Constants.CIC_USERNAME + ":" + Constants.CIC_PASSWORD + "@" + loginPageURL;
            driver.get(url);
        }

        return new GenericWorkflow(driver);
    }

    public GenericWorkflow login(String email, String userPassword) {
        executeLogin(email, userPassword);
        return new GenericWorkflow(driver);
    }

    public GenericWorkflow login() {
        UserCredentials userCredentials;

        if (Constants.PROP_USER_NAME != null && Constants.PROP_USER_PASSWORD != null) {
            userCredentials = new UserCredentials(Constants.PROP_USER_NAME, Constants.PROP_USER_PASSWORD);
        } else {
            userCredentials = UserUtil.getUser();
        }
        System.out.println(userCredentials.getUsername() + "," + userCredentials.getPassword());

        executeLogin(userCredentials.getUsername(), userCredentials.getPassword());
        return new GenericWorkflow(driver);
    }

    /**
     * Enter email address
     *
     * @param emailAddress - user email address
     */
    private void enterEmail(String emailAddress) {
        pageUtils.waitForElementToAppear(email);
        email.click();
        pageUtils.clearInput(email);
        email.sendKeys(emailAddress);
    }

    /**
     * Enter password
     *
     * @param userPassword - user password
     */
    private void enterPassword(String userPassword) {
        pageUtils.waitForElementToAppear(password);
        password.click();
        pageUtils.clearInput(password);
        password.sendKeys(userPassword);
    }

    /**
     * Submit login credentials
     */
    private void submitLogin() {
        pageUtils.waitForElementAndClick(submitButton);
    }

    /**
     * Execute login
     *
     * @param email        - user email
     * @param userPassword - user password
     * @return new page object
     */
    private GenericWorkflow executeLogin(String email, String userPassword) {
        enterEmail(email);
        enterPassword(userPassword);
        submitLogin();
        return new GenericWorkflow(driver);
    }

}