package com.apriori.cir.ui.tests.ootbreports.dtcmetrics.sheetmetaldtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.enums.SortOrderEnum;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.SheetMetalDtcReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ListNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class SheetMetalDtcComparisonReportTests extends TestBaseUI {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public SheetMetalDtcComparisonReportTests() {
        super();
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7313")
    @TestRail(id = {7313})
    @Description("Validate report is available by navigation - Sheet Metal DTC Comparison Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7316")
    @TestRail(id = {7316})
    @Description("Validate report is available by library - Sheet Metal DTC Comparison Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7319")
    @TestRail(id = {7319})
    @Description("Validate report is available by search - Sheet Metal DTC Comparison Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3038")
    @TestRail(id = {3038})
    @Description("Verify Export Sets are available for selection")
    public void testExportSetAndRollupSelection() {
        String rollupName = String.format(
            "%s (%s)",
            ReportNamesEnum.SHEET_METAL_DTC.getReportName().toUpperCase(),
            Constants.DEFAULT_SCENARIO_NAME
        );
        SheetMetalDtcReportPage sheetMetalDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(), SheetMetalDtcReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), SheetMetalDtcReportPage.class)
            .waitForCorrectRollupInDropdown(rollupName)
            .clickOk(SheetMetalDtcReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getDisplayedRollup(), is(equalTo(rollupName)));
        assertThat(sheetMetalDtcReportPage.getDisplayedExportSet(),
            is(equalTo(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3039")
    @TestRail(id = {3039})
    @Description("Verify earliest and latest export date calendar widgets correctly filter the list of export sets")
    public void testExportSetDateFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7367")
    @TestRail(id = {7367})
    @Description("Verify apply button functionality - Sheet Metal DTC Comparison Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            RollupEnum.SHEET_METAL_DTC.getRollupName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7370")
    @TestRail(id = {7370})
    @Description("Verify cancel button functionality - Sheet Metal DTC Comparison Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7373")
    @TestRail(id = {7373})
    @Description("Verify reset button functionality - Sheet Metal DTC Comparison Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @Disabled("Not applicable due to reports configuration")
    @TmsLink("7376")
    @TestRail(id = {7376})
    @Description("Verify save button functionality - Sheet Metal DTC Comparison Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7694")
    @TestRail(id = {7694})
    @Description("Verify Export Set list controls function correctly - Panel Buttons - Sheet Metal DTC Comparison Reports")
    public void testExportSetListControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7691")
    @TestRail(id = {7691})
    @Description("Verify Export Set list controls function correctly - Search - Sheet Metal DTC Comparison Reports")
    public void testExportSetSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSearch(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3042")
    @TestRail(id = {3042})
    @Description("Verify Roll-up input control functions correctly")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            RollupEnum.SHEET_METAL_DTC.getRollupName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7419")
    @TestRail(id = {7419})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Comparison Report")
    public void testCostMetricPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7420")
    @TestRail(id = {7420})
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Comparison Report")
    public void testCostMetricFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7399")
    @TestRail(id = {7399})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7400")
    @TestRail(id = {7400})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7672")
    @TestRail(id = {7672})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Comparison Report")
    public void testSortOrderManufacturingIssues() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {"1271576 (Bulkload)", "BRACKET_V1 (rev1)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7673")
    @TestRail(id = {7673})
    @Description("Verify Sort Order input control functions correctly - Bends - Sheet Metal DTC Comparison Report")
    public void testSortOrderBends() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {"BRACKET_SHORTENED (rev1)", "BRACKET_SHORTENED_ISSUES (Initial)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            SortOrderEnum.BENDS.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7674")
    @TestRail(id = {7674})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Comparison Report")
    public void testSortOrderTolerances() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {"BRACKET_V1 (rev1)", "BRACKET_V2 (rev1)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            SortOrderEnum.TOLERANCES.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7675")
    @TestRail(id = {7675})
    @Description("Verify Sort Order input control functions correctly - Machining Times - Sheet Metal DTC Comparison Report")
    public void testSortOrderMachiningTime() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {"1271576 (Bulkload)", "BRACKET_V3 (rev1)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            SortOrderEnum.MACHINING_TIME.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7676")
    @TestRail(id = {7676})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Comparison Report")
    public void testSortOrderAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {"1271576 (Bulkload)", "3575137 (Bulkload)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7378")
    @TestRail(id = {7378})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Comparison Report")
    public void testCurrencyCodeInputControl() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCodeDtcReports(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7449")
    @TestRail(id = {7449})
    @Description("Verify process group input control functionality - No Selection - Sheet Metal DTC Comparison Report")
    public void testProcessGroupInputControlNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testProcessGroupInputControlNoSelection(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3048")
    @TestRail(id = {3048})
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
    @Tag(REPORTS)
    @TmsLink("3051")
    @TestRail(id = {3051})
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
    @Tag(REPORTS)
    @TmsLink("7530")
    @TestRail(id = {7530})
    @Description("Verify DTC Score Input Control - No Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7533")
    @TestRail(id = {7533})
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7536")
    @TestRail(id = {7536})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7539")
    @TestRail(id = {7539})
    @Description("Verify DTC Score Input Control - High Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7541")
    @TestRail(id = {7541})
    @Description("Verify DTC Score Input Control - All Selection - Sheet Metal DTC Comparison Report")
    public void testDtcScoreAll() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            DtcScoreEnum.ALL.getDtcScoreName()
        );
    }
}
