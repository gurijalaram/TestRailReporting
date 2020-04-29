package ciadmintests.navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.admin.pages.homepage.HomePage;
import com.apriori.pageobjects.admin.pages.login.LoginPage;
import com.apriori.pageobjects.admin.pages.logout.Logout;
import com.apriori.pageobjects.admin.pages.manage.ScenarioExport;
import com.apriori.pageobjects.admin.pages.manage.SystemDataExport;
import com.apriori.pageobjects.admin.pages.userguides.CiaUserGuide;
import com.apriori.pageobjects.reports.pages.userguides.CirUserGuidePage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class NavigationTests extends TestBase {

    private SystemDataExport systemDataExport;
    private ScenarioExport scenarioExport;
    private CirUserGuidePage cirUserGuide;
    private CiaUserGuide ciaUserGuide;
    private HomePage homePage;
    private Logout logout;

    public NavigationTests() {
        super();
    }

    @Test
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = "2980")
    @Description("Ensure that the Manage Scenario Export Link works")
    public void testManageScenarioExportNavigation() {
        scenarioExport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToManageScenarioExport();

        assertThat(scenarioExport.isHeaderDisplayed(), is(equalTo(true)));
        assertThat(scenarioExport.isHeaderEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "2981")
    @Description("Ensure that the Manage System Data Export Link works")
    public void testManageSystemDataExportNavigation() {
        systemDataExport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToManageSystemDataExport();

        assertThat(systemDataExport.isHeaderDisplayed(), is(equalTo(true)));
        assertThat(systemDataExport.isHeaderEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "2982")
    @Issue("AP-58758")
    @Description("Ensure that the Help Cost Insight Report Guide Link works")
    public void testHelpCostInsightReportGuideNavigation() throws Exception {
        cirUserGuide = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToHelpReportsGuide()
            .switchTab()
            .switchToIFrameUserGuide("page_iframe");

        assertThat(cirUserGuide.getReportsUserGuidePageHeading(), is(equalTo("Cost Insight Report:User Guide")));
        assertThat(cirUserGuide.getCurrentUrl(), is(containsString("CIR_UG")));
        assertThat(cirUserGuide.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = "2983")
    @Issue("AP-58758")
    @Description("Ensure that the Help Cost Insight Admin Guide Link works")
    public void testHelpCostInsightAdminGuideNavigation() {
        ciaUserGuide = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToHelpAdminGuide();

        assertThat(ciaUserGuide.getAdminUserGuidePageHeading(), is(equalTo("Cost Insight Admin:User Guide")));
        assertThat(ciaUserGuide.getCurrentUrl(), is(containsString("CI_ADMIN_USER_GUIDE")));
        assertThat(ciaUserGuide.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = "2984")
    @Description("Ensure that the Scenario Export Chapter Link works")
    public void testHelpScenarioExportChapterNavigation() {
        ciaUserGuide = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToScenarioExportChapterPage();

        String currentUrl = ciaUserGuide.getCurrentUrl();
        assertThat(ciaUserGuide.getTabCount(), is(2));
        assertThat(currentUrl, is(containsString(Constants.SCENARIO_EXPORT_CHAPTER_URL_PART_ONE)));
        assertThat(currentUrl, is(containsString(Constants.SCENARIO_EXPORT_CHAPTER_URL_PART_TWO)));
        assertThat(ciaUserGuide.getAdminUserGuidePageHeading(), is(equalTo(Constants.SCENARIO_EXPORT_CHAPTER_PAGE_TITLE)));
    }

    @Test
    @TestRail(testCaseId = "2985")
    @Description("Ensure that the CI Admin Logout Link works")
    public void testCIAdminLogoutNavigation() {
        logout = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToAdminLogout();

        String headerToCheck = logout.getHeaderToCheck();

        assertThat(logout.getHeaderText(), equalTo(headerToCheck));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderDisplayed(), is(true));
    }

    @Test
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = {"2966"})
    @Description("Ensure that the link from Admin to Reports works")
    public void testAdminToReportNavigation() {
        homePage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToReports();

        String urlToCheck = homePage.getUrlToCheck();
        homePage.waitForReportsLogoutDisplayedToAppear();

        assertThat(homePage.getCurrentUrl(), equalTo(urlToCheck + Constants.REPORTS_URL_SUFFIX + Constants.REPORTS_LAST_SUFFIX));
        assertThat(homePage.getTabCount(), is(equalTo(2)));
        assertThat(homePage.isReportsLogoutDisplayed(), is(true));
        assertThat(homePage.isReportsLogoutEnabled(), is(true));
    }
}
