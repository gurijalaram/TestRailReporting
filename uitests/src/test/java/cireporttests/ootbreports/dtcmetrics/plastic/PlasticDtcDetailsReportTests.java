package cireporttests.ootbreports.dtcmetrics.plastic;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.PlasticDtcReportsEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import cireporttests.inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import org.junit.Test;

public class PlasticDtcDetailsReportTests extends TestBase {

    private InputControlsTests inputControlsTests;

    public PlasticDtcDetailsReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Plastic DTC Details input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                PlasticDtcReportsEnum.PLASTIC_DTC_DETAILS.getReportName(),
                RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Plastic DTC Details input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(PlasticDtcReportsEnum.PLASTIC_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Plastic DTC Details input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                PlasticDtcReportsEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Plastic DTC Details input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
                PlasticDtcReportsEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }
}
