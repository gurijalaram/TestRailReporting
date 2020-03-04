package com.apriori.pageobjects.admin.header;

import com.apriori.pageobjects.admin.pages.homepage.HomePage;
import com.apriori.pageobjects.admin.pages.logout.Logout;
import com.apriori.pageobjects.admin.pages.manage.ScenarioExport;
import com.apriori.pageobjects.admin.pages.manage.SystemDataExport;
import com.apriori.pageobjects.admin.pages.userguides.CiaUserGuide;
import com.apriori.pageobjects.reports.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.utils.PageUtils;
import com.apriori.utils.constants.Constants;

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
    private  WebElement adminTitle;

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
        navigateToPage(manageMenuOption, manageScenarioExportMenuOption);
        return new ScenarioExport(driver);
    }

    /**
     * Navigates to Manage System Data Export
     * @return System Data Export Page Object Model
     */
    public SystemDataExport navigateToManageSystemDataExport() {
        navigateToPage(manageMenuOption, manageScenarioExportMenuOption);
        return new SystemDataExport(driver);
    }

    /**
     * Navigates to Reports System
     * @return Reports Page Object Model
     */
    public HomePage navigateToReports() {
        isLoaded();
        navigateToPage(reportButton);
        isLoadedNow();
        return new HomePage(driver);
    }

    public void isLoadedNow() {
        pageUtils.waitForElementToAppear(email);
        //return pageUtils.isElementEnabled(reportsLogoutOption);
    }

    /**
     * Navigates to Reports System User Guide
     * @return Home Page Page Object Model (since help is external to system)
     */
    public CirUserGuidePage navigateToHelpReportsGuide() {
        navigateToPage(helpButton, reportGuideButton);
        return new CirUserGuidePage(driver);
    }

    /**
     * Navigates to Admin System User Guide
     * @return Home Page Page Object Model (since help is external to system)
     */
    public CiaUserGuide navigateToHelpAdminGuide() {
        navigateToPage(helpButton, adminGuideButton);
        return new CiaUserGuide(driver);
    }

    /**
     * Navigates to Scenario Export Chapter Page
     * @return Scenario Export Chapter Page Object Model
     */
    public HomePage navigateToScenarioExportChapterPage() {
        navigateToPage(helpButton, scenarioExportButton);
        return new HomePage(driver);
    }

    /**
     * Navigates to Logout/Login page
     * @return Logout Page Object Model
     */
    public Logout navigateToAdminLogout() {
        pageUtils.waitForElementToAppear(userButton);
        navigateToPage(userButton, logoutButton);
        return new Logout(driver);
    }

    /**
     * Returns header to check
     * @return
     */
    public String getHeaderToCheck() {
        return pageUtils.getHeaderToCheck(false);
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
     */
    private void navigateToPage(WebElement parentPage) {
        pageUtils.waitForElementToAppear(parentPage);
        parentPage.click();
    }

    /**
     * Another general navigation method
     * @param parentPage
     * @param childPage
     */
    private void navigateToPage(WebElement parentPage, WebElement childPage) {
        this.isLoaded();
        pageUtils.waitForElementToAppear(parentPage);
        parentPage.click();
        if (isElementLoaded()) {
            pageUtils.waitForElementToAppear(childPage);
            childPage.click();
        }
    }

    public boolean isElementLoaded() {
        boolean resultOne = pageUtils.isElementDisplayed(manageScenarioExportMenuOption);
        boolean resultTwo = pageUtils.isElementEnabled(manageScenarioExportMenuOption);
        return resultOne && resultTwo;
    }
}
