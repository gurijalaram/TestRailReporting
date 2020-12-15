package com.ootbreports.cycletimevaluetracking;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.CycleTimeValueTrackingPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import testsuites.suiteinterface.CiaCirTestDevTest;
import utils.Constants;

public class CycleTimeValueTrackingTests extends TestBase {

    private CycleTimeValueTrackingPage cycleTimeValueTrackingPage;
    private CommonReportTests commonReportTests;

    public CycleTimeValueTrackingTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "92")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName());
    }

    @Test
    @TestRail(testCaseId = "92")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName());
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "2331")
    @Description("Projects rollup drop list functionality test")
    public void testProjectRollupDropdownList() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(), CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), is(equalTo("1")));
        String expectedProjectRollup = "AC CYCLE TIME VT 1";
        assertThat(cycleTimeValueTrackingPage.getFirstRollupName(), is(equalTo(expectedProjectRollup)));

        cycleTimeValueTrackingPage.clickOk();

        assertThat(cycleTimeValueTrackingPage.getRollupInUseAboveChart(), is(equalTo(expectedProjectRollup)));
        assertThat(cycleTimeValueTrackingPage.getRollupInUseInChart(), is(equalTo(expectedProjectRollup)));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "2332")
    @Description("Export date lists all available versions from selected export set rollup")
    public void testExportDateFilterFunctionality() {
        cycleTimeValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(), CycleTimeValueTrackingPage.class);

        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("1"), is(equalTo("1")));
        assertThat(cycleTimeValueTrackingPage.getCountOfDropdownItems("2"), is(equalTo("1")));

        cycleTimeValueTrackingPage.clickOk();
        assertThat(cycleTimeValueTrackingPage.getRollupInUseAboveChart(), is(equalTo("AC CYCLE TIME VT 1")));
    }
}
