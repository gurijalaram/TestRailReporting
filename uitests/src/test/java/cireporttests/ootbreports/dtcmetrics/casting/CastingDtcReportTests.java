package cireporttests.ootbreports.dtcmetrics.casting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.enums.CastingReportsEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.RollupEnum;
import com.apriori.pageobjects.reports.pages.view.reports.CastingDtcReportHeader;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CIARStagingSmokeTest;
import testsuites.suiteinterface.CustomerSmokeTests;

import java.math.BigDecimal;

public class CastingDtcReportTests extends TestBase {

    private ViewRepositoryPage repository;
    private GenericReportPage genericReportPage;
    private CastingDtcReportHeader castingDtcReportHeader;
    private LibraryPage libraryPage;

    public CastingDtcReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1676")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByMenu() {
        repository = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToViewRepositoryPage()
            .navigateToCastingFolder();

        CastingReportsEnum[] reportNames = CastingReportsEnum.values();
        for (CastingReportsEnum report : reportNames) {
            assertThat(repository.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @TestRail(testCaseId = "1692")
    @Description("Verify export set input controls function correctly")
    public void testCastingDtcExportSetInputControls() {
        genericReportPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName())
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
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = "1694")
    @Description("Verify roll-up dropdown functions correctly for Casting DTC report")
    public void testRollupDropDown() {
        castingDtcReportHeader = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName())
            .waitForInputControlsLoad()
            .expandRollupDropDown()
            .selectRollupByDropDownSearch(RollupEnum.CASTING_DTC_ALL.getRollupName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), CastingDtcReportHeader.class);

        assertThat(castingDtcReportHeader.getDisplayedRollup(CastingReportsEnum.CASTING_DTC.getReportName()),
            is(equalTo(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC input control panel functions correctly")
    public void testApplyButton() {
        castingDtcReportHeader = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName())
            .waitForInputControlsLoad()
            .expandRollupDropDown()
            .selectRollupByDropDownSearch(RollupEnum.CASTING_DTC_ALL.getRollupName())
            .clickApply()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), CastingDtcReportHeader.class);

        assertThat(castingDtcReportHeader.getDisplayedRollup(
            CastingReportsEnum.CASTING_DTC.getReportName()),
            is(equalTo(
                RollupEnum.UC_CASTING_DTC_ALL.getRollupName())));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC input control panel works")
    public void testCancelButton() {
        libraryPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName())
            .waitForInputControlsLoad()
            .clickCancel();

        assertThat(libraryPage.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC input control panel works")
    public void testResetButton() {
        genericReportPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .clickReset()
            .waitForExpectedExportCount("0");

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Casting DTC input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        genericReportPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .clickSave()
            .enterSaveName("Saved Config")
            .clickSaveAsButton()
            .clickReset()
            .selectSavedOptionByName("Saved Config");

        assertThat(genericReportPage.isExportSetSelected(ExportSetEnum.CASTING_DTC.getExportSetName()), is(true));

        genericReportPage.clickRemove();

        assertThat(genericReportPage.isOptionInDropDown("Saved Config", 1), is(false));
    }

    @Test
    @TestRail(testCaseId = "102990")
    @Description("Verify that aPriori costed scenarios are represented correctly")
    public void testVerifyCastingDtcReportIsAvailableWithRollUp() {
        genericReportPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk();

        BigDecimal reportFbcValue = genericReportPage.getFBCValueFromBubbleTooltip(true);
        String partName = genericReportPage.getPartNameDtcCastingReports(Constants.CASTING_DTC_REPORT_NAME);
        genericReportPage.openNewTabAndFocus(1);

        String[] attributesArray = { "Part Name", "Scenario Name" };
        String[] valuesArray = { partName, Constants.DEFAULT_SCENARIO_NAME};
        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filterCriteria()
                .multiFilterPublicCriteria(Constants.PART_SCENARIO_TYPE, attributesArray, valuesArray)
                .apply(ExplorePage.class)
                .openFirstScenario();

        BigDecimal cidFbcValue = evaluatePage.getBurdenedCostValue();

        /*
            This is great now, but rounding in CID is not done at all really (long story)
            Thus this may start failing in due course, and can be fixed then
            Currency in Reports and CID needs to match for this test also (both default to USD)
         */
        assertThat(reportFbcValue, is(equalTo(cidFbcValue)));
    }
}
