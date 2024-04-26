package com.apriori.cir.ui.tests.ootbreports.dtcmetrics.castingdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ListNameEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CastingDtcReportTests extends TestBaseUI {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public CastingDtcReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7240")
    @TestRail(id = {7240})
    @Description("Validate report is available by navigation - Casting DTC Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.CASTING_DTC.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7243")
    @TestRail(id = {7243})
    @Description("Verify report is available by library - Casting DTC Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7247")
    @TestRail(id = 7247)
    @Description("Verify report availability by search - Casting DTC Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1692")
    @TestRail(id = {1692})
    @Description("Verify Export Set list controls function correctly - Casting DTC Report")
    public void testCastingDtcExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1694")
    @TestRail(id = {1694})
    @Description("Verify Roll-up input control functions correctly - Casting DTC Report")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7342")
    @TestRail(id = 7342)
    @Description("Verify apply button functionality - Casting DTC Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7345")
    @TestRail(id = {7345})
    @Description("Verify cancel button functionality - Casting DTC Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7346")
    @TestRail(id = {7346})
    @Description("Verify reset button functionality - Casting DTC Report")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @Disabled("Not applicable due to reports configuration")
    @TmsLink("7347")
    @TestRail(id = {7347})
    @Description("Verify save button functionality - Casting DTC Report")
    public void testSaveButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1691")
    @TestRail(id = {1691})
    @Description("Verify export date filters correctly filters export sets - Picker - Casting DTC Report")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7427")
    @TestRail(id = {7427})
    @Description("Verify export date filters correctly filters export sets - Input - Casting DTC Report")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.CASTING_DTC.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1715")
    @TestRail(id = {1715})
    @Description("Verify that aPriori costed scenarios are represented correctly - Casting DTC Report")
    public void testVerifyCastingDtcReportIsAvailableWithRollUp() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), GenericReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class);

        genericReportPage.setReportName(ReportNamesEnum.CASTING_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal reportFbcValue = genericReportPage.getFBCValueFromBubbleTooltip("FBC Value");
        String partName = genericReportPage.getPartNameDtcReports();

        genericReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, partName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openFirstScenario();

        BigDecimal cidFbcValue = new BigDecimal(String.valueOf(evaluatePage.getCostResults("Fully BurdenedCost")));

        /*
            This is great now, but rounding in CID is not done at all really (long story)
            Thus this may start failing in due course, and can be fixed then
            Currency in Reports and CID needs to match for this test also (both default to USD)
         */
        assertThat(reportFbcValue, is(equalTo(cidFbcValue)));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1699")
    @TestRail(id = {1699})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCurrencyCodeDtcReports(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1703")
    @TestRail(id = {1703})
    @Description("Verify Select Parts list controls function correctly")
    public void testPartListInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            "",
            ListNameEnum.PARTS.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1695")
    @TestRail(id = {1695})
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7408")
    @TestRail(id = {7408})
    @Description("Verify cost metric input control functions correctly - FBC - Casting DTC Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1696")
    @TestRail(id = {1696})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Casting DTC Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7388")
    @TestRail(id = {7388})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Casting DTC Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7454")
    @TestRail(id = {7454})
    @Description("Verify process group input control functionality - Die Casting - Casting DTC Report")
    public void testProcessGroupDieCastingOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            ProcessGroupEnum.CASTING_DIE.getProcessGroup()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7453")
    @TestRail(id = {7453})
    @Description("Verify process group input control functionality - Sand Casting - Casting DTC Report")
    public void testProcessGroupSandCastingOnly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSingleProcessGroup(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            ProcessGroupEnum.CASTING_SAND.getProcessGroup()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7455")
    @TestRail(id = 7455)
    @Description("Verify process group input control functionality - Sand and Die Casting - Casting DTC Report")
    public void testProcessGroupSandAndDieCasting() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testTwoProcessGroupsCasting();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1709")
    @TestRail(id = {1709})
    @Description("Validate chart tool-tips")
    public void testChartToolTips() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcChartTooltips(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7505")
    @TestRail(id = {7505})
    @Description("Verify DTC Score Input Control - No Selection - Casting DTC Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7508")
    @TestRail(id = {7508})
    @Description("Verify DTC Score Input Control - Low Selection - Casting DTC Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7511")
    @TestRail(id = {7511})
    @Description("Verify DTC Score Input Control - Medium Selection - Casting DTC Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName().concat(" Casting")
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7514")
    @TestRail(id = {7514})
    @Description("Verify DTC Score Input Control - High Selection - Casting DTC Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1710")
    @TestRail(id = {1710})
    @Description("Verify links to help files function correctly")
    public void testLinkToReportsUserGuide() throws Exception {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportsUserGuideNavigation(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1700")
    @TestRail(id = {1700})
    @Description("Verify Minimum Annual Spend input control functions correctly - Casting DTC Report")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpend(
            ReportNamesEnum.CASTING_DTC.getReportName(),
            ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }
}
