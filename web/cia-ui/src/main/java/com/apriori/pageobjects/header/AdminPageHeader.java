package com.apriori.pageobjects.header;

import com.apriori.PageUtils;
import com.apriori.pageobjects.cirpages.CirUserGuidePage;
import com.apriori.pageobjects.homepage.AdminHomePage;
import com.apriori.pageobjects.logout.AdminLogoutPage;
import com.apriori.pageobjects.manage.ScenarioExport;
import com.apriori.pageobjects.manage.SystemDataExport;
import com.apriori.pageobjects.userguides.CiaUserGuide;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class AdminPageHeader extends LoadableComponent<AdminPageHeader> {

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

    public AdminPageHeader(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.isElementDisplayed(adminHomePageWelcomeText);
        pageUtils.isElementEnabled(adminHomePageWelcomeText);
    }

    /**
     * Navigates to Manage Scenario Export
     *
     * @return Manage Scenario Export Page Object Model
     */
    public ScenarioExport navigateToManageScenarioExport() {
        return navigateToSubPage(manageMenuOption, manageScenarioExportMenuOption, ScenarioExport.class);
    }

    /**
     * Navigates to Manage System Data Export
     *
     * @return System Data Export Page Object Model
     */
    public SystemDataExport navigateToManageSystemDataExport() {
        return navigateToSubPage(manageMenuOption, manageSystemDataExportMenuOption, SystemDataExport.class);
    }

    /**
     * Navigates to Reports System
     *
     * @return Reports Page Object Model
     */
    public AdminHomePage navigateToReports() {
        return navigateToPage(reportButton);
    }

    /**
     * Navigates to Reports System User Guide
     *
     * @return CIR user guide instance
     */
    public CirUserGuidePage navigateToHelpReportsGuide() {
        return navigateToSubPage(helpButton, reportGuideButton, CirUserGuidePage.class);
    }

    /**
     * Navigates to Admin System User Guide
     *
     * @return CIA user guide instance
     */
    public CiaUserGuide navigateToHelpAdminGuide() {
        return navigateToSubPage(helpButton, adminGuideButton, CiaUserGuide.class);
    }

    /**
     * Navigates to Scenario Export Chapter Page
     *
     * @return Scenario Export Chapter Page Object Model
     */
    public CiaUserGuide navigateToScenarioExportChapterPage() {
        return navigateToSubPage(helpButton, scenarioExportButton, CiaUserGuide.class);
    }

    /**
     * Navigates to Logout/Login page
     *
     * @return Logout Page Object Model
     */
    public AdminLogoutPage navigateToAdminLogout() {
        pageUtils.waitForElementToAppear(userButton);
        return navigateToSubPage(userButton, logoutButton, AdminLogoutPage.class);
    }

    /**
     * General navigation method
     *
     * @param parentPage WebElement
     * @return Instance of AdminHomePage
     */
    private AdminHomePage navigateToPage(WebElement parentPage) {
        pageUtils.waitForElementAndClick(parentPage);
        return new AdminHomePage(driver);
    }

    /**
     * Another general navigation method
     * @param parentPage - WebElement
     * @param childPage - WebElement
     * @param className - Class
     * @param <T> - Generic
     * @return Instance of class
     */
    private <T> T navigateToSubPage(WebElement parentPage, WebElement childPage, Class<T> className) {
        pageUtils.waitForElementAndClick(parentPage);
        pageUtils.waitForElementAndClick(childPage);
        return PageFactory.initElements(driver, className);
    }
}
