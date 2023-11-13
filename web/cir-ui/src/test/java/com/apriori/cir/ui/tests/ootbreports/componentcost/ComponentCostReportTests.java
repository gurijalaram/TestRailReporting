package com.apriori.cir.ui.tests.ootbreports.componentcost;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.ComponentCostReportPage;
import com.apriori.cir.ui.pageobjects.view.reports.GenericReportPage;
import com.apriori.cir.ui.tests.navigation.CommonReportTests;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ListNameEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ComponentCostReportTests extends TestBaseUI {

    private ComponentCostReportPage componentCostReportPage;
    private CommonReportTests commonReportTests;

    public ComponentCostReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3323})
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByNavigation() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            ReportNamesEnum.COMPONENT_COST.getReportName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {7134})
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {7133})
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3324})
    @Description("Verify Export Set drop-down functions correctly")
    public void testExportSetSelection() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ComponentCostReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .waitForComponentFilter();

        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available: ", "1");
        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("1")));
        assertThat(componentCostReportPage.getFirstScenarioName(), is(equalTo(Constants.DEFAULT_SCENARIO_NAME)));

        assertThat(componentCostReportPage.getComponentListCount(), is(equalTo("14")));
        assertThat(componentCostReportPage.getCountOfComponentTypeElements("part"), is(equalTo(11)));
        assertThat(componentCostReportPage.getCountOfComponentTypeElements("assembly"), is(equalTo(3)));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3325})
    @Description("Verify Component Select drop-down functions correctly")
    public void testComponentSelectDropdown() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ComponentCostReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .waitForComponentFilter();

        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(),
            "Available: ",
            "1"
        );

        assertThat(componentCostReportPage.getComponentListCount(), is(equalTo("14")));
        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("1")));
        assertThat(componentCostReportPage.getFirstScenarioName(), is(equalTo(Constants.DEFAULT_SCENARIO_NAME)));

        componentCostReportPage.selectComponent("TOP-LEVEL")
            .clickOk(ComponentCostReportPage.class)
            .waitForCorrectPartName("TOP-LEVEL");

        assertThat(componentCostReportPage.getComponentCostPartNumber(), is(equalTo("TOP-LEVEL")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3326})
    @Description("Verify Component Type drop-down functions correctly")
    public void testComponentTypeDropdown() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ComponentCostReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .waitForComponentFilter();

        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(),
            "Available: ",
            "1"
        );
        componentSelectAsserts(true);
        componentSelectAsserts(false);

        componentCostReportPage.clickOk(ComponentCostReportPage.class)
            .waitForCorrectPartName("3538968");

        assertThat(componentCostReportPage.getComponentCostPartNumber(), is(equalTo("3538968")));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3327})
    @Description("Verify scenario name input control functions correctly")
    public void testScenarioNameInputControl() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ComponentCostReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .waitForComponentFilter()
            .selectDefaultScenarioName(ComponentCostReportPage.class);

        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(),
            "Selected: ",
            "1"
        );
        assertThat(componentCostReportPage.getComponentListCount(), is(equalTo("14")));
        ArrayList<String> partComponentNames = componentCostReportPage.getComponentSelectNames();
        for (String componentName : partComponentNames) {
            assertThat(componentName.contains(Constants.DEFAULT_SCENARIO_NAME), is(true));
        }
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3329})
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName(), GenericReportPage.class)
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .clickOk(ComponentCostReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), ComponentCostReportPage.class);

        BigDecimal lifetimeCostUSD = componentCostReportPage.getLifetimeCost();

        componentCostReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class)
            .clickOk(ComponentCostReportPage.class)
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), ComponentCostReportPage.class);

        BigDecimal lifetimeCostGBP = componentCostReportPage.getLifetimeCost();

        assertThat(componentCostReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(lifetimeCostUSD, is(not(lifetimeCostGBP)));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {3328})
    @Description("Verify latest export date input control functions correctly")
    public void testLatestExportDateFilter() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ComponentCostReportPage.class)
            .setLatestExportDateToTodayMinusTwoYears()
            .clickComponentTypeDropdownTwice();

        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.CREATED_BY.getListName(), "Available: ", "0");

        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.CREATED_BY.getListName(), "Available"), is(equalTo("0")));

        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available"), is(equalTo("0")));

        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("0")));

        assertThat(componentCostReportPage.getComponentSelectDropdownText(),
            is(equalTo("Click to select item...")));

        assertThat(componentCostReportPage.getWarningMessageText(), is(equalTo(Constants.WARNING_TEXT)));

        assertThat(componentCostReportPage.isWarningDisplayedAndEnabled(), is(equalTo(true)));
    }

    private void componentSelectAsserts(boolean isAssembly) {
        String componentType = isAssembly ? "assembly" : "part";
        String expectedCount = isAssembly ? "3" : "11";

        componentCostReportPage.setComponentType(componentType);
        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available: ", "1");
        componentCostReportPage.waitForComponentFilter(isAssembly);

        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("1")));
        assertThat(componentCostReportPage.getFirstScenarioName(), is(equalTo(Constants.DEFAULT_SCENARIO_NAME)));

        assertThat(componentCostReportPage.getComponentListCount(), is(equalTo(expectedCount)));
        ArrayList<String> partComponentNames = componentCostReportPage.getComponentSelectNames();
        for (String componentName : partComponentNames) {
            assertThat(componentName.contains(componentType), is(true));
        }
    }
}