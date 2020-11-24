package com.ootbreports.componentcost;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.ComponentCostReportPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.navigation.CommonReportTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;

import java.util.ArrayList;

public class ComponentCostReportTests extends TestBase {

    private ComponentCostReportPage componentCostReportPage;
    private CommonReportTests commonReportTests;

    public ComponentCostReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "3323")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenu() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.COMPONENT_COST.getReportName()
        );
    }

    @Test
    @TestRail(testCaseId = "3323")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByLibrary() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3323")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityBySearch() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3324")
    @Description("Verify Export Set drop-down functions correctly")
    public void testExportSetSelection() {
        componentCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
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
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3326")
    @Description("Verify Component Type drop-down functions correctly")
    public void testComponentTypeDropdown() {
        componentCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .waitForComponentFilter();

        componentSelectAsserts(true);
        componentSelectAsserts(false);

        componentCostReportPage.clickOk()
                .waitForCorrectPartName("3538968");

        assertThat(componentCostReportPage.getComponentCostPartNumber(), is(equalTo("3538968")));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3325")
    @Description("Verify Component Select drop-down functions correctly")
    public void testComponentSelectDropdown() {
        componentCostReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .waitForComponentFilter();

        assertThat(componentCostReportPage.getComponentListCount(), is(equalTo("14")));

        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.SCENARIO_NAME.getListName(), "Available"), is(equalTo("1")));
        assertThat(componentCostReportPage.getFirstScenarioName(), is(equalTo(Constants.DEFAULT_SCENARIO_NAME)));

        componentCostReportPage.selectComponent("TOP-LEVEL")
                .clickOk()
                .waitForCorrectPartName("TOP-LEVEL");

        assertThat(componentCostReportPage.getComponentCostPartNumber(), is(equalTo("TOP-LEVEL")));
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
