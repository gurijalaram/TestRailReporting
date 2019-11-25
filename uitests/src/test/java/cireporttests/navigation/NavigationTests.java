package cireporttests.navigation;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;
import reports.pages.create.*;
import reports.pages.homepage.HomePage;
import reports.pages.library.Library;
import reports.pages.login.LoginPage;
import reports.pages.manage.Roles;
import reports.pages.manage.Users;
import reports.pages.privacypolicy.PrivacyPolicyPage;
import reports.pages.view.Messages;
import reports.pages.view.Repository;
import reports.pages.view.Schedules;
import reports.pages.view.SearchResults;

import static com.apriori.utils.constants.Constants.ciaURL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NavigationTests extends TestBase {

    private PrivacyPolicyPage privacyPolicyPage;
    private SearchResults searchResults;
    private HomePage homePage;
    private LoginPage loginPage;
    private HelpPage helpPage;
    private Library library;
    private Repository repository;
    private Schedules schedules;
    private Messages messages;
    private Users users;
    private Roles roles;
    private AdHocView adHocView;
    private Report report;
    private Dashboard dashboard;
    private Domain domain;
    private DataSource dataSource;

    public NavigationTests() {
        super();
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure link from Admin to Reporting works")
    public void testAdminToReportNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        homePage = loginPage.login(UserUtil.getUser())
                .navigateToReports();

        assertThat(homePage.getTabCount(), is(equalTo(2)));
        assertThat(homePage.getChildWindowURL(), is(equalTo(Constants.reportsHomeUrl)));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Home works (doesn't navigate elsewhere - negative test)")
    public void testHomeNavigation() {
        loginPage = new LoginPage(driver);
        homePage = loginPage.login(UserUtil.getUser())
                .navigateToHomePage();

        assertThat(homePage.getHomeTitleText(), is(equalTo("Home")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Library works")
    public void testLibraryNavigation() {
        loginPage = new LoginPage(driver);
        library = loginPage.login(UserUtil.getUser())
                .navigateToLibraryPage();

        assertThat(library.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to View Search Results works")
    public void testViewSearchResultsNavigation() {
        loginPage = new LoginPage(driver);
        searchResults = loginPage.login(UserUtil.getUser())
                .navigateToViewSearchResultsPage();

        assertThat(searchResults.getSearchResultsTitleText(), is(equalTo("repoSearch")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to View Repository works")
    public void testViewRepositoryNavigation() {
        loginPage = new LoginPage(driver);
        repository = loginPage.login(UserUtil.getUser())
                .navigateToViewRepositoryPage();

        assertThat(repository.getRepositoryTitleText(), is(equalTo("Repository")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to View Schedules works")
    public void testViewSchedulesNavigation() {
        loginPage = new LoginPage(driver);
        schedules = loginPage.login(UserUtil.getUser())
                .navigateToViewSchedulesPage();

        assertThat(schedules.getSchedulesTitleText(), is(equalTo("Schedules")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to View Messages works")
    public void testViewMessagesNavigation() {
        loginPage = new LoginPage(driver);
        messages = loginPage.login(UserUtil.getUser())
                .navigateToViewMessagesPage();

        assertThat(messages.getMessagesTitleText(), is(equalTo("Messages:")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Manage Users works")
    public void testManageUsersNavigation() {
        loginPage = new LoginPage(driver);
        users = loginPage.login(UserUtil.getUser())
                .navigateToManageUsersPage();

        assertThat(users.getUsersTitleText(), is(equalTo("Users")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Manage Roles works")
    public void testManageRolesNavigation() {
        loginPage = new LoginPage(driver);
        roles = loginPage.login(UserUtil.getUser())
                .navigateToManageRolesPage();

        assertThat(roles.getRolesTitleText(), is(equalTo("Roles")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Create Ad Hoc View works")
    public void testCreateAdHocViewNavigation() {
        loginPage = new LoginPage(driver);
        adHocView = loginPage.login(UserUtil.getUser())
                .navigateToCreateAdHocViewPage();

        assertThat(adHocView.getAdHocViewTitleText(), is(equalTo("New Ad Hoc View")));
        assertThat(adHocView.isDialogDisplayed(), is(equalTo(true)));
        assertThat(adHocView.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Create Report works")
    public void testCreateReportNavigation() {
        loginPage = new LoginPage(driver);
        report = loginPage.login(UserUtil.getUser())
                .navigateToCreateReportPage();

        assertThat(report.isDialogDisplayed(), is(equalTo(true)));
        assertThat(report.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Create Dashboard works")
    public void testCreateDashboardNavigation() {
        loginPage = new LoginPage(driver);
        dashboard = loginPage.login(UserUtil.getUser())
                .navigateToCreateDashboardPage();

        assertThat(dashboard.getAdHocViewTitleText(), is(equalTo("New Dashboard")));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Create Domain works")
    public void testCreateDomainNavigation() {
        loginPage = new LoginPage(driver);
        domain = loginPage.login(UserUtil.getUser())
                .navigateToCreateDomainPage();

        assertThat(driver.getCurrentUrl(), is(equalTo(Constants.reportingDomainDesignerUrl)));
        assertThat(domain.isDialogDisplayed(), is(equalTo(true)));
        assertThat(domain.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    //@TestRail(testCaseId = {""})
    @Description("Ensure that the link to Create Data Source works")
    public void testCreateDataSourceNavigation() {
        loginPage = new LoginPage(driver);
        dataSource = loginPage.login(UserUtil.getUser())
                .navigateToCreateDataSourcePage();

        assertThat(dataSource.getDataSourceTitleText(), is(equalTo("New Data Source")));
    }

    @Test
    //@TestRail(testCaseId = {"2700"})
    @Description("Link to privacy policy working")
    public void testPrivacyPolicyLink() {
        loginPage = new LoginPage(driver);
        privacyPolicyPage = loginPage.waitForPrivacyPolicyLinkVisibility()
                .goToPrivacyPolicy();

        assertThat(privacyPolicyPage.getTabCount(), is(2));
        assertThat(privacyPolicyPage.getChildWindowURL(), is(equalTo(Constants.privacyPolicyUrl)));
    }

    @Test
    //@TestRail(testCaseId = {"2701"})
    @Description("Link to help page working")
    public void testHelpLink() {
        loginPage = new LoginPage(driver);
        helpPage = loginPage.login(UserUtil.getUser())
                .goToHelpPage();

        assertThat(helpPage.getTabCount(), is(2));
        assertThat(helpPage.getChildWindowURL(), is(equalTo(Constants.reportingHelpUrl)));
    }
}
