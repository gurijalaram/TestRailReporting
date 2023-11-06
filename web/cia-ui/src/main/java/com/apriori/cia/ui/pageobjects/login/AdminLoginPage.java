package com.apriori.cia.ui.pageobjects.login;

import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cia.ui.pageobjects.header.AdminHeader;
import com.apriori.cia.ui.pageobjects.homepage.AdminHomePage;
import com.apriori.cia.ui.utils.Constants;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.web.app.util.PageUtils;
import com.apriori.web.app.util.login.LoginService;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

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
        assertThat("CIA login page was not displayed", aprioriLoginService.getLoginTitle().contains(Constants.WELCOME_PAGE_TEXT_ADMIN));
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
