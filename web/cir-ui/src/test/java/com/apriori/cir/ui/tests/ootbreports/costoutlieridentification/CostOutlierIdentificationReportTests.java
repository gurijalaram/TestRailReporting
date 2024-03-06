package com.apriori.cir.ui.tests.ootbreports.costoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cir.ui.enums.CostMetricEnum;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.CostOutlierIdentificationReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CostOutlierIdentificationReportTests extends TestBaseUI {

    private CostOutlierIdentificationReportPage costOutlierIdentificationReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public CostOutlierIdentificationReportTests() {
        super();
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("1944")
    @TestRail(id = {1944})
    @Description("Validate report is available by navigation - menu")
    public void testReportAvailableByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6182")
    @TestRail(id = {6182})
    @Description("Validate report is available by library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1945")
    @TestRail(id = {1945})
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("1954")
    @TestRail(id = {1954})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricFbcFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1954")
    @TestRail(id = {1954})
    @Description("Cost metric options available & selected cost metric used in report generated (incl. report header)")
    public void testCostMetricPpcFunctionality() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlGeneric(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
            CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1956")
    @TestRail(id = {1956})
    @Description("Min & Max costs filter works")
    public void testMinMaxAprioriCost() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierMainReports(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            "Cost"
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("6253")
    @TestRail(id = {6253})
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValues() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            "Cost"
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1959")
    @TestRail(id = {1959})
    @Description("Validate report content aligns to aP desktop or CID (where appropriate) - Main Report")
    public void testDataIntegrityAgainstCID() {
        String reportName = ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName();
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName, GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), GenericReportPage.class)
            .clickOk(GenericReportPage.class);

        genericReportPage.setReportName(reportName);
        String[] partScenarioName = genericReportPage.getPartNameDtcReports().split(" ");
        genericReportPage.hoverPartNameBubbleDtcReports();
        genericReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal reportsCostValue = genericReportPage.getFBCValueFromBubbleTooltip(
            "aPriori Cost Value (Cost Outlier) Bottom");

        genericReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.EQUALS, partScenarioName[0])
            .submit(ExplorePage.class)
            .openFirstScenario();

        BigDecimal cidFbc = new BigDecimal(String.valueOf(evaluatePage.getCostResults("Fully Burdened Cost")));

        assertThat(reportsCostValue.compareTo(cidFbc), is(equalTo(0)));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1947")
    @TestRail(id = {1947})
    @Description("Export date range presents correctly filtered export sets")
    public void testExportSetFilterByDateCalendarWidget() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("6986")
    @TestRail(id = {6986})
    @Description("Percent difference threshold filter - main report - junk value")
    public void testPercentDifferenceFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            Constants.PERCENT_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6991")
    @TestRail(id = {6991})
    @Description("Annualised potential savings threshold filter - main report - junk value")
    public void testAnnualisedFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6987")
    @TestRail(id = {6987})
    @Description("Percent difference threshold filter - main report - decimal places")
    public void testPercentDifferenceFilterDecimalPlaces() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentDecimalPlaces(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            Constants.PERCENT_VALUE
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("7023")
    @TestRail(id = {7023})
    @Description("Annualised potential savings threshold filter - main report - no data available")
    public void testAnnualisedPotentialSavingsNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7025")
    @TestRail(id = {7025})
    @Description("Percent difference threshold filter - main report - no data available")
    public void testPercentDifferenceThresholdNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
            Constants.PERCENT_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1958")
    @TestRail(id = {1958})
    @Description("Percent difference threshold filter works - main report")
    public void testPercentDifferenceThresholdFilter() {
        costOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(), CostOutlierIdentificationReportPage.class)
            .selectExportSet(ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName(), CostOutlierIdentificationReportPage.class)
            .inputAnnualisedOrPercentValue(Constants.PERCENT_VALUE, "100")
            .clickOk(CostOutlierIdentificationReportPage.class);

        costOutlierIdentificationReportPage.waitForReportToLoad();

        assertThat(
            costOutlierIdentificationReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                true,
                Constants.ANNUALISED_VALUE
            ),
            is(equalTo("n/a"))
        );

        assertThat(
            costOutlierIdentificationReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                true,
                Constants.PERCENT_VALUE
            ),
            is(equalTo("100.0%"))
        );

        costOutlierIdentificationReportPage.waitForSvgToRender();

        assertThat(costOutlierIdentificationReportPage.getCostOutlierBarChartBarCount(Constants.ANNUALISED_VALUE),
            is(equalTo(1))
        );
        assertThat(costOutlierIdentificationReportPage.getCostOutlierBarChartBarCount(Constants.PERCENT_VALUE),
            is(equalTo(1))
        );

        assertThat(costOutlierIdentificationReportPage.isCostOutlierBarDisplayedAndEnabled(Constants.ANNUALISED_VALUE),
            is(equalTo(true))
        );
        assertThat(costOutlierIdentificationReportPage.isCostOutlierBarDisplayedAndEnabled(Constants.PERCENT_VALUE),
            is(equalTo(true))
        );
    }

    @Test
    @Tags({
        @Tag(REPORTS),
        @Tag(ON_PREM)
    })
    @TmsLink("1957")
    @TestRail(id = {1957})
    @Description("Annualised potential savings threshold filter - main report")
    public void testAnnualisedPotentialSavingsThresholdFilter() {
        costOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(), CostOutlierIdentificationReportPage.class)
            .selectExportSetDtcTests(ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName())
            .inputAnnualisedOrPercentValue(Constants.ANNUALISED_VALUE, "10000")
            .clickOk(CostOutlierIdentificationReportPage.class);

        costOutlierIdentificationReportPage.waitForReportToLoad();

        assertThat(
            costOutlierIdentificationReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                false,
                Constants.ANNUALISED_VALUE
            ),
            is(equalTo("10,000.00"))
        );

        assertThat(
            costOutlierIdentificationReportPage.getCostOutlierAnnualisedOrPercentValueFromAboveChart(
                false,
                Constants.PERCENT_VALUE
            ),
            is(equalTo("n/a"))
        );

        assertThat(costOutlierIdentificationReportPage.getCostOutlierBarChartBarCount(Constants.ANNUALISED_VALUE),
            is(equalTo(2))
        );
        assertThat(costOutlierIdentificationReportPage.getCostOutlierBarChartBarCount(Constants.PERCENT_VALUE),
            is(equalTo(2))
        );

        assertThat(costOutlierIdentificationReportPage.isCostOutlierBarDisplayedAndEnabled(Constants.ANNUALISED_VALUE),
            is(equalTo(true))
        );
        assertThat(costOutlierIdentificationReportPage.isCostOutlierBarDisplayedAndEnabled(Constants.PERCENT_VALUE),
            is(equalTo(true))
        );
    }
}
