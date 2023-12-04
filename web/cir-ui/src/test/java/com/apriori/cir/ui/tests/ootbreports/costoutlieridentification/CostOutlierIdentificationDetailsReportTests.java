package com.apriori.cir.ui.tests.ootbreports.costoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
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
import org.openqa.selenium.By;

import java.math.BigDecimal;

public class CostOutlierIdentificationDetailsReportTests extends TestBaseUI {

    private CostOutlierIdentificationReportPage costOutlierIdentificationReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public CostOutlierIdentificationDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
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
    @TmsLink("6184")
    @TestRail(id = {6184})
    @Description("Validate details report is available by library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6183")
    @TestRail(id = {6183})
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1956")
    @TestRail(id = {1956})
    @Description("Min & Max costs filter works")
    public void testMinMaxAprioriCost() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierDetailsReports(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            Constants.COST_NAME
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6253")
    @TestRail(id = {6253})
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValues() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            Constants.COST_NAME
        );
    }

    @Test
    //@Tag(REPORTS)
    @TmsLink("6823")
    @TestRail(id = {6823})
    @Description("Validate report content aligns to aP desktop or CID (where appropriate) - Details Report")
    public void testDataIntegrityAgainstCID() {
        costOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                CostOutlierIdentificationReportPage.class)
            .selectExportSet(
                ExportSetEnum.SHEET_METAL_DTC.getExportSetName(),
                CostOutlierIdentificationReportPage.class)
            .clickOk(CostOutlierIdentificationReportPage.class);

        String partName = costOutlierIdentificationReportPage.getPartNameCastingSheetMetalDtcDetails(
            true);
        BigDecimal reportsFbc = costOutlierIdentificationReportPage.getFirstFbcCostOutlierDetailsReport();

        costOutlierIdentificationReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
            .filter()
            .saveAs()
            .inputName(new GenerateStringUtil().generateFilterName())
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.EQUALS, partName)
            .submit(ExplorePage.class)
            .openFirstScenario();

        BigDecimal cidFbc = new BigDecimal(String.valueOf(evaluatePage.getCostResults("Fully Burdened Cost")));

        assertThat(reportsFbc.compareTo(cidFbc), is(equalTo(0)));
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1965")
    @TestRail(id = {1965})
    @Description("Validate details report generates")
    public void testDetailsReportGenerates() {
        costOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                GenericReportPage.class)
            .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName(), CostOutlierIdentificationReportPage.class)
            .clickOk(CostOutlierIdentificationReportPage.class);

        costOutlierIdentificationReportPage.waitForReportToLoad();
        if (driver.findElement(By.xpath("//span[contains(text(), 'Rollup')]/../following-sibling::td[2]/span"))
            .getText().contains("SHEET METAL DTC")) {
            costOutlierIdentificationReportPage.clickInputControlsButton()
                .selectExportSetDtcTests(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())
                .clickOk(GenericReportPage.class)
                .waitForReportToLoad();
            costOutlierIdentificationReportPage.waitForSvgToRender();
        }

        assertThat(costOutlierIdentificationReportPage.isCostOutlierSvgDisplayedAndEnabled("1"),
            is(equalTo(true))
        );
        assertThat(costOutlierIdentificationReportPage.isCostOutlierSvgDisplayedAndEnabled("2"),
            is(equalTo(true))
        );
        assertThat(costOutlierIdentificationReportPage.isCostOutlierTableTitleDisplayed(true),
            is(equalTo(true))
        );
        assertThat(costOutlierIdentificationReportPage.isCostOutlierTableTitleDisplayed(false),
            is(equalTo(true))
        );

        costOutlierIdentificationReportPage.clickDetailsLink();
        assertThat(
            costOutlierIdentificationReportPage.isCostOutlierDetailsTableTitleDisplayed(Constants.PERCENT_VALUE),
            is(equalTo(true))
        );
        assertThat(
            costOutlierIdentificationReportPage.isCostOutlierDetailsTableTitleDisplayed(Constants.ANNUALISED_VALUE),
            is(equalTo(true))
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6988")
    @TestRail(id = {6988})
    @Description("Percent difference threshold filter - details report - junk value")
    public void testPercentDifferenceFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            Constants.PERCENT_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6992")
    @TestRail(id = {6992})
    @Description("Annualised potential savings threshold filter - details report - junk value")
    public void testAnnualisedFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentError(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6989")
    @TestRail(id = {6989})
    @Description("Percent difference threshold filter - details report - decimal places")
    public void testPercentDifferenceFilterDecimalPlaces() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAnnualisedOrPercentDecimalPlaces(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            Constants.PERCENT_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7024")
    @TestRail(id = {7024})
    @Description("Annualised potential savings threshold filter - details report - no data available")
    public void testAnnualisedPotentialSavingsNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            Constants.ANNUALISED_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7026")
    @TestRail(id = {7026})
    @Description("Percent difference threshold filter - details report - no data available")
    public void testPercentDifferenceThresholdNoDataAvailable() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostOutlierReportAnnualisedOrPercentFilterNoDataAvailable(
            ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            Constants.PERCENT_VALUE
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6990")
    @TestRail(id = {6990})
    @Description("Annualised potential savings threshold filter - details report")
    public void testAnnualisedPotentialSavingsThresholdFilter() {
        costOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                CostOutlierIdentificationReportPage.class)
            .selectExportSet(ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName(),
                CostOutlierIdentificationReportPage.class)
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

        assertThat(
            costOutlierIdentificationReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("SM_CLEVIS_2207240161")));

        assertThat(
            costOutlierIdentificationReportPage.getTotalAnnualisedOrPercentValue(
                "Percent Value Annualised Set"),
            is(equalTo("33.9%")));
        assertThat(
            costOutlierIdentificationReportPage.getTotalAnnualisedOrPercentValue(
                "Annualised Value Annualised Set"),
            is(equalTo("266,056.86")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TmsLink("6985")
    @TestRail(id = {6985})
    @Description("Percent difference threshold filter works - details report")
    public void testPercentDifferenceThresholdFilter() {
        costOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                CostOutlierIdentificationReportPage.class)
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

        assertThat(
            costOutlierIdentificationReportPage.getPartNameCastingSheetMetalDtcDetails(true),
            is(equalTo("-12")));

        assertThat(
            costOutlierIdentificationReportPage.getTotalAnnualisedOrPercentValue("Percent Value Percent Set"),
            is(equalTo("100.0%")));
        assertThat(
            costOutlierIdentificationReportPage.getTotalAnnualisedOrPercentValue(
                "Annualised Value Percent Set"),
            is(equalTo("7,200.00")));
    }
}
