package cireporttests.navigation;

import com.apriori.pageobjects.admin.pages.manage.ScenarioExport;
import com.apriori.pageobjects.admin.pages.manage.SystemDataExport;
import com.apriori.pageobjects.admin.pages.userguides.CiaUserGuide;
import com.apriori.pageobjects.pages.login.PrivacyPolicyPage;
import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.pageobjects.reports.pages.userguides.CirUserGuide;
import com.apriori.pageobjects.reports.pages.logout.Logout;
import com.apriori.utils.users.UserUtil;
import com.apriori.pageobjects.reports.pages.view.SearchResults;
import com.apriori.pageobjects.reports.pages.create.DataSource;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.create.AdHocView;
import com.apriori.pageobjects.reports.pages.create.Dashboard;
import com.apriori.pageobjects.reports.pages.library.Library;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.Repository;
import io.qameta.allure.Description;
import com.apriori.pageobjects.reports.pages.view.Schedules;
import com.apriori.pageobjects.reports.pages.create.Domain;
import com.apriori.pageobjects.reports.pages.create.Report;
import com.apriori.pageobjects.reports.pages.view.Messages;
import com.apriori.pageobjects.reports.pages.manage.Roles;
import com.apriori.pageobjects.reports.pages.manage.Users;
import com.apriori.utils.TestRail;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringStartsWith.startsWith;

public class NavigationTests extends TestBase {

    private PrivacyPolicyPage privacyPolicyPage;
    private SearchResults searchResults;
    private CirUserGuide cirUserGuide;
    private DataSource dataSource;
    private Repository repository;
    private LoginPage loginPage;
    private Dashboard dashboard;
    private AdHocView adHocView;
    private Schedules schedules;
    private HomePage homePage;
    private HelpPage helpPage;
    private Messages messages;
    private Library library;
    private Domain domain;
    private Report report;
    private Logout logout;
    private Roles roles;
    private Users users;

    public NavigationTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "2987")
    @Description("Ensure that the CI Reports User Guide Link works")
    public void testCIReportsUserGuideNavigation() {
        cirUserGuide = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToReportUserGuide();

        assertThat(cirUserGuide.getReportsUserGuidePageHeading(), is(equalTo("Cost Insight Report:User Guide")));
        assertThat(cirUserGuide.getCurrentUrl(), is(containsString("CI_REPORT_USER_GUIDE")));
        assertThat(cirUserGuide.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = "2986")
    @Description("Ensure that the CI Reports Logout Link works")
    public void testCIReportsLogoutNavigation() {
        logout = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToReportLogout();

        assertThat(logout.getHeaderText(), is(equalTo("CI Design (TE)")));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"2967"})
    @Description("Ensure that the link to Home works (doesn't navigate elsewhere - negative test)")
    public void testHomeNavigation() {
        homePage = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToHomePage();

        assertThat(homePage.getHomeTitleText(), is(equalTo("Home")));
    }

    @Test
    @TestRail(testCaseId = {"2968"})
    @Description("Ensure that the link to Library works")
    public void testLibraryNavigation() {
        library = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage();

        assertThat(library.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @TestRail(testCaseId = {"2969"})
    @Description("Ensure that the link to View Search Results works")
    public void testViewSearchResultsNavigation() {
        searchResults = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToViewSearchResultsPage();

        assertThat(searchResults.getSearchResultsTitleText(), is(equalTo("repoSearch")));
    }

    @Test
    @TestRail(testCaseId = {"2970"})
    @Description("Ensure that the link to View Repository works")
    public void testViewRepositoryNavigation() {
        repository = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToViewRepositoryPage();

        assertThat(repository.getRepositoryTitleText(), is(equalTo("Repository")));
    }

    @Test
    @TestRail(testCaseId = {"2971"})
    @Description("Ensure that the link to View Schedules works")
    public void testViewSchedulesNavigation() {
        schedules = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToViewSchedulesPage();

        assertThat(schedules.getSchedulesTitleText(), is(equalTo("Schedules")));
    }

    @Test
    @TestRail(testCaseId = {"2972"})
    @Description("Ensure that the link to View Messages works")
    public void testViewMessagesNavigation() {
        messages = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToViewMessagesPage();

        assertThat(messages.getMessagesTitleText(), is(equalTo("Messages:")));
    }

    @Test
    @TestRail(testCaseId = {"2973"})
    @Description("Ensure that the link to Manage Users works")
    public void testManageUsersNavigation() {
        users = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToManageUsersPage();

        assertThat(users.getUsersTitleText(), is(equalTo("Users")));
    }

    @Test
    @TestRail(testCaseId = {"2974"})
    @Description("Ensure that the link to Manage Roles works")
    public void testManageRolesNavigation() {
        roles = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToManageRolesPage();

        assertThat(roles.getRolesTitleText(), is(equalTo("Roles")));
    }

    @Test
    @TestRail(testCaseId = {"2975"})
    @Description("Ensure that the link to Create Ad Hoc View works")
    public void testCreateAdHocViewNavigation() {
        adHocView = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToCreateAdHocViewPage();

        assertThat(adHocView.getAdHocViewTitleText(), is(equalTo("New Ad Hoc View")));
        assertThat(adHocView.isDialogDisplayed(), is(equalTo(true)));
        assertThat(adHocView.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = {"2976"})
    @Description("Ensure that the link to Create Report works")
    public void testCreateReportNavigation() {
        report = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToCreateReportPage();

        assertThat(report.isDialogDisplayed(), is(equalTo(true)));
        assertThat(report.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = {"2977"})
    @Description("Ensure that the link to Create Dashboard works")
    public void testCreateDashboardNavigation() {
        dashboard = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToCreateDashboardPage();

        assertThat(dashboard.getAdHocViewTitleText(), is(equalTo("New Dashboard")));
    }

    @Test
    @TestRail(testCaseId = {"2978"})
    @Description("Ensure that the link to Create Domain works")
    public void testCreateDomainNavigation() {
        domain = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToCreateDomainPage();

        assertThat(domain.getCurrentUrl(), is(equalTo(Constants.reportingDomainDesignerUrl)));
        assertThat(domain.isDialogDisplayed(), is(equalTo(true)));
        assertThat(domain.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = {"2979"})
    @Description("Ensure that the link to Create Data Source works")
    public void testCreateDataSourceNavigation() {
        dataSource = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToCreateDataSourcePage();

        assertThat(dataSource.getDataSourceTitleText(), is(equalTo("New Data Source")));
    }

    @Test
    @TestRail(testCaseId = {"2700"})
    @Description("Ensure that the link to the privacy policy works")
    public void testPrivacyPolicyNavigation() {
        privacyPolicyPage = new LoginPage(driver)
                .waitForPrivacyPolicyLinkVisibility()
                .goToPrivacyPolicy();

        assertThat(privacyPolicyPage.getPageHeading(), containsString("APRIORI TECHNOLOGIES, INC. PRIVACY POLICY"));
        assertThat(privacyPolicyPage.getChildWindowURL(), is(equalTo(Constants.privacyPolicyUrl)));
        assertThat(privacyPolicyPage.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = {"2701"})
    @Description("Ensure that the link to the help page works")
    public void testHelpNavigation() {
        String iFrameId = "topic";
        helpPage = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToHelpPage()
                .switchToIFrame(iFrameId);

        assertThat(helpPage.getPageHeading(), is(equalTo("Introduction to JasperReports Server")));
        assertThat(helpPage.getChildWindowURL(), is(startsWith(Constants.reportingHelpUrl)));
        assertThat(helpPage.getTabCount(), is(2));
    }
}
