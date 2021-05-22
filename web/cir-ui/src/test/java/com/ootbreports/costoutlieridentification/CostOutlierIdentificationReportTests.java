package com.ootbreports.costoutlieridentification;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ExplorePage;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

import java.math.BigDecimal;

public class CostOutlierIdentificationReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public CostOutlierIdentificationReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1944"})
    @Description("Validate report is available by navigation - menu")
    public void testReportAvailableByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6182"})
    @Description("Validate report is available by library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1945"})
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1954"})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricFbcFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1954"})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricPpcFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1956"})
    @Description("Min & Max costs filter works")
    public void testMinMaxAprioriCost() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierMainReports(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6253"})
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValues() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1959"})
    @Description("Validate report content aligns to aP desktop or CID (where appropriate) - Main Report")
    public void testDataIntegrityAgainstCID() {
        String reportName = ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName();
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())
                .clickOk();

        genericReportPage.setReportName(reportName);
        String[] partScenarioName = genericReportPage.getPartNameDtcReports().split(" ");
        genericReportPage.hoverPartNameBubbleDtcReports();
        genericReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal reportsCostValue = genericReportPage.getFBCValueFromBubbleTooltip(
                "aPriori Cost Value (Cost Outlier) Bottom");

        genericReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setRowOne("Part Name", "Contains", partScenarioName[0])
                .apply(ExplorePage.class)
                .openFirstScenario();

        BigDecimal cidFbc = evaluatePage.getBurdenedCostValue();

        assertThat(reportsCostValue.compareTo(cidFbc), is(equalTo(0)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1947"})
    @Description("Export date range presents correctly filtered export sets")
    public void testExportSetFilterByDateCalendarWidget() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6986"})
    @Description("Percent difference threshold filter - main report - junk value")
    public void testPercentDifferenceFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                Constants.PERCENT_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6991"})
    @Description("Annualised potential savings threshold filter - main report - junk value")
    public void testAnnualisedFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6987"})
    @Description("Percent difference threshold filter - main report - decimal places")
    public void testPercentDifferenceFilterDecimalPlaces() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentDecimalPlaces(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                Constants.PERCENT_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7023"})
    @Description("Annualised potential savings threshold filter - main report - no data available")
    public void testAnnualisedPotentialSavingsNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7025"})
    @Description("Percent difference threshold filter - main report - no data available")
    public void testPercentDifferenceThresholdNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                Constants.PERCENT_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1958"})
    @Description("Percent difference threshold filter works - main report")
    public void testPercentDifferenceThresholdFilter() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName())
                .inputAnnualisedOrPercentValue(Constants.PERCENT_VALUE, "100")
                .clickOk();

        genericReportPage.waitForReportToLoad();

        assertThat(
                genericReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                        true,
                        Constants.ANNUALISED_VALUE
                ),
                is(equalTo("n/a"))
        );

        assertThat(
                genericReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                        true,
                        Constants.PERCENT_VALUE
                ),
                is(equalTo("100.0%"))
        );

        assertThat(genericReportPage.getCostOutlierBarChartBarCount(Constants.ANNUALISED_VALUE),
                is(equalTo(1))
        );
        assertThat(genericReportPage.getCostOutlierBarChartBarCount(Constants.PERCENT_VALUE),
                is(equalTo(1))
        );

        assertThat(genericReportPage.isCostOutlierBarEnabledAndDisplayed(Constants.ANNUALISED_VALUE),
                is(equalTo(true))
        );
        assertThat(genericReportPage.isCostOutlierBarEnabledAndDisplayed(Constants.PERCENT_VALUE),
                is(equalTo(true))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1957"})
    @Description("Annualised potential savings threshold filter - main report")
    public void testAnnualisedPotentialSavingsThresholdFilter() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName())
                .inputAnnualisedOrPercentValue(Constants.ANNUALISED_VALUE, "10000")
                .clickOk();

        genericReportPage.waitForReportToLoad();

        assertThat(
                genericReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                        false,
                        Constants.ANNUALISED_VALUE
                ),
                is(equalTo("10,000.00"))
        );

        assertThat(
                genericReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                        false,
                        Constants.PERCENT_VALUE
                ),
                is(equalTo("n/a"))
        );

        assertThat(genericReportPage.getCostOutlierBarChartBarCount(Constants.ANNUALISED_VALUE),
                is(equalTo(2))
        );
        assertThat(genericReportPage.getCostOutlierBarChartBarCount(Constants.PERCENT_VALUE),
                is(equalTo(2))
        );

        assertThat(genericReportPage.isCostOutlierBarEnabledAndDisplayed(Constants.ANNUALISED_VALUE),
                is(equalTo(true))
        );
        assertThat(genericReportPage.isCostOutlierBarEnabledAndDisplayed(Constants.PERCENT_VALUE),
                is(equalTo(true))
        );
    }
}