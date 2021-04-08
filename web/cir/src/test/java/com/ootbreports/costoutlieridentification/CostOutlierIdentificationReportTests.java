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
import testsuites.suiteinterface.CiaCirTestDevTest;
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
    @TestRail(testCaseId = "6182")
    @Description("Validate report is available by library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1945")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1954")
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
    @TestRail(testCaseId = "1954")
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
    @TestRail(testCaseId = "1956")
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
    @TestRail(testCaseId = "6253")
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
    @TestRail(testCaseId = "1959")
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
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1947")
    @Description("Export date range presents correctly filtered export sets")
    public void testExportSetFilterByDateCalendarWidget() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName()
        );
    }
}
