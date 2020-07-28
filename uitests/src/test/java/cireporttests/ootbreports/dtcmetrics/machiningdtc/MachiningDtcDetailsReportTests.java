package cireporttests.ootbreports.dtcmetrics.machiningdtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MachiningReportsEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import cireporttests.inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import org.junit.Test;

public class MachiningDtcDetailsReportTests extends TestBase {

    private InputControlsTests inputControlsTests;

    public MachiningDtcDetailsReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "3020")
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControlFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(
                MachiningReportsEnum.MACHINING_DTC_DETAILS.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify apply button on Machining DTC Details input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                MachiningReportsEnum.MACHINING_DTC_DETAILS.getReportName(),
                RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify cancel button on Machining DTC Details input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(MachiningReportsEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify reset button on Machining DTC Details input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                MachiningReportsEnum.MACHINING_DTC_DETAILS.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify save button on Machining DTC Details input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
                MachiningReportsEnum.MACHINING_DTC_DETAILS.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3565")
    @Description("Verify that earliest and latest export date fields function correctly using input field")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(MachiningReportsEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3566")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(MachiningReportsEnum.MACHINING_DTC_DETAILS.getReportName());
    }

}
