package com.ootbreports.targetquotedcosttrend;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.TargetAndQuotedCostValueTrackingPage;
import com.apriori.pageobjects.pages.view.reports.TargetQuotedCostTrendReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;

import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;

import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class TargetAndQuotedCostValueTrackingTests extends TestBase {

    private TargetAndQuotedCostValueTrackingPage targetAndQuotedCostValueTrackingPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostValueTrackingTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3363")
    @Description("Validate Target and Quoted Cost Value Tracking report is available by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3363")
    @Description("Validate Target and Quoted Cost Value Tracking report is available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3363")
    @Description("Validate Target and Quoted Cost Value Tracking report is available by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName()
        );
    }
    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3363")
    @Description("Validate Target and Quoted Cost Value Tracking report is available by library")
    public void testDetailsReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3363")
    @Description("Validate Target and Quoted Cost Value Tracking report is available by navigation")
    public void testDetailsReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3363")
    @Description("Validate Target and Quoted Cost Value Tracking report is available by search")
    public void testDetailsReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3364")
    @Description("Validate Cost Metric Input Control")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricTargetQuotedCostValueTrackingReport(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3364")
    @Description("Validate Cost Metric Input Control")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricTargetQuotedCostValueTrackingReport(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3365")
    @Description("Validate Currency Code Input Control Functionality")
    public void testCurrencyCodeInputControl() {
        targetAndQuotedCostValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                        TargetAndQuotedCostValueTrackingPage.class);

        targetAndQuotedCostValueTrackingPage.checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class);

        String usdFinalAprioriCost = targetAndQuotedCostValueTrackingPage.getFinalCost();

        targetAndQuotedCostValueTrackingPage.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), TargetQuotedCostTrendReportPage.class);

        String gbpFinalAprioriCost = targetAndQuotedCostValueTrackingPage.getFinalCost();

        assertThat(usdFinalAprioriCost, is(not(equalTo(gbpFinalAprioriCost))));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3368")
    @Description("Validate subreport hyperlinks to Target Cost Value Tracking details report for each milestone")
    public void testLinksToMilestoneOne() {
        targetAndQuotedCostValueTrackingPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                        TargetAndQuotedCostValueTrackingPage.class)
                .selectProjectRollup(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetAndQuotedCostValueTrackingPage.class)
                .clickProjectLink("1")
                .switchTab(1)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetAndQuotedCostValueTrackingPage.class);

        assertThat(targetAndQuotedCostValueTrackingPage.getProjectName(),
                is(equalTo("PROJECT 1")));
    }
}
