package com.ootbreports.costoutlieridentification;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;
import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.ReportsTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CostOutlierIdentificationReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

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
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
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
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "6253")
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValues() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                "Cost"
        );
    }
}
