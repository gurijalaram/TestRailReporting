package com.apriori.pageobjects.header;

import com.apriori.pageobjects.cirpages.CirUserGuidePage;
import com.apriori.pageobjects.pages.homepage.AdminHomePage;
import com.apriori.pageobjects.pages.logout.Logout;
import com.apriori.pageobjects.pages.manage.ScenarioExport;
import com.apriori.pageobjects.pages.manage.SystemDataExport;
import com.apriori.pageobjects.pages.userguides.CiaUserGuide;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

public class PageHeader extends LoadableComponent<PageHeader> {

    private static final Logger logger = LoggerFactory.getLogger(PageHeader.class);

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

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

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
        return navigateToPage(reportButton, AdminHomePage.class);
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
    public Logout navigateToAdminLogout() {
        pageUtils.waitForElementToAppear(userButton);
        return navigateToSubPage(userButton, logoutButton, Logout.class);
    }

    /**
     * Returns header to check
     *
     * @return
     */
    public String getExpectedHeader() {
        return Constants.LOGOUT_HEADER;
    }

    /**
     * General navigation method
     *
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
     *
     * @param parentPage
     * @param childPage
     */
    private <T> T navigateToSubPage(WebElement parentPage, WebElement childPage, Class<T> className) {
        pageUtils.waitForElementAndClick(parentPage);
        pageUtils.waitForElementAndClick(childPage);
        return PageFactory.initElements(driver, className);
    }
}
