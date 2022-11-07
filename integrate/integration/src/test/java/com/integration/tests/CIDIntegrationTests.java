package com.integration.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.header.ReportsHeader;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.create.CreateAdHocViewPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.AdminLoginPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.manage.ScenarioExport;
import com.apriori.pageobjects.pages.settings.ProductionDefaultsPage;
import com.apriori.pageobjects.pages.view.reports.ComponentCostReportPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.StringUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class CIDIntegrationTests extends TestBase {

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

    @BeforeClass
    public static void setup() {
        testDataService = new TestDataService();
        testDataService.setInputData(testDataService.deserializeDataToMap("CIDIntegrationTestData.json"));
        testDataService.getInputData().replace("scenarioName", StringUtils.saltString((String) testDataService.getInputData().get("scenarioName")));
        testDataService.getInputData().replace("exportSetName", StringUtils.saltString((String) testDataService.getInputData().get("exportSetName")));
    }

    @Test
    @TestRail(testCaseId = {"12106"})
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
    @TestRail(testCaseId = {"12107"})
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
            .selectMaterial("ABS, Plating")
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

    @AfterClass
    public static void testCleanup() {
    }
}
