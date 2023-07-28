package com.ootbreports.targetquotedcosttrend;

import static com.apriori.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.TestSuiteType.TestSuite.REPORTS;

import com.apriori.TestBaseUI;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import enums.CostMetricEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;

public class TargetAndQuotedCostValueTrackingDetailsReportTests extends TestBaseUI {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostValueTrackingDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7324})
    @Description("Validate report is available by navigation - Target and Quoted Cost Trend Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7326})
    @Description("Validate report is available by library - Target and Quoted Cost Trend Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7328})
    @Description("Validate report is available by search - Target and Quoted Cost Trend Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7425})
    @Description("Validate Cost Metric Input Control - PPC - Target and Quoted Cost Value Tracking Details Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricTargetQuotedCostValueTrackingDetailsReport(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7426})
    @Description("Validate Cost Metric Input Control - FBC - Target and Quoted Cost Value Tracking Details Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricTargetQuotedCostValueTrackingDetailsReport(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }
}
