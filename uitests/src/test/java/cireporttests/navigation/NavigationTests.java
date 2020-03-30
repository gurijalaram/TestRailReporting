package cireporttests.navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.pageobjects.pages.login.HelpPage;
import com.apriori.pageobjects.pages.login.PrivacyPolicyPage;
import com.apriori.pageobjects.reports.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.reports.pages.create.CreateDashboardPage;
import com.apriori.pageobjects.reports.pages.create.CreateDataSourcePage;
import com.apriori.pageobjects.reports.pages.create.CreateDomainPage;
import com.apriori.pageobjects.reports.pages.create.CreateReportPage;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.logout.LogoutPage;
import com.apriori.pageobjects.reports.pages.manage.ManageRolesPage;
import com.apriori.pageobjects.reports.pages.manage.ManageUsersPage;
import com.apriori.pageobjects.reports.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.reports.pages.view.ViewMessagesPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.ViewSchedulesPage;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;

public class NavigationTests extends TestBase {

    private PrivacyPolicyPage privacyPolicyPage;
    private ViewSearchResultsPage searchResults;
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
    private HomePage homePage;
    private HelpPage helpPage;
    private LogoutPage logout;

    public NavigationTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "2987")
    @Issue("AP-58758")
    @Description("Ensure that the CI Reports User Guide Link works")
    public void testCIReportsUserGuideNavigation() throws Exception {
        cirUserGuide = new LoginPage(driver)
            .login(UserUtil.getUser())
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
        logout = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToReportLogout();

        String headerToCheck = logout.getHeaderToCheck();

        assertThat(logout.getHeaderText(), equalTo(headerToCheck));
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

        String urlToCheck = domain.getUrlToCheck();

        assertThat(domain.getCurrentUrl(), equalTo(String.format("%s%s", urlToCheck, Constants.domainDesignerUrlSuffix)));
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
        String iframeId = "topic";
        helpPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToHelpPage()
            .switchToIFrameHelpPage(iframeId)
            .ensurePageIsLoaded();

        assertThat(helpPage.getPageHeading(), is(equalTo("Introduction to JasperReports Server")));
        assertThat(helpPage.getChildWindowURL(), is(startsWith(Constants.reportingHelpUrl)));
        assertThat(helpPage.getTabCount(), is(2));
    }
}
