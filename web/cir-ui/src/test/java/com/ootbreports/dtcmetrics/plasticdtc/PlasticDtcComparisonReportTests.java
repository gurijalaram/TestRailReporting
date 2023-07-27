package com.ootbreports.dtcmetrics.plasticdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import enums.CostMetricEnum;
import enums.DtcScoreEnum;
import enums.MassMetricEnum;
import enums.RollupEnum;
import enums.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;

public class PlasticDtcComparisonReportTests extends TestBaseUI {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public PlasticDtcComparisonReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7304})
    @Description("Validate report is available by navigation - Plastic DTC Comparison Report")
    public void testPlasticDtcReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7307})
    @Description("Validate report is available by library - Plastic DTC Comparison Report")
    public void testPlasticDtcReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7310})
    @Description("Validate report is available by search - Plastic DTC Comparison")
    public void testPlasticDtcReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7355})
    @Description("Verify apply button functionality - Plastic DTC Comparison Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7358})
    @Description("Verify cancel button functionality - Plastic DTC Comparison Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7361})
    @Description("Verify reset button functionality - Plastic DTC Comparison Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(id = {7364})
    @Description("Verify save button functionality - Plastic DTC Comparison Report ")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7404})
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Comparison Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7405})
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Comparison Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7383})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Comparison Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7384})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Comparison Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7518})
    @Description("Verify DTC Score Input Control - No Selection - Plastic DTC Comparison Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7521})
    @Description("Verify DTC Score Input Control - Low Selection - Plastic DTC Comparison Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7524})
    @Description("Verify DTC Score Input Control - Medium Selection - Plastic DTC Comparison Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7527})
    @Description("Verify DTC Score Input Control - High Selection - Plastic DTC Comparison Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7542})
    @Description("Verify DTC Score Input Control - All Selection - Plastic DTC Comparison Report")
    public void testDtcScoreAll() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreComparisonReports(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.ALL.getDtcScoreName()
        );
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {1371})
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendComparisonReports(
            ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1369})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingIssues() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        genericReportPage.waitForReportToLoad();
        genericReportPage.waitForSvgToRender();
        String elementName = "PLASTIC MOULDED CAP THICKPART";

        assertThat(genericReportPage.getTableElementNameDtcComparison("1", "1"),
            is(equalTo(elementName)));

        assertThat(genericReportPage.getTableElementNameDtcComparison("2", "1"),
            is(equalTo(elementName)));

        assertThat(genericReportPage.getTableElementNameDtcComparison("3", "1"),
            is(equalTo(elementName)));

        assertThat(genericReportPage.getTableElementNameDtcComparison("4", "1"),
            is(equalTo(elementName)));
    }

    @Test
    //@Category(ReportsTest.class)
    @TestRail(id = {1378})
    @Description("Verify DTC issue counts are correct")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testPlasticDtcIssueCounts(ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName());
    }
}
