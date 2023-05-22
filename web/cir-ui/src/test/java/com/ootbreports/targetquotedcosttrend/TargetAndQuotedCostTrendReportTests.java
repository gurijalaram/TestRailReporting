package com.ootbreports.targetquotedcosttrend;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
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
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;

public class TargetAndQuotedCostTrendReportTests extends TestBase {

    private TargetQuotedCostTrendReportPage targetQuotedCostTrendReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostTrendReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3352"})
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7323"})
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3354"})
    @Description("Validate report is available by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3355"})
    @Description("Validate Projects Rollup drop-down Input Control functionality")
    public void testProjectRollupDropdown() {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                        TargetQuotedCostTrendReportPage.class);

        assertThat(targetQuotedCostTrendReportPage.getProjectRollupDropdownOptionText(),
                is(equalTo("AC CYCLE TIME VT 1")));

        targetQuotedCostTrendReportPage.clickOk(TargetQuotedCostTrendReportPage.class);
        assertThat(targetQuotedCostTrendReportPage.isChartDisplayedAndEnabled(), is(equalTo(true)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3356"})
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
            assertThat(targetQuotedCostTrendReportPage.getProjectNameDropdownOptionText(String.valueOf(i)),
                    is(equalTo(String.format("PROJECT %d", i))));
        }

        String projectToSelect = "PROJECT 4";
        targetQuotedCostTrendReportPage.selectProject(projectToSelect);
        targetQuotedCostTrendReportPage.clickOk(TargetQuotedCostTrendReportPage.class);

        assertThat(targetQuotedCostTrendReportPage.isChartDisplayedAndEnabled(), is(equalTo(true)));
        assertThat(targetQuotedCostTrendReportPage.getProjectNameAboveChart(), is(equalTo(projectToSelect)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3357"})
    @Description("Validate Export Date drop-down Input Control")
    public void testExportDateDropdown() {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                        TargetQuotedCostTrendReportPage.class);

        assertThat(targetQuotedCostTrendReportPage.getCountOfExportDateOptions(), is(equalTo("1")));
        assertThat(targetQuotedCostTrendReportPage.isExportDateRecent(), is(equalTo(true)));

        String exportDateSelected = targetQuotedCostTrendReportPage.getCurrentExportDate().replace("T", " ");
        targetQuotedCostTrendReportPage.clickOk(TargetQuotedCostTrendReportPage.class);
        assertThat(targetQuotedCostTrendReportPage.getExportDateFromAboveChart(), is(equalTo(exportDateSelected)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3358"})
    @Description("Validate Cost Metric Input Control - PPC")
    public void testCostMetricFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlTargetQuotedCostTrendReports(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7423"})
    @Description("Validate Cost Metric Input Control - FBC")
    public void testCostMetricPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlTargetQuotedCostTrendReports(
                ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3359"})
    @Description("Validate Currency drop-down Input Control")
    public void testCurrencyCodeInputControl() {
        targetQuotedCostTrendReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
                        TargetQuotedCostTrendReportPage.class);

        targetQuotedCostTrendReportPage.checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
                .clickOk(TargetQuotedCostTrendReportPage.class)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class);

        String usdFinalAprioriCost = targetQuotedCostTrendReportPage.getFinalAprioriCost();

        targetQuotedCostTrendReportPage.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
                .clickOk(TargetQuotedCostTrendReportPage.class)
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), TargetQuotedCostTrendReportPage.class);

        String gbpFinalAprioriCost = targetQuotedCostTrendReportPage.getFinalAprioriCost();

        assertThat(usdFinalAprioriCost, is(not(equalTo(gbpFinalAprioriCost))));
    }

    @Test
    //@Category(ReportsTest.class)
    @TestRail(testCaseId = {"3360"})
    @Description("Validate Target and Quoted Cost Trend report aligns to CID values (where appropriate)")
    public void testDataIntegrityInCidBase() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendDataIntegrity("Base");
    }

    @Test
    //@Category(ReportsTest.class)
    @TestRail(testCaseId = {"3360"})
    @Description("Validate Target and Quoted Cost Trend report aligns to CID values (where appropriate)")
    public void testDataIntegrityInCidFinal() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendDataIntegrity("Final");
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7668"})
    @Description("Validate hyperlinks to Target and Quoted Cost Value Tracking report - Base Milestone")
    public void testHyperlinksToDetailsReportBaseMilestone() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendReportHyperlinks("Base");
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = {"3361"})
    @Description("Validate hyperlinks to Target and Quoted Cost Value Tracking report - Final Milestone")
    public void testHyperlinksToDetailsReportFinalMilestone() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendReportHyperlinks("Final");
    }
}
