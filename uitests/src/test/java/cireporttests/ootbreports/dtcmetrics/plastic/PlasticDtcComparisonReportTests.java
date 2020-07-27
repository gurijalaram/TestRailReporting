package cireporttests.ootbreports.dtcmetrics.plastic;

import cireporttests.inputcontrols.InputControlsTests;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.PlasticDtcReportsEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;

public class PlasticDtcComparisonReportTests extends TestBase {

    private InputControlsTests inputControlsTests;

    public PlasticDtcComparisonReportTests() { super(); }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Plastic DTC Comparison input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                PlasticDtcReportsEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Plastic DTC Comparison input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(PlasticDtcReportsEnum.PLASTIC_DTC_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Plastic DTC Comparison input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                PlasticDtcReportsEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Plastic DTC Comparison input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
                PlasticDtcReportsEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

}
