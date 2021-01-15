package com.ootbreports.cycletimevaluetracking;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.CycleTimeValueTrackingPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.navigation.CommonReportTests;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ExplorePage;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class CycleTimeValueTrackingDetailsTests extends TestBase {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingDetailsTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "92")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "2331")
    @Description("Projects rollup drop list functionality test")
    public void testProjectRollupDropdownList() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(), CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), is(equalTo("2")));
        cycleTimeValueTrackingPage.selectProjectRollup()
                .clickOk();

        assertThat(cycleTimeValueTrackingPage.getProjectName(), is(equalTo("PROJECT 1")));
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "2332")
    @Description("Export date lists all available versions from selected export set rollup")
    public void testExportDateFilterFunctionality() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                        CycleTimeValueTrackingPage.class)
                .selectProjectRollup();

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), is(equalTo("2")));
        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("2"), is(equalTo("4")));
        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("3"), is(equalTo("1")));

        cycleTimeValueTrackingPage.clickOk();
        assertThat(cycleTimeValueTrackingPage.getProjectName(), is(equalTo("PROJECT 1")));
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "2334")
    @Description("Validate Cycle Time Value Tracking Details report aligns to CID values (where appropriate)")
    public void testValueIntegrityAgainstCID() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                        CycleTimeValueTrackingPage.class)
                .selectProjectRollup();

        cycleTimeValueTrackingPage.clickOk()
                .waitForCorrectPartName("IROBOT_18874");

        String reportsPartNumber = cycleTimeValueTrackingPage.getPartNumber();
        String reportsScenarioName = cycleTimeValueTrackingPage.getReportsValue("Scenario Name");
        String reportsFinishMass = cycleTimeValueTrackingPage.getReportsValue("Finish Mass").substring(0, 4);
        String reportsProcessGroup = cycleTimeValueTrackingPage.getReportsValue("Process Group");
        String reportsMaterialComposition = cycleTimeValueTrackingPage.getReportsValue("Material Composition");
        String reportsAnnualVolume = cycleTimeValueTrackingPage.getReportsValue("Annual Volume");
        String reportsFinalCycleTime = cycleTimeValueTrackingPage.getReportsValue("Final Cycle Time")
                .replace(",", "");

        cycleTimeValueTrackingPage.openNewCidTabAndFocus(1);

        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setRowOne("Part Name", "Contains", reportsPartNumber)
                .setRowTwo("VPE", "is", VPEEnum.APRIORI_USA.getVpe())
                .apply(ExplorePage.class)
                .openFirstScenario();

        String cidPartNumber = evaluatePage.getPartName();
        String cidScenarioName = evaluatePage.getScenarioName();
        String cidFinishMass = String.valueOf(evaluatePage.getFinishMass());
        String cidProcessGroup = evaluatePage.getSelectedProcessGroupName();
        String cidMaterialComposition = evaluatePage.getMaterialInfo();
        String cidAnnualVolume = evaluatePage.getAnnualVolume();
        String cidFinalCycleTime = String.valueOf(evaluatePage.getCycleTimeCount());

        assertThat(reportsPartNumber, is(equalTo(cidPartNumber)));
        assertThat(reportsScenarioName, is(equalTo(cidScenarioName)));
        assertThat(reportsFinishMass, is(equalTo(cidFinishMass)));
        assertThat(reportsProcessGroup, is(equalTo(cidProcessGroup)));
        assertThat(reportsMaterialComposition, is(equalTo(cidMaterialComposition)));
        assertThat(reportsAnnualVolume, is(equalTo(cidAnnualVolume)));
        assertThat(reportsFinalCycleTime, is(equalTo(cidFinalCycleTime)));
    }
}
