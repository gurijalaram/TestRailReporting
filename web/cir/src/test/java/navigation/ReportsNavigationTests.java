package navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.header.ReportsPageHeader;
import pageobjects.pages.create.CreateAdHocViewPage;
import pageobjects.pages.create.CreateDashboardPage;
import pageobjects.pages.create.CreateDataSourcePage;
import pageobjects.pages.create.CreateDomainPage;
import pageobjects.pages.create.CreateReportPage;
import pageobjects.pages.library.LibraryPage;
import pageobjects.pages.login.PrivacyPolicyPage;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.logout.LogoutPage;
import pageobjects.pages.manage.ManageRolesPage;
import pageobjects.pages.manage.ManageUsersPage;
import pageobjects.pages.userguides.CirUserGuidePage;
import pageobjects.pages.view.ViewMessagesPage;
import pageobjects.pages.view.ViewRepositoryPage;
import pageobjects.pages.view.ViewSchedulesPage;
import pageobjects.pages.view.ViewSearchResultsPage;
import testsuites.suiteinterface.CIARStagingSmokeTest;

public class ReportsNavigationTests extends TestBase {

    private PrivacyPolicyPage privacyPolicyPage;
    private ViewSearchResultsPage searchResults;
    private ReportsPageHeader reportsPageHeader;
    private CreateDataSourcePage dataSource;
    private CirUserGuidePage cirUserGuide;
    private ViewRepositoryPage repository;
    private CreateDashboardPage dashboard;
    private CreateAdHocViewPage adHocView;
    private ViewSchedulesPage schedules;
    private ViewMessagesPage messages;
    private CreateDomainPage domain;
    private CreateReportPage report;
    private ManageRolesPage roles;
    private ManageUsersPage users;
    private LibraryPage library;
    private LogoutPage logout;

