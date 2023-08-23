package com.apriori.pageobjects.login;

import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.PageUtils;
import com.apriori.login.LoginService;
import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.pageobjects.homepage.AdminHomePage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utils.Constants;

@Slf4j
public class AdminLoginPage extends AdminHeader {

    private WebDriver driver;
    private PageUtils pageUtils;
    private LoginService aprioriLoginService;
    private UserCredentials userCredentials = UserUtil.getUserOnPrem();

    public AdminLoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginService = new LoginService(driver, "admin");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertThat("CIA login page was not displayed", aprioriLoginService.getLoginTitle().contains(Constants.WELCOME_PAGE_TEXT));
    }

    /**
     * Login to CIA
     *
     * @return new page object
     */
    public AdminHomePage login() {
        return aprioriLoginService.login(userCredentials, AdminHomePage.class);
    }
}
