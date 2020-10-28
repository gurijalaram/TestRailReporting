package navigation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.ComponentTypeEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.cirpages.CirUserGuidePage;
import pageobjects.pages.homepage.AdminHomePage;
import pageobjects.pages.login.AdminLoginPage;
import pageobjects.pages.logout.Logout;
import pageobjects.pages.manage.NewExportSet;
import pageobjects.pages.manage.ScenarioExport;
import pageobjects.pages.manage.SystemDataExport;
import pageobjects.pages.userguides.CiaUserGuide;
import testsuites.suiteinterface.CustomerSmokeTests;

public class ExportSetTests extends TestBase {

    private SystemDataExport systemDataExport;
    private ScenarioExport scenarioExport;
    private CirUserGuidePage cirUserGuide;
    private CiaUserGuide ciaUserGuide;
    private AdminHomePage adminHomePage;
    private Logout logout;
    private NewExportSet newExportSet;

    public ExportSetTests() {
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
        assertThat(scenarioExport.getHeaderText(), is(equalTo(Constants.MANAGE_SCENARIO_TITLE)));
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
        assertThat(systemDataExport.getHeaderText(), is(equalTo(Constants.MANAGE_SYSTEM_DATA_EXPORT_TITLE)));
    }

    @Test
    @TestRail(testCaseId = "2982")
    @Issue("AP-58758")
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
    @Issue("AP-58758")
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

        String headerToCheck = logout.getHeaderToCheck();

        assertThat(logout.getActualHeaderText(), equalTo(headerToCheck));
        assertThat(logout.isHeaderEnabled(), is(equalTo(true)));
        assertThat(logout.isHeaderDisplayed(), is(true));
    }

    @Test
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = {"2966"})
    @Description("Ensure that the link from Admin to Reports works")
    public void testAdminToReportNavigation() {
        adminHomePage = new AdminLoginPage(driver)
                .login()
                .navigateToReports();

        String urlToCheck = adminHomePage.getUrlToCheck();
        adminHomePage.waitForReportsLogoutDisplayedToAppear();

        assertThat(adminHomePage.getTabCount(), is(equalTo(2)));
        assertThat(adminHomePage.isReportsWelcomeTextDisplayed(), is(true));
        assertThat(adminHomePage.isReportsWelcomeTextEnabled(), is(true));

        assertThat(adminHomePage.getCurrentUrl(), containsString(urlToCheck));
        assertThat(adminHomePage.getCurrentUrl(), containsString(Constants.REPORTS_URL_SUFFIX));
        assertThat(adminHomePage.getCurrentUrl(), containsString(Constants.REPORTS_LOGIN_LOCAL_SUFFIX));
    }

    @Test
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = "80686")
    @Description("Export specific scenario and view results")
    public void testScenarioExportAndViewResults() {
        newExportSet = new AdminLoginPage(driver)
                .login()
                .navigateToManageScenarioExport()
                .clickNew()
                .inputSetName()
                .selectComponentType(ComponentTypeEnum.ROLLUP.getComponentType())
                .inputNamePartNumber("ALL CASTING")
                .inputScenarioName(Constants.DEFAULT_SCENARIO_NAME)
                .setDateTimeToNow()
                .clickCreateExportButton()
                .goToHistoryTab()
                .clickRefreshButton();

        assertThat(newExportSet.getFirstExportSetNameFromTable(), is(equalTo(newExportSet.getExpectedExportSetName())));
        assertThat(newExportSet.getFirstExportSetStatusFromTable(), is(containsString("Started")));
    }
}
