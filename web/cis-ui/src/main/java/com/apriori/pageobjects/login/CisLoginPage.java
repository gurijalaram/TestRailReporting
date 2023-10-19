package com.apriori.pageobjects.login;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.PageUtils;
import com.apriori.login.LoginService;
import com.apriori.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CisLoginPage extends LoadableComponent<CisLoginPage> {

    @FindBy(css = ".auth0-lock-header-logo")
    private WebElement aprioriLogo;

    private WebDriver driver;
    private PageUtils pageUtils;
    private LoginService aprioriLoginService;

    public CisLoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginService = new LoginService(driver, "cis");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(aprioriLoginService.getLoginTitle().contains("aP Workspace"), "CIS login page was not displayed");
    }

    /**
     * Login to CIS
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public  <T> T login(UserCredentials userCredentials, Class<T> klass) {
        return aprioriLoginService.login(userCredentials, klass);
    }



    /**
     * Login to CIS New Application
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public LeftHandNavigationBar cisLogin(UserCredentials userCredentials) {
        return aprioriLoginService.login(userCredentials, LeftHandNavigationBar.class);
    }

    /**
     * Checks if apriori logo displayed
     *
     * @return true/false
     */
    public boolean isLogoDisplayed() {
        return pageUtils.waitForElementToAppear(aprioriLogo).isDisplayed();
    }
}