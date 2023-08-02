package com.integration.tests;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.dataservice.TestDataService;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.manage.ScenarioExport;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.settings.ProductionDefaultsPage;
import com.apriori.pageobjects.view.reports.ComponentCostReportPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class CIDIntegrationTests extends TestBaseUI {

    private UserCredentials currentUser = UserUtil.getUser();
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private ComponentInfoBuilder cidComponentItem;
    private ProductionDefaultsPage productionDefaultPage;
    private ScenarioExport scenarioExport;
    private ComponentCostReportPage componentCostReportPage;
    private static TestDataService testDataService;
    private static final String FILE_NAME = "CID_To_AppStream.csv";

    public CIDIntegrationTests() {
        super();
    }

    @BeforeAll
    public static void setup() {
        testDataService = new TestDataService();
        testDataService.setInputData(testDataService.deserializeDataToMap("CIDIntegrationTestData.json"));
        testDataService.getInputData().replace("scenarioName", GenerateStringUtil.saltString((String) testDataService.getInputData().get("scenarioName")));
        testDataService.getInputData().replace("exportSetName", GenerateStringUtil.saltString((String) testDataService.getInputData().get("exportSetName")));
    }

    @Test
    @TestRail(id = 12106)
    @Description("Upload, Cost and Publish part")
    public void testCreateCostAndPublishPart() {
        SoftAssertions softAssertions = new SoftAssertions();
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, testDataService.getInputData().get("componentName") + ".prt");
        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(testDataService.getInputData().get("componentName").toString(), testDataService.getInputData().get("scenarioName").toString(), resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(ProcessGroupEnum.fromString((String) testDataService.getInputData().get("processGroup")))
            .openMaterialSelectorTable()
            .search(testDataService.getInputData().get("partialMaterialName").toString())
            .selectMaterial(testDataService.getInputData().get("materialName").toString())
            .submit(EvaluatePage.class)
            .costScenario(3)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios(testDataService.getInputData().get("componentName").toString(), testDataService.getInputData().get("scenarioName").toString())).isGreaterThan(0);
        testDataService.exportDataToCloud(FILE_NAME);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 12107)
    @Description("User can change the default Production Defaults")
    public void changeUserSettings() {
        SoftAssertions softAssertions = new SoftAssertions();
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
            .selectMaterial(MaterialNameEnum.ABS_PLATING.getMaterialName())
            .submit(ProductionDefaultsPage.class)
            .inputAnnualVolume("3000")
            .inputYears("7")
            .inputBatchSize("50")
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getScenarioName()).contains("MP Auto Test");
        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).contains(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).contains(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).contains(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).contains("ABS, Plating");

        softAssertions.assertAll();
    }

    @AfterAll
    public static void testCleanup() {
    }
}
