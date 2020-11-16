package ootbreports.general.scenariocomparison;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.view.reports.ScenarioComparisonReportPage;
import testsuites.suiteinterface.CiaCirTestDevTest;

import java.math.BigDecimal;

public class ScenarioComparisonTests extends TestBase {

    private ScenarioComparisonReportPage scenarioComparisonReportPage;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private CommonReportTests commonReportTests;

    public ScenarioComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "3245")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.SCENARIO_COMPARISON.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "3245")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3245")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3246")
    @Description("Verify Export Set input control functions correctly")
    public void testExportSetFilterFunctionality() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.COMPONENT_TYPE.getListName(), "Available: ", "2");
        assertThat(genericReportPage.getCountOfListAvailableItems(
                ListNameEnum.COMPONENT_TYPE.getListName(), "Available"), is(equalTo("2")));
        assertThat(genericReportPage.getComponentName("1"), is(equalTo("assembly")));
        assertThat(genericReportPage.getComponentName("2"), is(equalTo("part")));

        assertThat(genericReportPage.getCountOfListAvailableItems(
                ListNameEnum.CREATED_BY.getListName(), "Available"), is(equalTo("1")));

        assertThat(genericReportPage.getCountOfListAvailableItems(
                ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available"), is(equalTo("2")));

        assertThat(genericReportPage.getCountOfListAvailableItems(
                ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("1")));
        assertThat(genericReportPage.getFirstScenarioName(), is(equalTo(Constants.DEFAULT_SCENARIO_NAME)));

        assertThat(genericReportPage.getCountOfListAvailableItems(
                ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available"), is(equalTo("14")));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3305")
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        BigDecimal gbpFirstFbc;
        BigDecimal gbpSecondFbc;
        BigDecimal usdFirstFbc;
        BigDecimal usdSecondFbc;

        scenarioComparisonReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .selectFirstTwoComparisonScenarios()
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), ScenarioComparisonReportPage.class);

        usdFirstFbc = scenarioComparisonReportPage.getFbcValue(true);
        usdSecondFbc = scenarioComparisonReportPage.getFbcValue(false);

        scenarioComparisonReportPage.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), ScenarioComparisonReportPage.class);

        gbpFirstFbc = scenarioComparisonReportPage.getFbcValue(true);
        gbpSecondFbc = scenarioComparisonReportPage.getFbcValue(false);

        assertThat(scenarioComparisonReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpFirstFbc, is(not(usdFirstFbc)));
        assertThat(gbpSecondFbc, is(not(usdSecondFbc)));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3249")
    @Description("Verfiy scenario name input control functions correctly")
    public void testScenarioNameInputControl() {
        scenarioComparisonReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectDefaultScenarioName();

        String rowOneScenarioName = scenarioComparisonReportPage.getScenariosToCompareName(1);
        String rowTwoScenarioName = scenarioComparisonReportPage.getScenariosToCompareName(2);

        assertThat(rowOneScenarioName.isEmpty(), is(false));
        assertThat(rowOneScenarioName.contains(Constants.DEFAULT_SCENARIO_NAME), is(true));

        assertThat(rowTwoScenarioName.isEmpty(), is(false));
        assertThat(rowTwoScenarioName.contains(Constants.DEFAULT_SCENARIO_NAME), is(true));
    }
}
