package com.ootbreports.cycletimevaluetracking;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.CycleTimeValueTrackingPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import utils.Constants;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CycleTimeValueTrackingDetailsTests extends TestBase {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingDetailsTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "92")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "2331")
    @Description("Projects rollup drop list functionality test")
    public void testProjectRollupDropdownList() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(), CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getCountOfRollupItems(), is(equalTo("2")));
        cycleTimeValueTrackingPage.selectProjectRollup("2")
                .clickOk();

        assertThat(cycleTimeValueTrackingPage.getProjectName(), is(equalTo("PROJECT 1")));
    }
}
