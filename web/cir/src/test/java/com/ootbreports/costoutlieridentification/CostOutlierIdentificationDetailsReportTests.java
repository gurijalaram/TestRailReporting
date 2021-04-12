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
    @TestRail(testCaseId = "1944")
    @Description("Validate report is available by navigation - menu")
    public void testReportAvailableByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "6184")
    @Description("Validate details report is available by library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "6183")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1956")
    @Description("Min & Max costs filter works")
    public void testMinMaxAprioriCost() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierDetailsReports(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "6253")
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValues() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "6823")
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
    @TestRail(testCaseId = "1965")
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
        assertThat(genericReportPage.isCostOutlierDetailsTableTitleDisplayed("Percent"),
                is(equalTo(true))
        );
        assertThat(genericReportPage.isCostOutlierDetailsTableTitleDisplayed("Annualized"),
                is(equalTo(true))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "3988")
    @Description("Percent difference threshold filter - details report - junk value")
    public void testPercentDifferenceFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                "Percent"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "6992")
    @Description("Annualised potential savings threshold filter - details report - junk value")
    public void testAnnualisedFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                "Annualized"
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "6987")
    @Description("Percent difference threshold filter - main report - decimal places")
    public void testPercentDifferenceFilterDecimalPlaces() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentDecimalPlaces(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                "Percent"
        );
    }
}
