package com.ootbreports.dtcmetrics.plasticdtc;

import static com.apriori.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.TestBaseUI;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.PlasticDtcReportPage;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;

public class PlasticDtcDetailsReportTests extends TestBaseUI {

    private PlasticDtcReportPage plasticDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public PlasticDtcDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7305})
    @Description("Validate report is available by navigation - Plastic DTC Details Report")
    public void testPlasticDtcReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7308})
    @Description("Validate report is available by library - Plastic DTC Details Report")
    public void testPlasticDtcReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7311})
    @Description("Validate report is available by search - Plastic DTC Details")
    public void testPlasticDtcReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7356})
    @Description("Verify apply button functionality - Plastic DTC Details Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7359})
    @Description("Verify cancel button functionality - Plastic DTC Details Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7362})
    @Description("Verify reset button functionality - Plastic DTC Details Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(id = {7365})
    @Description("Verify save button functionality - Plastic DTC Details Report")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7406})
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Details Report ")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7407})
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Details Report ")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7381})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7382})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Details Report ")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7519})
    @Description("Verify DTC Score Input Control - No Selection - Plastic DTC Details Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7522})
    @Description("Verify DTC Score Input Control - Low Selection - Plastic DTC Details Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7525})
    @Description("Verify DTC Score Input Control - Medium Selection - Plastic DTC Details Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7528})
    @Description("Verify DTC Score Input Control - High Selection - Plastic DTC Details Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {1371})
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendDetailsReports(
            ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {1369})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingIssues() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(), PlasticDtcReportPage.class)
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), PlasticDtcReportPage.class)
            .selectSortOrder(SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum())
            .clickOk(PlasticDtcReportPage.class);

        plasticDtcReportPage.waitForReportToLoad();

        assertThat(plasticDtcReportPage.getPlasticDtcDetailsRowOnePartName(),
            is(equalTo("PLASTIC MOULDED CAP THICKPART")));
    }

    @Test
    //@Tag(REPORTS)
    @TestRail(id = {1378})
    @Description("Verify DTC issue counts are correct")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testPlasticDtcIssueCounts(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName());
    }
}
