package com.ootbreports.targetquotedcosttrend;

import com.apriori.TestBaseUI;
import com.apriori.testrail.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;

public class TargetAndQuotedCostValueTrackingDetailsReportTests extends TestBaseUI {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostValueTrackingDetailsReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7324})
    @Description("Validate report is available by navigation - Target and Quoted Cost Trend Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7326})
    @Description("Validate report is available by library - Target and Quoted Cost Trend Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7328})
    @Description("Validate report is available by search - Target and Quoted Cost Trend Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
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
    @Category( {ReportsTest.class, OnPremTest.class})
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
