package com.apriori.pageobjects.pages.login;

import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.pageobjects.pages.homepage.AdminHomePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class AdminLoginPage extends AdminHeader {

    private WebDriver driver;
    private PageUtils pageUtils;
    private AprioriLoginPage aprioriLoginPage;
    private boolean isEnvOnPrem = PropertiesContext.get("${env}").equals("onprem");

    //private UserCredentials userCredentials = UserUtil.getUser();
    private UserCredentials userCredentials = UserUtil.getUserOnPrem();

    public AdminLoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "admin");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertThat("CIA login page was not displayed", aprioriLoginPage.getLoginTitle(true).contains("Cost Insight | Admin"));
    }

    /**
     * Login to CIA
     *
     * @return new page object
     */
    public AdminHomePage login() {
        return aprioriLoginPage.login(userCredentials, AdminHomePage.class);
    }
}