package com.ootbreports.targetquotedcosttrend;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.ScenarioComparisonReportPage;
import com.apriori.pageobjects.pages.view.reports.TargetQuotedCostTrendReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;
import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class TargetAndQuotedCostTrendTests extends TestBase {

    private TargetQuotedCostTrendReportPage targetQuotedCostTrendReportPage;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostTrendTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3352")
    @Description("Validate Target and Quoted Cost Trend report is available by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3352")
    @Description("Validate Target and Quoted Cost Trend report is available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3354")
    @Description("Validate Target and Quoted Cost Trend report is available by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3355")
    @Description("Validate Projects Rollup drop-down Input Control functionality")
    public void testProjectRollupDropdown() {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                        TargetQuotedCostTrendReportPage.class);

        assertThat(targetQuotedCostTrendReportPage.getProjectRollupDropdownItemCount(), is(equalTo("2")));
        assertThat(targetQuotedCostTrendReportPage.getProjectRollupDropdownOptionText("1"),
                is(equalTo("AC CYCLE TIME VT 1")));
        String secondValue = Constants.environment.equals("cir-qa") ? "SPEND ANALYSIS VALUE TRACKING" :
                "CYCLE TIME VALUE TRACKING ROLLUP";
        assertThat(targetQuotedCostTrendReportPage.getProjectRollupDropdownOptionText("2"),
                is(equalTo(secondValue)));

        targetQuotedCostTrendReportPage.clickOk();
        assertThat(targetQuotedCostTrendReportPage.isChartDisplayedAndEnabled(), is(equalTo(true)));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3356")
    @Description("Validate Project Name drop-down Input Control functionality")
    public void testProjectNameDropdown() {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                        TargetQuotedCostTrendReportPage.class);

        int itemCount = Integer.parseInt(targetQuotedCostTrendReportPage.getProjectNameDropdownItemCount());
        assertThat(itemCount, is(equalTo(4)));

        for (int i = 0; i == itemCount; i++) {
            assertThat(targetQuotedCostTrendReportPage.getProjectNameDropdownOptionText(String.valueOf(i)), is(equalTo(String.format("PROJECT %d", i))));
        }

        String projectToSelect = "PROJECT 4";
        targetQuotedCostTrendReportPage.selectProject(projectToSelect);
        targetQuotedCostTrendReportPage.clickOk();

        assertThat(targetQuotedCostTrendReportPage.isChartDisplayedAndEnabled(), is(equalTo(true)));
        assertThat(targetQuotedCostTrendReportPage.getProjectNameAboveChart(), is(equalTo(projectToSelect)));
    }
}
