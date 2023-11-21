package com.apriori.cir.ui.tests.ootbreports.targetquotedcosttrend;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;

import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class TargetAndQuotedCostValueTrackingDetailsReportTests extends TestBaseUI {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostValueTrackingDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7324")
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
    @TmsLink("7326")
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
    @TmsLink("7328")
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
    @TmsLink("7425")
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
    @TmsLink("7426")
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
