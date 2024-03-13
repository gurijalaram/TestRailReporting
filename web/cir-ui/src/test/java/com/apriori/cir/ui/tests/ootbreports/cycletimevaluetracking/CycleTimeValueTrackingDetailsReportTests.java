package com.apriori.cir.ui.tests.ootbreports.cycletimevaluetracking;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.CycleTimeValueTrackingPage;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CycleTimeValueTrackingDetailsReportTests extends TestBaseUI {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7325")
    @TestRail(id = {7325})
    @Description("Validate report available by navigation - Cycle Time Value Tracking Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7327")
    @TestRail(id = {7327})
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7329")
    @TestRail(id = {7239})
    @Description("Verify report availability by search - Cycle Time Value Tracking Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7628")
    @TestRail(id = {7628})
    @Description("Projects rollup drop list functionality test - Cycle Time Value Tracking Details Report")
    public void testProjectRollupDropdownList() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
                CycleTimeValueTrackingPage.class
            );

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), anyOf(equalTo("2"), equalTo("3")));
        cycleTimeValueTrackingPage.selectProjectRollup()
            .clickOk(CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getProjectName(), is(equalTo("PROJECT 1")));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7627")
    @TestRail(id = {7627})
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
    @Tag(REPORTS)
    @TmsLink("2334")
    @TestRail(id = {2334})
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

        cycleTimeValueTrackingPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.EQUALS, reportsPartNumber)
            .addCriteria(PropertyEnum.DIGITAL_FACTORY, OperationEnum.IN, DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
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
