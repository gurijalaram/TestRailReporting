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
    @Tag(REPORTS)
    @TmsLink("1985")
    @TestRail(id = {1985})
    @Description("Validate report is available by navigation - Design Outlier Identification Report")
    public void testReportAvailableByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("6178")
    @TestRail(id = {6178})
    @Description("Validate report is available by library - Design Outlier Identification Report")
    public void testReportAvailableByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1986")
    @TestRail(id = {1986})
    @Description("Validate report is available by search - Design Outlier Identification Report")
    public void testReportAvailableBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1997")
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
    @Tag(REPORTS)
    @TmsLink("7385")
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
    @Tag(REPORTS)
    @TmsLink("1988")
    @TestRail(id = {1988})
    @Description("Export date calendar widgets")
    public void testExportSetFilterByDateCalendar() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
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
            ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("1995")
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
    @Tag(REPORTS)
    @TmsLink("1998")
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
    @Tag(REPORTS)
    @TmsLink("6260")
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
    @Tag(REPORTS)
    @TmsLink("1998")
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
    @Tag(REPORTS)
    @TmsLink("6265")
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TmsLink("2006")
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
