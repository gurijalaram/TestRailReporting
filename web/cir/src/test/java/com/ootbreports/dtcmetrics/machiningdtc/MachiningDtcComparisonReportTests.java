package com.ootbreports.dtcmetrics.machiningdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;

public class MachiningDtcComparisonReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public MachiningDtcComparisonReportTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "2024")
    @Description("Verify report availability by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3415")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3416")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3020")
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControlFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3021")
    @Description("Verify apply button on Machining DTC Comparison input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3021")
    @Description("Verify cancel button on Machining DTC Comparison input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3021")
    @Description("Verify reset button on Machining DTC Comparison input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = "3021")
    @Description("Verify save button on Machining DTC Comparison input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3565")
    @Description("Verify that earliest and latest export date fields function correctly using input field")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3567")
    @Description("Verify that earlier and latest export fields throw an error when letters and special characters are entered")
    public void testExportSetDateInputInvalidCharacters() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterInvalidCharacters(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3566")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "3023")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
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
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
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
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
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
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3029")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3029")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
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
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
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
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3029")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreAll() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.ALL.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "2039")
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testComponentCostDetailReportLink() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        String partName = genericReportPage.clickMachiningDtcComparisonBar();

        assertThat(
            genericReportPage.getUpperTitleText(),
            is(equalTo(ReportNamesEnum.DTC_PART_SUMMARY.getReportName()))
        );
        assertThat(partName, is(startsWith(genericReportPage.getDtcPartSummaryPartNameValue())));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3031")
    @Description("Verify Select Parts list controls function correctly")
    public void testPartListInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
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
        inputControlsTests.testMinimumAnnualSpendComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3025")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingIssues() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"DTCMACHINING_001 (Toleranced)", "MACHININGDESIGN_TO_COST (Initial)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3025")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlDesignStandards() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"DTCMACHINING_001 (Toleranced)", "PUNCH (Initial)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.DESIGN_STANDARDS.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3025")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlTolerances() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"MACHININGDESIGN_TO_COST (Initial)", "PARTBODY_1 (Initial)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.TOLERANCES.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3025")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlSlowOperations() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"MACHININGDESIGN_TO_COST (Initial)", "DTCMACHINING_001 (Toleranced)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3025")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[]{"PMI_ROUGHNESSCREO (Initial)", "PMI_PROFILEOFSURFACECREO (Initial)"};
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(),
            partNames
        );
    }
}
