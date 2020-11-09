package ootbreports.dtcmetrics.plastic;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.reports.CostMetricEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MassMetricEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.enums.reports.SortOrderEnum;
import com.apriori.utils.web.driver.TestBase;

import inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import navigation.CommonReportTests;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.view.reports.GenericReportPage;
import testsuites.suiteinterface.OnPremTest;

public class PlasticDtcDetailsReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private CommonReportTests commonReportTests;
    private GenericReportPage genericReportPage;

    public PlasticDtcDetailsReportTests() {
        super();
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.DTC_METRICS_FOLDER,
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Plastic DTC Details input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                RollupEnum.ROLL_UP_A.getRollupName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Plastic DTC Details input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName());
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Plastic DTC Details input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @Ignore("not applicable due to reports configuration")
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Plastic DTC Details input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlPpc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                CostMetricEnum.PIECE_PART_COST.getCostMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1366")
    @Description("Verify cost metric input control functions correctly")
    public void testCostMetricInputControlFbc() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCostMetricInputControlOtherMachiningDtcReports(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                CostMetricEnum.FULLY_BURDENED_COST.getCostMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlFinishMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                MassMetricEnum.FINISH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1368")
    @Description("Verify Mass Metric input control functions correctly")
    public void testMassMetricInputControlRoughMass() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMassMetric(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                MassMetricEnum.ROUGH_MASS.getMassMetricName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1701")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreLow() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                DtcScoreEnum.LOW.getDtcScoreName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1701")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreMedium() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                DtcScoreEnum.MEDIUM.getDtcScoreName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1701")
    @Description("Verify DTC Score input control functions correctly")
    public void testDtcScoreHigh() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testDtcScoreDetailsReports(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName(),
                DtcScoreEnum.HIGH.getDtcScoreName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1371")
    @Description("Verify Minimum Annual Spend input control functions correctly")
    public void testMinimumAnnualSpend() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testMinimumAnnualSpendDetailsReports(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(),
                ExportSetEnum.ROLL_UP_A.getExportSetName()
        );
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1369")
    @Description("Verify Sort Order input control functions correctly")
    public void testSortOrderInputControlManufacturingIssues() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
                .selectSortOrder(SortOrderEnum.MANUFACTURING_ISSUES.getSortOrderEnum())
                .clickOk();

        genericReportPage.waitForReportToLoad();

        assertThat(genericReportPage.getPlasticDtcDetailsRowOnePartName(),
                is(equalTo("PLASTIC MOULDED CAP THICKPART")));
    }

    @Test
    @Category(OnPremTest.class)
    @TestRail(testCaseId = "1378")
    @Description("Verify DTC issue counts are correct")
    public void testDtcIssueCountsAreCorrect() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testPlasticDtcIssueCounts(ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName());
    }
}
