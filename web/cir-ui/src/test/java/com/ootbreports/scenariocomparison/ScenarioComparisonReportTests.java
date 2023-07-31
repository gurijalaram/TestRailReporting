package com.ootbreports.scenariocomparison;

import static com.apriori.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.TestBaseUI;
import com.apriori.enums.CurrencyEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.enums.ListNameEnum;
import com.apriori.enums.ReportNamesEnum;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.ScenarioComparisonReportPage;
import com.apriori.properties.PropertiesContext;
import com.apriori.testrail.TestRail;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import utils.Constants;

import java.math.BigDecimal;

public class ScenarioComparisonReportTests extends TestBaseUI {

    private ScenarioComparisonReportPage scenarioComparisonReportPage;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private CommonReportTests commonReportTests;

    public ScenarioComparisonReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3245})
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.SCENARIO_COMPARISON.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7321})
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3245})
    @Description("Validate report is available by search")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3246})
    @Description("Verify Export Set input control functions correctly")
    public void testExportSetFilterFunctionality() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .waitForExportSetSelection(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.COMPONENT_TYPE.getListName(), "Available: ", "2");
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.COMPONENT_TYPE.getListName(), "Available"), is(equalTo("2")));
        assertThat(genericReportPage.getComponentName("1"), is(equalTo("assembly")));
        assertThat(genericReportPage.getComponentName("2"), is(equalTo("part")));

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.CREATED_BY.getListName(), "Available"), is(equalTo("1")));

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available"), is(equalTo("2")));

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("1")));
        assertThat(genericReportPage.getFirstScenarioName(), is(equalTo(Constants.DEFAULT_SCENARIO_NAME)));

        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available"), is(equalTo("14")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3305})
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
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .selectFirstTwoComparisonScenarios()
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), ScenarioComparisonReportPage.class);

        usdFirstFbc = scenarioComparisonReportPage.getFbcValue(true);
        usdSecondFbc = scenarioComparisonReportPage.getFbcValue(false);

        scenarioComparisonReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
            .clickOk(GenericReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), ScenarioComparisonReportPage.class);

        gbpFirstFbc = scenarioComparisonReportPage.getFbcValue(true);
        gbpSecondFbc = scenarioComparisonReportPage.getFbcValue(false);

        assertThat(scenarioComparisonReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpFirstFbc, is(not(usdFirstFbc)));
        assertThat(gbpSecondFbc, is(not(usdSecondFbc)));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3249})
    @Description("Verify scenario name input control functions correctly")
    public void testScenarioNameInputControl() {
        scenarioComparisonReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectDefaultScenarioName(ScenarioComparisonReportPage.class);

        scenarioComparisonReportPage.waitForScenarioFilter();
        String rowOneScenarioName = scenarioComparisonReportPage.getScenariosToCompareName(1);
        String rowTwoScenarioName = scenarioComparisonReportPage.getScenariosToCompareName(2);

        assertThat(rowOneScenarioName.isEmpty(), is(false));
        assertThat(rowOneScenarioName.contains(Constants.DEFAULT_SCENARIO_NAME), is(true));

        assertThat(rowTwoScenarioName.isEmpty(), is(false));
        assertThat(rowTwoScenarioName.contains(Constants.DEFAULT_SCENARIO_NAME), is(true));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7665})
    @Description("Verify Created By input control search works - Scenario Comparison Report")
    public void testCreatedByFilterSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterSearch(
            ReportNamesEnum.SCENARIO_COMPARISON.getReportName(),
            ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3307})
    @Description("Verify created by input control works correctly - Filter Operation - Scenario Comparison Report")
    public void testCreatedByFilterOperation() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class);

        String lastModifiedByAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available");
        String scenarioNameAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available");
        String scenariosToCompareAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available");

        genericReportPage.selectListItem(ListNameEnum.CREATED_BY.getListName(), Constants.NAME_TO_SELECT);

        genericReportPage.waitForCorrectAvailableSelectedCount(ListNameEnum.CREATED_BY.getListName(), "Selected: ", "1");
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.CREATED_BY.getListName(), "Selected"), is(equalTo("1")));

        String expectedLastModifiedCount = PropertiesContext.get("{env}.name").equals("cir-qa") ? "2" : "1";
        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available: ", expectedLastModifiedCount);
        String lastModifiedByAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available");
        String scenarioNameAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        String scenariosToCompareAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available");

        assertThat(lastModifiedByAvailableCountPreSelection,
            is(not(equalTo(lastModifiedByAvailableCountPostSelection))));
        assertThat(scenarioNameAvailableCountPreSelection,
            is(not(equalTo(scenarioNameAvailableCountPostSelection))));
        assertThat(scenariosToCompareAvailableCountPreSelection,
            is(not(equalTo(scenariosToCompareAvailableCountPostSelection))));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7667})
    @Description("Verify Created By input control buttons work - Scenario Comparison Report")
    public void testCreatedByFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.SCENARIO_COMPARISON.getReportName(),
            "",
            ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7664})
    @Description("Verify Last Modified By input control search works - Scenario Comparison Report")
    public void testLastModifiedByFilterSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterSearch(
            ReportNamesEnum.SCENARIO_COMPARISON.getReportName(),
            ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3349})
    @Description("Verify Last Modified By input control works correctly - Filter Operation - Scenario Comparison Report")
    public void testLastModifiedByFilterOperation() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class);

        String scenarioNameAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available");
        String scenariosToCompareAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available");

        genericReportPage.selectListItem(ListNameEnum.LAST_MODIFIED_BY.getListName(), Constants.NAME_TO_SELECT);

        genericReportPage.waitForCorrectAvailableSelectedCount(ListNameEnum.LAST_MODIFIED_BY.getListName(), "Selected: ", "1");
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(ListNameEnum.LAST_MODIFIED_BY.getListName(), "Selected"), is(equalTo("1")));

        String expectedScenarioNameCount = PropertiesContext.get("{env}.name").equals("cir-qa") ? "2" : "1";
        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available: ", expectedScenarioNameCount);
        String scenarioNameAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available");
        String scenariosToCompareAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available");

        assertThat(scenarioNameAvailableCountPreSelection,
            is(not(equalTo(scenarioNameAvailableCountPostSelection))));
        assertThat(scenariosToCompareAvailableCountPreSelection,
            is(not(equalTo(scenariosToCompareAvailableCountPostSelection))));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7666})
    @Description("Verify Last Modified By input control buttons work - Scenario Comparison Report")
    public void testLastModifiedByFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.SCENARIO_COMPARISON.getReportName(),
            "",
            ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3247})
    @Description("Verify Scenarios to Compare input control functions correctly")
    public void testScenariosToCompareInputControlFunctionality() {
        scenarioComparisonReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectDefaultScenarioName(ScenarioComparisonReportPage.class);

        scenarioComparisonReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(),
            "Selected: ",
            "1"
        );

        scenarioComparisonReportPage.selectAllScenariosToCompare();
        assertThat(scenarioComparisonReportPage.getCountOfSelectedScenariosToCompare(),
            is(equalTo(scenarioComparisonReportPage.getCountOfAvailableScenariosToCompare())));

        scenarioComparisonReportPage.deselectAllScenariosToCompare();
        assertThat(scenarioComparisonReportPage.getCountOfSelectedScenariosToCompare(), is(equalTo("0")));

        scenarioComparisonReportPage.invertScenariosToCompare();
        assertThat(scenarioComparisonReportPage.getCountOfSelectedScenariosToCompare(),
            is(equalTo(scenarioComparisonReportPage.getCountOfAvailableScenariosToCompare())));

        scenarioComparisonReportPage.deselectAllScenariosToCompare();
        scenarioComparisonReportPage.selectFirstScenarioToCompare();
        scenarioComparisonReportPage.clickSelectedTabAndThenX();
        assertThat(scenarioComparisonReportPage.getCountOfSelectedScenariosToCompare(), is(equalTo("0")));
        assertThat(scenarioComparisonReportPage.isNoItemsAvailableTextDisplayed(), is(equalTo(true)));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3248})
    @Description("Verify Component Type input control functions correctly")
    public void testPartNumberSearchCriteriaInputControl() {
        scenarioComparisonReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), ScenarioComparisonReportPage.class);

        scenarioComparisonReportPage.waitForInputControlsLoad();
        scenarioComparisonReportPage.selectComponentType("assembly");

        assertThat(scenarioComparisonReportPage.getFirstScenarioName(Constants.ASSEMBLY_COMPONENT_TYPE),
            containsString(String.format("[%s]", Constants.ASSEMBLY_COMPONENT_TYPE)));

        scenarioComparisonReportPage.selectComponentType(Constants.PART_COMPONENT_TYPE);
        assertThat(scenarioComparisonReportPage.getFirstScenarioName(Constants.PART_COMPONENT_TYPE),
            containsString(String.format("[%s]", Constants.PART_COMPONENT_TYPE)));

        scenarioComparisonReportPage.selectComponentType(Constants.ROLLUP_COMPONENT_TYPE);
        assertThat(scenarioComparisonReportPage.getFirstScenarioName(Constants.ROLLUP_COMPONENT_TYPE),
            containsString(String.format("[%s]", Constants.ROLLUP_COMPONENT_TYPE)));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3304})
    @Description("Verify export date input controls functions correctly")
    public void testExportSetInputControlEarliestDateFilterFunctionality() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .setExportDateUsingInput(true, "")
            .setExportDateUsingInput(false, "")
            .clickUseLatestExportDropdownTwice();

        genericReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.EXPORT_SET.getListName(), "Available: ", "0");
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.EXPORT_SET.getListName(), "Available"), is(equalTo("0")));
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.CREATED_BY.getListName(), "Available"), is(equalTo("0")));
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available"), is(equalTo("0")));
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("0")));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3306})
    @Description("Verify Part Number Search Criteria input control works correctly")
    public void testPartNumberSearchCriteriaFunctionality() {
        scenarioComparisonReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectDefaultScenarioName(ScenarioComparisonReportPage.class);

        scenarioComparisonReportPage.waitForScenarioFilter();
        String nameToInput = scenarioComparisonReportPage.getNameOfFirstScenarioToCompare(true);
        scenarioComparisonReportPage.inputPartNumberSearchCriteria(nameToInput);

        assertThat(scenarioComparisonReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("2")));
        assertThat(scenarioComparisonReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIOS_TO_COMPARE.getListName(), "Available"), is(equalTo("1")));
        assertThat(scenarioComparisonReportPage.getNameOfFirstScenarioToCompare(false),
            is(equalTo("0200613 (Initial) [part]")));
    }
}
