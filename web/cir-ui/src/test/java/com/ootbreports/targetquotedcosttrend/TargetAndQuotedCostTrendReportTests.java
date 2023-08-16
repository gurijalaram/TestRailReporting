package com.ootbreports.targetquotedcosttrend;

import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.login.ReportsLoginPage;
import com.apriori.pageobjects.view.reports.GenericReportPage;
import com.apriori.pageobjects.view.reports.TargetQuotedCostTrendReportPage;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import enums.CostMetricEnum;
import enums.RollupEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class TargetAndQuotedCostTrendReportTests extends TestBaseUI {

    private TargetQuotedCostTrendReportPage targetQuotedCostTrendReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostTrendReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3352})
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7323})
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3354})
    @Description("Validate report is available by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3355})
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
    @Tag(REPORTS)
    @TestRail(id = {3356})
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
    @Tag(REPORTS)
    @TestRail(id = {3357})
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
    @Tag(REPORTS)
    @TestRail(id = {3358})
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
    @Tag(REPORTS)
    @TestRail(id = {7423})
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
    @Tag(REPORTS)
    @TestRail(id = {3359})
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
    //@Tag(REPORTS)
    @TestRail(id = {3360})
    @Description("Validate Target and Quoted Cost Trend report aligns to CID values (where appropriate)")
    public void testDataIntegrityInCidBase() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendDataIntegrity("Base");
    }

    @Test
    //@Tag(REPORTS)
    @TestRail(id = {3360})
    @Description("Validate Target and Quoted Cost Trend report aligns to CID values (where appropriate)")
    public void testDataIntegrityInCidFinal() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendDataIntegrity("Final");
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7668})
    @Description("Validate hyperlinks to Target and Quoted Cost Value Tracking report - Base Milestone")
    public void testHyperlinksToDetailsReportBaseMilestone() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendReportHyperlinks("Base");
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3361})
    @Description("Validate hyperlinks to Target and Quoted Cost Value Tracking report - Final Milestone")
    public void testHyperlinksToDetailsReportFinalMilestone() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTargetQuotedCostTrendReportHyperlinks("Final");
    }
}
