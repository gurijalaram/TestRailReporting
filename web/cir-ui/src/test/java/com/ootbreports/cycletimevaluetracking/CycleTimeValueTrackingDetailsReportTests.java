package com.ootbreports.cycletimevaluetracking;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.CycleTimeValueTrackingPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;

public class CycleTimeValueTrackingDetailsReportTests extends TestBase {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingDetailsReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7325"})
    @Description("Validate report available by navigation - Cycle Time Value Tracking Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = {"7327"})
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7239"})
    @Description("Verify report availability by search - Cycle Time Value Tracking Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7628"})
    @Description("Projects rollup drop list functionality test - Cycle Time Value Tracking Details Report")
    public void testProjectRollupDropdownList() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(
                        ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                        CycleTimeValueTrackingPage.class
                );

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), is(equalTo("2")));
        cycleTimeValueTrackingPage.selectProjectRollup()
                .clickOk(CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getProjectName(), is(equalTo("PROJECT 1")));
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = {"7627"})
    @Description("Export date lists all available versions from selected export set rollup - Cycle Time Value Tracking Details Report")
    public void testExportDateFilterFunctionality() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(
                        ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                        CycleTimeValueTrackingPage.class)
                .selectProjectRollup();

        assertThat(Integer.parseInt(cycleTimeValueTrackingPage.getCountOfDropdownItems("1")),
                is(greaterThan(1)));
        assertThat(Integer.parseInt(cycleTimeValueTrackingPage.getCountOfDropdownItems("2")),
                is(greaterThan(3)));
        assertThat(Integer.parseInt(cycleTimeValueTrackingPage.getCountOfDropdownItems("3")),
                is(greaterThan(0)));

        cycleTimeValueTrackingPage.clickOk(CycleTimeValueTrackingPage.class);
        assertThat(cycleTimeValueTrackingPage.getProjectName(), is(equalTo("PROJECT 1")));
    }

    @Test
    //@Category(ReportsTest.class)
    @TestRail(testCaseId = {"2334"})
    @Description("Validate Cycle Time Value Tracking Details report aligns to CID values (where appropriate)")
    public void testValueIntegrityAgainstCID() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                        CycleTimeValueTrackingPage.class)
                .selectProjectRollup();

        cycleTimeValueTrackingPage.clickOk(CycleTimeValueTrackingPage.class)
                .waitForCorrectPartName("IROBOT_18874");

        String reportsPartNumber = cycleTimeValueTrackingPage.getPartNumber();
        String reportsScenarioName = cycleTimeValueTrackingPage.getReportsValue("Scenario Name");
        String reportsFinishMass = cycleTimeValueTrackingPage.getReportsValue("Finish Mass").substring(0, 4);
        String reportsProcessGroup = cycleTimeValueTrackingPage.getReportsValue("Process Group");
        String reportsMaterialComposition = cycleTimeValueTrackingPage.getReportsValue("Material Composition");
        String reportsAnnualVolume = cycleTimeValueTrackingPage.getReportsValue("Annual Volume");
        String reportsFinalCycleTime = cycleTimeValueTrackingPage.getReportsValue("Final Cycle Time")
                .replace(",", "");

        //cycleTimeValueTrackingPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .saveAs()
                .inputName(new GenerateStringUtil().generateFilterName())
                .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.EQUALS, reportsPartNumber)
                .addCriteria(PropertyEnum.DIGITAL_FACTORY,OperationEnum.IN, DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                .submit(ExplorePage.class)
                .openFirstScenario();

        String cidPartNumber = evaluatePage.getPartName();
        String cidScenarioName = evaluatePage.getCurrentScenarioName();
        String cidFinishMass = String.valueOf(evaluatePage.getFinishMass());
        String cidProcessGroup = evaluatePage.getSelectedProcessGroup();
        String cidMaterialComposition = evaluatePage
                .openMaterialProcess().openMaterialUtilizationTab().getMaterialName();
        String cidAnnualVolume = evaluatePage.getAnnualVolume();
        String cidFinalCycleTime = String.valueOf(evaluatePage.getProcessesResult("Total Cycle Time"));

        assertThat(reportsPartNumber, is(equalTo(cidPartNumber)));
        assertThat(reportsScenarioName, is(equalTo(cidScenarioName)));
        assertThat(reportsFinishMass, is(equalTo(cidFinishMass)));
        assertThat(reportsProcessGroup, is(equalTo(cidProcessGroup)));
        assertThat(reportsMaterialComposition, is(equalTo(cidMaterialComposition)));
        assertThat(reportsAnnualVolume, is(equalTo(cidAnnualVolume)));
        assertThat(reportsFinalCycleTime, is(equalTo(cidFinalCycleTime)));
    }
}
