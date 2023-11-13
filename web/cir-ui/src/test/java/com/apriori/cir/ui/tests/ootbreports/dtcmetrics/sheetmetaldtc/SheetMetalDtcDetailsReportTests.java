package com.apriori.cir.ui.tests.ootbreports.dtcmetrics.sheetmetaldtc;

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
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SheetMetalDtcDetailsReportTests extends TestBaseUI {

    private SheetMetalDtcReportPage sheetMetalDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public SheetMetalDtcDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7314})
    @Description("Validate report is available by navigation - Sheet Metal DTC Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7317})
    @Description("Validate report is available by library - Sheet Metal DTC Details Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7320})
    @Description("Validate report is available by search - Sheet Metal DTC Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3038})
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
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), SheetMetalDtcReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), SheetMetalDtcReportPage.class)
            .waitForCorrectRollupInDropdown(rollupName)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getDisplayedRollup(), is(equalTo(rollupName)));
        assertThat(sheetMetalDtcReportPage.getDisplayedExportSet(),
            is(equalTo(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3039})
    @Description("Verify earliest and latest export date calendar widgets correctly filter the list of export sets")
    public void testExportSetDateFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7368})
    @Description("Verify apply button functionality - Sheet Metal DTC Details Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            RollupEnum.SHEET_METAL_DTC.getRollupName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7371})
    @Description("Verify cancel button functionality - Sheet Metal DTC Details Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7374})
    @Description("Verify reset button functionality - Sheet Metal DTC Details Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName());
    }

    @Test
    @Disabled("not applicable due to reports configuration")
    @TestRail(id = {7377})
    @Description("Verify save button functionality - Sheet Metal DTC Details Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7695})
    @Description("Verify Export Set list controls function correctly - Panel Buttons - Sheet Metal DTC Details Reports")
    public void testExportSetListControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7693})
    @Description("Verify Export Set list controls function correctly - Search - Sheet Metal DTC Details Reports")
    public void testExportSetSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSearch(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3042})
    @Description("Verify Roll-up input control functions correctly")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            RollupEnum.SHEET_METAL_DTC.getRollupName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7421})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7422})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Details Report")
    public void testCostMetricFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7401})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7402})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7682})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Issues - Sheet Metal DTC Details Report")
    public void testSortOrderManufacturingIssues() {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum())
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("1271576")));
        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("BRACKET_V1")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7681})
    @Description("Verify Sort Order input control functions correctly - Bends- Sheet Metal DTC Details Report")
    public void testSortOrderBends() {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.BENDS.getSortOrderEnum())
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("BRACKET_SHORTENED")));
        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("BRACKET_SHORTENED_ISSUES")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7677})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Sheet Metal DTC Details Report")
    public void testSortOrderTolerances() {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.TOLERANCES.getSortOrderEnum())
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("BRACKET_V1")));
        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("BRACKET_V2")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7678})
    @Description("Verify Sort Order input control functions correctly - Machining Time - Sheet Metal DTC Details Report")
    public void testSortOrderMachiningTime() {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.MACHINING_TIME.getSortOrderEnum())
            .clickOk(SheetMetalDtcReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("1271576")));
        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("BRACKET_V3")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7679})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Details Report")
    public void testSortOrderAnnualSpend() {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum())
            .clickOk(SheetMetalDtcReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("1271576")));
        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("3575137")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7680})
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Sheet Metal DTC Details Report")
    public void testSortOrderDtcRank() {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.DTC_RANK.getSortOrderEnum())
            .clickOk(SheetMetalDtcReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("BRACKET_SHORTENED")));
        assertThat(sheetMetalDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("BRACKET_V1")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7379})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Details Report")
    public void testCurrencyCodeInputControl() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCodeDtcReports(
            ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7450})
    @Description("Verify process group input control functionality - Single Selection - Sheet Metal DTC Details Report ")
    public void testProcessGroupInputControlNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testProcessGroupInputControlNoSelection(
            ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3048})
    @Description("Verify Process Group input control functions correctly - Single Selection")
    public void testSingleProcessGroup() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            ProcessGroupEnum.SHEET_METAL.getProcessGroup());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3051})
    @Description("Verify Select Parts list controls function correctly - Panel Buttons")
    public void testPartListInputControlButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            ListNameEnum.PARTS_NO_SPACE.getListName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7531})
    @Description("Verify DTC Score Input Control - No Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7534})
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7537})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7540})
    @Description("Verify DTC Score Input Control - High Selection - Sheet Metal DTC Details Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName());
    }
}