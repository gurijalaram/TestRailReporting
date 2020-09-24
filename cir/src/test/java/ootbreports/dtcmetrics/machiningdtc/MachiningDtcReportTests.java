package ootbreports.dtcmetrics.machiningdtc;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import navigation.ReportAvailabilityTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.view.reports.GenericReportPage;
import testsuites.suiteinterface.CIARStagingSmokeTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class MachiningDtcReportTests extends TestBase {

    private ReportAvailabilityTests reportAvailabilityTests;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;

    public MachiningDtcReportTests() {
        super();
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "2024")
    @Description("Verify report availability by navigation")
    public void testReportAvailabilityByNavigation() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByNavigation(
                Constants.DTC_METRICS_FOLDER,
                ReportNamesEnum.MACHINING_DTC.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "3415")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByLibrary(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3416")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityBySearch(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3026")
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCode(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3567")
    @Description("Verify that earlier and latest export fields throw an error when letters and special characters are entered")
    public void testExportSetDateInputInvalidCharacters() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterInvalidCharacters(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3565")
    @Description("Verify that earliest and latest export date fields function correctly using input field")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3566")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3020")
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControlFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify apply button on Machining DTC input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify cancel button on Machining DTC input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify reset button on Machining DTC input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "3021")
    @Description("Verify save button on Machining DTC input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "2026")
    @Description("Verify Export Sets are available for selection")
    public void testExportSetSelectionAndAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailabilityAndSelection(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
                RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "3022")
    @Description("Verify Roll-up input control functions correctly")
    public void testRollupDropDown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1690")
    @Description("Verify export sets are available for selection")
    public void testExportSetAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailability(ReportNamesEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3023")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningDtc(
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "3023")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningDtc(
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
                MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
                MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "3028")
    @Description("Verify Process Group input control functions correctly")
    public void testProcessGroupStockMachiningOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
                ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()
        );
    }

    @Test
    @TestRail(testCaseId = "3028")
    @Description("Verify Process Group input control functions correctly")
    public void testProcessGroupTwoModelMachiningOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
                ReportNamesEnum.MACHINING_DTC.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
                ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()
        );
    }

    @Test
    @TestRail(testCaseId = "3028")
    @Description("Verify Process Group input control functions correctly")
    public void testProcessGroupSandAndDieCasting() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTwoProcessGroupsMachining();
    }

    @Test
    @TestRail(testCaseId = "2039")
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testComponentCostDetailReportLink() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        String partName = genericReportPage.getPartNameAndClickBubbleTwice();

        assertThat(genericReportPage.getUpperTitleText(), is(equalTo("DTC Part Summary")));
        assertThat(partName, is(startsWith(genericReportPage.getDtcPartSummaryPartNameValue())));
    }
}
