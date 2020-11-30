package com.ootbreports.dtcmetrics.sheetmetaldtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.SheetMetalDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
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
import testsuites.suiteinterface.CiaCirTestDevTest;
import utils.Constants;

public class SheetMetalDtcComparisonReportTests extends TestBase {

    private SheetMetalDtcReportPage sheetMetalDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public SheetMetalDtcComparisonReportTests() {
        super();
    }

    @Test
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
    @TestRail(testCaseId = "3039")
    @Description("Verify earliest and latest export date calendar widgets correctly filter the list of export sets")
    public void testExportSetDateFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "3041")
    @Description("Verify Input Controls panel buttons function correctly (Apply, OK, Reset, Cancel, Save)")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                RollupEnum.SHEET_METAL_DTC.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "3041")
    @Description("Verify Input Controls panel buttons function correctly (Apply, OK, Reset, Cancel, Save)")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3041")
    @Description("Verify Input Controls panel buttons function correctly (Apply, OK, Reset, Cancel, Save)")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3040")
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3040")
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSearch(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
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
    @TestRail(testCaseId = "3043")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "3043")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "3044")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "3044")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderManufacturingIssues() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.machiningDtcComparisonSortOrderTest(
                SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum(),
                "1271576 (Bulkload)",
                "BRACKET_V1 (rev1)"
        );
    }

    @Test
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderBends() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.machiningDtcComparisonSortOrderTest(
                SortOrderEnum.BENDS.getSortOrderEnum(),
                "BRACKET_SHORTENED (rev1)",
                "BRACKET_SHORTENED_ISSUES (Initial)"
        );
    }

    @Test
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderTolerances() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.machiningDtcComparisonSortOrderTest(
                SortOrderEnum.TOLERANCES.getSortOrderEnum(),
                "BRACKET_V1 (rev1)",
                "BRACKET_V2 (rev1)"
        );
    }

    @Test
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderMachiningTime() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.machiningDtcComparisonSortOrderTest(
                SortOrderEnum.MACHINING_TIME.getSortOrderEnum(),
                "1271576 (Bulkload)",
                "BRACKET_V3 (rev1)"
        );
    }

    @Test
    @TestRail(testCaseId = "3045")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.machiningDtcComparisonSortOrderTest(
                SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(),
                "1271576 (Bulkload)",
                "3575137 (Bulkload)"
        );
    }
}
