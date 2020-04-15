package com.apriori.pageobjects.reports.header;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.pageobjects.reports.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.reports.pages.create.CreateDashboardPage;
import com.apriori.pageobjects.reports.pages.create.CreateDataSourcePage;
import com.apriori.pageobjects.reports.pages.create.CreateDomainPage;
import com.apriori.pageobjects.reports.pages.create.CreateReportPage;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.logout.LogoutPage;
import com.apriori.pageobjects.reports.pages.manage.ManageRolesPage;
import com.apriori.pageobjects.reports.pages.manage.ManageUsersPage;
import com.apriori.pageobjects.reports.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.reports.pages.view.ViewMessagesPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.ViewSchedulesPage;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageHeader extends LoadableComponent<PageHeader> {

    private final static Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @FindBy(id = "helpLink")
    private WebElement helpButton;

    @FindBy(css = "div[id='display'] > div > div > div:nth-child(1) > div")
    private WebElement homePageTitle;

    @FindBy(id = "main_home")
    private WebElement homeMenuOption;

    @FindBy(id = "main_library")
    private WebElement libraryMenuOption;

    @FindBy(id = "main_view")
    private WebElement viewMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(1)")
    private WebElement viewSearchResultsMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(2)")
    private WebElement viewRepositoryMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(3)")
    private WebElement viewSchedulesMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(4)")
    private WebElement viewMessagesMenuOption;

    @FindBy(id = "main_manage")
    private WebElement manageMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(1)")
    private WebElement manageUsersMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(2)")
    private WebElement manageRolesMenuOption;

    @FindBy(id = "main_create")
    private WebElement createMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(1)")
    private WebElement createAdHocViewMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(2)")
    private WebElement createReportMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(3)")
    private WebElement createDashboardMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(4)")
    private WebElement createDomainMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(5)")
    private WebElement createDataSourceMenuOption;

    @FindBy(id = "jasper")
    private WebElement reportsMenuOption;

    @FindBy(css = "ul[id='metaLinks'] > li:nth-child(2) > a")
    private WebElement reportUserGuide;

    @FindBy(id = "main_logOut_link")
    private WebElement logoutMenuOption;

    @FindBy(xpath = "//h2[contains(text(), 'Admin')]")
    private WebElement adminTitle;

    @FindBy(css = "div[id='header']")
    private WebElement jasperLogo;

    @FindBy(id = "searchInput")
    private WebElement searchInput;

    @FindBy(css = "span[id='globalSearch'] > a")
    private WebElement searchButton;

    @FindBy(id = "toolbar_logo_link")
    private WebElement pageTitle;

    @FindBy(xpath = "//h1[contains(text(), '404')]")
    private WebElement errorTitle;

    @FindBy(css = "body")
    private WebElement pageBody;

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
     * Home page navigation method
     *
     * @return Home Page page object
     */
    public HomePage navigateToHomePage() {
        return navigateToPage(homeMenuOption, HomePage.class);
    }

    /**
     * Library page navigation method
     *
     * @return Library Page page object
     */
    public LibraryPage navigateToLibraryPage() {
        return navigateToPage(libraryMenuOption, LibraryPage.class);
    }

    /**
     * View Search Results page navigation method
     *
     * @return Search Results Page page object
     */
    public ViewSearchResultsPage navigateToViewSearchResultsPage() {
        return navigateToSubPage(viewMenuOption, viewSearchResultsMenuOption, ViewSearchResultsPage.class);
    }

    /**
     * View Repository page navigation method
     *
     * @return Repository Page page object
     */
    public ViewRepositoryPage navigateToViewRepositoryPage() {
        return navigateToSubPage(viewMenuOption, viewRepositoryMenuOption, ViewRepositoryPage.class);
    }

    /**
     * View Schedules page navigation method
     *
     * @return Schedules Page page object
     */
    public ViewSchedulesPage navigateToViewSchedulesPage() {
        return navigateToSubPage(viewMenuOption, viewSchedulesMenuOption, ViewSchedulesPage.class);
    }

    /**
     * View Messages page navigation method
     *
     * @return Messages Page page object
     */
    public ViewMessagesPage navigateToViewMessagesPage() {
        return navigateToSubPage(viewMenuOption, viewMessagesMenuOption, ViewMessagesPage.class);
    }

    /**
     * Manage Users page navigation method
     *
     * @return Users Page page object
     */
    public ManageUsersPage navigateToManageUsersPage() {
        return navigateToSubPage(manageMenuOption, manageUsersMenuOption, ManageUsersPage.class);
    }

    /**
     * Manage Roles page navigation method
     *
     * @return Roles Page page object
     */
    public ManageRolesPage navigateToManageRolesPage() {
        return navigateToSubPage(manageMenuOption, manageRolesMenuOption, ManageRolesPage.class);
    }

    /**
     * Create Ad Hoc View page navigation method
     *
     * @return Ad Hoc View Page page object
     */
    public CreateAdHocViewPage navigateToCreateAdHocViewPage() {
        return navigateToSubPage(createMenuOption, createAdHocViewMenuOption, CreateAdHocViewPage.class);
    }

    /**
     * Create Report page navigation method
     *
     * @return Report Page page object
     */
    public CreateReportPage navigateToCreateReportPage() {
        return navigateToSubPage(createMenuOption, createReportMenuOption, CreateReportPage.class);
    }

    /**
     * Create Dashboard page navigation method
     *
     * @return Dashboard Page page object
     */
    public CreateDashboardPage navigateToCreateDashboardPage() {
        return navigateToSubPage(createMenuOption, createDashboardMenuOption, CreateDashboardPage.class);
    }

    /**
     * Create Domain page navigation method
     *
     * @return Domain Page page object
     */
    public CreateDomainPage navigateToCreateDomainPage() {
        return navigateToSubPage(createMenuOption, createDomainMenuOption, CreateDomainPage.class);
    }

    /**
     * Create Data Source page navigation method
     *
     * @return Data Source Page page object
     */
    public CreateDataSourcePage navigateToCreateDataSourcePage() {
        return navigateToSubPage(createMenuOption, createDataSourceMenuOption, CreateDataSourcePage.class);
    }

    /**
     * Navigate to Reports from Admin
     *
     * @return Reports Page page object model
     */
    public CreateReportPage navigateToReports() {
        return navigateToPage(reportsMenuOption, CreateReportPage.class);
    }

    /**
     * Navigates to Reports User Guide
     *
     * @return Home Page page object model
     */
    public CirUserGuidePage navigateToReportUserGuide() {
        return navigateToPage(reportUserGuide, CirUserGuidePage.class);
    }

    /**
     * Navigates to help page
     *
     * @return Help Page page object
     */
    public HelpPage navigateToHelpPage() {
        pageUtils.waitForElementToAppear(adminTitle);
        HelpPage helpPage = navigateToPage(helpButton, HelpPage.class);
        pageUtils.windowHandler();
        pageUtils.waitForElementToAppear(jasperLogo);
        return helpPage;
    }

    /**
     * Switches to iframe within a page by its "id" value
     *
     * @param iframeId - iframe id attribute
     * @return new HelpPage object
     */
    public HelpPage switchToIFrameHelpPage(String iframeId) {
        driver.switchTo().frame(iframeId);
        return new HelpPage(driver);
    }

    /**
     * Switches to iframe within a page by its "id" value
     *
     * @param iframeId - iframe id attribute
     * @return new CirUserGuide page object
     */
    public CirUserGuidePage switchToIFrameUserGuide(String iframeId) throws Exception {
        pageUtils.waitForElementToAppear(pageTitle);

        if (pageBody.getAttribute("className").startsWith("error404")) {
            throw new Exception("Link broken. Wrong page was opened - iframe wasn't found as a result");
        } else {
            driver.switchTo().frame(iframeId);
        }

        return new CirUserGuidePage(driver);
    }

    /**
     * Switches tab using window handler
     *
     * @return new CirUserGuide page object
     */
    public CirUserGuidePage switchTab() {
        pageUtils.windowHandler();
        return new CirUserGuidePage(driver);
    }

    /**
     * Navigates to log out screen
     *
     * @return Logout page object model
     */
    public LogoutPage navigateToReportLogout() {
        pageUtils.waitForElementToAppear(adminTitle);
        return navigateToPage(logoutMenuOption, LogoutPage.class);
    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getHomeTitleText() {
        pageUtils.waitForElementToAppear(homePageTitle);
        return homePageTitle.getText();
    }

    /**
     * Navigates to a particular page and returns an instance of the specified page object
     *
     * @param <T>
     * @return Instance of relevant page object class
     */
    private <T> T navigateToPage(WebElement parentPageButton, Class<T> className) {
        pageUtils.waitForElementAndClick(parentPageButton);
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

    /**
     * Search for Report
     *
     * @param textToType
     * @return current page object
     */
    public HomePage searchForReport(String textToType) {
        pageUtils.waitForElementToBeClickable(searchInput);
        searchInput.sendKeys(textToType);
        searchButton.click();
        pageUtils.isPageLoaded(homePageTitle);
        return new HomePage(driver);
    }
}
