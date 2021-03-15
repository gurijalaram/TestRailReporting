package com.ootbreports.designoutlieridentification;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
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
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

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
    @TestRail(testCaseId = "1985")
    @Description("Validate report is available navigation - menu")
    public void testReportAvailableByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.SOLUTIONS_FOLDER,
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1985")
    @Description("Validate report is available navigation - library")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1985")
    @Description("Validate report is available navigation - search")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1997")
    @Description("Mass metric options available & selected cost metric report generated (incl. report header)")
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
    @TestRail(testCaseId = "1997")
    @Description("Mass metric options available & selected cost metric report generated (incl. report header)")
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
    @TestRail(testCaseId = "1988")
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateCalendar() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1988")
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = "1995")
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
    @TestRail(testCaseId = "1998")
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

        designOutlierIdentificationReportPage.hoverBubble(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName());
        BigDecimal fbcValueOne = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip();
        designOutlierIdentificationReportPage.hoverBubble(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 2"));
        BigDecimal fbcValueTwo = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip();

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
    @TestRail(testCaseId = "1998")
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

        designOutlierIdentificationReportPage.hoverBubble(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName());
        BigDecimal massValueOne = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip();
        designOutlierIdentificationReportPage.hoverBubble(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 2"));
        BigDecimal massValueTwo = designOutlierIdentificationReportPage.getFBCValueFromBubbleTooltip();

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
}
