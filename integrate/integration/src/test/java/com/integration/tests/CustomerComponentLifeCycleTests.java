package com.integration.tests;

import com.apriori.cia.ui.pageobjects.login.AdminLoginPage;
import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.DataCreationUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


public class CustomerComponentLifeCycleTests extends TestBaseUI {

    private static SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;
    private ScenarioResponse publishedScenarioResponse;

    @Test
    @TestRail(id = {31015})
    public void testValidateComponentLifeCycleFromUploadToCostReport() {
        component = new ComponentRequestUtil().getComponent();
        component.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(component.getProcessGroup().getProcessGroup()).build()
        );

        publishedScenarioResponse = new DataCreationUtil(component)
            .createPublishComponent();

        softAssertions.assertThat(publishedScenarioResponse.getPublished()).isTrue();
        softAssertions.assertAll();

        GenerateStringUtil generateStringUtil = new GenerateStringUtil();

        final String exportSetName = generateStringUtil.generateStringForAutomation("SetName");

        new AdminLoginPage(driver)
            .loginWithSpecificUser(component.getUser())
            .navigateToManageScenarioExport()
            .clickNewScenarioExport()
            .insertSetNameValue(exportSetName)
            .insertDescriptionValue(generateStringUtil.generateStringForAutomation("Description"))
            .insertNamePartValue(component.getComponentName())
            .selectComponentType("Part")
            .insertScenarioName(publishedScenarioResponse.getScenarioName())
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
        if (component != null && publishedScenarioResponse != null) {
            new ScenariosUtil().deleteScenario(component.getComponentIdentity(), publishedScenarioResponse.getIdentity(), component.getUser());
        }
    }
}
