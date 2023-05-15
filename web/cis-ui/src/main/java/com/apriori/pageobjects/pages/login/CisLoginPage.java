package com.apriori.pageobjects.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.reader.file.user.UserCredentials;

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
    private AprioriLoginPage aprioriLoginPage;

    public CisLoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "cis");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CIS login page was not displayed", aprioriLoginPage.getLoginTitle(false).contains("aP Workspace"));
    }

    /**
     * Login to CIS
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public ExplorePage login(UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, ExplorePage.class);
    }

    /**
     * Login to CIS New Application
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public LeftHandNavigationBar cisLogin(UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, LeftHandNavigationBar.class);
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