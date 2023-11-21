package com.apriori.cir.ui.tests.ootbreports.dtcmetrics.plasticdtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.PlasticDtcReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ListNameEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class PlasticDtcReportTests extends TestBaseUI {

    private PlasticDtcReportPage plasticDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public PlasticDtcReportTests() {
        super();
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TmsLink("7303")
    @TestRail(id = {7303})
    @Description("Validate report is available by navigation - Plastic DTC Report")
    public void testPlasticDtcReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7306")
    @TestRail(id = {7306})
    @Description("Validate report is available by library - Plastic DTC Report")
    public void testPlasticDtcReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7309")
    @TestRail(id = {7309})
    @Description("Validate report is available by search - Plastic DTC Report")
    public void testPlasticDtcReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1344")
    @TestRail(id = {1344})
    @Description("Test Plastic DTC Reports Export Set Availability")
    public void testPlasticDtcReportExportSetAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailability(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1365")
    @TestRail(id = {1365})
    @Description("Verify rollup dropdown input control functions correctly")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1370")
    @TestRail(id = {1370})
    @Description("Verify currency code functionality works correctly")
    public void testCurrencyCodeFunctionality() {
        BigDecimal gbpAnnualSpend;
        BigDecimal usdAnnualSpend;

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), PlasticDtcReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), GenericReportPage.class);

        assertThat(
            genericReportPage.getSelectedRollup(RollupEnum.ROLL_UP_A.getRollupName()),
            is(equalTo(RollupEnum.ROLL_UP_A.getRollupName()))
        );

        genericReportPage.checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        genericReportPage.setReportName(ReportNamesEnum.PLASTIC_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        usdAnnualSpend = genericReportPage.getAnnualSpendFromBubbleTooltip();

        genericReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), PlasticDtcReportPage.class);

        genericReportPage.hoverPartNameBubbleDtcReports();
        gbpAnnualSpend = genericReportPage.getAnnualSpendFromBubbleTooltip();

        assertThat(genericReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpAnnualSpend, is(not(usdAnnualSpend)));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1345")
    @TestRail(id = {1345})
    @Description("Test Plastic DTC Export Set Filter using Input Field")
    public void testPlasticDtcExportSetFilterInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
                ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1345")
    @TestRail(id = {1345})
    @Description("Test Plastic DTC Export Set Filter using Date Picker")
    public void testPlasticDtcExportSetFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1346")
    @TestRail(id = {1346})
    @Description("Test Plastic DTC Export Set Selection")
    public void testPlasticDtcExportSetSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1376")
    @TestRail(id = {1376})
    @Description("Test Plastic DTC Data Integrity")
    public void testPlasticDtcDataIntegrity() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), GenericReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class);

        genericReportPage.setReportName(ReportNamesEnum.PLASTIC_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        String partName = genericReportPage.getPartNameDtcReports();
        BigDecimal reportFbcValue = genericReportPage.getFBCValueFromBubbleTooltip("FBC Value");

        genericReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .saveAs()
                .inputName(new GenerateStringUtil().generateFilterName())
                .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, partName)
                .addCriteria(PropertyEnum.SCENARIO_NAME,OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
                .submit(ExplorePage.class)
                .openFirstScenario();

        BigDecimal cidFbcValue = new BigDecimal(String.valueOf(evaluatePage.getCostResults("Fully Burdened Cost")));

        assertThat(reportFbcValue, is(equalTo(cidFbcValue)));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7354")
    @TestRail(id = {7354})
    @Description("Verify apply button functionality - Plastic DTC Report")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7357")
    @TestRail(id = {7357})
    @Description("Verify cancel button functionality - Plastic DTC Report")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7360")
    @TestRail(id = {7360})
    @Description("Verify reset button functionality - Plastic DTC Report ")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Disabled("not applicable due to reports configuration")
    @TmsLink("7363")
    @TestRail(id = {7363})
    @Description("Verify save button functionality - Plastic DTC Report ")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveButton(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1366")
    @TestRail(id = {1366})
    @Description("Verify cost metric input control functions correctly - PPC - Plastic DTC Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7403")
    @TestRail(id = {7403})
    @Description("Verify cost metric input control functions correctly - FBC - Plastic DTC Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7380")
    @TestRail(id = {7380})
    @Description("Verify Mass Metric input control functions correctly - Finish Mass - Plastic DTC Report")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1368")
    @TestRail(id = {1368})
    @Description("Verify Mass Metric input control functions correctly - Rough Mass - Plastic DTC Report")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1709")
    @TestRail(id = {1709})
    @Description("Validate chart tool-tips")
    public void testChartToolTips() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcChartTooltips(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7517")
    @TestRail(id = {7517})
    @Description("Verify DTC Score Input Control - No Selection - Plastic DTC Report")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7520")
    @TestRail(id = {7520})
    @Description("Verify DTC Score Input Control - Low Selection - Plastic DTC Report")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7523")
    @TestRail(id = {7523})
    @Description("Verify DTC Score Input Control - Medium Selection - Plastic DTC Report")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7326")
    @TestRail(id = {7526})
    @Description("Verify DTC Score Input Control - High Selection - Plastic DTC Report")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1380")
    @TestRail(id = {1380})
    @Description("Verify links to help files function correctly")
    public void testLinkToReportsUserGuide() throws Exception {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportsUserGuideNavigation(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1374")
    @TestRail(id = {1374})
    @Description("Verify Select Parts list controls function correctly")
    public void testPartListInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            "",
            ListNameEnum.PARTS.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1371")
    @TestRail(id = {1371})
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpend(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("2320")
    @TestRail(id = {2320})
    @Description("Verify minimum annual spend input control correctly filters list of available parts")
    public void testMinimumAnnualSpendFiltersPartList() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), PlasticDtcReportPage.class)
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName(), PlasticDtcReportPage.class);

        plasticDtcReportPage.inputMinimumAnnualSpend();
        plasticDtcReportPage.clickDistanceOutlierInputAndScrollDown();

        assertThat(
                plasticDtcReportPage.getCountOfListAvailableOrSelectedItems(
                        ListNameEnum.PARTS.getListName(),
                        "Available"),
                is(equalTo("0"))
        );
    }
}
