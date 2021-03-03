package com.ootbreports.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import com.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.pageobjects.pages.explore.ExplorePage;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class CastingDtcComparisonReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public CastingDtcComparisonReportTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1676")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            Constants.DTC_METRICS_FOLDER,
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1676")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1676")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1692")
    @Description("Verify export set input controls function correctly")
    public void testCastingDtcComparisonExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1694")
    @Description("Verify roll-up dropdown functions correctly for Casting DTC Comparison report")
    public void testRollupDropDown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC Comparison input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC Comparison input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Casting DTC Comparison input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1691")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1691")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "102990")
    @Description("Verify that aPriori costed scenarios are represented correctly")
    public void testVerifyComparisonReportAvailableAndCorrectData() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .clickComparison()
            .switchTab(1);

        genericReportPage.waitForNewTabSwitchCastingDtcToComparison();
        genericReportPage.setReportName(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
        String partName = genericReportPage.getPartNameDtcReports();
        String holeIssueNumReports = genericReportPage.getHoleIssuesFromComparisonReport();
        genericReportPage.openNewCidTabAndFocus(2);

        DesignGuidancePage designGuidancePage = new ExplorePage(driver)
            .filter()
            .setWorkspace(Constants.PUBLIC_WORKSPACE)
            .setScenarioType(Constants.PART_SCENARIO_TYPE)
            .setRowOne("Part Name", "Contains", partName)
            .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
            .apply(ExplorePage.class)
            .openFirstScenario()
            .openDesignGuidance();

        String holeIssueCidValue = designGuidancePage.getHoleIssueValue();

        assertThat(holeIssueNumReports, is(equalTo(holeIssueCidValue)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1695")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1695")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1696")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1696")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1372")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1372")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1372")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1372")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1972")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreAll() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.ALL.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1700")
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendComparisonReports(
            ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testCastingDtcComparisonSortOrderManufacturingCastingIssues() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.CASTING_ISSUES.getSortOrderEnum(),
            "JEEP WJ FRONT BRAKE DISC 99-04 (Init…",
            "GEAR HOUSING (Initial)"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testCastingDtcComparisonSortOrderManufacturingMachiningIssues() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum(),
            "DTCCASTINGISSUES (sand casting)",
            "DTCCASTINGISSUES (Initial)"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testCastingDtcComparisonSortOrderMaterialScrap() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum(),
            "OBSTRUCTED MACHINING (Initial)",
            "B2315 (Initial)"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testCastingDtcComparisonSortOrderTolerances() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.TOLERANCES.getSortOrderEnum(),
            "DTCCASTINGISSUES (Initial)",
            "DTCCASTINGISSUES (sand casting)"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testCastingDtcComparisonSortOrderSlowOperations() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum(),
            "DTCCASTINGISSUES (Initial)",
            "DTCCASTINGISSUES (sand casting)"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testCastingDtcComparisonSortOrderSpecialTooling() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum(),
            "DU600051458 (Initial)",
            "DU200068073_B (Initial)"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testCastingDtcComparisonSortOrderAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.castingDtcComparisonSortOrderTest(
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(),
            "E3-241-4-N (Initial)",
            "40137441.MLDES.0002 (Initial)"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1708")
    @Description("Verify DTC issue counts are correct")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testCastingDtcIssueCounts(ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName());
    }
}
