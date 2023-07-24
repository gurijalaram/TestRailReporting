package com.apriori.pageobjects.header;

import com.apriori.PageUtils;
import com.apriori.pageobjects.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.pages.create.CreateDashboardPage;
import com.apriori.pageobjects.pages.create.CreateDataSourcePage;
import com.apriori.pageobjects.pages.create.CreateDomainPage;
import com.apriori.pageobjects.pages.create.CreateReportPage;
import com.apriori.pageobjects.pages.library.LibraryPage;
import com.apriori.pageobjects.pages.logout.ReportsLogoutPage;
import com.apriori.pageobjects.pages.manage.ManageRolesPage;
import com.apriori.pageobjects.pages.manage.ManageUsersPage;
import com.apriori.pageobjects.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.pages.view.ViewMessagesPage;
import com.apriori.pageobjects.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.pages.view.ViewSchedulesPage;
import com.apriori.pageobjects.pages.view.ViewSearchResultsPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ReportsHeader extends LoadableComponent<ReportsHeader> {

    @FindBy(xpath = "//div[contains(text(), 'Repository')]")
    private WebElement repositoryLink;

    @FindBy(css = "button[aria-label='Create Dashboards']")
    protected WebElement createDashboardsButton;

    @FindBy(id = "helpLink")
    private WebElement helpButton;

    @FindBy(css = "title")
    private WebElement homePageTitle;

    @FindBy(id = "logo")
    private WebElement homePageAprioriLogo;

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

    @FindBy(css = "ul[id=menuList] > li:nth-child(4)")
    private WebElement viewSchedulesMenuOption;

    @FindBy(css = "ul[id=menuList] > li:nth-child(5)")
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

    @FindBy(xpath = "//h2[contains(text(), 'Dashboards')]")
    private WebElement dashboardsTitle;

    @FindBy(css = "div[id='header']")
    private WebElement jasperLogo;

    @FindBy(id = "searchInput")
    private WebElement searchInput;

    @FindBy(css = "span[id='globalSearch'] > a")
    private WebElement searchButton;

    @FindBy(css = ".pageHeader-title-text")
    protected WebElement pageTitle;

    @FindBy(xpath = "//h1[contains(text(), '404')]")
    private WebElement errorTitle;

    @FindBy(css = "body")
    private WebElement pageBody;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public ReportsHeader(WebDriver driver) {
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
    }

    /**
     * Home page navigation method
     *
     * @return Home Page page object
     */
    public ReportsPageHeader navigateToHomePage() {
        return navigateToPage(homeMenuOption, ReportsPageHeader.class);
    }

    /**
     * Library page navigation method
     *
     * @return Library Page page object
     */
    public LibraryPage navigateToLibraryPage() {
        LibraryPage libraryPage = navigateToPage(libraryMenuOption, LibraryPage.class);
        pageUtils.waitForElementToAppear(By.xpath("//a[text() = 'Assembly Cost (A4)']"));
        return libraryPage;
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
    public CirUserGuidePage navigateToHelpPage() {
        pageUtils.waitForElementToAppear(createDashboardsButton);
        CirUserGuidePage helpPage = navigateToPage(helpButton, CirUserGuidePage.class);
        pageUtils.windowHandler(1);
        pageUtils.waitForElementToAppear(jasperLogo);
        return helpPage;
    }

    /**
     * Switches to iframe within a page by its "id" value
     *
     * @param iframeId - iframe id attribute
     * @return new HelpPage object
     */
    public CirUserGuidePage switchToIFrameHelpPage(String iframeId) {
        driver.switchTo().frame(iframeId);
        return new CirUserGuidePage(driver);
    }

    /**
     * Navigates to log out screen
     *
     * @return Logout page object model
     */
    public ReportsLogoutPage navigateToReportLogout() {
        pageUtils.waitForElementToAppear(dashboardsTitle);
        return navigateToPage(logoutMenuOption, ReportsLogoutPage.class);
    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getHomeTitleText() {
        return driver.getTitle();
    }

    /**
     * Navigates to a particular page and returns an instance of the specified page object
     *
     * @param parentPageButton - WebElement
     * @param className        - Class to return
     * @param <T>              - Generic type
     * @return Instance of relevant page object class
     */
    private <T> T navigateToPage(WebElement parentPageButton, Class<T> className) {
        pageUtils.waitForElementAndClick(parentPageButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Another general navigation method
     *
     * @param parentPage - WebElement
     * @param childPage  - WebElement
     * @param className  - Class to return
     * @param <T>        - Generic type
     * @return Instance of passed in class
     */
    private <T> T navigateToSubPage(WebElement parentPage, WebElement childPage, Class<T> className) {
        pageUtils.waitForElementAndClick(parentPage);
        pageUtils.waitForElementAndClick(childPage);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Search for Report
     *
     * @param textToType - String
     * @return current page object
     */
    public ViewSearchResultsPage searchForReport(String textToType) {
        pageUtils.waitForElementAndClick(searchInput);
        searchInput.sendKeys(textToType);
        pageUtils.waitForSteadinessOfElement(By.cssSelector("span[id='globalSearch'] > a"));
        searchInput.sendKeys(Keys.ENTER);
        pageUtils.isPageLoaded(homePageAprioriLogo);
        pageUtils.waitForElementToAppear(By.xpath(String.format("//a[text() = '%s']", textToType)));
        return new ViewSearchResultsPage(driver);
    }
}
