package com.ootbreports.designoutlieridentification;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.DesignOutlierIdentificationReportPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import enums.MassMetricEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;

import java.math.BigDecimal;

public class DesignOutlierIdentificationReportTests extends TestBaseUI {

    private DesignOutlierIdentificationReportPage designOutlierIdentificationReportPage;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private CommonReportTests commonReportTests;

    public DesignOutlierIdentificationReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1985})
    @Description("Validate report is available by navigation - Design Outlier Identification Report")
    public void testReportAvailableByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {6178})
    @Description("Validate report is available by library - Design Outlier Identification Report")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1986})
    @Description("Validate report is available by search - Design Outlier Identification Report")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1997})
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
    @TestRail(id = {7385})
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
    @TestRail(id = {1988})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateCalendar() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1988})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1995})
    @Description("Export date lists all available versions from selected export set(s)")
    public void testExportDateAvailability() {
        designOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                    DesignOutlierIdentificationReportPage.class);

        designOutlierIdentificationReportPage.selectExportSet(
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                GenericReportPage.class
        );

        assertThat(
                designOutlierIdentificationReportPage.getExportDateCount(),
                is(equalTo("2"))
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1998})
    @Description("Min and max cost filter works")
    public void testMinAndMaxCostFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierMainReports(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = 6260)
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValues() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = 1998)
    @Description("Min and max mass filter works")
    public void testMinAndMaxMassFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierMainReports(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                "Mass"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = 6265)
    @Description("Min and max mass filter - junk value test")
    public void testMinAndMaxMassFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                "Mass"
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {2006})
    @Description("Validate the reports correct with user overrides")
    public void testReportFunctionsWithUserCostOverride() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                        GenericReportPage.class)
                .selectExportSetDtcTests(ExportSetEnum.ROLL_UP_A.getExportSetName())
                .clickOk(GenericReportPage.class);

        genericReportPage.setReportName(
                ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName().concat(" 3")
        );
        for (int i = 0; i < 2; i++) {
            genericReportPage.hoverPartNameBubbleDtcReports();
        }
        BigDecimal fbcValue = genericReportPage.getFBCValueFromBubbleTooltip(
                "FBC Value"
        );

        assertThat(fbcValue.compareTo(new BigDecimal("10429.19")), is(equalTo(0)));
    }
}
