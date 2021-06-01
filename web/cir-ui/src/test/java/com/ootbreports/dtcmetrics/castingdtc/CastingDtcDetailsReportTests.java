package com.ootbreports.dtcmetrics.castingdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.CastingDtcReportPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.GenerateStringUtil;
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
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class CastingDtcDetailsReportTests extends TestBase {

    private CastingDtcReportPage castingDtcReportPage;
    private CommonReportTests commonReportTests;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;

    public CastingDtcDetailsReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7241"})
    @Description("Validate report is available by navigation - Casting DTC Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"7244"})
    @Description("Verify report is available by library - Casting DTC Details Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7249"})
    @Description("Verify report is available by search - Casting DTC Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7653"})
    @Description("Verify Export Set list controls function correctly - Casting DTC Details Report")
    public void testCastingDtcDetailsExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7655"})
    @Description("Verify Roll-up input control functions correctly - Casting DTC Details Report")
    public void testRollupDropDown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7344"})
    @Description("Verify apply button functionality - Casting DTC Details Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7349"})
    @Description("Verify cancel button functionality - Casting DTC Details Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7351"})
    @Description("Verify reset button functionality - Casting DTC Details Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = {"7353"})
    @Description("Verify save button functionality - Casting DTC Details Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7428"})
    @Description("Verify export date filters correctly filters export sets - Picker - Casting DTC Details Report")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7429"})
    @Description("Verify export date filters correctly filters export sets - Input - Casting DTC Details Report")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    //@Category(ReportsTest.class)
    @TestRail(testCaseId = {"7620"})
    @Description("Verify that aPriori costed scenarios are represented correctly - Casting DTC Details Report")
    public void testVerifyDetailsReportAvailableAndCorrectData() {
        castingDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), CastingDtcReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), CastingDtcReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), CastingDtcReportPage.class)
            .clickOk(CastingDtcReportPage.class);

        castingDtcReportPage.setReportName(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
        String partName = castingDtcReportPage.getPartNameDtcReports();
        String holeIssueNumReports = castingDtcReportPage.getHoleIssuesFromDetailsReport();
        castingDtcReportPage.openNewCidTabAndFocus(1);

        genericReportPage.openNewCidTabAndFocus(1);
        GuidanceIssuesPage guidanceIssuesPage = new ExplorePage(driver)
                .filter()
                .saveAs()
                .inputName(new GenerateStringUtil().generateFilterName())
                .addCriteriaWithOption("Component Name", "Equals", partName)
                .addCriteriaWithOption("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
                .submit(ExplorePage.class)
                .openFirstScenario()
                .openDesignGuidance();

        String holeIssueCidValue = guidanceIssuesPage.getDtcIssueCount("Hole");

        assertThat(holeIssueNumReports, is(equalTo(holeIssueCidValue)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7411"})
    @Description("Verify cost metric input control functions correctly - PPC - Casting DTC Details Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7412"})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Details Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7391"})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Details Report ")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7392"})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Details Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7507"})
    @Description("Verify DTC Score Input Control - No Selection - Casting DTC Details Report ")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7510"})
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Details Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7513"})
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Details Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = {"7516"})
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Details Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7657"})
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Details Report")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendDetailsReports(
            ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7629"})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Casting - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingCasting() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.CASTING_ISSUES.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("JEEP WJ FRONT BRAKE DISC 99-04"))
        );

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("GEAR HOUSING"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7630"})
    @Description("Verify Sort Order input control functions correctly - Manufacturing Machining - Casting DTC Details Report")
    public void testSortOrderInputControlManufacturingMachining() {
        castingDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.MACHINING_ISSUES.getSortOrderEnum())
            .clickOk(CastingDtcReportPage.class);

        assertThat(
                castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("DTCCASTINGISSUES"))
        );

        assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(true),
                is(equalTo("sand casting"))
        );

        assertThat(
                castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("DTCCASTINGISSUES"))
        );

        assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(false),
                is(equalTo("Initial"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7631"})
    @Description("Verify Sort Order input control functions correctly - Material Scrap - Casting DTC Details Report")
    public void testSortOrderInputControlMaterialScrap() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.MATERIAL_SCRAP.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("OBSTRUCTED MACHINING"))
        );

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("B2315"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7632"})
    @Description("Verify Sort Order input control functions correctly - Tolerances - Casting DTC Details Report")
    public void testSortOrderInputControlTolerances() {
        castingDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.TOLERANCES.getSortOrderEnum())
            .clickOk(CastingDtcReportPage.class);

        assertThat(
                castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("DTCCASTINGISSUES"))
        );

        assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(true),
                is(equalTo("Initial"))
        );

        assertThat(
                castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("DTCCASTINGISSUES"))
        );

        assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(false),
                is(equalTo("sand casting"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7633"})
    @Description("Verify Sort Order input control functions correctly - Slow Operations - Casting DTC Details Report")
    public void testSortOrderInputControlSlowOperations() {
        castingDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.SLOW_OPERATIONS.getSortOrderEnum())
            .clickOk(CastingDtcReportPage.class);

        assertThat(
                castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("DTCCASTINGISSUES"))
        );

        assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(true),
                is(equalTo("Initial"))
        );

        assertThat(
                castingDtcReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("DTCCASTINGISSUES"))
        );

        assertThat(
                castingDtcReportPage.getScenarioNameCastingDtcDetails(false),
                is(equalTo("sand casting"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7634"})
    @Description("Verify Sort Order input control functions correctly - Special Tooling - Casting DTC Details Report")
    public void testSortOrderInputControlSpecialTooling() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.SPECIAL_TOOLING.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("DU600051458"))
        );

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("DU200068073_B"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7635"})
    @Description("Verify Sort Order input control functions correctly - Annual Spend - Casting DTC Details Report")
    public void testSortOrderInputControlAnnualSpend() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.ANNUAL_SPEND.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("E3-241-4-N"))
        );

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("40137441.MLDES.0002"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7636"})
    @Description("Verify Sort Order input control functions correctly - DTC Rank - Casting DTC Details Report")
    public void testSortOrderInputControlDtcRank() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName(), GenericReportPage.class)
            .selectSortOrder(SortOrderEnum.DTC_RANK.getSortOrderEnum())
            .clickOk(GenericReportPage.class);

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(true),
                is(equalTo("BARCO_R8552931"))
        );

        assertThat(
                genericReportPage.getPartNameCastingSheetMetalDtcDetails(false),
                is(equalTo("BARCO_R8761310"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7644"})
    @Description("Verify DTC issue counts are correct - Casting DTC Details Report")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testCastingDtcIssueCounts(ReportNamesEnum.CASTING_DTC_DETAILS.getReportName());
    }
}
