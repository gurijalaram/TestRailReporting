package com.apriori.pageobjects.admin.pages.homepage;

import com.apriori.pageobjects.admin.header.PageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends PageHeader {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    @FindBy(id = "manage.scenario-export-manager")
    private WebElement manageScenarioExportMenuOption;

    @FindBy(id = "manage.system-data-export-manager")
    private WebElement manageSystemDataExportMenuOption;

    @FindBy(id = "jasper")
    private WebElement reportMenuOption;

    @FindBy(id = "help")
    private WebElement helpMenuOption;

    @FindBy(id = "help.cost-insight_rep")
    private WebElement helpCIReportGuideMenuOption;

    @FindBy(id = "help.cost-insight_adm")
    private WebElement helpCIAdminGuideMenuOption;

    @FindBy(id = "help.scenario-expt")
    private WebElement helpScenarioExportChapterMenuOption;

    @FindBy(id = "user")
    private WebElement userMenuOption;

    @FindBy(css = "input[name='j_username']")
    private WebElement email;

    @FindBy(id = "user.log-out")
    private WebElement logoutMenuOption;

    @FindBy(id = "main_logOut_link")
    private WebElement reportsLogoutOption;

    @FindBy(xpath = "//div[contains(text(), 'Welcome to')]")
    private WebElement adminHomePageWelcomeText;

    private WebDriver driver;
    private PageUtils pageUtils;

    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.isElementDisplayed(adminHomePageWelcomeText);
        pageUtils.isElementEnabled(adminHomePageWelcomeText);
        //pageUtils.isElementDisplayed(manageScenarioExportMenuOption);
        //pageUtils.isElementDisplayed(manageScenarioExportMenuOption);
    }

    /**
     * Gets current URL of new tab
     *
     * @return String
     */
    public String getCurrentUrl() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets count of open tabs
     *
     * @return int
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }
	
    /**
     * Gets url to check
     *
     * @return String
     */
    public String getUrlToCheck() {
        return pageUtils.getUrlToCheck();
    }

    /**
     * Wait for element to appear
     */
    public void waitForReportsLogoutDisplayedToAppear() {
        pageUtils.windowHandler(1);
        pageUtils.waitForElementToAppear(reportsLogoutOption);
    }

    /**
     * Checks if Reports Logout button element is displayed
     *
     * @return boolean
     */
    public boolean isReportsLogoutDisplayed() {
        return pageUtils.isElementDisplayed(reportsLogoutOption);
    }

    /**
     * Checks if Reports Logout button element is enabled
     *
     * @return boolean
     */
    public boolean isReportsLogoutEnabled() {
        return pageUtils.isElementEnabled(reportsLogoutOption);
    }
}
