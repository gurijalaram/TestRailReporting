package com.integration.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.ReportsHeader;
import com.apriori.pageobjects.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.AdminLoginPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.manage.ScenarioExport;
import com.apriori.pageobjects.pages.view.reports.ComponentCostReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class CIAIntegrationTests extends TestBase {

    private UserCredentials currentUser = UserUtil.getUser();
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioExport scenarioExport;
    private ComponentCostReportPage componentCostReportPage;
    private TestDataService testDataService;

    public CIAIntegrationTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"2695"})
    @Description("Verify User can login CIA")
    public void testUserLoginCIA() {
        scenarioExport = new AdminLoginPage(driver)
            .login()
            .navigateToManageScenarioExport();

        assertThat(scenarioExport.isHeaderDisplayed(), CoreMatchers.is(equalTo(true)));
        assertThat(scenarioExport.isHeaderEnabled(), CoreMatchers.is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = {"12046"})
    @Description("Verify user can login CIR")
    public void testUserLoginCIR() {
        ReportsHeader reportsHeader = new ReportsLoginPage(driver)
            .login()
            .navigateToHomePage();

        assertThat(reportsHeader.getHomeTitleText(), CoreMatchers.is(containsString("Home")));
    }

    @Test
    @TestRail(testCaseId = "12517")
    @Description("Verify Create Simple Ad Hoc View Report")
    public void testCreateAdHocViewReport() {
        CreateAdHocViewPage createAdHocViewPage = new ReportsLoginPage(driver)
            .login()
            .navigateToCreateAdHocViewPage()
            .clickFirstDataSource()
            .clickChooseData()
            .waitForChooseDataDialogToAppear()
            .clickMoveDataToRightButton()
            .clickGoToDesignerOkButton()
            .changeVisualizationToTable()
            .changeDataScopeToFull()
            .addDataToTable()
            .addFilterToTable();

        assertThat(createAdHocViewPage.getTableCellValue("1", "1"),
            CoreMatchers.is(equalTo("0200613"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("1", "2"),
            CoreMatchers.is(equalTo("Initial"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("1", "3"),
            CoreMatchers.is(equalTo("4.35"))
        );

        assertThat(createAdHocViewPage.getTableCellValue("14", "1"),
            CoreMatchers.is(equalTo("TOP-LEVEL"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("14", "2"),
            CoreMatchers.is(equalTo("Initial"))
        );
        assertThat(createAdHocViewPage.getTableCellValue("14", "3"),
            CoreMatchers.is(equalTo("27.28"))
        );
    }

    @Test
    @Issue("DEVTOOLS-145")
    @TestRail(testCaseId = {"12046"})
    @Description("Create and verify component cost OOTB report ")
    public void testCreateComponentCostOOTBReport() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ComponentCostReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName(), ComponentCostReportPage.class)
            .waitForComponentFilter();

        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available: ", "1");
        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), CoreMatchers.is(equalTo("1")));
        assertThat(componentCostReportPage.getFirstScenarioName(), CoreMatchers.is(equalTo("Initial")));

        assertThat(componentCostReportPage.getComponentListCount(), CoreMatchers.is(equalTo("14")));
        assertThat(componentCostReportPage.getCountOfComponentTypeElements("part"), CoreMatchers.is(equalTo(11)));
        assertThat(componentCostReportPage.getCountOfComponentTypeElements("assembly"), CoreMatchers.is(equalTo(3)));
        componentCostReportPage = componentCostReportPage.clickOk(true, ComponentCostReportPage.class);
        assertThat(componentCostReportPage.getPartNumber(), CoreMatchers.is(equalTo("0200613")));
    }

}
