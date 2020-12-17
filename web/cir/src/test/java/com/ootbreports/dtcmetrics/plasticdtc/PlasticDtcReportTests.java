package com.ootbreports.dtcmetrics.plasticdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.PlasticDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ExplorePage;
import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsSmokeTest;
import utils.Constants;

import java.math.BigDecimal;

public class PlasticDtcReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public PlasticDtcReportTests() {
        super();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            Constants.DTC_METRICS_FOLDER,
            ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
	@Category({ReportsSmokeTest.class, OnPremTest.class})
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1344")
    @Description("Test Plastic DTC Reports Export Set Availability")
    public void testPlasticDtcReportExportSetAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailability(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1365")
    @Description("Verify rollup dropdown input control functions correctly")
    public void testRollupDropdown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1370")
    @Description("Verify currency code functionality works correctly")
    public void testCurrencyCodeFunctionality() {
        BigDecimal gbpAnnualSpend;
        BigDecimal usdAnnualSpend;

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), PlasticDtcReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName());

        assertThat(
            genericReportPage.getSelectedRollup(RollupEnum.ROLL_UP_A.getRollupName()),
            is(equalTo(RollupEnum.ROLL_UP_A.getRollupName()))
        );

        genericReportPage.checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        genericReportPage.setReportName(ReportNamesEnum.PLASTIC_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        usdAnnualSpend = genericReportPage.getAnnualSpendFromBubbleTooltip();

        genericReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), PlasticDtcReportPage.class);

        genericReportPage.hoverPartNameBubbleDtcReports();
        gbpAnnualSpend = genericReportPage.getAnnualSpendFromBubbleTooltip();

        assertThat(genericReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpAnnualSpend, is(not(usdAnnualSpend)));
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Input Field")
    public void testPlasticDtcExportSetFilterInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
	@Category({ReportsSmokeTest.class, OnPremTest.class})
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Date Picker")
    public void testPlasticDtcExportSetFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1346")
    @Description("Test Plastic DTC Export Set Selection")
    public void testPlasticDtcExportSetSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1376")
    @Description("Test Plastic DTC Data Integrity")
    public void testPlasticDtcDataIntegrity() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk();

        genericReportPage.setReportName(ReportNamesEnum.PLASTIC_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        String partName = genericReportPage.getPartNameDtcReports();
        BigDecimal reportFbcValue = genericReportPage.getFBCValueFromBubbleTooltip();
        genericReportPage.openNewCidTabAndFocus(1);

        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .setScenarioType(Constants.PART_SCENARIO_TYPE)
            .setWorkspace(Constants.PUBLIC_WORKSPACE)
            .setRowOne("Part Name", "Contains", partName)
            .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
            .apply(ExplorePage.class)
            .openFirstScenario();

        BigDecimal cidFbcValue = evaluatePage.getBurdenedCostValue();

        assertThat(reportFbcValue, is(equalTo(cidFbcValue)));
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Plastic DTC input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlComparisonDetailsDtcReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricDtcReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1709")
    @Description("Validate chart tool-tips")
    public void testChartToolTips() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcChartTooltips(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1701")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreNoSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreInputControlNoSelection(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1701")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.LOW.getDtcScoreName()
        );
    }


    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1701")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1701")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreMainReports(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName(),
            DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1380")
    @Description("Verify links to help files function correctly")
    public void testLinkToReportsUserGuide() throws Exception {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportsUserGuideNavigation(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1374")
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
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1371")
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpend(
            ReportNamesEnum.PLASTIC_DTC.getReportName(),
            ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "2320")
    @Description("Verify minimum annual spend input control correctly filters list of available parts")
    public void testMinimumAnnualSpendFiltersPartList() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .inputMinimumAnnualSpend()
            .clickDistanceOutlierInputAndScrollDown();

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.PARTS.getListName(), "Available"), is(equalTo("0")));
    }
}
