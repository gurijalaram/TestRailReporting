package cireporttests.ootbreports.dtcmetrics.casting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.CastingReportsEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import cireporttests.inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CIARStagingSmokeTest;

public class CastingDtcDetailsReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;

    public CastingDtcDetailsReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1692")
    @Description("Verify export set input controls function correctly")
    public void testCastingDtcDetailsExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(
                CastingReportsEnum.CASTING_DTC_DETAILS.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @TestRail(testCaseId = "1694")
    @Description("Verify roll-up dropdown functions correctly for Casting DTC Details report")
    public void testRollupDropDown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
                CastingReportsEnum.CASTING_DTC_DETAILS.getReportName(),
                RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC Details input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                CastingReportsEnum.CASTING_DTC_DETAILS.getReportName(),
                RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC Details input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC Details input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                CastingReportsEnum.CASTING_DTC_DETAILS.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "102990")
    @Description("Verify that aPriori costed scenarios are represented correctly")
    public void testVerifyDetailsReportAvailableAndCorrectData() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk();

        genericReportPage.setReportName(CastingReportsEnum.CASTING_DTC_DETAILS.getReportName());
        String partName = genericReportPage.getPartNameDtcReports();
        String holeIssueNumReports = genericReportPage.getHoleIssuesFromDetailsReport();
        genericReportPage.openNewTabAndFocus(1);

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
