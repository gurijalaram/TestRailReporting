package com.ootbreports.cycletimevaluetracking;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.ComponentCostReportPage;
import com.apriori.pageobjects.pages.view.reports.CycleTimeValueTrackingPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;

public class CycleTimeValueTrackingReportTests extends TestBase {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private ComponentCostReportPage componentCostReportPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"2325"})
    @Description("Validate report is available by navigation - Cycle Time Value Tracking Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7236"})
    @Description("Verify report availability by library - Cycle Time Value Tracking Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7238"})
    @Description("Verify report availability by search - Cycle Time Value Tracking Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"2331"})
    @Description("Projects rollup drop list functionality test - Cycle Time Value Tracking Report")
    public void testProjectRollupDropdownList() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(
                        ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                        CycleTimeValueTrackingPage.class
                );

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), is(equalTo("1")));
        String expectedProjectRollup = "AC CYCLE TIME VT 1";
        assertThat(cycleTimeValueTrackingPage.getFirstRollupName(), is(equalTo(expectedProjectRollup)));

        cycleTimeValueTrackingPage.clickOk(true, CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getRollupInUseAboveChart(), is(equalTo(expectedProjectRollup)));
        assertThat(cycleTimeValueTrackingPage.getRollupInUseInChart(), is(equalTo(expectedProjectRollup)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"2332"})
    @Description("Export date lists all available versions from selected export set rollup - Cycle Time Value Tracking Report")
    public void testExportDateFilterFunctionality() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(
                        ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                        CycleTimeValueTrackingPage.class
                );

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), is(equalTo("1")));
        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("2"), is(equalTo("1")));

        cycleTimeValueTrackingPage.clickOk(true, CycleTimeValueTrackingPage.class);
        assertThat(cycleTimeValueTrackingPage.getRollupInUseAboveChart(), is(equalTo("AC CYCLE TIME VT 1")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"2335"})
    @Description("Validate Cycle Time Value Tracking Report hyperlinks to Details and then to Component Cost report")
    public void testReportHyperlinks() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(
                        ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
                        CycleTimeValueTrackingPage.class
                );

        cycleTimeValueTrackingPage.clickOk(true, CycleTimeValueTrackingPage.class);
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
