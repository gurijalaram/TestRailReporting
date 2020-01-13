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
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PageHeader extends LoadableComponent<PageHeader> {

    private static Logger logger = LoggerFactory.getLogger(PageHeader.class);

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

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
    private WebElement  viewSearchResultsMenuOption;

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
    private  WebElement adminTitle;

    @FindBy(css = "div[id='header']")
    private WebElement jasperLogo;

    @FindBy(id = "searchInput")
    private WebElement searchInput;

    @FindBy(css = "span[id='globalSearch'] > a")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@class='gdpr-popup white-popup-block']/button[contains(@class, 'cookie-agree-btn')]")
    private WebElement cookieAgreeButton;

    @FindBy(xpath = "//img[@alt='aPriori']")
    private WebElement logoImage;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PageHeader(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Home page navigation method
     * @return Home Page page object
     */
    public HomePage navigateToHomePage() {
        navigateToPage(homeMenuOption);
        return new HomePage(driver);
    }

    /**
     * Library page navigation method
     * @return Library Page page object
     */
    public LibraryPage navigateToLibraryPage() {
        navigateToPage(libraryMenuOption);
        return new LibraryPage(driver);
    }

    /**
     * View Search Results page navigation method
     * @return Search Results Page page object
     */
    public ViewSearchResultsPage navigateToViewSearchResultsPage() {
        navigateToPage(viewMenuOption, viewSearchResultsMenuOption);
        return new ViewSearchResultsPage(driver);
    }

    /**
     * View Repository page navigation method
     * @return Repository Page page object
     */
    public ViewRepositoryPage navigateToViewRepositoryPage() {
        navigateToPage(viewMenuOption, viewRepositoryMenuOption);
        return new ViewRepositoryPage(driver);
    }

    /**
     * View Schedules page navigation method
     * @return Schedules Page page object
     */
    public ViewSchedulesPage navigateToViewSchedulesPage() {
        navigateToPage(viewMenuOption, viewSchedulesMenuOption);
        return new ViewSchedulesPage(driver);
    }

    /**
     * View Messages page navigation method
     * @return Messages Page page object
     */
    public ViewMessagesPage navigateToViewMessagesPage() {
        navigateToPage(viewMenuOption, viewMessagesMenuOption);
        return new ViewMessagesPage(driver);
    }

    /**
     * Manage Users page navigation method
     * @return Users Page page object
     */
    public ManageUsersPage navigateToManageUsersPage() {
        navigateToPage(manageMenuOption, manageUsersMenuOption);
        return new ManageUsersPage(driver);
    }

    /**
     * Manage Roles page navigation method
     * @return Roles Page page object
     */
    public ManageRolesPage navigateToManageRolesPage() {
        navigateToPage(manageMenuOption, manageRolesMenuOption);
        return new ManageRolesPage(driver);
    }

    /**
     * Create Ad Hoc View page navigation method
     * @return Ad Hoc View Page page object
     */
    public CreateAdHocViewPage navigateToCreateAdHocViewPage() {
        navigateToPage(createMenuOption, createAdHocViewMenuOption);
        return new CreateAdHocViewPage(driver);
    }

    /**
     * Create Report page navigation method
     * @return Report Page page object
     */
    public CreateReportPage navigateToCreateReportPage() {
        navigateToPage(createMenuOption, createReportMenuOption);
        return new CreateReportPage(driver);
    }

    /**
     * Create Dashboard page navigation method
     * @return Dashboard Page page object
     */
    public CreateDashboardPage navigateToCreateDashboardPage() {
        navigateToPage(createMenuOption, createDashboardMenuOption);
        return new CreateDashboardPage(driver);
    }

    /**
     * Create Domain page navigation method
     * @return Domain Page page object
     */
    public CreateDomainPage navigateToCreateDomainPage() {
        navigateToPage(createMenuOption, createDomainMenuOption);
        return new CreateDomainPage(driver);
    }

    /**
     * Create Data Source page navigation method
     * @return Data Source Page page object
     */
    public CreateDataSourcePage navigateToCreateDataSourcePage() {
        navigateToPage(createMenuOption, createDataSourceMenuOption);
        return new CreateDataSourcePage(driver);
    }

    /**
     * Navigate to Reports from Admin
     * @return Reports Page page object model
     */
    public CreateReportPage navigateToReports() {
        navigateToPage(reportsMenuOption);
        return new CreateReportPage(driver);
    }

    /**
     * Navigates to Reports User Guide
     * @return Home Page page object model
     */
    public CirUserGuidePage navigateToReportUserGuide() {
        navigateToPage(reportUserGuide);
        return new CirUserGuidePage(driver);
    }

    /**
     * Navigates to help page
     * @return Help Page page object
     */
    public HelpPage navigateToHelpPage() {
        pageUtils.waitForElementToAppear(adminTitle);
        navigateToPage(helpButton);
        pageUtils.windowHandler();
        pageUtils.waitForElementToAppear(jasperLogo);
        return new HelpPage(driver);
    }

    /**
     * Switches to iframe within a page by its "id" value
     * @param iFrameId - iframe id attribute
     * @return new HelpPage object
     */
    public HelpPage switchToIFrameHelpPage(String iFrameId) {
        driver.switchTo().frame(iFrameId);
        return new HelpPage(driver);
    }

    /**
     * Switches to iframe within a page by its "id" value
     * @param iframeId - iframe id attribute
     * @return new CirUserGuide page object
     */
    public CirUserGuidePage switchToIFrameUserGuide(String iframeId) throws Exception {
        pageUtils.waitForElementAndClick(cookieAgreeButton);
        pageUtils.waitForElementToAppear(logoImage);

        String errorElementLocator = "//h1[contains(text(), '404')]";
        if (driver.findElement(By.xpath(errorElementLocator)).isDisplayed()) {
            throw new Exception("Link broken. Wrong page was opened");
        }

        driver.switchTo().frame(iframeId);
        return new CirUserGuidePage(driver);
    }

    /**
     * Switches tab using window handler
     * @return new CirUserGuide page object
     */
    public CirUserGuidePage switchTab() {
        pageUtils.windowHandler();
        return new CirUserGuidePage(driver);
    }

    /**
     * Navigates to log out screen
     * @return Logout page object model
     */
    public LogoutPage navigateToReportLogout() {
        pageUtils.waitForElementToAppear(adminTitle);
        navigateToPage(logoutMenuOption);
        return new LogoutPage(driver);
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
        pageUtils.waitForElementToAppear(parentPage);
        parentPage.click();
        pageUtils.waitForElementToAppear(childPage);
        childPage.click();
    }

    /**
     * Search for Report
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
