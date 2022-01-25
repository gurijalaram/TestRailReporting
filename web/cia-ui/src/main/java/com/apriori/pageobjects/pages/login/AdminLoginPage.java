package com.apriori.pageobjects.pages.login;

import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.pageobjects.pages.homepage.AdminHomePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class AdminLoginPage extends AdminHeader {

    private static final Logger logger = LoggerFactory.getLogger(AdminLoginPage.class);
    private static String loginPageURL = PropertiesContext.get("${env}.admin.ui_url");

    @FindBy(css = "input[name='username']")
    private WebElement emailInput;

    @FindBy(css = "input[name='password']")
    private WebElement passwordInput;

    @FindBy(css = "a[href='javascript:void(0)']")
    private WebElement forgotPassword;

    @FindBy(css = "button[id='login']")
    private WebElement loginButton;

    @FindBy(css = "span[class='animated fadeInUp']")
    private WebElement loginMsg;

    @FindBy(css = "div.auth0-lock-error-msg")
    private WebElement inputErrorMsg;

    @FindBy(css = "a[href='https://www.apriori.com/sso-instructions-page']")
    private WebElement helpButton;

    @FindBy(css = "a[href='https://www.apriori.com/privacy-policy']")
    private WebElement privacyPolicyButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private AprioriLoginPage aprioriLoginPage;
    private UserCredentials userCredentials = UserUtil.getUser();

    public AdminLoginPage(WebDriver driver) {
        super(driver);
        init(driver, "", true);
    }

    public AdminLoginPage(WebDriver driver, String url) {
        super(driver);
        init(driver, url, true);
    }

    public AdminLoginPage(WebDriver driver, boolean loadNewPage) {
        super(driver);
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
     * Login to CIA
     *
     * @param userCredentials - user credentials
     * @return new page object
     */
    public AdminHomePage login() {
        return aprioriLoginPage.login(userCredentials, AdminHomePage.class);
    }
}
