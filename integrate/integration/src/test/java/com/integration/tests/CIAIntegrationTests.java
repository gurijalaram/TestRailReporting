package com.integration.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cia.ui.pageobjects.login.AdminLoginPage;
import com.apriori.cia.ui.pageobjects.manage.ScenarioExport;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.pageobjects.view.reports.ComponentCostReportPage;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ListNameEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

public class CIAIntegrationTests extends TestBaseUI {

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
    @TestRail(id = {2695})
    @Description("Verify User can login CIA")
    public void testUserLoginCIA() {
        scenarioExport = new AdminLoginPage(driver)
            .login()
            .navigateToManageScenarioExport();

        assertThat(scenarioExport.isHeaderDisplayed(), CoreMatchers.is(equalTo(true)));
        assertThat(scenarioExport.isHeaderEnabled(), CoreMatchers.is(equalTo(true)));
    }

    @Test
    @TestRail(id = 3326)
    @Description("Create and verify component cost OOTB report")
    public void testCreateComponentCostOOTBReport() {
        componentCostReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad()
            .selectExportSetDtcTests(ExportSetEnum.TOP_LEVEL.getExportSetName());

        componentCostReportPage.waitForCorrectAvailableSelectedCount(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available: ", "1");
        assertThat(componentCostReportPage.getCountOfListAvailableOrSelectedItems(
            ListNameEnum.SCENARIO_NAME.getListName(), "Available"), CoreMatchers.is(equalTo("1")));
        assertThat(componentCostReportPage.getFirstScenarioName(), CoreMatchers.is(equalTo("Initial")));

        assertThat(componentCostReportPage.getComponentListCount(), CoreMatchers.is(equalTo("14")));
        assertThat(componentCostReportPage.getCountOfComponentTypeElements("part"), CoreMatchers.is(equalTo(11)));
        assertThat(componentCostReportPage.getCountOfComponentTypeElements("assembly"), CoreMatchers.is(equalTo(3)));

        componentCostReportPage.selectComponent("SUB-SUB-ASM");
        componentCostReportPage = componentCostReportPage.clickOk(ComponentCostReportPage.class);
        assertThat(componentCostReportPage.getPartNumber(), CoreMatchers.is(equalTo("SUB-SUB-ASM")));
    }
}
