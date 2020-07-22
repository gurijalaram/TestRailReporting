package cireporttests.ootbreports.dtcmetrics.casting;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.enums.CastingReportsEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.RollupEnum;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CIARStagingSmokeTest;
import testsuites.suiteinterface.CiaCirTestDevTest;

public class CastingDtcComparisonReportTests extends TestBase {

    private GenericReportPage genericReportPage;

    public CastingDtcComparisonReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1692")
    @Description("Verify export set input controls function correctly")
    public void testCastingDtcComparisonExportSetInputControls() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .exportSetSelectAll();


        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount())));

        genericReportPage.deselectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName());

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(genericReportPage.getAvailableExportSetCount() - 1)));

        genericReportPage.invertExportSetSelection();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(1)));

        genericReportPage.exportSetDeselectAll();

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1694")
    @Description("Verify roll-up dropdown functions correctly for Casting DTC Comparison report")
    public void testRollupDropDown() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectRollup(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getDisplayedRollup(),
            is(equalTo(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC input control panel functions correctly")
    public void testApplyButton() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectRollup(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())
            .clickApply()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        assertThat(genericReportPage.getDisplayedRollup(),
            is(equalTo(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC Comparison input control panel works")
    public void testCancelButton() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_COMPARISON.getReportName(), LibraryPage.class)
            .waitForInputControlsLoad()
            .clickCancel(GenericReportPage.class);

        assertThat(genericReportPage.getInputControlsDivClassName(), containsString("hidden"));
        assertThat(genericReportPage.inputControlsIsDisplayed(), is(equalTo(false)));
        assertThat(genericReportPage.inputControlsIsEnabled(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC Comparison input control panel works")
    public void testResetButton() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .selectRollup(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())
            .clickReset()
            .waitForExpectedExportCount("0");

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "102990")
    @Description("Verify that aPriori costed scenarios are represented correctly")
    public void testVerifyComparisonReportAvailableAndCorrectData() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk()
                .clickComparison()
                .newTabTransfer();

        genericReportPage.setReportName(CastingReportsEnum.CASTING_DTC_COMPARISON.getReportName());
        String partName = genericReportPage.getPartNameDtcReports();
        String holeIssueNumReports = genericReportPage.getHoleIssuesFromComparisonReport();
        genericReportPage.openNewTabAndFocus(2);

        DesignGuidancePage designGuidancePage = new ExplorePage(driver)
                .filter()
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setRowOne("Part Name", "Contains", partName)
                .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
                .apply(ExplorePage.class)
                .openFirstScenario()
                .openDesignGuidance();

        String holeIssueCidValue = designGuidancePage.getHoleIssueValue();

        assertThat(holeIssueNumReports, is(equalTo(holeIssueCidValue)));
    }
}
