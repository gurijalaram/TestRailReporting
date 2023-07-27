package com.ootbreports.dtcmetrics.machiningdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ListNameEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.MachiningDtcReportPage;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import enums.CostMetricEnum;
import enums.DtcScoreEnum;
import enums.MassMetricEnum;
import enums.RollupEnum;
import enums.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;

public class MachiningDtcDetailsReportTests extends TestBaseUI {

    private MachiningDtcReportPage machiningDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public MachiningDtcDetailsReportTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7254})
    @Description("Validate report is available by navigation - Machining DTC Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7255})
    @Description("Validate report is available by library - Machining DTC Details Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7256})
    @Description("Validate report is available by search - Machining DTC Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {3020})
    @Description("Verify Export Set list controls function correctly")
    public void testExportSetListControlFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7338})
    @Description("Verify reset button functionality - Machining DTC Details Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            RollupEnum.DTC_MACHINING_DATASET.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7335})
    @Description("Verify cancel button functionality - Machining DTC Details Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7338})
    @Description("Verify reset button functionality - Machining DTC Details Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(id = {7341})
    @Description("Verify save button functionality - Machining DTC Details Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3567})
    @Description("Verify that earlier and latest export fields throw an error when letters and special characters are entered")
    public void testExportSetDateInputInvalidCharacters() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterInvalidCharacters(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7435})
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7434})
    @Description("Verify export date filters correctly filters export sets - Input - Machining DTC Details Report")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
                ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7416})
    @Description("Verify cost metric input control functions correctly - PPC - Machining DTC Details Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7417})
    @Description("Verify cost metric input control functions correctly - FBC - Machining DTC Details Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7396})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Machining DTC Details Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7397})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Machining DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7459})
    @Description("Verify DTC Score Input Control - No Selection - Machining DTC Details Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
                ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {7497})
    @Description("Verify DTC Score Input Control - Low Selection - Machining DTC Details Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7500})
    @Description("Verify DTC Score Input Control - Medium Selection - Machining DTC Details Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7502})
    @Description("Verify DTC Score Input Control - High Selection - Machining DTC Details Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {2039})
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testComponentCostDetailReportLink() {
        machiningDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), MachiningDtcReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), MachiningDtcReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), MachiningDtcReportPage.class);

        String partName = machiningDtcReportPage.clickMachiningDtcDetailsPartName().replace("", "");

        assertThat(
            machiningDtcReportPage.getUpperTitleText(),
            is(equalTo(ReportNamesEnum.DTC_PART_SUMMARY.getReportName()))
        );
        assertThat(
                partName,
                is(startsWith(machiningDtcReportPage.getDtcPartSummaryPartNameValue()))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3031})
    @Description("Verify Select Parts list controls function correctly")
    public void testPartListInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(),
            ListNameEnum.PARTS_NO_SPACE.getListName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {3027})
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendDetailsReports(
            ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(),
            ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingIssues() {
        String exportSet = ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName();
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(exportSet, GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        genericReportPage.waitForReportToLoad();

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("DTCMACHINING_001")));
        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("MACHININGDESIGN_TO_COST")));
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlDesignStandards() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.DESIGN_STANDARDS.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("DTCMACHINING_001")));
        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("PUNCH")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlTolerances() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.TOLERANCES.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("MACHININGDESIGN_TO_COST")));
        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("PARTBODY_1")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlSlowOperations() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("MACHININGDESIGN_TO_COST")));
        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("DTCMACHINING_001")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlAnnualSpend() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("PMI_ROUGHNESSCREO")));
        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("PMI_PROFILEOFSURFACECREO")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {3025})
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlDtcRank() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.DTC_RANK.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("MACHININGDESIGN_TO_COST")));
        assertThat(genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
            is(equalTo("PUNCH")));
    }
}
