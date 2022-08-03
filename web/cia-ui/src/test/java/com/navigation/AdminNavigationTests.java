package com.navigation;

import com.apriori.pageobjects.cirpages.CirUserGuidePage;
import com.apriori.pageobjects.pages.homepage.AdminHomePage;
import com.apriori.pageobjects.pages.login.AdminLoginPage;
import com.apriori.pageobjects.pages.logout.AdminLogoutPage;
import com.apriori.pageobjects.pages.manage.ScenarioExport;
import com.apriori.pageobjects.pages.manage.SystemDataExport;
import com.apriori.pageobjects.pages.userguides.CiaUserGuide;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import utils.Constants;

public class AdminNavigationTests extends TestBase {

    private SoftAssertions softAssertions = new SoftAssertions();
    private SystemDataExport systemDataExport;
    private ScenarioExport scenarioExport;
    private CirUserGuidePage cirUserGuide;
    private CiaUserGuide ciaUserGuide;
    private AdminHomePage homePage;
    private AdminLogoutPage logout;

    public AdminNavigationTests() {
        super();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2980"})
    @Description("Ensure that the Manage Scenario Export Link works")
    public void testManageScenarioExportNavigation() {
        scenarioExport = new AdminLoginPage(driver)
            .login()
            .navigateToManageScenarioExport();

        softAssertions.assertThat(scenarioExport.isHeaderDisplayed()).isEqualTo(true);
        softAssertions.assertThat(scenarioExport.isHeaderEnabled()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2981"})
    @Description("Ensure that the Manage System Data Export Link works")
    public void testManageSystemDataExportNavigation() {
        systemDataExport = new AdminLoginPage(driver)
            .login()
            .navigateToManageSystemDataExport();

        softAssertions.assertThat(systemDataExport.isHeaderDisplayed()).isEqualTo(true);
        softAssertions.assertThat(systemDataExport.isHeaderEnabled()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2982"})
    @Description("Ensure that the Help Cost Insight Report Guide Link works")
    public void testHelpCostInsightReportGuideNavigation() {
        cirUserGuide = new AdminLoginPage(driver)
            .login()
            .navigateToHelpReportsGuide()
            .switchTab();

        softAssertions.assertThat(cirUserGuide.getReportsUserGuidePageHeading()).startsWith("Cost Insight Report");
        softAssertions.assertThat(cirUserGuide.getCurrentUrl()).contains("CIR_UG");
        softAssertions.assertThat(cirUserGuide.getTabCount()).isEqualTo(2);
        softAssertions.assertAll();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2983"})
    @Description("Ensure that the Help Cost Insight Admin Guide Link works")
    public void testHelpCostInsightAdminGuideNavigation() {
        ciaUserGuide = new AdminLoginPage(driver)
            .login()
            .navigateToHelpAdminGuide();

        softAssertions.assertThat(ciaUserGuide.getTabCount()).isEqualTo(2);
        softAssertions.assertThat(ciaUserGuide.getCurrentUrl()).contains(Constants.CIA_USER_GUIDE_URL_SUBSTRING);
        softAssertions.assertThat(ciaUserGuide.getAdminOrScenarioChapterUserGuidePageHeading(false))
            .contains(Constants.CIA_USER_GUIDE_TITLE);
        softAssertions.assertAll();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2984"})
    @Description("Ensure that the Scenario Export Chapter Link works")
    public void testHelpScenarioExportChapterNavigation() {
        ciaUserGuide = new AdminLoginPage(driver)
            .login()
            .navigateToScenarioExportChapterPage();

        String currentUrl = ciaUserGuide.getCurrentUrl();
        softAssertions.assertThat(ciaUserGuide.getTabCount()).isEqualTo(2);
        softAssertions.assertThat(currentUrl).contains(Constants.SCENARIO_EXPORT_CHAPTER_URL);
        softAssertions.assertThat(ciaUserGuide.getAdminOrScenarioChapterUserGuidePageHeading(true))
            .startsWith(Constants.SCENARIO_EXPORT_CHAPTER_PAGE_TITLE);
        softAssertions.assertAll();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2985"})
    @Description("Ensure that the CI Admin Logout Link works")
    public void testCIAdminLogoutNavigation() {
        logout = new AdminLoginPage(driver)
            .login()
            .navigateToAdminLogout();

        softAssertions.assertThat(logout.isLoginButtonDisplayedAndEnabled()).isEqualTo(true);
        softAssertions.assertThat(logout.isHeaderEnabled()).isEqualTo(true);
        softAssertions.assertThat(logout.isHeaderDisplayed()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = {"2966"})
    @Description("Ensure that the link from Admin to Reports works")
    public void testAdminToReportNavigation() {
        homePage = new AdminLoginPage(driver)
            .login()
            .navigateToReports();

        homePage.waitForReportsLogoutDisplayedToAppear();

        softAssertions.assertThat(homePage.getCurrentUrl()).startsWith(PropertiesContext.get("${env}.reports.ui_url").substring(0, 71));
        softAssertions.assertThat(homePage.getTabCount()).isEqualTo(2);
        softAssertions.assertThat(homePage.isReportsWelcomeTextDisplayed()).isEqualTo(true);
        softAssertions.assertThat(homePage.isReportsWelcomeTextEnabled()).isEqualTo(true);
        softAssertions.assertAll();
    }
}
