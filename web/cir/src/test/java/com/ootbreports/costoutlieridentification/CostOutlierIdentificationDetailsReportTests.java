package com.ootbreports.costoutlieridentification;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
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
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

import java.math.BigDecimal;

public class CostOutlierIdentificationDetailsReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public CostOutlierIdentificationDetailsReportTests() {
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
    @TestRail(testCaseId = {"6184"})
    @Description("Validate details report is available by library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6183"})
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1956"})
    @Description("Min & Max costs filter works")
    public void testMinMaxAprioriCost() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierDetailsReports(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                Constants.COST_NAME
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6253"})
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValues() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                Constants.COST_NAME
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6823"})
    @Description("Validate report content aligns to aP desktop or CID (where appropriate) - Details Report")
    public void testDataIntegrityAgainstCID() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                        GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())
                .clickOk();

        String partName = genericReportPage.getPartNameCastingSheetMetalDtcDetails(true);
        BigDecimal reportsFbc = genericReportPage.getFirstFbcCostOutlierDetailsReport();

        genericReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setRowOne("Part Name", "Contains", partName)
                .apply(ExplorePage.class)
                .openFirstScenario();

        BigDecimal cidFbc = evaluatePage.getBurdenedCostValue();

        assertThat(reportsFbc.compareTo(cidFbc), is(equalTo(0)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1965"})
    @Description("Validate details report generates")
    public void testDetailsReportGenerates() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                        GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())
                .clickOk();

        genericReportPage.waitForReportToLoad();

        assertThat(genericReportPage.isCostOutlierSvgDisplayedAndEnabled("1"),
                is(equalTo(true))
        );
        assertThat(genericReportPage.isCostOutlierSvgDisplayedAndEnabled("2"),
                is(equalTo(true))
        );
        assertThat(genericReportPage.isCostOutlierTableTitleDisplayed(true),
                is(equalTo(true))
        );
        assertThat(genericReportPage.isCostOutlierTableTitleDisplayed(false),
                is(equalTo(true))
        );

        genericReportPage.clickDetailsLink();
        assertThat(genericReportPage.isCostOutlierDetailsTableTitleDisplayed(Constants.PERCENT_VALUE),
                is(equalTo(true))
        );
        assertThat(genericReportPage.isCostOutlierDetailsTableTitleDisplayed(Constants.ANNUALISED_VALUE),
                is(equalTo(true))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6988"})
    @Description("Percent difference threshold filter - details report - junk value")
    public void testPercentDifferenceFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                Constants.PERCENT_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6992"})
    @Description("Annualised potential savings threshold filter - details report - junk value")
    public void testAnnualisedFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6989"})
    @Description("Percent difference threshold filter - details report - decimal places")
    public void testPercentDifferenceFilterDecimalPlaces() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentDecimalPlaces(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                Constants.PERCENT_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7024"})
    @Description("Annualised potential savings threshold filter - details report - no data available")
    public void testAnnualisedPotentialSavingsNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7026"})
    @Description("Percent difference threshold filter - details report - no data available")
    public void testPercentDifferenceThresholdNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                Constants.PERCENT_VALUE
        );
    }


    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6990"})
    @Description("Annualised potential savings threshold filter - details report")
    public void testAnnualisedPotentialSavingsThresholdFilter() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(
                        ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                        GenericReportPage.class)
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

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("SM_CLEVIS_2207240161")));

        assertThat(genericReportPage.getTotalAnnualisedOrPercentValue("Percent Value Annualised Set"),
                is(equalTo("33.9%")));
        assertThat(genericReportPage.getTotalAnnualisedOrPercentValue("Annualised Value Annualised Set"),
                is(equalTo("266,056.86")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6985"})
    @Description("Percent difference threshold filter works - details report")
    public void testPercentDifferenceThresholdFilter() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(
                        ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                        GenericReportPage.class)
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

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("-12")));

        assertThat(genericReportPage.getTotalAnnualisedOrPercentValue("Percent Value Percent Set"),
                is(equalTo("100.0%")));
        assertThat(genericReportPage.getTotalAnnualisedOrPercentValue("Annualised Value Percent Set"),
                is(equalTo("7,200.00")));
    }
}
