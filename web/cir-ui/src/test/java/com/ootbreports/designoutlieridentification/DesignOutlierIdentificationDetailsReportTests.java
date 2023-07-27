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
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;

public class DesignOutlierIdentificationDetailsReportTests extends TestBaseUI {

    private DesignOutlierIdentificationReportPage designOutlierIdentificationReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public DesignOutlierIdentificationDetailsReportTests() {
        super();
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {6179})
    @Description("Validate report is available by navigation - Design Outlier Identification Details Report")
    public void testReportAvailableByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {6181})
    @Description("Validate report is available by library - Design Outlier Identification Details Report")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {6180})
    @Description("Validate report is available by search - Design Outlier Identification Details Report")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {7387})
    @Description("Verify mass metric - finish mass - Design Outlier Identification Details Report")
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
    @TestRail(id = {7386})
    @Description("Verify mass metric - rough mass - Design Outlier Identification Details Report")
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
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {1988})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(id = {1995})
    @Description("Export date lists all available versions from selected export set(s)")
    public void testExportDateAvailability() {
        designOutlierIdentificationReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
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
    @TestRail(id = 6249)
    @Description("Min and max cost filter works")
    public void testMinAndMaxCostFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierDetailsReports(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = 6261)
    @Description("Min and max cost filter - junk value test")
    public void testMinAndMaxCostFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            "Cost"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = 6250)
    @Description("Min and max mass filter works")
    public void testMinAndMaxMassFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterDesignCostOutlierDetailsReports(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            "Mass"
        );
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = 6264)
    @Description("Min and max mass filter - junk value test")
    public void testMinAndMaxMassFilterJunkValue() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinAndMaxMassOrCostFilterJunkValues(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
            "Mass"
        );
    }
}
