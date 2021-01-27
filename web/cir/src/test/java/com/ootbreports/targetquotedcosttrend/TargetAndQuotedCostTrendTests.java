package com.ootbreports.targetquotedcosttrend;

import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.ScenarioComparisonReportPage;
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

    private ScenarioComparisonReportPage scenarioComparisonReportPage;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostTrendTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3352")
    @Description("Validate Target and Quoted Cost Trend report is available by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3352")
    @Description("Validate Target and Quoted Cost Trend report is available by navigation")
    public void testReportAvailabilityByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3354")
    @Description("Validate Target and Quoted Cost Trend report is available by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

}
