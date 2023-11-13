package com.apriori.cir.ui.tests.ootbreports.dtcmetrics.castingdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.enums.SortOrderEnum;
import com.apriori.cir.ui.enums.SortOrderItemsEnum;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.CastingDtcReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CastingDtcDetailsReportTests extends TestBaseUI {

    private CastingDtcReportPage castingDtcReportPage;
    private CommonReportTests commonReportTests;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;

    public CastingDtcDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7241})
    @Description("Validate report is available by navigation - Casting DTC Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7244})
    @Description("Verify report is available by library - Casting DTC Details Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7249})
    @Description("Verify report is available by search - Casting DTC Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7653})
    @Description("Verify Export Set list controls function correctly - Casting DTC Details Report")
    public void testCastingDtcDetailsExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7655})
    @Description("Verify Roll-up input control functions correctly - Casting DTC Details Report")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7344})
    @Description("Verify apply button functionality - Casting DTC Details Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7349})
    @Description("Verify cancel button functionality - Casting DTC Details Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7351})
    @Description("Verify reset button functionality - Casting DTC Details Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Disabled("not applicable due to reports configuration")
    @TestRail(id = {7353})
    @Description("Verify save button functionality - Casting DTC Details Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7428})
    @Description("Verify export date filters correctly filters export sets - Picker - Casting DTC Details Report")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7429})
    @Description("Verify export date filters correctly filters export sets - Input - Casting DTC Details Report")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7620})
    @Description("Verify that aPriori costed scenarios are represented correctly - Casting DTC Details Report")
    public void testVerifyDetailsReportAvailableAndCorrectData() {
        castingDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), CastingDtcReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), CastingDtcReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), CastingDtcReportPage.class)
            .clickOk(CastingDtcReportPage.class);

        castingDtcReportPage.setReportName(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
        String partName = castingDtcReportPage.getPartNameDtcReports();
        String holeIssueNumReports = castingDtcReportPage.getHoleIssuesFromDetailsReport();

        genericReportPage.openNewCidTabAndFocus(1);
        GuidanceIssuesPage guidanceIssuesPage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, partName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openFirstScenario()
            .openDesignGuidance();

        String holeIssueCidValue = guidanceIssuesPage.getDtcIssueCount("Hole");

        assertThat(holeIssueNumReports, is(equalTo(holeIssueCidValue)));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7411})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7412})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Details Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7391})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Details Report ")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7392})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7507})
    @Description("Verify DTC Score Input Control - No Selection - Casting DTC Details Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7510})
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Details Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7513})
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Details Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7516})
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Details Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7657})
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7629})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingCasting() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.JEEP.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.GEAR_HOUSING.getSortOrderItemName());
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.CASTING_ISSUES.getSortOrderEnum(), false, assertValues);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7630})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingMachining() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.DTC.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.DTC.getSortOrderItemName());
        assertValues.add(Constants.OTHER_SCENARIO_NAME);
        assertValues.add(Constants.DEFAULT_SCENARIO_NAME);
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum(), false, assertValues);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7631})
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Details Report")
    public void testSortOrderInputControlMaterialScrap() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.OBSTRUCTED.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.B2.getSortOrderItemName());
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum(), false, assertValues);
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7632})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.DTC.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.DTC.getSortOrderItemName());
        assertValues.add(Constants.DEFAULT_SCENARIO_NAME);
        assertValues.add(Constants.OTHER_SCENARIO_NAME);
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.TOLERANCES.getSortOrderEnum(), true, assertValues);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7633})
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.DTC.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.DTC.getSortOrderItemName());
        assertValues.add(Constants.DEFAULT_SCENARIO_NAME);
        assertValues.add(Constants.OTHER_SCENARIO_NAME);
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum(), true, assertValues);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7634})
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Details Report")
    public void testSortOrderInputControlSpecialTooling() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.DU_TWO.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.DU_THREE.getSortOrderItemName());
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum(), false, assertValues);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7635})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.E3.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.MLDES.getSortOrderItemName());
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum(), false, assertValues);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7636})
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Casting DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        commonReportTests = new CommonReportTests(driver);
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(SortOrderItemsEnum.BARCO.getSortOrderItemName());
        assertValues.add(SortOrderItemsEnum.BARCO_THREE.getSortOrderItemName());
        commonReportTests.castingDtcDetailsSortOrderTest(
            SortOrderEnum.DTC_RANK.getSortOrderEnum(), false, assertValues);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7644})
    @Description("Verify DTC issue counts are correct - Casting DTC Details Report")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testCastingDtcIssueCounts(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }
}