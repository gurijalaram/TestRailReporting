package com.apriori.cir.ui.tests.ootbreports.designoutlieridentification;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.ui.enums.MassMetricEnum;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.DesignOutlierIdentificationReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class DesignOutlierIdentificationDetailsReportTests extends TestBaseUI {

    private DesignOutlierIdentificationReportPage designOutlierIdentificationReportPage;
    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;

    public DesignOutlierIdentificationDetailsReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6179")
    @TestRail(id = {6179})
    @Description("Validate report is available by navigation - Design Outlier Identification Details Report")
    public void testReportAvailableByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6181")
    @TestRail(id = {6181})
    @Description("Validate report is available by library - Design Outlier Identification Details Report")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6180")
    @TestRail(id = {6180})
    @Description("Validate report is available by search - Design Outlier Identification Details Report")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7387")
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
    @Tag(REPORTS)
    @TmsLink("7386")
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
    @Tag(REPORTS)
    @TmsLink("1988")
    @TestRail(id = {1988})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateCalendar() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1988")
    @TestRail(id = {1988})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName()
        );
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TmsLink("1995")
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
    @Tag(REPORTS)
    @TmsLink("6249")
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
    @Tag(REPORTS)
    @TmsLink("6261")
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
    @Tag(REPORTS)
    @TmsLink("6250")
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
    @Tag(REPORTS)
    @TmsLink("6264")
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
