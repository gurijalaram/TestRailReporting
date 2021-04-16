package com.ootbreports.designoutlieridentification;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.DesignOutlierIdentificationReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;

import java.math.BigDecimal;

public class DesignOutlierIdentificationReportTests extends TestBase {

    private DesignOutlierIdentificationReportPage designOutlierIdentificationReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public DesignOutlierIdentificationReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1985"})
    @Description("Validate report is available by navigation - Design Outlier Identification Report")
    public void testReportAvailableByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"6178"})
    @Description("Validate report is available by library - Design Outlier Identification Report")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1986"})
    @Description("Validate report is available by search - Design Outlier Identification Report")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1997"})
    @Description("Verify mass metric - finish mass - Design Outlier Identification Report")
    public void testMassMetricFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"7385"})
    @Description("Verify mass metric - rough mass - Design Outlier Identification Report")
    public void testMassMetricRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetricReportsWithChart(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1988"})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateCalendar() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1988"})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1995"})
    @Description("Export date lists all available versions from selected export set(s)")
    public void testExportDateAvailability() {
        designOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                    DesignOutlierIdentificationReportPage.class);

        designOutlierIdentificationReportPage.selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName());

        assertThat(designOutlierIdentificationReportPage.getExportDateCount(), is(equalTo("2")));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1998"})
    @Description("MIN. & MAX. costs filter works (incl. extreme values, confirm chart header)")
    public void testMinAndMaxCostFilter() {
        designOutlierIdentificationReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                        DesignOutlierIdentificationReportPage.class);

        designOutlierIdentificationReportPage.selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName());

        String minValue = "2.00";
        String maxValue = "6,125.00";
        designOutlierIdentificationReportPage.inputMaxOrMinCostOrMass(
                "Cost",
                "Min",
                minValue
        );
        designOutlierIdentificationReportPage.inputMaxOrMinCostOrMass(
                "Cost",
                "Max",
                maxValue.replace(",", "")
        );
        designOutlierIdentificationReportPage.clickOk();

        assertThat(designOutlierIdentificationReportPage.getCostMinOrMaxAboveChartValue(
                "Min"),
                is(equalTo(minValue))
        );
        assertThat(designOutlierIdentificationReportPage.getCostMinOrMaxAboveChartValue(
                "Max"),
                is(equalTo(maxValue))
        );

        designOutlierIdentificationReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
        designOutlierIdentificationReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal fbcValueOne = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip(
                "FBC Value");

        designOutlierIdentificationReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 2")
        );
        designOutlierIdentificationReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal fbcValueTwo = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip(
                "FBC Value");

        assertThat(fbcValueOne.compareTo(new BigDecimal(minValue)), is(equalTo(1)));
        assertThat(fbcValueOne.compareTo(
                new BigDecimal(maxValue.replace(",", ""))),
                is(equalTo(-1))
        );

        assertThat(fbcValueTwo.compareTo(new BigDecimal(minValue)), is(equalTo(1)));
        assertThat(fbcValueTwo.compareTo(
                new BigDecimal(maxValue.replace(",", ""))),
                is(equalTo(-1))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1998"})
    @Description("MIN. & MAX. costs filter works (incl. extreme values, confirm chart header)")
    public void testMinAndMaxMassFilter() {
        designOutlierIdentificationReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                        DesignOutlierIdentificationReportPage.class);

        designOutlierIdentificationReportPage.selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName());

        String minValue = "1.00";
        String maxValue = "1,173.00";
        designOutlierIdentificationReportPage.inputMaxOrMinCostOrMass(
                "Mass",
                "Min",
                minValue
        );
        designOutlierIdentificationReportPage.inputMaxOrMinCostOrMass(
                "Mass",
                "Max",
                maxValue.replace(",", "")
        );
        designOutlierIdentificationReportPage.clickOk();

        assertThat(designOutlierIdentificationReportPage.getMassMinOrMaxAboveChartValue(
                "Min"),
                is(equalTo(minValue.concat("0")))
        );
        assertThat(designOutlierIdentificationReportPage.getMassMinOrMaxAboveChartValue(
                "Max"),
                is(equalTo(maxValue.concat("0")))
        );

        designOutlierIdentificationReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
        for (int i = 0; i < 3; i++) {
            designOutlierIdentificationReportPage.hoverPartNameBubbleDtcReports();
        }
        BigDecimal massValueOne = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip(
                "Finish Mass Value"
        );

        designOutlierIdentificationReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 2")
        );
        designOutlierIdentificationReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal massValueTwo = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip(
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
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"2006"})
    @Description("Validate the reports correct with user overrides")
    public void testReportFunctionsWithUserCostOverride() {
        designOutlierIdentificationReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                        DesignOutlierIdentificationReportPage.class);

        designOutlierIdentificationReportPage.selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
                .clickOk();

        designOutlierIdentificationReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 2")
        );
        for (int i = 0; i < 3; i++) {
            designOutlierIdentificationReportPage.hoverPartNameBubbleDtcReports();
        }
        BigDecimal fbcValue = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip(
                "FBC Value"
        );

        assertThat(fbcValue.compareTo(new BigDecimal("9883.65")), is(equalTo(0)));
    }
}
