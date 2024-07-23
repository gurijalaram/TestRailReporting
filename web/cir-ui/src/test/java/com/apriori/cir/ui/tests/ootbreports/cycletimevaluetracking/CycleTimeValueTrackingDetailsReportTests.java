package com.apriori.cir.ui.tests.ootbreports.cycletimevaluetracking;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.CycleTimeValueTrackingPage;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CycleTimeValueTrackingDetailsReportTests extends TestBaseUI {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7325")
    @TestRail(id = {7325})
    @Description("Validate report available by navigation - Cycle Time Value Tracking Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7327")
    @TestRail(id = {7327})
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7329")
    @TestRail(id = {7239})
    @Description("Verify report availability by search - Cycle Time Value Tracking Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }
}
