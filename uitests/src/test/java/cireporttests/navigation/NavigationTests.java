package cireporttests.navigation;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.utils.TestRail;
import io.qameta.allure.Description;
import org.junit.Test;
import reports.pages.admin.logout.Logout;
import reports.pages.admin.manage.ScenarioExport;
import reports.pages.admin.manage.SystemDataExport;
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
    private SystemDataExport systemDataExport;
    private ScenarioExport scenarioExport;
    private SearchResults searchResults;
    private DataSource dataSource;
    private Repository repository;
    private Schedules schedules;
    private LoginPage loginPage;
    private AdHocView adHocView;
    private Dashboard dashboard;
    private Messages messages;
    private HomePage homePage;
    private HelpPage helpPage;
    private Library library;
    private Report report;
    private Domain domain;
    private Logout logout;
    private Users users;
    private Roles roles;

    public NavigationTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Ensure that the Manage Scenario Export Link works")
    public void testManageScenarioExportNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        scenarioExport = loginPage.loginToCIAdmin(UserUtil.getUser())
                .navigateToManageScenarioExport();

        assertThat(scenarioExport.isHeaderDisplayed(), is(equalTo(true)));
        assertThat(scenarioExport.isHeaderEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Ensure that the Manage System Data Export Link works")
    public void testManageSystemDataExportNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        systemDataExport = loginPage.loginToCIAdmin(UserUtil.getUser())
                .navigateToManageSystemDataExport();

        assertThat(systemDataExport.isHeaderDisplayed(), is(equalTo(true)));
        assertThat(systemDataExport.isHeaderEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Ensure that the Help Cost Insight Report Guide Link works")
    public void testHelpCostInsightReportGuideNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        homePage = loginPage.loginToCIAdmin(UserUtil.getUser())
                .navigateToHelpReportsGuide();

        assertThat(homePage.getTabCount(), is(2));
        assertThat(homePage.getChildWindowURL(), is(equalTo(Constants.reportsUserGuideUrl)));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Ensure that the Help Cost Insight Admin Guide Link works")
    public void testHelpCostInsightAdminGuideNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        homePage = loginPage.loginToCIAdmin(UserUtil.getUser())
                .navigateToHelpAdminGuide();

        assertThat(homePage.getTabCount(), is(2));
        assertThat(homePage.getChildWindowURL(), is(equalTo(Constants.adminUserGuideUrl)));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Ensure that the Scenario Export Chapter Link works")
    public void testHelpScenarioExportChapterNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        homePage = loginPage.loginToCIAdmin(UserUtil.getUser())
                .navigateToScenarioExportChapterPage();

        assertThat(homePage.getTabCount(), is(2));
        assertThat(homePage.getChildWindowURL(), is(equalTo(Constants.scenarioExportChapterUrl)));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Ensure that the CI Admin Logout Link works")
    public void testCIAdminLogoutNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        logout = loginPage.loginToCIAdmin(UserUtil.getUser())
                .navigateToAdminLogout();

        assertThat(logout.isHeaderDisplayed(), is(true));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.getHeaderText(), is(equalTo("CI Design (TE)")));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Ensure that the CI Reports User Guide Link works")
    public void testCIReportsUserGuideNavigation() {
        loginPage = new LoginPage(driver);
        homePage = loginPage.login(UserUtil.getUser())
                .navigateToReportUserGuide();

        assertThat(homePage.getTabCount(), is(equalTo(2)));
        assertThat(homePage.getChildWindowURL(), is(equalTo(Constants.reportsUserGuideUrl)));
    }

    @Test
    @TestRail(testCaseId = "2986")
    @Description("Ensure that the CI Reports Logout Link works")
    public void testCIReportsLogoutNavigation() {
        loginPage = new LoginPage(driver);
        logout = loginPage.login(UserUtil.getUser())
                .navigateToReportLogout();

        assertThat(logout.isHeaderDisplayed(), is(true));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.getHeaderText(), is(equalTo("CI Design (TE)")));
    }


    @Test
    @TestRail(testCaseId = {"2966"})
    @Description("Ensure that the link from Admin to Reports works")
    public void testAdminToReportNavigation() {
        loginPage = new LoginPage(driver, ciaURL);
        homePage = loginPage.login(UserUtil.getUser())
                .navigateToReports();

        assertThat(homePage.getTabCount(), is(equalTo(2)));
        assertThat(homePage.getChildWindowURL(), is(equalTo(Constants.reportsHomeUrl)));
    }

    @Test
    @TestRail(testCaseId = {"2967"})
    @Description("Ensure that the link to Home works (doesn't navigate elsewhere - negative test)")
    public void testHomeNavigation() {
        loginPage = new LoginPage(driver);
        homePage = loginPage.login(UserUtil.getUser())
                .navigateToHomePage();

        assertThat(homePage.getHomeTitleText(), is(equalTo("Home")));
    }

    @Test
    @TestRail(testCaseId = {"2968"})
    @Description("Ensure that the link to Library works")
    public void testLibraryNavigation() {
        loginPage = new LoginPage(driver);
        library = loginPage.login(UserUtil.getUser())
                .navigateToLibraryPage();

        assertThat(library.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @TestRail(testCaseId = {"2969"})
    @Description("Ensure that the link to View Search Results works")
    public void testViewSearchResultsNavigation() {
        loginPage = new LoginPage(driver);
        searchResults = loginPage.login(UserUtil.getUser())
                .navigateToViewSearchResultsPage();

        assertThat(searchResults.getSearchResultsTitleText(), is(equalTo("repoSearch")));
    }

    @Test
    @TestRail(testCaseId = {"2970"})
    @Description("Ensure that the link to View Repository works")
    public void testViewRepositoryNavigation() {
        loginPage = new LoginPage(driver);
        repository = loginPage.login(UserUtil.getUser())
                .navigateToViewRepositoryPage();

        assertThat(repository.getRepositoryTitleText(), is(equalTo("Repository")));
    }

    @Test
    @TestRail(testCaseId = {"2971"})
    @Description("Ensure that the link to View Schedules works")
    public void testViewSchedulesNavigation() {
        loginPage = new LoginPage(driver);
        schedules = loginPage.login(UserUtil.getUser())
                .navigateToViewSchedulesPage();

        assertThat(schedules.getSchedulesTitleText(), is(equalTo("Schedules")));
    }

    @Test
    @TestRail(testCaseId = {"2972"})
    @Description("Ensure that the link to View Messages works")
    public void testViewMessagesNavigation() {
        loginPage = new LoginPage(driver);
        messages = loginPage.login(UserUtil.getUser())
                .navigateToViewMessagesPage();

        assertThat(messages.getMessagesTitleText(), is(equalTo("Messages:")));
    }

    @Test
    @TestRail(testCaseId = {"2973"})
    @Description("Ensure that the link to Manage Users works")
    public void testManageUsersNavigation() {
        loginPage = new LoginPage(driver);
        users = loginPage.login(UserUtil.getUser())
                .navigateToManageUsersPage();

        assertThat(users.getUsersTitleText(), is(equalTo("Users")));
    }

    @Test
    @TestRail(testCaseId = {"2974"})
    @Description("Ensure that the link to Manage Roles works")
    public void testManageRolesNavigation() {
        loginPage = new LoginPage(driver);
        roles = loginPage.login(UserUtil.getUser())
                .navigateToManageRolesPage();

        assertThat(roles.getRolesTitleText(), is(equalTo("Roles")));
    }

    @Test
    @TestRail(testCaseId = {"2975"})
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
    @TestRail(testCaseId = {"2976"})
    @Description("Ensure that the link to Create Report works")
    public void testCreateReportNavigation() {
        loginPage = new LoginPage(driver);
        report = loginPage.login(UserUtil.getUser())
                .navigateToCreateReportPage();

        assertThat(report.isDialogDisplayed(), is(equalTo(true)));
        assertThat(report.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = {"2977"})
    @Description("Ensure that the link to Create Dashboard works")
    public void testCreateDashboardNavigation() {
        loginPage = new LoginPage(driver);
        dashboard = loginPage.login(UserUtil.getUser())
                .navigateToCreateDashboardPage();

        assertThat(dashboard.getAdHocViewTitleText(), is(equalTo("New Dashboard")));
    }

    @Test
    @TestRail(testCaseId = {"2978"})
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
    @TestRail(testCaseId = {"2979"})
    @Description("Ensure that the link to Create Data Source works")
    public void testCreateDataSourceNavigation() {
        loginPage = new LoginPage(driver);
        dataSource = loginPage.login(UserUtil.getUser())
                .navigateToCreateDataSourcePage();

        assertThat(dataSource.getDataSourceTitleText(), is(equalTo("New Data Source")));
    }

    @Test
    @TestRail(testCaseId = {"2700"})
    @Description("Ensure that the link to the privacy policy works")
    public void testPrivacyPolicyLink() {
        loginPage = new LoginPage(driver);
        privacyPolicyPage = loginPage.waitForPrivacyPolicyLinkVisibility()
                .goToPrivacyPolicy();

        assertThat(privacyPolicyPage.getTabCount(), is(2));
        assertThat(privacyPolicyPage.getChildWindowURL(), is(equalTo(Constants.privacyPolicyUrl)));
    }

    @Test
    @TestRail(testCaseId = {"2701"})
    @Description("Ensure that the link to the help page works")
    public void testHelpLink() {
        loginPage = new LoginPage(driver);
        helpPage = loginPage.login(UserUtil.getUser())
                .navigateToHelpPage();

        assertThat(helpPage.getTabCount(), is(2));
        assertThat(helpPage.getChildWindowURL(), is(equalTo(Constants.reportingHelpUrl)));
    }
}
