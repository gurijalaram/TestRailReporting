package com.navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.pages.create.CreateDashboardPage;
import com.apriori.pageobjects.pages.create.CreateDataSourcePage;
import com.apriori.pageobjects.pages.create.CreateDomainPage;
import com.apriori.pageobjects.pages.create.CreateReportPage;
import com.apriori.pageobjects.pages.library.LibraryPage;
import com.apriori.pageobjects.pages.login.PrivacyPolicyPage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.logout.ReportsLogoutPage;
import com.apriori.pageobjects.pages.manage.ManageRolesPage;
import com.apriori.pageobjects.pages.manage.ManageUsersPage;
import com.apriori.pageobjects.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.pages.view.ViewMessagesPage;
import com.apriori.pageobjects.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.pages.view.ViewSchedulesPage;
import com.apriori.pageobjects.pages.view.ViewSearchResultsPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

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
    private ReportsLogoutPage logout;

    public ReportsNavigationTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"2987"})
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
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"2986"})
    @Description("Ensure that the CI Reports Logout Link works")
    public void testCIReportsLogoutNavigation() {
        logout = new ReportsLoginPage(driver)
            .login()
            .navigateToReportLogout();

        assertThat(logout.isLoginButtonDisplayedAndEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderDisplayed(), is(true));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"2967"})
    @Description("Ensure that the link to Home works (doesn't navigate elsewhere - negative test)")
    public void testHomeNavigation() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login()
            .navigateToHomePage();

        assertThat(reportsPageHeader.getHomeTitleText(), is(containsString("Home")));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"2968"})
    @Description("Ensure that the link to Library works")
    public void testLibraryNavigation() {
        library = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        assertThat(library.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"2969"})
    @Description("Ensure that the link to View Search Results works")
    public void testViewSearchResultsNavigation() {
        searchResults = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSearchResultsPage();

        assertThat(searchResults.getSearchResultsTitleText(), is(equalTo("repoSearch")));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"2970"})
    @Description("Ensure that the link to View Repository works")
    public void testViewRepositoryNavigation() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage();

        assertThat(repository.getRepositoryTitleText(), is(equalTo("Repository")));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"2971"})
    @Description("Ensure that the link to View Schedules works")
    public void testViewSchedulesNavigation() {
        schedules = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSchedulesPage();

        assertThat(schedules.getSchedulesTitleText(), is(equalTo("Schedules")));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
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
    @Category({ReportsTest.class, ReportsSmokeTest.class})
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
    @Category({ReportsTest.class, ReportsSmokeTest.class})
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
    @Category({ReportsTest.class, ReportsSmokeTest.class})
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
    @Ignore("Privacy policy link gone on 21.1 - not valid test anymore")
    @TestRail(testCaseId = {"2700"})
    @Description("Ensure that the link to the privacy policy works")
    public void testPrivacyPolicyNavigation() {
        privacyPolicyPage = new ReportsLoginPage(driver)
            .waitForPrivacyPolicyLinkVisibility()
            .goToPrivacyPolicy();

        assertThat(privacyPolicyPage.getPageHeading(), containsString(Constants.PRIVACY_POLICY_STRING));
        assertThat(privacyPolicyPage.getChildWindowURL(), is(equalTo(Constants.PRIVACY_POLICY_URL)));
        assertThat(privacyPolicyPage.getTabCount(), is(2));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
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
