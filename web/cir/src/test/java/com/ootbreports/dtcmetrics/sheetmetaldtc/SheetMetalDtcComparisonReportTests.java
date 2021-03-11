package com.ootbreports.dtcmetrics.sheetmetaldtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.SheetMetalDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class SheetMetalDtcComparisonReportTests extends TestBase {

    private SheetMetalDtcReportPage sheetMetalDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public SheetMetalDtcComparisonReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "92")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.DTC_METRICS_FOLDER,
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }


    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3038")
    @Description("Verify Export Sets are available for selection")
    public void testExportSetAndRollupSelection() {
        String rollupName = String.format(
                "%s (%s)",
                ReportNamesEnum.SHEET_METAL_DTC.getReportName().toUpperCase(),
                Constants.DEFAULT_SCENARIO_NAME
        );
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())
                .waitForCorrectRollupInDropdown(rollupName)
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getDisplayedRollup(), is(equalTo(rollupName)));
        assertThat(sheetMetalDtcReportPage.getDisplayedExportSet(),
                is(equalTo(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3039")
    @Description("Verify earliest and latest export date calendar widgets correctly filter the list of export sets")
    public void testExportSetDateFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3041")
    @Description("Verify Input Controls panel buttons function correctly (Apply, OK, Reset, Cancel, Save) - Apply")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                RollupEnum.SHEET_METAL_DTC.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3041")
    @Description("Verify Input Controls panel buttons function correctly (Apply, OK, Reset, Cancel, Save) - Reset")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3041")
    @Description("Verify Input Controls panel buttons function correctly (Apply, OK, Reset, Cancel, Save) - Cancel")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3040")
    @Description("Verify Export Set list controls function correctly - Panel Buttons")
    public void testExportSetListControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3040")
    @Description("Verify Export Set list controls function correctly - Search")
    public void testExportSetSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSearch(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3042")
    @Description("Verify Roll-up input control functions correctly")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                RollupEnum.SHEET_METAL_DTC.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3043")
    @Description("Verify cost metric input control functions correctly - Piece Part Cost")
    public void testCostMetricPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3043")
    @Description("Verify cost metric input control functions correctly - Fully Burdened Cost")
    public void testCostMetricFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3044")
    @Description("Verify Mass Metric input control functions correctly - Finish Mass")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3044")
    @Description("Verify Mass Metric input control functions correctly - Rough Mass")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues")
    public void testSortOrderManufacturingIssues() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"1271576 (Bulkload)", "BRACKET_V1 (rev1)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum(),
                partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly - Bends")
    public void testSortOrderBends() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"BRACKET_SHORTENED (rev1)", "BRACKET_SHORTENED_ISSUES (Initial)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                SortOrderEnum.BENDS.getSortOrderEnum(),
                partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly - Tolerances")
    public void testSortOrderTolerances() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"BRACKET_V1 (rev1)", "BRACKET_V2 (rev1)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                SortOrderEnum.TOLERANCES.getSortOrderEnum(),
                partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly - Machining Time")
    public void testSortOrderMachiningTime() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"1271576 (Bulkload)", "BRACKET_V3 (rev1)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                SortOrderEnum.MACHINING_TIME.getSortOrderEnum(),
                partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly - Annual Spend")
    public void testSortOrderAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"1271576 (Bulkload)", "3575137 (Bulkload)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(),
                partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3046")
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCodeInputControl() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCodeDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3048")
    @Description("Verify Process Group input control functions correctly - No Selection")
    public void testProcessGroupInputControlNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testProcessGroupInputControlNoSelection(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3048")
    @Description("Verify Process Group input control functions correctly - Single Selection")
    public void testSingleProcessGroup() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3051")
    @Description("Verify Select Parts list controls function correctly - Panel Buttons")
    public void testPartListInputControlButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                ListNameEnum.PARTS.getListName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3049")
    @Description("Verify DTC Score input control functions correctly - No Selection")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3049")
    @Description("Verify DTC Score input control functions correctly - All")
    public void testDtcScoreAll() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                DtcScoreEnum.ALL.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3049")
    @Description("Verify DTC Score input control functions correctly - Low")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3049")
    @Description("Verify DTC Score input control functions correctly - Medium")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3049")
    @Description("Verify DTC Score input control functions correctly - High")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }
}
