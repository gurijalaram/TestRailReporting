package com.ootbreports.cycletimevaluetracking;

import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;

import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.login.ReportsLoginPage;
import com.apriori.pageobjects.view.reports.ComponentCostReportPage;
import com.apriori.pageobjects.view.reports.CycleTimeValueTrackingPage;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class CycleTimeValueTrackingReportTests extends TestBaseUI {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private ComponentCostReportPage componentCostReportPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {2325})
    @Description("Validate report is available by navigation - Cycle Time Value Tracking Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7236})
    @Description("Verify report availability by library - Cycle Time Value Tracking Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7238})
    @Description("Verify report availability by search - Cycle Time Value Tracking Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {2331})
    @Description("Projects rollup drop list functionality test - Cycle Time Value Tracking Report")
    public void testProjectRollupDropdownList() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                CycleTimeValueTrackingPage.class
            );

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), anyOf(equalTo("1"), equalTo("2")));
        String expectedProjectRollup = "AC CYCLE TIME VT 1";
        assertThat(cycleTimeValueTrackingPage.getFirstRollupName(), is(equalTo(expectedProjectRollup)));

        cycleTimeValueTrackingPage.clickOk(CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getRollupInUseAboveChart(), is(equalTo(expectedProjectRollup)));
        assertThat(cycleTimeValueTrackingPage.getRollupInUseInChart(), is(equalTo(expectedProjectRollup)));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2332})
    @Description("Export date lists all available versions from selected export set rollup - Cycle Time Value Tracking Report")
    public void testExportDateFilterFunctionality() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                CycleTimeValueTrackingPage.class
            ).selectCycleTimeRollup();

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), anyOf(equalTo("1"), equalTo("2")));
        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("2"), anyOf(equalTo("1"), equalTo("6")));

        cycleTimeValueTrackingPage.clickOk(CycleTimeValueTrackingPage.class);
        assertThat(cycleTimeValueTrackingPage.getRollupInUseAboveChart(), is(equalTo("AC CYCLE TIME VT 1")));
    }

    @Test
    @Issue("AP-66960")
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2335})
    @Description("Validate Cycle Time Value Tracking Report hyperlinks to Details and then to Component Cost report")
    public void testReportHyperlinks() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                CycleTimeValueTrackingPage.class
            );

        cycleTimeValueTrackingPage.clickOk(CycleTimeValueTrackingPage.class);
        cycleTimeValueTrackingPage.clickHyperlink("PROJECT 2", CycleTimeValueTrackingPage.class);
        cycleTimeValueTrackingPage.switchTab(1);
        cycleTimeValueTrackingPage.waitForNewTabSwitchCycleTimeToDetailsOrComponentCost();

        assertThat(cycleTimeValueTrackingPage.getCycleTimeReportTitle(),
            is(equalTo(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName())));

        String partNumber = "IROBOT_18874";
        componentCostReportPage = cycleTimeValueTrackingPage.clickHyperlink(partNumber, ComponentCostReportPage.class);
        componentCostReportPage.switchTab(2);
        cycleTimeValueTrackingPage.waitForNewTabSwitchCycleTimeToDetailsOrComponentCost();

        assertThat(
            componentCostReportPage.getComponentCostReportTitle(),
            is(equalTo(ReportNamesEnum.COMPONENT_COST.getReportName()))
        );
        assertThat(
            componentCostReportPage.getPartNumber(),
            is(equalTo(partNumber))
        );
    }
}
