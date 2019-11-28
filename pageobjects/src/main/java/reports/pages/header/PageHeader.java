package reports.pages.header;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.pages.admin.logout.Logout;
import reports.pages.create.AdHocView;
import reports.pages.create.Dashboard;
import reports.pages.create.DataSource;
import reports.pages.create.Domain;
import reports.pages.create.Report;
import reports.pages.homepage.HomePage;
import reports.pages.library.Library;
import reports.pages.manage.Roles;
import reports.pages.manage.Users;
import reports.pages.userguides.CirUserGuide;
import reports.pages.view.Messages;
import reports.pages.view.Repository;
import reports.pages.view.Schedules;
import reports.pages.view.SearchResults;

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
    public Library navigateToLibraryPage() {
        navigateToPage(libraryMenuOption);
        return new Library(driver);
    }

    /**
     * View Search Results page navigation method
     * @return Search Results Page page object
     */
    public SearchResults navigateToViewSearchResultsPage() {
        navigateToPage(viewMenuOption, viewSearchResultsMenuOption);
        return new SearchResults(driver);
    }

    /**
     * View Repository page navigation method
     * @return Repository Page page object
     */
    public Repository navigateToViewRepositoryPage() {
        navigateToPage(viewMenuOption, viewRepositoryMenuOption);
        return new Repository(driver);
    }

    /**
     * View Schedules page navigation method
     * @return Schedules Page page object
     */
    public Schedules navigateToViewSchedulesPage() {
        navigateToPage(viewMenuOption, viewSchedulesMenuOption);
        return new Schedules(driver);
    }

    /**
     * View Messages page navigation method
     * @return Messages Page page object
     */
    public Messages navigateToViewMessagesPage() {
        navigateToPage(viewMenuOption, viewMessagesMenuOption);
        return new Messages(driver);
    }

    /**
     * Manage Users page navigation method
     * @return Users Page page object
     */
    public Users navigateToManageUsersPage() {
        navigateToPage(manageMenuOption, manageUsersMenuOption);
        return new Users(driver);
    }

    /**
     * Manage Roles page navigation method
     * @return Roles Page page object
     */
    public Roles navigateToManageRolesPage() {
        navigateToPage(manageMenuOption, manageRolesMenuOption);
        return new Roles(driver);
    }

    /**
     * Create Ad Hoc View page navigation method
     * @return Ad Hoc View Page page object
     */
    public AdHocView navigateToCreateAdHocViewPage() {
        navigateToPage(createMenuOption, createAdHocViewMenuOption);
        return new AdHocView(driver);
    }

    /**
     * Create Report page navigation method
     * @return Report Page page object
     */
    public Report navigateToCreateReportPage() {
        navigateToPage(createMenuOption, createReportMenuOption);
        return new Report(driver);
    }

    /**
     * Create Dashboard page navigation method
     * @return Dashboard Page page object
     */
    public Dashboard navigateToCreateDashboardPage() {
        navigateToPage(createMenuOption, createDashboardMenuOption);
        return new Dashboard(driver);
    }

    /**
     * Create Domain page navigation method
     * @return Domain Page page object
     */
    public Domain navigateToCreateDomainPage() {
        navigateToPage(createMenuOption, createDomainMenuOption);
        return new Domain(driver);
    }

    /**
     * Create Data Source page navigation method
     * @return Data Source Page page object
     */
    public DataSource navigateToCreateDataSourcePage() {
        navigateToPage(createMenuOption, createDataSourceMenuOption);
        return new DataSource(driver);
    }

    /**
     * Navigate to Reports from Admin
     * @return Home Page page object model
     */
    public HomePage navigateToReports() {
        navigateToPage(reportsMenuOption);
        return new HomePage(driver);
    }

    /**
     * Navigates to Reports User Guide
     * @return Home Page page object model
     */
    public CirUserGuide navigateToReportUserGuide() {
        navigateToPage(reportUserGuide);
        return new CirUserGuide(driver);
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
     * switches to iframe within a page by its "id" value
     * @param iFrameId - iframe id attribute
     * @return new HelpPage object
     */
    public HelpPage switchToIFrame(String iFrameId) {
        driver.switchTo().frame(iFrameId);
        return new HelpPage(driver);
    }

    /**
     * Navigates to log out screen
     * @return Logout page object model
     */
    public Logout navigateToReportLogout() {
        pageUtils.waitForElementToAppear(adminTitle);
        navigateToPage(logoutMenuOption);
        return new Logout(driver);
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
}
