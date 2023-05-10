package com.ootbreports.dtcmetrics.sheetmetaldtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.SheetMetalDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

import java.math.BigDecimal;

public class SheetMetalDtcReportTests extends TestBase {

    private SheetMetalDtcReportPage sheetMetalDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public SheetMetalDtcReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7312"})
    @Description("Validate report is available by navigation - Sheet Metal DTC Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7315"})
    @Description("Validate report is available by library - Sheet Metal DTC Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.SHEET_METAL_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7318"})
    @Description("Validate report is available by search - Sheet Metal DTC Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.SHEET_METAL_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3038"})
    @Description("Verify Export Sets are available for selection")
    public void testExportSetAndRollupSelection() {
        String rollupName = String.format(
                "%s (%s)",
                ReportNamesEnum.SHEET_METAL_DTC.getReportName().toUpperCase(),
                Constants.DEFAULT_SCENARIO_NAME
        );
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC.getReportName(), SheetMetalDtcReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), SheetMetalDtcReportPage.class)
                .waitForCorrectRollupInDropdown(rollupName)
                .clickOk(true, SheetMetalDtcReportPage.class)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getDisplayedRollup(), is(equalTo(rollupName)));
        assertThat(sheetMetalDtcReportPage.getDisplayedExportSet(),
                is(equalTo(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3039"})
    @Description("Verify earliest and latest export date calendar widgets correctly filter the list of export sets")
    public void testExportSetDateFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.SHEET_METAL_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7366"})
    @Description("Verify apply button functionality - Sheet Metal DTC Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                RollupEnum.SHEET_METAL_DTC.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7369"})
    @Description("Verify cancel button functionality - Sheet Metal DTC Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.SHEET_METAL_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7372"})
    @Description("Verify reset button functionality - Sheet Metal DTC Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = {"7375"})
    @Description("Verify save button functionality - Sheet Metal DTC Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3040"})
    @Description("Verify Export Set list controls function correctly - Panel Buttons - Sheet Metal DTC Reports")
    public void testExportSetListControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.SHEET_METAL_DTC.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7690"})
    @Description("Verify Export Set list controls function correctly - Search - Sheet Metal DTC Reports")
    public void testExportSetSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSearch(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3042"})
    @Description("Verify Roll-up input control functions correctly")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                RollupEnum.SHEET_METAL_DTC.getRollupName()
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"3043"})
    @Description("Verify cost metric input control functions correctly - PPC - Sheet Metal DTC Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningSheetMetalDtc(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7418"})
    @Description("Verify cost metric input control functions correctly - FBC - Sheet Metal DTC Report ")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlMachiningSheetMetalDtc(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3044"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7398"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Sheet Metal DTC Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3045"})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Sheet Metal DTC Report")
    public void testSortOrderAnnualSpend() {
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
                .selectSortOrder(SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum())
                .clickOk(true, SheetMetalDtcReportPage.class)
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        sheetMetalDtcReportPage.waitForReportToLoad();

        sheetMetalDtcReportPage.setReportName(ReportNamesEnum.SHEET_METAL_DTC.getReportName());
        sheetMetalDtcReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal largerAnnualSpend = sheetMetalDtcReportPage.getAnnualSpendFromBubbleTooltip();

        sheetMetalDtcReportPage.setReportName(ReportNamesEnum.SHEET_METAL_DTC.getReportName().concat(" 2"));
        sheetMetalDtcReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal smallerAnnualSpend = sheetMetalDtcReportPage.getAnnualSpendFromBubbleTooltip();

        assertThat(smallerAnnualSpend.compareTo(largerAnnualSpend), is(equalTo(-1)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3046"})
    @Description("Verify Currency Code input control functions correctly - Sheet Metal DTC Report")
    public void testCurrencyCodeInputControl() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCodeDtcReports(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7445"})
    @Description("Verify process group input control functionality - No Selection - Sheet Metal DTC Report")
    public void testProcessGroupInputControlNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testProcessGroupInputControlNoSelection(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7448"})
    @Description("Verify process group input control functionality - Single Selection - Sheet Metal DTC Report")
    public void testSingleProcessGroup() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"3051"})
    @Description("Verify Select Parts list controls function correctly - Panel Buttons")
    public void testPartListInputControlButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                ListNameEnum.PARTS.getListName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7529"})
    @Description("Verify DTC Score Input Control - No Selection - Sheet Metal DTC Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7532"})
    @Description("Verify DTC Score Input Control - Low Selection - Sheet Metal DTC Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7535"})
    @Description("Verify DTC Score Input Control - Medium Selection - Sheet Metal DTC Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7538"})
    @Description("Verify DTC Score Input Control - High Selection - Sheet Metal DTC Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(),
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }
}
