package com.ootbreports.dtcmetrics.machiningdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DateElementsEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class MachiningDtcReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public MachiningDtcReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2024")
    @Description("Verify report availability by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            Constants.DTC_METRICS_FOLDER,
            ReportNamesEnum.MACHINING_DTC.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "3415")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "3416")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "3026")
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCodeDtcReports(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class, OnPremTest.class})
    @TestRail(testCaseId = "3567")
    @Description("Verify that earlier and latest export fields throw an error when letters and special characters are entered")
    public void testExportSetDateInputInvalidCharacters() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterInvalidCharacters(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3565")
    @Description("Verify that earliest and latest export date fields function correctly using input field")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3566")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "3020")
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControlFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3021")
    @Description("Verify apply button on Machining DTC input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3021")
    @Description("Verify cancel button on Machining DTC input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3021")
    @Description("Verify reset button on Machining DTC input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = "3021")
    @Description("Verify save button on Machining DTC input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2026")
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
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3022")
    @Description("Verify Roll-up input control functions correctly")
    public void testRollupDropDown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1690")
    @Description("Verify export sets are available for selection")
    public void testExportSetAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailability(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3023")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningSheetMetalDtc(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3023")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningSheetMetalDtc(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3028")
    @Description("Verify Process Group input control functions correctly")
    public void testProcessGroupStockMachiningOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3028")
    @Description("Verify Process Group input control functions correctly")
    public void testProcessGroupTwoModelMachiningOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3028")
    @Description("Verify Process Group input control functions correctly")
    public void testProcessGroupSandAndDieCasting() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTwoProcessGroupsMachining();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3029")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3029")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3029")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3029")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "2039")
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testComponentCostDetailReportLink() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        genericReportPage.hoverMachiningBubbleTwice();
        genericReportPage.waitForCorrectPartNameMachiningDtc(false);
        genericReportPage.setReportName(ReportNamesEnum.MACHINING_DTC.getReportName());
        String partName = genericReportPage.getPartNameDtcReports();

        assertThat(
            genericReportPage.getPartNameDtcReports(),
            is(equalTo(Constants.PART_NAME_EXPECTED_MACHINING_DTC))
        );

        genericReportPage.clickMachiningBubbleAndSwitchTab();

        assertThat(
            genericReportPage.getUpperTitleText(),
            is(equalTo(ReportNamesEnum.DTC_PART_SUMMARY.getReportName()))
        );
        assertThat(partName, is(startsWith(genericReportPage.getDtcPartSummaryPartNameValue())));
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "3572")
    @Description("Verify that hours value greater than hours in day in both earliest and latest export date field fails")
    public void testInvalidHourValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.HOUR.getDateElement()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "3573")
    @Description("Verify that minutes value greater than 60 minutes in both earliest and latest export date field fails")
    public void testInvalidMinuteValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.MINUTE.getDateElement()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = "3575")
    @Description("Verify that invalid date (year) fails in both earliest and latest export date field")
    public void testInvalidYearValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.YEAR.getDateElement()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3576")
    @Description("Verify that invalid date (month) fails in both earliest and latest export date field")
    public void testInvalidMonthValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.MONTH.getDateElement()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3577")
    @Description("Verify that invalid date (day) fails in both earliest and latest export date field")
    public void testInvalidDayValueExportSetFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testInvalidExportSetFilterDateInputs(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            DateElementsEnum.DAY.getDateElement()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3031")
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
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3027")
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpend(
            ReportNamesEnum.MACHINING_DTC.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2027")
    @Description("Verify Select Parts list is correctly filtered by input control")
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
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2027")
    @Description("Verify Select Parts list is correctly filtered by input control")
    public void testPartListFilterByInputControlsExportSets() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "0");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("0")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2027")
    @Description("Verify Select Parts list is correctly filtered by input control")
    public void testPartListFilterByInputControlsRollup() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "0");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("0")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2027")
    @Description("Verify Select Parts list is correctly filtered by input control")
    public void testPartListFilterByInputControlsMinimumAnnualSpend() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName());

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
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2027")
    @Description("Verify Select Parts list is correctly filtered by input control")
    public void testPartListFilterByInputControlsProcessGroup() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
            .setProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "0");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("0")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2027")
    @Description("Verify Select Parts list is correctly filtered by input control")
    public void testPartListFilterByInputControlsDtcScore() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "43");

        genericReportPage.setDtcScore(DtcScoreEnum.LOW.getDtcScoreName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.PARTS.getListName(), "Available: ", "31");

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"),
            is(equalTo("31")));
    }
}
