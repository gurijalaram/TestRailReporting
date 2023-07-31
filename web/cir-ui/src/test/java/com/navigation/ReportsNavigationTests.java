package com.navigation;

import static com.apriori.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.pages.create.CreateDashboardPage;
import com.apriori.pageobjects.pages.create.CreateDataSourcePage;
import com.apriori.pageobjects.pages.create.CreateDomainPage;
import com.apriori.pageobjects.pages.create.CreateReportPage;
import com.apriori.pageobjects.pages.library.LibraryPage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.logout.ReportsLogoutPage;
import com.apriori.pageobjects.pages.manage.ManageRolesPage;
import com.apriori.pageobjects.pages.manage.ManageUsersPage;
import com.apriori.pageobjects.pages.userguides.CirUserGuidePage;
import com.apriori.pageobjects.pages.view.ViewMessagesPage;
import com.apriori.pageobjects.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.pages.view.ViewSchedulesPage;
import com.apriori.pageobjects.pages.view.ViewSearchResultsPage;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import utils.Constants;

public class ReportsNavigationTests extends TestBaseUI {

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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2987})
    @Description("Ensure that the CI Reports User Guide Link works")
    public void testCIReportsUserGuideNavigation() {
        cirUserGuide = new ReportsLoginPage(driver)
            .login()
            .navigateToReportUserGuide()
            .switchTab();

        assertThat(cirUserGuide.getReportsUserGuidePageHeading(), is(containsString("Cost Insight Report")));
        assertThat(cirUserGuide.getReportsUserGuidePageHeading(), is(containsString("User Guide")));
        assertThat(cirUserGuide.getCurrentUrl(), is(containsString("CIR_UG")));
        assertThat(cirUserGuide.getTabCount(), is(2));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2986})
    @Description("Ensure that the CI Reports Logout Link works")
    public void testCIReportsLogoutNavigation() {
        logout = new ReportsLoginPage(driver)
            .login()
            .navigateToReportLogout();

        assertThat(logout.isLoginButtonEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderDisplayed(), is(true));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2967})
    @Description("Ensure that the link to Home works (doesn't navigate elsewhere - negative test)")
    public void testHomeNavigation() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login()
            .navigateToHomePage();

        assertThat(reportsPageHeader.getHomeTitleText(), is(containsString("Home")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2968})
    @Description("Ensure that the link to Library works")
    public void testLibraryNavigation() {
        library = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        assertThat(library.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2969})
    @Description("Ensure that the link to View Search Results works")
    public void testViewSearchResultsNavigation() {
        searchResults = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSearchResultsPage();

        assertThat(searchResults.getSearchResultsTitleText(), is(equalTo("repoSearch")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2970})
    @Description("Ensure that the link to View Repository works")
    public void testViewRepositoryNavigation() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage();

        assertThat(repository.getRepositoryTitleText(), is(equalTo("Repository")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2971})
    @Description("Ensure that the link to View Schedules works")
    public void testViewSchedulesNavigation() {
        schedules = new ReportsLoginPage(driver)
            .login()
            .navigateToViewSchedulesPage();

        assertThat(schedules.getSchedulesTitleText(), is(equalTo("Schedules")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2972})
    @Description("Ensure that the link to View Messages works")
    public void testViewMessagesNavigation() {
        messages = new ReportsLoginPage(driver)
            .login()
            .navigateToViewMessagesPage();

        assertThat(messages.getMessagesTitleText(), is(equalTo("Messages")));
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(id = {2973})
    @Description("Ensure that the link to Manage Users works")
    public void testManageUsersNavigation() {
        users = new ReportsLoginPage(driver)
            .login()
            .navigateToManageUsersPage();

        assertThat(users.getUsersTitleText(), is(equalTo("Users")));
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(id = {2974})
    @Description("Ensure that the link to Manage Roles works")
    public void testManageRolesNavigation() {
        roles = new ReportsLoginPage(driver)
            .login()
            .navigateToManageRolesPage();

        assertThat(roles.getRolesTitleText(), is(equalTo("Roles")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2975})
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2976})
    @Description("Ensure that the link to Create Report works")
    public void testCreateReportNavigation() {
        report = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateReportPage();

        assertThat(report.isDialogDisplayed(), is(equalTo(true)));
        assertThat(report.isDialogEnabled(), is(equalTo(true)));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2977})
    @Description("Ensure that the link to Create Dashboard works")
    public void testCreateDashboardNavigation() {
        dashboard = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateDashboardPage();

        assertThat(dashboard.getAdHocViewTitleText(), is(equalTo("New Dashboard")));
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(id = {2978})
    @Description("Ensure that the link to Create Domain works")
    public void testCreateDomainNavigation() {
        domain = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateDomainPage();

        String urlToCheck = domain.getUrlToCheck();

        assertThat(
            domain.getCurrentUrl(),
            equalTo(String.format("%s%s", urlToCheck, Constants.DOMAIN_DESIGNER_URL_SUFFIX))
        );
        assertThat(
            domain.isDialogDisplayed(),
            is(equalTo(true))
        );
        assertThat(
            domain.isDialogEnabled(),
            is(equalTo(true))
        );
    }

    @Test
    @Ignore("Not valid in QA env due to permissions")
    @TestRail(id = {2979})
    @Description("Ensure that the link to Create Data Source works")
    public void testCreateDataSourceNavigation() {
        dataSource = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateDataSourcePage();

        assertThat(dataSource.getDataSourceTitleText(), is(equalTo("New Data Source")));
    }
}
