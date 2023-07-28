package com.ootbreports.dtcmetrics.castingdtc;

import static com.apriori.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.OperationEnum;
import com.apriori.enums.PropertyEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.CastingDtcReportPage;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import utils.Constants;

public class CastingDtcComparisonReportTests extends TestBaseUI {

    private CastingDtcReportPage castingDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public CastingDtcComparisonReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7242})
    @Description("Validate report available by navigation - Casting DTC Comparison Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7245})
    @Description("Verify report is available by library - Casting DTC Comparison Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7250})
    @Description("Verify report is available by search - Casting DTC Comparison Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7652})
    @Description("Verify Export Set list controls function correctly - Casting DTC Comparison Report")
    public void testCastingDtcComparisonExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7654})
    @Description("Verify Roll-up input control functions correctly - Casting DTC Comparison Report")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7343})
    @Description("Verify apply button functionality - Casting DTC Comparison Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7348})
    @Description("Verify cancel button functionality - Casting DTC Comparison Report ")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7350})
    @Description("Verify reset button functionality - Casting DTC Comparison Report ")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(id = {7352})
    @Description("Verify save button functionality - Casting DTC Comparison Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7430})
    @Description("Verify export date filters correctly filters export sets - Picker - Casting DTC Comparison Report")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7431})
    @Description("Verify export date filters correctly filters export sets - Input - Casting DTC Comparison Report")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
                ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7619})
    @Description("Verify that aPriori costed scenarios are represented correctly - Casting DTC Comparison Report")
    public void testVerifyComparisonReportAvailableAndCorrectData() {
        castingDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC.getReportName(), CastingDtcReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), CastingDtcReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), CastingDtcReportPage.class)
            .clickOk(CastingDtcReportPage.class);

        castingDtcReportPage.clickComparison()
            .switchTab(1);

        castingDtcReportPage.waitForNewTabSwitchCastingDtcToComparison();
        castingDtcReportPage.setReportName(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
        String partName = castingDtcReportPage.getPartNameDtcReports();
        String holeIssueNumReports = castingDtcReportPage.getHoleIssuesFromComparisonReport();

        castingDtcReportPage.openNewCidTabAndFocus(2);
        GuidanceIssuesPage guidanceIssuesPage = new ExplorePage(driver)
                .filter()
                .saveAs()
                .inputName(new GenerateStringUtil().generateFilterName())
                .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, partName)
                .addCriteria(PropertyEnum.SCENARIO_NAME,OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
                .submit(ExplorePage.class)
                .openFirstScenario()
                .openDesignGuidance();

        String holeIssueCidValue = guidanceIssuesPage.getDtcIssueCount("Hole");

        assertThat(holeIssueNumReports, is(equalTo(holeIssueCidValue)));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7409})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7410})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7389})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Comparison Report ")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7390})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7506})
    @Description("Verify DTC Score Input Control - No Selection - Casting DTC Comparison Report ")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7509})
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Comparison Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7512})
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Comparison Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7515})
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Comparison Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7544})
    @Description("Verify DTC Score Input Control - All Selection - Casting DTC Comparison Report")
    public void testDtcScoreAll() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.ALL.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7656})
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Comparison Report")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7643})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Comparison Report")
    public void testCastingDtcComparisonSortOrderManufacturingCastingIssues() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.CASTING_ISSUES.getSortOrderEnum(),
            SortOrderItemsEnum.JEEP_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.GEAR_HOUSING_INITIAL.getSortOrderItemName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7637})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Comparison Report")
    public void testCastingDtcComparisonSortOrderManufacturingMachiningIssues() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum(),
            SortOrderItemsEnum.DTC_SAND.getSortOrderItemName(),
            SortOrderItemsEnum.DTC_INITIAL.getSortOrderItemName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7638})
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Comparison Report")
    public void testCastingDtcComparisonSortOrderMaterialScrap() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum(),
            SortOrderItemsEnum.OBSTRUCTED_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.B2.getSortOrderItemName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7639})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Comparison Report")
    public void testCastingDtcComparisonSortOrderTolerances() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.TOLERANCES.getSortOrderEnum(),
            SortOrderItemsEnum.JEEP_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.GEAR_HOUSING_INITIAL.getSortOrderItemName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7640})
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Comparison Report")
    public void testCastingDtcComparisonSortOrderSlowOperations() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum(),
            SortOrderItemsEnum.DTC_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.DU_INITIAL.getSortOrderItemName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7641})
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Comparison Report ")
    public void testCastingDtcComparisonSortOrderSpecialTooling() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum(),
            SortOrderItemsEnum.DU_TWO_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.GEAR_HOUSING_INITIAL.getSortOrderItemName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7642})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Comparison Report")
    public void testCastingDtcComparisonSortOrderAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(),
            SortOrderItemsEnum.E3_INITIAL.getSortOrderItemName(),
            SortOrderItemsEnum.DU_INITIAL.getSortOrderItemName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {1708})
    @Description("Verify DTC issue counts are correct - Casting DTC Comparison Report")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testCastingDtcIssueCounts(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }
}
