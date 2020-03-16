package com.apriori.pageobjects.admin.header;

import com.apriori.pageobjects.admin.pages.homepage.HomePage;
import com.apriori.pageobjects.admin.pages.logout.Logout;
import com.apriori.pageobjects.admin.pages.manage.ScenarioExport;
import com.apriori.pageobjects.admin.pages.manage.SystemDataExport;
import com.apriori.pageobjects.admin.pages.userguides.CiaUserGuide;
import com.apriori.pageobjects.reports.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageHeader extends LoadableComponent<PageHeader> {

    private static Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.isElementDisplayed(adminHomePageWelcomeText);
        pageUtils.isElementEnabled(adminHomePageWelcomeText);
    }

    @FindBy(css = "input[name='j_username']")
    private WebElement email;

    @FindBy(xpath = "//div[contains(text(), 'Welcome to')]")
    private WebElement adminHomePageWelcomeText;

    @FindBy(xpath = "//div[contains(text(), 'Cost Insight | Admin')]")
    private WebElement adminHomePageMainText;

    @FindBy(css = "div[id='display'] > div > div > div:nth-child(1) > div")
    private WebElement homePageTitle;

    @FindBy(id = "manage.scenario-export-manager")
    private WebElement manageScenarioExportMenuOption;

    @FindBy(id = "manage.system-data-export-manager")
    private WebElement manageSystemDataExportMenuOption;

    @FindBy(id = "jasper")
    private WebElement reportButton;

    @FindBy(id = "help")
    private WebElement helpButton;

    @FindBy(id = "manage")
    private WebElement manageMenuOption;

    @FindBy(id = "help.cost-insight_rep")
    private WebElement reportGuideButton;

    @FindBy(id = "help.cost-insight_adm")
    private WebElement adminGuideButton;

    @FindBy(id = "main_logOut_link")
    private WebElement reportsLogoutOption;

    @FindBy(id = "help.scenario-expt")
    private WebElement scenarioExportButton;

    @FindBy(id = "user")
    private WebElement userButton;

    @FindBy(id = "user.log-out")
    private WebElement logoutButton;

    @FindBy(xpath = "//h2[contains(text(), 'Admin')]")
    private WebElement adminTitle;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PageHeader(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigates to Manage Scenario Export
     * @return Manage Scenario Export Page Object Model
     */
    public ScenarioExport navigateToManageScenarioExport() {
        return navigateToSubPage(manageMenuOption, manageScenarioExportMenuOption, ScenarioExport.class);
    }

    /**
     * Navigates to Manage System Data Export
     * @return System Data Export Page Object Model
     */
    public SystemDataExport navigateToManageSystemDataExport() {
        return navigateToSubPage(manageMenuOption, manageSystemDataExportMenuOption, SystemDataExport.class);
    }

    /**
     * Navigates to Reports System
     * @return Reports Page Object Model
     */
    public HomePage navigateToReports() {
        return navigateToPage(reportButton, HomePage.class);
    }

    /**
     * Navigates to Reports System User Guide
     * @return Home Page Page Object Model (since help is external to system)
     */
    public CirUserGuidePage navigateToHelpReportsGuide() {
        return navigateToSubPage(helpButton, reportGuideButton, CirUserGuidePage.class);
    }

    /**
     * Navigates to Admin System User Guide
     * @return Home Page Page Object Model (since help is external to system)
     */
    public CiaUserGuide navigateToHelpAdminGuide() {
        return navigateToSubPage(helpButton, adminGuideButton, CiaUserGuide.class);
    }

    /**
     * Navigates to Scenario Export Chapter Page
     * @return Scenario Export Chapter Page Object Model
     */
    public CiaUserGuide navigateToScenarioExportChapterPage() {
        return navigateToSubPage(helpButton, scenarioExportButton, CiaUserGuide.class);
    }

    /**
     * Navigates to Logout/Login page
     * @return Logout Page Object Model
     */
    public Logout navigateToAdminLogout() {
        pageUtils.waitForElementToAppear(userButton);
        return navigateToSubPage(userButton, logoutButton, Logout.class);
    }

    /**
     * Returns header to check
     * @return
     */
    public String getHeaderToCheck() {
        return pageUtils.getHeaderToCheck(true, true);
    }

    /**
     * Get page title text
     * @return String - page title text
     */
    public String getHomeTitleText() {
        pageUtils.waitForElementToAppear(homePageTitle);
        return homePageTitle.getText();
    }

    /**
     * General navigation method
     * @param parentPage
     * @param className
     * @param <T>
     * @return Instance of specified page object class
     */
    private <T> T navigateToPage(WebElement parentPage, Class<T> className) {
        pageUtils.waitForElementAndClick(parentPage);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Another general navigation method
     * @param parentPage
     * @param childPage
     */
    private <T> T navigateToSubPage(WebElement parentPage, WebElement childPage, Class<T> className) {
        pageUtils.waitForElementAndClick(parentPage);
        pageUtils.waitForElementAndClick(childPage);
        return PageFactory.initElements(driver, className);
    }
}
