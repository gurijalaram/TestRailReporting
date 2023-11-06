package com.apriori.cia.ui.tests.navigation;

import com.apriori.cia.ui.pageobjects.cirpages.CirUserGuidePage;
import com.apriori.cia.ui.pageobjects.homepage.AdminHomePage;
import com.apriori.cia.ui.pageobjects.login.AdminLoginPage;
import com.apriori.cia.ui.pageobjects.logout.AdminLogoutPage;
import com.apriori.cia.ui.pageobjects.manage.ScenarioExport;
import com.apriori.cia.ui.pageobjects.manage.SystemDataExport;
import com.apriori.cia.ui.pageobjects.userguides.CiaUserGuide;
import com.apriori.cia.ui.utils.Constants;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class AdminNavigationTests extends TestBaseUI {

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
    @TestRail(id = {2980})
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
    @TestRail(id = {2981})
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
    @TestRail(id = {2982})
    @Description("Ensure that the Help Cost Insight Report Guide Link works")
    public void testHelpCostInsightReportGuideNavigation() {
        cirUserGuide = new AdminLoginPage(driver)
            .login()
            .navigateToHelpReportsGuide()
            .switchTab();

        softAssertions.assertThat(cirUserGuide.getReportsUserGuidePageHeading().trim()).startsWith("Cost Insight Report");
        softAssertions.assertThat(cirUserGuide.getCurrentUrl()).contains("CIR_UG");
        softAssertions.assertThat(cirUserGuide.getTabCount()).isEqualTo(2);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {2983})
    @Description("Ensure that the Help Cost Insight Admin Guide Link works")
    public void testHelpCostInsightAdminGuideNavigation() {
        ciaUserGuide = new AdminLoginPage(driver)
            .login()
            .navigateToHelpAdminGuide();

        softAssertions.assertThat(ciaUserGuide.getTabCount()).isEqualTo(2);
        softAssertions.assertThat(ciaUserGuide.getCurrentUrl()).contains(Constants.CIA_USER_GUIDE_URL_SUBSTRING);
        softAssertions.assertThat(
            ciaUserGuide.getAdminOrScenarioChapterUserGuidePageHeading(false))
            .contains(Constants.CIA_USER_GUIDE_TITLE);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {2984})
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
    @TestRail(id = {2985})
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
    @TestRail(id = {2966})
    @Description("Ensure that the link from Admin to Reports works")
    public void testAdminToReportNavigation() {
        homePage = new AdminLoginPage(driver)
            .login()
            .navigateToReports();

        homePage.waitForReportsLogoutDisplayedToAppear();

        softAssertions.assertThat(homePage.getCurrentUrl()).startsWith(PropertiesContext.get("reports.ui_url"));
        softAssertions.assertThat(homePage.getTabCount()).isEqualTo(2);
        softAssertions.assertThat(homePage.isReportsWelcomeTextDisplayed()).isEqualTo(true);
        softAssertions.assertThat(homePage.isReportsWelcomeTextEnabled()).isEqualTo(true);
        softAssertions.assertAll();
    }
}
