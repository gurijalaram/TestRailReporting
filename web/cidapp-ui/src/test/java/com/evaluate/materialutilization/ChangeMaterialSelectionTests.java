package com.evaluate.materialutilization;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ChangeMaterialSelectionTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private MaterialUtilizationPage materialUtilizationPage;
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();

    public ChangeMaterialSelectionTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6186", "5898"})
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("270")
            .selectMaterial(MaterialNameEnum.BRASS_YELLOW_270.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.BRASS_YELLOW_270.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6187"})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("C28000")
            .selectMaterial(MaterialNameEnum.COPPER_UNS_C28000.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.COPPER_UNS_C28000.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6188"})
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap DFM";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.ABS.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("PET")
            .selectMaterial(MaterialNameEnum.PET_30_GLASS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.PET_30_GLASS.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6189"})
    @Description("Test making changes to the Material for Sheet Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .search("625")
            .selectMaterial(MaterialNameEnum.INCONEL_625.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.INCONEL_625.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }


    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6190", "5420"})
    @Description("Test making changes to the Material for Stock Machining, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestStockMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())).isEqualTo(true);

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem,  EvaluatePage.class)
            .clickExplore()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .openScenario(componentName, scenarioName);

        softAssertions.assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName())).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6191"})
    @Description("Test re-selecting same material and the scenario can be recost")
    public void changeMaterialSelectionTestReSelect() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialSelectorTable()
            .search("PEEK")
            .selectMaterial(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.POLYETHERETHERKETONE_PEEK.getMaterialName()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6192", "5896"})
    @Description("Test closing and opening Material Properties, information within correct")
    public void changeMaterialSelectionTestMaterialProperties() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialSelectorTable()
            .search("625")
            .selectMaterial(MaterialNameEnum.INCONEL_625.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo(MaterialNameEnum.INCONEL_625.getMaterialName())));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6193", "5420", "5910", "6303"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMI() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "Machined Box AMERICAS";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName, scenarioName);

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_HOT_WORKED_AISI1095.getMaterialName()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6194", "5911"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMINotExist() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "Machined Box AMERICAS IronCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterialMode("Digital Factory Default [Steel, Hot Worked, AISI 1010]")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6195", "5921"})
    @Description("Test opening material selection and selecting apply without making a selection")
    public void changeMaterialSelectionTestNoChange() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .openMaterialSelectorTable()
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6196"})
    @Description("Test opening material selection and selecting cancel after making a selection")
    public void changeMaterialSelectionTestCancel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .openMaterialSelectorTable()
            .search("316")
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .cancel(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName()), is(true));
    }
}