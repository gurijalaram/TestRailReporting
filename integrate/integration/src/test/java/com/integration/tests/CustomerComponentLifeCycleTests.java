package com.integration.tests;

import com.apriori.cia.ui.pageobjects.login.AdminLoginPage;
import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.DataCreationUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.testconfig.TestBaseUI;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.Suite;

@Suite
public class CustomerComponentLifeCycleTests extends TestBaseUI {

    private static SoftAssertions softAssertions = new SoftAssertions();
    private UserCredentials testingUser = UserUtil.getUser();


    /**
     * Test to validate component life cycle on customer side
     * Steps of validation:
     * 1) cost part in APD
     * 2) publish part
     * 3) go to ap admin, run export of that part
     * 4) go to ap analytics, generate a report or validate that reports input control works
     */
    @Test
    public void testValidateComponentLifeCycleFromUploadToCostReport() {
//        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();
//        component.setCostingTemplate(CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build());
//
//        ScenarioResponse scenarioResponse = new DataCreationUtil(component)
//            .createPublishComponent();
//
//        softAssertions.assertThat(scenarioResponse.getPublished()).isTrue();
//        softAssertions.assertAll();


      new AdminLoginPage(driver)
            .login()
            .navigateToManageScenarioExport()
            .clickNewScenarioExport()
            .insertSetNameValue("Automation Set name Test")
            .insertDescriptionValue("Automation Desc Test")
            .insertNamePartValue("Automation Name Test")
            .insertScenarioName("Automation Scenario Test")
            .doubleClickCalendarButton()
            .clickCreate()
//            .filterScenarioByName(scenarioResponse.getScenarioName())
            .filterScenarioByName("")
            .clickExportButton()
            .validateExportNowPopupAndCloseIt()
            .navigateToReports()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad();

//      new ScenariosUtil().deleteScenario(component.getComponentIdentity(), scenarioResponse.getIdentity(), component.getUser());
    }

    private TestDataService initTestDataService() {
        TestDataService testDataService = new TestDataService();
        testDataService.setInputData(testDataService.deserializeDataToMap("CIDIntegrationTestData.json"));
        testDataService.getInputData().replace("scenarioName", "Automation_" + GenerateStringUtil.saltString((String) testDataService.getInputData().get("scenarioName")));
        testDataService.getInputData().replace("exportSetName", "Automation_" + GenerateStringUtil.saltString((String) testDataService.getInputData().get("exportSetName")));

        return testDataService;
    }

    public void validateInputControlsPresentForComponentCostReport() {
        new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad();
    }
}
