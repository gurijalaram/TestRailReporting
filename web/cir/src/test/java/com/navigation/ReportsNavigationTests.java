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
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.logout.LogoutPage;
import com.apriori.pageobjects.pages.manage.ManageRolesPage;
import com.apriori.pageobjects.pages.manage.ManageUsersPage;
import com.apriori.pageobjects.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.pages.view.ViewMessagesPage;
import com.apriori.pageobjects.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.pages.view.ViewSchedulesPage;
import com.apriori.pageobjects.pages.view.ViewSearchResultsPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.login.PrivacyPolicyPage;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CIARStagingSmokeTest;
import testsuites.suiteinterface.OnPremTest;

public class ReportsNavigationTests extends TestBase {

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
    @Category(OnPremTest.class)
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
    @Category(OnPremTest.class)
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
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2967"})
    @Description("Ensure that the link to Home works (doesn't navigate elsewhere - negative test)")
    public void testHomeNavigation() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login()
            .navigateToHomePage();

        assertThat(reportsPageHeader.getHomeTitleText(), is(equalTo("Home")));
    }

    @Test
    @Category({OnPremTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = {"2968"})
    @Description("Ensure that the link to Library works")
    public void testLibraryNavigation() {
        library = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        assertThat(library.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2969"})
    @Description("Ensure that the link to View Search Results works")
    public void testViewSearchResultsNavigation() {
        searchResults = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSearchResultsPage();

        assertThat(searchResults.getSearchResultsTitleText(), is(equalTo("repoSearch")));
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2970"})
    @Description("Ensure that the link to View Repository works")
    public void testViewRepositoryNavigation() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage();

        assertThat(repository.getRepositoryTitleText(), is(equalTo("Repository")));
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2971"})
    @Description("Ensure that the link to View Schedules works")
    public void testViewSchedulesNavigation() {
        schedules = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSchedulesPage();

        assertThat(schedules.getSchedulesTitleText(), is(equalTo("Schedules")));
    }

    @Test
    @Category(OnPremTest.class)
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
    @Category(OnPremTest.class)
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
    @Category(OnPremTest.class)
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
    @Category(OnPremTest.class)
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
}
