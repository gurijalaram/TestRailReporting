package com.apriori.cir.ui.tests.ootbreports.dtcmetrics.machiningdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DateElementsEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.MachiningDtcReportPage;
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

public class MachiningDtcReportTests extends TestBaseUI {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public MachiningDtcReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("2024")
    @TestRail(id = {2024})
    @Description("Validate report is available by navigation - Machining DTC Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.MACHINING_DTC.getReportName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3415")
    @TestRail(id = {3415})
    @Description("Validate report is available by library - Machining DTC Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3416")
    @TestRail(id = {3416})
    @Description("Validate report is available by search - Machining DTC Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3026")
    @TestRail(id = {3026})
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCodeDtcReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3567")
    @TestRail(id = {3567})
    @Description("Verify that earlier and latest export fields throw an error when letters and special characters are entered")
    public void testExportSetDateInputInvalidCharacters() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterInvalidCharacters(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3566")
    @TestRail(id = {3566})
    @Description("Verify export date filters correctly filters export sets - Picker - Machining DTC Report")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3565")
    @TestRail(id = {3565})
    @Description("Verify export date filters correctly filters export sets - Input - Machining DTC Report ")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.MACHINING_DTC.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3020")
    @TestRail(id = {3020})
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControlFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7330")
    @TestRail(id = {7330})
    @Description("Verify apply button functionality - Machining DTC Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7333")
    @TestRail(id = {7333})
    @Description("Verify cancel button functionality - Machining DTC Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7336")
    @TestRail(id = {7336})
    @Description("Verify reset button functionality - Machining DTC Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @Disabled("Not applicable due to reports configuration")
    @TmsLink("7339")
    @TestRail(id = {7339})
    @Description("Verify save button functionality - Machining DTC Report")
    public void testSaveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("2026")
    @TestRail(id = {2026})
    @Description("Verify Export Sets are available for selection")
    public void testExportSetSelectionAndAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailabilityAndSelection(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3022")
    @TestRail(id = {3022})
    @Description("Verify Roll-up input control functions correctly")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1690")
    @TestRail(id = {1690})
    @Description("Verify export sets are available for selection")
    public void testExportSetAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailability(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3023")
    @TestRail(id = {3023})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningSheetMetalDtc(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7413")
    @TestRail(id = {7413})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningSheetMetalDtc(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3024")
    @TestRail(id = {3024})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7393")
    @TestRail(id = {7393})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7452")
    @TestRail(id = {7452})
    @Description("Verify process group input control functionality - Stock Machining - Machining DTC Report")
    public void testProcessGroupStockMachiningOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7451")
    @TestRail(id = {7451})
    @Description("Verify process group input control functionality - 2 Model Machining - Machining DTC Report")
    public void testProcessGroupTwoModelMachiningOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7456")
    @TestRail(id = {7456})
    @Description("Verify process group input control functionality - 2 Model and Stock Machining - Machining DTC Report")
    public void testProcessGroupSandAndDieCasting() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTwoProcessGroupsMachining();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7457")
    @TestRail(id = {7457})
    @Description("Verify DTC Score Input Control - No Selection - Machining DTC Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7460")
    @TestRail(id = {7460})
    @Description("Verify DTC Score Input Control - Low Selection - Machining DTC Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7498")
    @TestRail(id = {7498})
    @Description("Verify DTC Score Input Control - Medium Selection - Machining DTC Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7501")
    @TestRail(id = {7501})
    @Description("Verify DTC Score Input Control - High Selection - Machining DTC Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("2039")
    @TestRail(id = {2039})
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testComponentCostDetailReportLink() {
        MachiningDtcReportPage machiningDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), MachiningDtcReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), MachiningDtcReportPage.class)
            .clickOk(MachiningDtcReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), MachiningDtcReportPage.class);

        machiningDtcReportPage.hoverMachiningBubbleTwice();
        machiningDtcReportPage.waitForCorrectPartNameMachiningDtc(false);
        machiningDtcReportPage.setReportName(ReportNamesEnum.MACHINING_DTC.getReportName());
        String partName = machiningDtcReportPage.getPartNameDtcReports();

        assertThat(
            machiningDtcReportPage.getPartNameDtcReports(),
            is(equalTo(Constants.PART_NAME_EXPECTED_MACHINING_DTC))
        );

        machiningDtcReportPage.clickMachiningBubbleAndSwitchTab();

        assertThat(
            machiningDtcReportPage.getUpperTitleText(),
            is(equalTo(ReportNamesEnum.DTC_PART_SUMMARY.getReportName()))
        );
        assertThat(partName, is(startsWith(machiningDtcReportPage.getDtcPartSummaryPartNameValue())));
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3572")
    @TestRail(id = {3572})
    @Description("Verify that hours value greater than hours in day in both earliest and latest export date field fails")
    public void testInvalidHourValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.HOUR.getDateElement()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3573")
    @TestRail(id = {3573})
    @Description("Verify that minutes value greater than 60 minutes in both earliest and latest export date field fails")
    public void testInvalidMinuteValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.MINUTE.getDateElement()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3575")
    @TestRail(id = {3575})
    @Description("Verify that invalid date (year) fails in both earliest and latest export date field")
    public void testInvalidYearValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.YEAR.getDateElement()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3576")
    @TestRail(id = {3576})
    @Description("Verify that invalid date (month) fails in both earliest and latest export date field")
    public void testInvalidMonthValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.MONTH.getDateElement()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("3577")
    @TestRail(id = {3577})
    @Description("Verify that invalid date (day) fails in both earliest and latest export date field")
    public void testInvalidDayValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.DAY.getDateElement()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3031")
    @TestRail(id = {3031})
    @Description("Verify Select Parts list controls function correctly")
    public void testPartListInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            ListNameEnum.PARTS.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("3027")
    @TestRail(id = {3027})
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpend(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7658")
    @TestRail(id = {7658})
    @Description("Verify Select Parts list is correctly filtered by input control - Export Dates - Machining DTC Report ")
    public void testPartListFilterByInputControlsExportDates() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .setExportDateUsingInput(true, "")
            .clickUseLatestExportDropdownTwice()
            .waitForCorrectExportSetListCount("Single export set selection.", "0");

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "0");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("0")));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7659")
    @TestRail(id = {7659})
    @Description("Verify Select Parts list is correctly filtered by input control - Export Set - Machining DTC Report")
    public void testPartListFilterByInputControlsExportSets() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class);

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "0");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("0")));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7660")
    @TestRail(id = {7660})
    @Description("Verify Select Parts list is correctly filtered by input control - Rollup - Machining DTC Report")
    public void testPartListFilterByInputControlsRollup() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class);

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "0");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("0")));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7661")
    @TestRail(id = {7661})
    @Description("Verify Select Parts list is correctly filtered by input control - Min. Annual Spend - Machining DTC Report")
    public void testPartListFilterByInputControlsMinimumAnnualSpend() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class);

        assertThat(genericReportPage.getSelectedRollup(RollupEnum.DTC_MACHINING_DATASET.getRollupName()),
            is(equalTo(RollupEnum.DTC_MACHINING_DATASET.getRollupName())));

        genericReportPage
            .inputMinimumAnnualSpend()
            .clickUseLatestExportDropdownTwice();

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "18");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("18")));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7662")
    @TestRail(id = {7662})
    @Description("Verify Select Parts list is correctly filtered by input control - Process Group - Machining DTC Report")
    public void testPartListFilterByInputControlsProcessGroup() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class)
            .setProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "0");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("0")));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7663")
    @TestRail(id = {7663})
    @Description("Verify Select Parts list is correctly filtered by input control - DTC Score - Machining DTC Report")
    public void testPartListFilterByInputControlsDtcScore() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class);

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "43");

        genericReportPage.setDtcScore(DtcScoreEnum.LOW.getDtcScoreName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "31");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("31")));
    }
}
