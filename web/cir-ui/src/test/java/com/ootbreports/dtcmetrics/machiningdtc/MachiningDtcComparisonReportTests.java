package com.ootbreports.dtcmetrics.machiningdtc;

import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ListNameEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.login.ReportsLoginPage;
import com.apriori.pageobjects.view.reports.GenericReportPage;
import com.apriori.pageobjects.view.reports.MachiningDtcReportPage;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import enums.CostMetricEnum;
import enums.DtcScoreEnum;
import enums.MassMetricEnum;
import enums.RollupEnum;
import enums.SortOrderEnum;
import enums.SortOrderItemsEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class MachiningDtcComparisonReportTests extends TestBaseUI {

    private MachiningDtcReportPage machiningDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public MachiningDtcComparisonReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7251})
    @Description("Validate report is available by navigation - Machining DTC Comparison Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7252})
    @Description("Validate report is available by library - Machining DTC Comparison Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7253})
    @Description("Validate report is available by search - Machining DTC Comparison Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3020})
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControlFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7337})
    @Description("Verify reset button functionality - Machining DTC Comparison Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7334})
    @Description("Verify cancel button functionality - Machining DTC Comparison Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7337})
    @Description("Verify reset button functionality - Machining DTC Comparison Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Disabled("not applicable due to reports configuration")
    @TestRail(id = {7340})
    @Description("Verify save button functionality - Machining DTC Comparison Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3567})
    @Description("Verify that earlier and latest export fields throw an error when letters and special characters are entered")
    public void testExportSetDateInputInvalidCharacters() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterInvalidCharacters(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7433})
    @Description("Verify export date filters correctly filters export sets - Picker - Machining DTC Comparison Report")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7432})
    @Description("Verify export date filters correctly filters export sets - Input - Machining DTC Comparison Report")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7414})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7415})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7394})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7395})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Comparison Report ")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7458})
    @Description("Verify DTC Score Input Control - No Selection - Machining DTC Comparison Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7496})
    @Description("Verify DTC Score Input Control - Low Selection - Machining DTC Comparison Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7499})
    @Description("Verify DTC Score Input Control - Medium Selection - Machining DTC Comparison Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7502})
    @Description("Verify DTC Score Input Control - High Selection - Machining DTC Comparison Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7543})
    @Description("Verify DTC Score Input Control - All Selection - Machining DTC Comparison Report")
    public void testDtcScoreAll() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.ALL.getDtcScoreName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2039})
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testComponentCostDetailReportLink() {
        machiningDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(), MachiningDtcReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), MachiningDtcReportPage.class)
            .clickOk(MachiningDtcReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), MachiningDtcReportPage.class);

        String partName = machiningDtcReportPage.clickMachiningDtcComparisonBar();

        assertThat(
            machiningDtcReportPage.getUpperTitleText(),
            is(equalTo(ReportNamesEnum.DTC_PART_SUMMARY.getReportName()))
        );
        assertThat(
            partName,
            is(startsWith(machiningDtcReportPage.getDtcPartSummaryPartNameValue()))
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3031})
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3027})
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendComparisonReports(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingIssues() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {
            SortOrderItemsEnum.DTC_MACHINING_TOLERANCED.getSortOrderItemName(),
            SortOrderItemsEnum.MACHINING_DTC_INITIAL.getSortOrderItemName()
        };
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlDesignStandards() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {
            SortOrderItemsEnum.DTC_MACHINING_TOLERANCED.getSortOrderItemName(),
            SortOrderItemsEnum.PUNCH_INITIAL.getSortOrderItemName()
        };
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.DESIGN_STANDARDS.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlTolerances() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {
            SortOrderItemsEnum.MACHINING_DTC_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.PARTBODY_INITIAL.getSortOrderItemName()
        };
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.TOLERANCES.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlSlowOperations() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {
            SortOrderItemsEnum.MACHINING_DTC_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.DTC_MACHINING_TOLERANCED.getSortOrderItemName()
        };
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum(),
            partNames
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        String[] partNames = new String[] {
            SortOrderItemsEnum.PMI_ROUGH_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.PMI_PROFILE_INITIAL.getSortOrderItemName()
        };
        commonReportTests.machiningSheetMetalDtcComparisonSortOrderTest(
            ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(),
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(),
            partNames
        );
    }
}
