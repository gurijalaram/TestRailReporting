package com.ootbreports.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import com.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.pageobjects.pages.explore.ExplorePage;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import utils.Constants;

public class CastingDtcDetailsReportTests extends TestBase {

    private CommonReportTests commonReportTests;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;

    public CastingDtcDetailsReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1676")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            Constants.DTC_METRICS_FOLDER,
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1676")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1676")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1692")
    @Description("Verify export set input controls function correctly")
    public void testCastingDtcDetailsExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1694")
    @Description("Verify roll-up dropdown functions correctly for Casting DTC Details report")
    public void testRollupDropDown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC Details input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC Details input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC Details input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Casting DTC Details input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1691")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1691")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }


    @Test
    @TestRail(testCaseId = "102990")
    @Description("Verify that aPriori costed scenarios are represented correctly")
    public void testVerifyDetailsReportAvailableAndCorrectData() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk();

        genericReportPage.setReportName(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
        String partName = genericReportPage.getPartNameDtcReports();
        String holeIssueNumReports = genericReportPage.getHoleIssuesFromDetailsReport();
        genericReportPage.openNewCidTabAndFocus(1);

        DesignGuidancePage designGuidancePage = new ExplorePage(driver)
            .filter()
            .setScenarioType(Constants.PART_SCENARIO_TYPE)
            .setWorkspace(Constants.PUBLIC_WORKSPACE)
            .setRowOne("Part Name", "Contains", partName)
            .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
            .apply(ExplorePage.class)
            .openFirstScenario()
            .openDesignGuidance();

        String holeIssueCidValue = designGuidancePage.getHoleIssueValue();

        assertThat(holeIssueNumReports, is(equalTo(holeIssueCidValue)));
    }

    @Test
    @TestRail(testCaseId = "1695")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1695")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1696")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1696")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1372")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "1372")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "1372")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @TestRail(testCaseId = "1700")
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingCasting() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.CASTING_ISSUES.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("JEEP WJ FRONT BRAKE DISC 99-04")));
        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("GEAR HOUSING")));
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingMachining() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("DTCCASTINGISSUES")));
        assertThat(genericReportPage.getScenarioNameCastingDtcDetails(true),
            is(equalTo("sand casting")));

        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("DTCCASTINGISSUES")));
        assertThat(genericReportPage.getScenarioNameCastingDtcDetails(false),
            is(equalTo("Initial")));
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlMaterialScrap() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("OBSTRUCTED MACHINING")));
        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("B2315")));
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlTolerances() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.TOLERANCES.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("DTCCASTINGISSUES")));
        assertThat(genericReportPage.getScenarioNameCastingDtcDetails(true),
            is(equalTo("Initial")));

        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("DTCCASTINGISSUES")));
        assertThat(genericReportPage.getScenarioNameCastingDtcDetails(false),
            is(equalTo("sand casting")));
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlSlowOperations() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("DTCCASTINGISSUES")));
        assertThat(genericReportPage.getScenarioNameCastingDtcDetails(true),
            is(equalTo("Initial")));

        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("DTCCASTINGISSUES")));
        assertThat(genericReportPage.getScenarioNameCastingDtcDetails(false),
            is(equalTo("sand casting")));
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlSpecialTooling() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("DU600051458")));
        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("DU200068073_B")));
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlAnnualSpend() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("E3-241-4-N")));
        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("40137441.MLDES.0002")));
    }

    @Test
    @TestRail(testCaseId = "1698")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlDtcRank() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectSortOrder(SortOrderEnum.DTC_RANK.getSortOrderEnum())
            .clickOk();

        assertThat(genericReportPage.getPartNameCastingDtcDetails(true),
            is(equalTo("BARCO_R8552931")));
        assertThat(genericReportPage.getPartNameCastingDtcDetails(false),
            is(equalTo("BARCO_R8761310")));
    }

    @Test
    @TestRail(testCaseId = "1708")
    @Description("Verify DTC issue counts are correct")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testCastingDtcIssueCounts(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }
}
