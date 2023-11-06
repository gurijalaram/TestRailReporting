package com.apriori.cir.ui.tests.ootbreports.targetquotedcosttrend;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.TargetAndQuotedCostValueTrackingPage;
import com.apriori.cir.ui.pageobjects.view.reports.TargetQuotedCostTrendReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class TargetAndQuotedCostValueTrackingReportTests extends TestBaseUI {

    private TargetAndQuotedCostValueTrackingPage targetAndQuotedCostValueTrackingPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public TargetAndQuotedCostValueTrackingReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7325})
    @Description("Validate report is available by navigation - Target and Quoted Cost Trend Details Report")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7327})
    @Description("Validate report available by library - Target and Quoted Cost Trend Details Report")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7329})
    @Description("Validate report is available by search - Target and Quoted Cost Trend Details Report")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3364})
    @Description("Validate Cost Metric Input Control - PPC - Target and Quoted Cost Value Tracking Report")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricTargetQuotedCostValueTrackingReport(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7424})
    @Description("Validate Cost Metric Input Control - FBC - Target and Quoted Cost Value Tracking Report")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricTargetQuotedCostValueTrackingReport(
            ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3365})
    @Description("Validate Currency Code Input Control Functionality")
    public void testCurrencyCodeInputControl() {
        targetAndQuotedCostValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                TargetAndQuotedCostValueTrackingPage.class);

        targetAndQuotedCostValueTrackingPage.checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(TargetAndQuotedCostValueTrackingPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetQuotedCostTrendReportPage.class);

        String usdFinalAprioriCost = targetAndQuotedCostValueTrackingPage.getFinalCost();

        targetAndQuotedCostValueTrackingPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
            .clickOk(TargetAndQuotedCostValueTrackingPage.class)
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), TargetQuotedCostTrendReportPage.class);

        String gbpFinalAprioriCost = targetAndQuotedCostValueTrackingPage.getFinalCost();

        assertThat(usdFinalAprioriCost, is(not(equalTo(gbpFinalAprioriCost))));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3368})
    @Description("Validate sub-report hyperlinks to Target Cost Value Tracking details report - Milestone 1")
    public void testLinksToMilestoneProjectOne() {
        testMilestoneProjectLink("1");
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7669})
    @Description("Validate sub-report hyperlinks to Target Cost Value Tracking details report - Milestone 2")
    public void testLinksToMilestoneProjectTwo() {
        testMilestoneProjectLink("2");
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7670})
    @Description("Validate sub-report hyperlinks to Target Cost Value Tracking details report - Milestone 3")
    public void testLinksToMilestoneProjectThree() {
        testMilestoneProjectLink("3");
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7671})
    @Description("Validate sub-report hyperlinks to Target Cost Value Tracking details report - Milestone 4")
    public void testLinksToMilestoneProjectFour() {
        testMilestoneProjectLink("4");
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3366})
    @Description("Export date lists all available versions from selected export set rollup")
    public void testExportDateListFunctionality() {
        targetAndQuotedCostValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                TargetAndQuotedCostValueTrackingPage.class)
            .selectProjectRollup(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName());

        assertThat(targetAndQuotedCostValueTrackingPage.getExportDateOptionCount(), is(equalTo("1")));
        String exportDateSelected = targetAndQuotedCostValueTrackingPage.getSelectedExportDate()
            .replace("T", " ");

        targetAndQuotedCostValueTrackingPage.clickOk(TargetAndQuotedCostValueTrackingPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetAndQuotedCostValueTrackingPage.class);

        assertThat(targetAndQuotedCostValueTrackingPage.getExportDateOnReport()
            .replace(" UTC", ""), is(equalTo(exportDateSelected)));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3367})
    @Description("Validate Target Cost Value Tracking report aligns to CID values")
    public void testDataIntegrityAgainstCID() {
        targetAndQuotedCostValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                TargetAndQuotedCostValueTrackingPage.class)
            .selectProjectRollup(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .clickOk(TargetAndQuotedCostValueTrackingPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetAndQuotedCostValueTrackingPage.class)
            .waitForCorrectProjectNameToAppear("1");

        targetAndQuotedCostValueTrackingPage.clickProjectLink("1");
        targetAndQuotedCostValueTrackingPage.switchTab(1);
        String partName = targetAndQuotedCostValueTrackingPage.getPartNumberFromDetailsReport();

        String reportsScenarioName = targetAndQuotedCostValueTrackingPage.getValueFromReport("5");
        String reportsVpe = targetAndQuotedCostValueTrackingPage.getValueFromReport("11");
        String reportsProcessGroup = targetAndQuotedCostValueTrackingPage.getValueFromReport("14");
        String reportsMaterialComposition = targetAndQuotedCostValueTrackingPage.getValueFromReport("17")
            .replace("", " ");
        String reportsAnnualVolume = targetAndQuotedCostValueTrackingPage.getValueFromReport("22")
            .replace(",", "");
        String reportsCurrentCost = targetAndQuotedCostValueTrackingPage.getValueFromReport("24");

        targetAndQuotedCostValueTrackingPage.openNewCidTabAndFocus(2);
        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, partName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, Constants.DEFAULT_SCENARIO_NAME)
            .submit(ExplorePage.class)
            .openScenario(partName, Constants.DEFAULT_SCENARIO_NAME);

        String cidScenarioName = evaluatePage.getCurrentScenarioName();
        String cidVPE = evaluatePage.getSelectedVPE();
        String cidProcessGroup = evaluatePage.getSelectedProcessGroup();
        String cidMaterialComposition =
            evaluatePage.openMaterialProcess().openMaterialUtilizationTab().getMaterialName();
        String cidAnnualVolume = evaluatePage.getAnnualVolume();
        String cidFbc = String.valueOf(evaluatePage.getCostResults("Fully Burdened Cost"));

        assertThat(reportsScenarioName, is(equalTo(cidScenarioName)));
        assertThat(reportsVpe, is(equalTo(cidVPE)));
        assertThat(reportsProcessGroup, is(equalTo(cidProcessGroup)));
        assertThat(reportsMaterialComposition, is(equalTo(cidMaterialComposition)));
        assertThat(reportsAnnualVolume, is(equalTo(cidAnnualVolume)));
        assertThat(reportsCurrentCost, is(equalTo(cidFbc)));
    }

    /**
     * Generic test method for milestone project link test
     *
     * @param index - index of project link to click
     */
    private void testMilestoneProjectLink(String index) {
        targetAndQuotedCostValueTrackingPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
                TargetAndQuotedCostValueTrackingPage.class)
            .selectProjectRollup(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName())
            .clickOk(TargetAndQuotedCostValueTrackingPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetAndQuotedCostValueTrackingPage.class)
            .clickProjectLink(index)
            .switchTab(1)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), TargetAndQuotedCostValueTrackingPage.class);

        assertThat(targetAndQuotedCostValueTrackingPage.getProjectName(),
            is(equalTo(String.format("PROJECT %s", index))));
    }
}