    public ReportsNavigationTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "2987")
    @Description("Ensure that the CI Reports User Guide Link works")
    public void testCIReportsUserGuideNavigation() throws Exception {
        cirUserGuide = new ReportsLoginPage(driver)
            .login()
            .navigateToReportUserGuide()
            .switchTab()
            .switchToIFrameUserGuide("page_iframe");

        assertThat(cirUserGuide.getReportsUserGuidePageHeading(), is(equalTo("Cost Insight Report:User Guide")));
        assertThat(cirUserGuide.getCurrentUrl(), is(containsString("CIR_UG")));
        assertThat(cirUserGuide.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = "2986")
    @Description("Ensure that the CI Reports Logout Link works")
    public void testCIReportsLogoutNavigation() {
        logout = new ReportsLoginPage(driver)
            .login()
            .navigateToReportLogout();

        String headerToCheck = logout.getHeaderToCheck();

        assertThat(logout.getHeaderText(), startsWith(headerToCheck));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"2967"})
    @Description("Ensure that the link to Home works (doesn't navigate elsewhere - negative test)")
    public void testHomeNavigation() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login()
            .navigateToHomePage();

        assertThat(reportsPageHeader.getHomeTitleText(), is(equalTo("Home")));
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = {"2968"})
    @Description("Ensure that the link to Library works")
    public void testLibraryNavigation() {
        library = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        assertThat(library.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @TestRail(testCaseId = {"2969"})
    @Description("Ensure that the link to View Search Results works")
    public void testViewSearchResultsNavigation() {
        searchResults = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSearchResultsPage();

        assertThat(searchResults.getSearchResultsTitleText(), is(equalTo("repoSearch")));
    }

    @Test
    @TestRail(testCaseId = {"2970"})
    @Description("Ensure that the link to View Repository works")
    public void testViewRepositoryNavigation() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage();

        assertThat(repository.getRepositoryTitleText(), is(equalTo("Repository")));
    }

    @Test
    @TestRail(testCaseId = {"2971"})
    @Description("Ensure that the link to View Schedules works")
    public void testViewSchedulesNavigation() {
        schedules = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSchedulesPage();

        assertThat(schedules.getSchedulesTitleText(), is(equalTo("Schedules")));
    }

    @Test
    @TestRail(testCaseId = {"2972"})
    @Description("Ensure that the link to View Messages works")
    public void testViewMessagesNavigation() {
        messages = new ReportsLoginPage(driver)
            .login()
            .navigateToViewMessagesPage();

        assertThat(messages.getMessagesTitleText(), is(equalTo("Messages:")));
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(testCaseId = {"2973"})
    @Description("Ensure that the link to Manage Users works")
    public void testManageUsersNavigation() {
        users = new ReportsLoginPage(driver)
            .login()
            .navigateToManageUsersPage();

        assertThat(users.getUsersTitleText(), is(equalTo("Users")));
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(testCaseId = {"2974"})
    @Description("Ensure that the link to Manage Roles works")
    public void testManageRolesNavigation() {
        roles = new ReportsLoginPage(driver)
            .login()
            .navigateToManageRolesPage();

        assertThat(roles.getRolesTitleText(), is(equalTo("Roles")));
    }

    @Test
    @TestRail(testCaseId = {"2975"})
    @Description("Ensure that the link to Create Ad Hoc View works")
    public void testCreateAdHocViewNavigation() {
        adHocView = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateAdHocViewPage();

        assertThat(adHocView.getAdHocViewTitleText(), is(equalTo("New Ad Hoc View")));
        assertThat(adHocView.isDialogDisplayed(), is(equalTo(true)));
        assertThat(adHocView.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = {"2976"})
    @Description("Ensure that the link to Create Report works")
    public void testCreateReportNavigation() {
        report = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateReportPage();

        assertThat(report.isDialogDisplayed(), is(equalTo(true)));
        assertThat(report.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = {"2977"})
    @Description("Ensure that the link to Create Dashboard works")
    public void testCreateDashboardNavigation() {
        dashboard = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateDashboardPage();

        assertThat(dashboard.getAdHocViewTitleText(), is(equalTo("New Dashboard")));
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(testCaseId = {"2978"})
    @Description("Ensure that the link to Create Domain works")
    public void testCreateDomainNavigation() {
        domain = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateDomainPage();

        String urlToCheck = domain.getUrlToCheck();

        assertThat(domain.getCurrentUrl(), equalTo(String.format("%s%s", urlToCheck, Constants.DOMAIN_DESIGNER_URL_SUFFIX)));
        assertThat(domain.isDialogDisplayed(), is(equalTo(true)));
        assertThat(domain.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(testCaseId = {"2979"})
    @Description("Ensure that the link to Create Data Source works")
    public void testCreateDataSourceNavigation() {
        dataSource = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateDataSourcePage();

        assertThat(dataSource.getDataSourceTitleText(), is(equalTo("New Data Source")));
    }

    @Test
    @TestRail(testCaseId = {"2700"})
    @Description("Ensure that the link to the privacy policy works")
    public void testPrivacyPolicyNavigation() {
        privacyPolicyPage = new ReportsLoginPage(driver)
            .waitForPrivacyPolicyLinkVisibility()
            .goToPrivacyPolicy();

        assertThat(privacyPolicyPage.getPageHeading(), containsString("APRIORI TECHNOLOGIES, INC. PRIVACY POLICY"));
        assertThat(privacyPolicyPage.getChildWindowURL(), is(equalTo(Constants.PRIVACY_POLICY_URL)));
        assertThat(privacyPolicyPage.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = {"2701"})
    @Description("Ensure that the link to the help page works")
    public void testHelpNavigation() {
        String iframeId = "topic";
        cirUserGuide = new ReportsLoginPage(driver)
            .login()
            .navigateToHelpPage()
            .switchToIFrameHelpPage(iframeId)
            .ensurePageIsLoaded();

        assertThat(cirUserGuide.getPageHeading(), is(equalTo("Introduction to JasperReports Server")));
        assertThat(cirUserGuide.getChildWindowURL(), is(startsWith(Constants.REPORTING_HELP_URL)));
        assertThat(cirUserGuide.getTabCount(), is(2));
    }
}
