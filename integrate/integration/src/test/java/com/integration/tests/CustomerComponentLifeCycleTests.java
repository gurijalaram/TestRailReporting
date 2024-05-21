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
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.testconfig.TestBaseUI;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.Suite;

import java.time.Duration;

@Suite
public class CustomerComponentLifeCycleTests extends TestBaseUI {

    private static SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;
    private ScenarioResponse scenarioResponse;


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
        component = new ComponentRequestUtil().getComponent();
        component.setCostingTemplate(CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build());

        scenarioResponse = new DataCreationUtil(component)
            .createPublishComponent();

        softAssertions.assertThat(scenarioResponse.getPublished()).isTrue();
        softAssertions.assertAll();

        try {
            Thread.sleep(Duration.ofMinutes(1).toMillis()); // wait until published part will be loaded to aP PRO
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final String exportSetName = new GenerateStringUtil().generateStringForAutomationMark();

        new AdminLoginPage(driver)
            .loginWithSpecificUser(UserUtil.getUser())//component.getUser())
            .navigateToManageScenarioExport()
            .clickNewScenarioExport()
            .insertSetNameValue(exportSetName)
            .insertDescriptionValue("Automation Test")
            .insertNamePartValue(component.getComponentName())
            .selectComponentType("Part")
            .insertScenarioName(scenarioResponse.getScenarioName())
            .doubleClickCalendarButton()
            .clickCreate()
            .clickViewHistoryTab()
            .validateStatusIsSuccessForExportSet(exportSetName)
            .navigateToReports()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), ReportsPageHeader.class)
            .waitForInputControlsLoad();


    }

    @AfterEach
    public void cleanUp() {
        if (component != null && scenarioResponse != null) {
            new ScenariosUtil().deleteScenario(component.getComponentIdentity(), scenarioResponse.getIdentity(), component.getUser());
        }
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
