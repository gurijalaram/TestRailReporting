package ootbreports.dtcmetrics.plastic;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import inputcontrols.InputControlsTests;
import navigation.ReportAvailabilityTests;
import io.qameta.allure.Description;
import org.junit.Test;

public class PlasticDtcComparisonReportTests extends TestBase {

    private ReportAvailabilityTests reportAvailabilityTests;
    private InputControlsTests inputControlsTests;

    public PlasticDtcComparisonReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityByNavigation() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByNavigation(
                Constants.DTC_METRICS_FOLDER,
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityByLibrary() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityBySearch() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityBySearch(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Plastic DTC Comparison input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Plastic DTC Comparison input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Plastic DTC Comparison input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Plastic DTC Comparison input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }
}
