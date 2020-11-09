package navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.cirpages.CirUserGuidePage;
import pageobjects.pages.homepage.AdminHomePage;
import pageobjects.pages.login.AdminLoginPage;
import pageobjects.pages.logout.Logout;
import pageobjects.pages.manage.ScenarioExport;
import pageobjects.pages.manage.SystemDataExport;
import pageobjects.pages.userguides.CiaUserGuide;
import testsuites.suiteinterface.CustomerSmokeTests;

public class AdminNavigationTests extends TestBase {

    private SystemDataExport systemDataExport;
    private ScenarioExport scenarioExport;
    private CirUserGuidePage cirUserGuide;
    private CiaUserGuide ciaUserGuide;
    private AdminHomePage homePage;
    private Logout logout;

    public AdminNavigationTests() {
        super();
    }

    @Test
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = "2980")
    @Description("Ensure that the Manage Scenario Export Link works")
    public void testManageScenarioExportNavigation() {
        scenarioExport = new AdminLoginPage(driver)
            .login()
            .navigateToManageScenarioExport();

        assertThat(scenarioExport.isHeaderDisplayed(), is(equalTo(true)));
        assertThat(scenarioExport.isHeaderEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "2981")
    @Description("Ensure that the Manage System Data Export Link works")
    public void testManageSystemDataExportNavigation() {
        systemDataExport = new AdminLoginPage(driver)
            .login()
            .navigateToManageSystemDataExport();

        assertThat(systemDataExport.isHeaderDisplayed(), is(equalTo(true)));
        assertThat(systemDataExport.isHeaderEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "2982")
    @Description("Ensure that the Help Cost Insight Report Guide Link works")
    public void testHelpCostInsightReportGuideNavigation() throws Exception {
        cirUserGuide = new AdminLoginPage(driver)
            .login()
            .navigateToHelpReportsGuide()
            .switchTab()
            .switchToIFrameUserGuide("page_iframe");

        assertThat(cirUserGuide.getReportsUserGuidePageHeading(), is(equalTo("Cost Insight Report:User Guide")));
        assertThat(cirUserGuide.getCurrentUrl(), is(containsString("CIR_UG")));
        assertThat(cirUserGuide.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = "2983")
    @Description("Ensure that the Help Cost Insight Admin Guide Link works")
    public void testHelpCostInsightAdminGuideNavigation() {
        ciaUserGuide = new AdminLoginPage(driver)
            .login()
            .navigateToHelpAdminGuide();

        assertThat(ciaUserGuide.getAdminUserGuidePageHeading(), is(equalTo(Constants.CIA_USER_GUIDE_TITLE)));
        assertThat(ciaUserGuide.getCurrentUrl(), is(containsString(Constants.CIA_USER_GUIDE_URL_SUBSTRING)));
        assertThat(ciaUserGuide.getTabCount(), is(2));
    }

    @Test
    @TestRail(testCaseId = "2984")
    @Description("Ensure that the Scenario Export Chapter Link works")
    public void testHelpScenarioExportChapterNavigation() {
        ciaUserGuide = new AdminLoginPage(driver)
            .login()
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
        logout = new AdminLoginPage(driver)
            .login()
            .navigateToAdminLogout();

        String expectedHeader = logout.getExpectedHeader();

        assertThat(logout.getActualHeaderText(), startsWith(expectedHeader));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderDisplayed(), is(true));
    }

    @Test
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = {"2966"})
    @Description("Ensure that the link from Admin to Reports works")
    public void testAdminToReportNavigation() {
        homePage = new AdminLoginPage(driver)
            .login()
            .navigateToReports();

        String urlToCheck = homePage.getUrlToCheck();
        homePage.waitForReportsLogoutDisplayedToAppear();

        assertThat(homePage.getCurrentUrl(), equalTo(urlToCheck + Constants.REPORTS_URL_SUFFIX + Constants.REPORTS_LAST_SUFFIX));
        assertThat(homePage.getTabCount(), is(equalTo(2)));
        assertThat(homePage.isReportsLogoutDisplayed(), is(true));
        assertThat(homePage.isReportsLogoutEnabled(), is(true));
    }
}
