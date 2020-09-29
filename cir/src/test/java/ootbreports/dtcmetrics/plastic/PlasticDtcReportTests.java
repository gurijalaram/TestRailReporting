package ootbreports.dtcmetrics.plastic;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import navigation.CommonReportTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.view.reports.GenericReportPage;
import pageobjects.pages.view.reports.PlasticDtcReportPage;
import testsuites.suiteinterface.CiaCirTestDevTest;

import java.math.BigDecimal;

public class PlasticDtcReportTests extends TestBase {

    private PlasticDtcReportPage plasticDtcReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public PlasticDtcReportTests() {
        super();
    }

    @Test
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
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.PLASTIC_DTC.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "1344")
    @Description("Test Plastic DTC Reports Export Set Availability")
    public void testPlasticDtcReportExportSetAvailability() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetAvailability(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1365")
    @Description("Verify rollup dropdown input control functions correctly")
    public void testRollupDropdownInputControlsFunctionsProperly() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1370")
    @Description("Verify currency code functionality works correctly")
    public void testCurrencyCodeFunctionality() {
        BigDecimal gbpAnnualSpend;
        BigDecimal usdAnnualSpend;

        plasticDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), PlasticDtcReportPage.class)
                .waitForInputControlsLoad()
                .selectRollup(RollupEnum.ROLL_UP_A.getRollupName())
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        plasticDtcReportPage.setReportName(ReportNamesEnum.PLASTIC_DTC.getReportName());
        plasticDtcReportPage.hoverPartNameBubbleDtcReports();
        usdAnnualSpend = plasticDtcReportPage.getAnnualSpendFromBubbleTooltip();

        plasticDtcReportPage.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), PlasticDtcReportPage.class);

        plasticDtcReportPage.hoverPartNameBubbleDtcReports();
        gbpAnnualSpend = plasticDtcReportPage.getAnnualSpendFromBubbleTooltip();

        assertThat(plasticDtcReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpAnnualSpend, is(not(usdAnnualSpend)));
    }

    @Test
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Input Field")
    public void testPlasticDtcExportSetFilterInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1345")
    @Description("Test Plastic DTC Export Set Filter using Date Picker")
    public void testPlasticDtcExportSetFilterDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1346")
    @Description("Test Plastic DTC Export Set Selection")
    public void testPlasticDtcExportSetSelection() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1376")
    @Description("Test Plastic DTC Data Integrity")
    public void testPlasticDtcDataIntegrity() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.PLASTIC_DTC.getReportName(), GenericReportPage.class)
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk();

        genericReportPage.setReportName(ReportNamesEnum.PLASTIC_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        String partName = genericReportPage.getPartNameDtcReports();
        BigDecimal reportFbcValue = genericReportPage.getFBCValueFromBubbleTooltip();
        genericReportPage.openNewTabAndFocus(1);

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
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.PLASTIC_DTC.getReportName());
    }

    @Test
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
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
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
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1380")
    @Description("Verify links to help files function correctly")
    public void testLinkToReportsUserGuide() throws Exception {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportsUserGuideNavigation(
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }
}
