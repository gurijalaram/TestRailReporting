package com.integration.tests;

import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.manage.ScenarioExport;
import com.apriori.pageobjects.pages.settings.ProductionDefaultsPage;
import com.apriori.pageobjects.pages.view.reports.ComponentCostReportPage;
import com.apriori.utils.TestRail;

import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.File;

public class CIPIntegrationTests extends TestBase {

    private UserCredentials currentUser = UserUtil.getUser();
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private ComponentInfoBuilder cidComponentItem;
    private ProductionDefaultsPage productionDefaultPage;
    private ScenarioExport scenarioExport;
    private ComponentCostReportPage componentCostReportPage;
    private TestDataService testDataService;

    public CIPIntegrationTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"12107"})
    @Description("User can change the default Production Defaults")
    public void changeUserSettings() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputScenarioName("MP Auto Test")
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE)
            .openMaterialSelectorTable()
            .selectMaterial("ABS, Plating")
            .submit(ProductionDefaultsPage.class)
            .inputAnnualVolume("3000")
            .inputYears("7")
            .inputBatchSize("50")
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        assertThat(productionDefaultPage.getScenarioName(), CoreMatchers.is("MP Auto Test"));
        assertThat(productionDefaultPage.getProcessGroup(), CoreMatchers.is(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup()));
        assertThat(productionDefaultPage.getDigitalFactory(), CoreMatchers.is(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterialCatalog(), CoreMatchers.is(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterial(), CoreMatchers.is("ABS, Plating"));
    }

}
