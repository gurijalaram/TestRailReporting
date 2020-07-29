package cireporttests.ootbreports.dtcmetrics.casting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.enums.reports.RollupEnum;
import com.apriori.utils.web.driver.TestBase;

import cireporttests.inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CIARStagingSmokeTest;
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.CustomerSmokeTests;

import java.math.BigDecimal;

public class CastingDtcReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private ViewRepositoryPage repository;

    public CastingDtcReportTests() {
        super();
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1676")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByMenu() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToCastingFolder();

        ReportNamesEnum[] reportNames = ReportNamesEnum.values();
        for (int i = 5; i <= 7; i++) {
            assertThat(
                    repository.getReportName(reportNames[i].getReportName()),
                    is(equalTo(reportNames[i].getReportName()))
            );
        }
    }

    @Test
    @TestRail(testCaseId = "1692")
    @Description("Verify export set input controls function correctly")
    public void testCastingDtcExportSetInputControls() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(
                ReportNamesEnum.CASTING_DTC.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(CustomerSmokeTests.class)
    @TestRail(testCaseId = "1694")
    @Description("Verify roll-up dropdown functions correctly for Casting DTC report")
    public void testRollupDropDown() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testRollupDropdown(
                ReportNamesEnum.CASTING_DTC.getReportName(),
                RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify apply button on Casting DTC input control panel functions correctly")
    public void testApplyButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testApplyButton(
                ReportNamesEnum.CASTING_DTC.getReportName(),
                RollupEnum.UC_CASTING_DTC_ALL.getRollupName()
        );
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify cancel button on Casting DTC input control panel works")
    public void testCancelButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testCancelButton(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1693")
    @Description("Verify reset button on Casting DTC input control panel works")
    public void testResetButton() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testResetButton(
                ReportNamesEnum.CASTING_DTC.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1693")
    @Description("Verify save button on Casting DTC input control panel functions correctly")
    public void testSaveAndRemoveButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testSaveAndRemoveButtons(
                ReportNamesEnum.CASTING_DTC.getReportName(),
                ExportSetEnum.CASTING_DTC.getExportSetName()
        );
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1691")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1691")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.CASTING_DTC.getReportName());
    }

    @Test
    @TestRail(testCaseId = "102990")
    @Description("Verify that aPriori costed scenarios are represented correctly")
    public void testVerifyCastingDtcReportIsAvailableWithRollUp() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.CASTING_DTC.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.ROLL_UP_A.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk();

        genericReportPage.setReportName(ReportNamesEnum.CASTING_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        BigDecimal reportFbcValue = genericReportPage.getFBCValueFromBubbleTooltip();
        String partName = genericReportPage.getPartNameDtcReports();
        genericReportPage.openNewTabAndFocus(1);

        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setRowOne("Part Name", "Contains", partName)
                .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
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
