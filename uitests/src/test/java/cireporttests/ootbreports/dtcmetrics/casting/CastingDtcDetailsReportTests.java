package cireporttests.ootbreports.dtcmetrics.casting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.enums.CastingReportsEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.RollupEnum;
import com.apriori.pageobjects.reports.pages.view.reports.CastingDtcReportHeader;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class CastingDtcDetailsReportTests extends TestBase {

    private GenericReportPage genericReportPage;
    private CastingDtcReportHeader castingDtcReportHeader;
    private LibraryPage libraryPage;

    public CastingDtcDetailsReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1692")
    @Description("Verify export set input controls function correctly")
    public void testCastingDtcDetailsExportSetInputControls() {
        genericReportPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName())
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
    @TestRail(testCaseId = "1694")
    @Description("Verify roll-up dropdown functions correctly for Casting DTC Details report")
    public void testRollupDropDown() {
        castingDtcReportHeader = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .expandRollupDropDown()
            .selectRollupByDropDownSearch(RollupEnum.CASTING_DTC_ALL.getRollupName())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), CastingDtcReportHeader.class);

        assertThat(castingDtcReportHeader.getDisplayedRollup(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName()),
            is(equalTo(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC Details input control panel functions correctly")
    public void testApplyButton() {
        castingDtcReportHeader = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .expandRollupDropDown()
            .selectRollupByDropDownSearch(RollupEnum.CASTING_DTC_ALL.getRollupName())
            .clickApply()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), CastingDtcReportHeader.class);

        assertThat(castingDtcReportHeader.getDisplayedRollup(CastingReportsEnum.CASTING_DTC.getReportName()),
            is(equalTo(RollupEnum.UC_CASTING_DTC_ALL.getRollupName())));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC Details input control panel works")
    public void testCancelButton() {
        libraryPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .clickCancel();

        assertThat(libraryPage.getLibraryTitleText(), is(equalTo("Library")));
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC Details input control panel works")
    public void testResetButton() {
        genericReportPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .expandRollupDropDown()
            .selectExportSet(ExportSetEnum.CASTING_DTC.getExportSetName())
            .clickReset()
            .waitForExpectedExportCount("0");

        assertThat(genericReportPage.getSelectedExportSetCount(), is(equalTo(0)));
    }
}
