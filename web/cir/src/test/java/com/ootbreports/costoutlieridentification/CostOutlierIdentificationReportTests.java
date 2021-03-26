package com.ootbreports.costoutlieridentification;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;
import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CostOutlierIdentificationReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public CostOutlierIdentificationReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1944")
    @Description("Validate report is available by navigation - menu")
    public void testReportAvailableByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "6182")
    @Description("Validate report is available by library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1945")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName());
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1954")
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
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1954")
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
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1956")
    @Description("Min & Max costs filter works (incl. extreme values, negitive values, MAX<MIN., confirm chart header)")
    public void testMinMaxAprioriCost() {
        /*genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                        GenericReportPage.class);

        genericReportPage.selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName());

        String minValue = "1.00";
        String maxValue = "1,173.00";
        genericReportPage.inputMaxOrMinCostOrMass(
                "Mass",
                "Min",
                minValue
        );
        genericReportPage.inputMaxOrMinCostOrMass(
                "Mass",
                "Max",
                maxValue.replace(",", "")
        );
        genericReportPage.clickOk();

        assertThat(genericReportPage.getMassMinOrMaxAboveChartValue(
                "Min"),
                is(equalTo(minValue.concat("0")))
        );
        assertThat(genericReportPage.getMassMinOrMaxAboveChartValue(
                "Max"),
                is(equalTo(maxValue.concat("0")))
        );

        genericReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
        for (int i = 0; i < 3; i++) {
            genericReportPage.hoverPartNameBubbleDtcReports();
        }
        BigDecimal massValueOne = genericReportPage.getFBCValueFromBubbleTooltip(
                "Finish Mass Value"
        );

        genericReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 2")
        );
        genericReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal massValueTwo = genericReportPage.getFBCValueFromBubbleTooltip(
                "Finish Mass Value"
        );

        assertThat(massValueOne.compareTo(new BigDecimal(minValue)), is(equalTo(1)));
        assertThat(massValueOne.compareTo(
                new BigDecimal(maxValue.replace(",", ""))),
                is(equalTo(-1))
        );

        assertThat(massValueTwo.compareTo(new BigDecimal(minValue)), is(equalTo(1)));
        assertThat(massValueTwo.compareTo(
                new BigDecimal(maxValue.replace(",", ""))),
                is(equalTo(-1))
        );*/
    }
}
