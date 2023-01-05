package com.evaluate;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.inputs.AdvancedPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SecondaryProcessTests extends TestBase {
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private AdvancedPage advancedPage;
    private MaterialProcessPage materialProcessPage;

    private File resourceFile;
    private UserCredentials currentUser;
    private SecondaryProcessesPage secondaryProcessPage;
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();

    public SecondaryProcessTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5140", "5115", "5132", "5444"})
    @Description("Test secondary process leak test - edit wall thickness PSO and validate the process chart")
    public void secondaryProcessLeakTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulding";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Hydrostatic Leak Testing")
            .inputOptionsOverride("0.21")
            .submit(AdvancedPage.class)
            .openSecondaryDF()
            .usePrimaryDF("No")
            .selectDropdown("Other Secondary Processes", DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Hydrostatic Leak Testing");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Hydrostatic Leak Testing")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getAverageWallThickness()).isEqualTo(0.21);
        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"5142", "5149"})
    @Description("Test secondary process Carburize")
    public void secondaryProcessCarburize() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .selectSecondaryProcess("Carburize")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Carburize");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Carburize")
            .selectOptionsTab()
            .inputCaseDepth("0.46")
            .inputMasking("1")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Carburize")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Case Depth Selection")).isEqualTo(0.46);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Masking")).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5151"})
    @Description("Test secondary process Atmosphere Oil Harden")
    public void secondaryProcessAtmosphereOilHarden() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Atmosphere Oil Harden")
            .inputMaskingOverride("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Atmosphere Oil Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(2.0);
        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"5157"})
    @Description("Test secondary process Standard Anneal")
    public void secondaryProcessStandardAnneal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("Standard Anneal")
            .inputMaskingOverride("1")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Standard Anneal");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Standard Anneal")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Masking")).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5161", "8866"})
    @Description("Test secondary process Vacuum Temper")
    public void secondaryProcessVacuumTemper() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .selectSecondaryProcess("Vacuum Temper")
            .inputMaskingOverride("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree(" Heat Treat Processes, Temper")
            .highlightSecondaryProcess("Vacuum Temper");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(3.0);
        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Stress Relief")
    public void secondaryProcessStressRelief() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("7075")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_7075.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes")
            .selectSecondaryProcess("Stress Relief")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Stress Relief");

        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Anodize")
    public void secondaryProcessAnodize() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Anodize, Anodizing Tank")
            .selectSecondaryProcess("Anodize:Anodize Type I")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Anodize");

        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Certification")
    public void secondaryProcessCertification() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Certification")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Certification");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5132"})
    @Description("Test secondary process Paint")
    public void secondaryProcessPaint() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Powder Coat Cart")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Powder Coat Cart");

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Powder Coat Cart");

        softAssertions.assertThat(materialProcessPage.getProcessPercentage("Powder Coat Cart")).contains("289.91s (96.29%)");

        softAssertions.assertAll();
    }

    @Test
    @Issues(@Issue("BA-2651"))
    @TestRail(testCaseId = {"5141", "5142", "5143"})
    @Description("Test secondary process powder coat cart PSO")
    public void psoPowderCoatCart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Powder Coat Cart")
            .inputFractionPartArea("0.30")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Powder Coat Cart")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Fraction of Part Area that is Powder Coated")).isEqualTo(0.30);

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"5144", "5145", "5146", "5147"})
    @Description("Test secondary process wet coat line PSO")
    public void psoWetCoatLine() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Wet Coat Line")
            .inputFractionOverride("0.40")
            .inputMaskedFeatures("1")
            .inputBatchSizeOverride("254")
            .inputCompLoadBar("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Wet Coat Line")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("What Fraction of Component is Painted?")).isEqualTo(0.40);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Number of Masked Features")).isEqualTo(1.0);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Number of Components Per Load Bar")).isEqualTo(1.0);

        softAssertions.assertAll();
    }

    @Test
    @Description("Test secondary process Passivation")
    public void secondaryProcessPassivation() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STAINLESS_STEEL_440B.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Passivation");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5116", "5119"})
    @Description("Multiple Secondary Processes before Costing")
    public void multiSecondaryProcessBeforeCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, 440B")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Passivation / Carton Forming / Pack & Load");

        evaluatePage.publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.EQUALS, scenarioName)
            .submit(ExplorePage.class)
            .openScenario("SheetMetal", scenarioName)
            .goToAdvancedTab();

        softAssertions.assertThat(evaluatePage.isSecondaryProcessButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Category({SmokeTests.class})
    @Test
    @Issue("BA-2561")
    @TestRail(testCaseId = {"5117"})
    @Description("Multiple Secondary Processes after Costing")
    public void multiSecondaryProcessAfterCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Anodize, Anodizing Tank")
            .selectSecondaryProcess("Anodize:Anodize Type I")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Anodize / Carton Forming / Pack & Load");

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2651")
    @TestRail(testCaseId = {"5150"})
    @Description("Test secondary process Carbonitride")
    public void secondaryProcessCarbonitride() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .selectSecondaryProcess("Carbonitride")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .highlightSecondaryProcess("Carbonitride");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5152"})
    @Description("Test secondary process Vacuum air harden")
    public void secondaryProcessVacuumAirHarden() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Vacuum Air Harden")
            .inputMasking("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Vacuum Air Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(2.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5153", "5162"})
    @Description("Test secondary process Vacuum Air Harden with High Temper")
    public void secondaryProcessVacuumAirHardenHighTemp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Vacuum Air Harden with High Temper")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Vacuum Air Harden with High Temper");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5154"})
    @Description("Test secondary process Spring steel")
    public void secondaryProcessSpringSteel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Spring Steel Harden")
            .inputMasking("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Spring Steel Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(3.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5155"})
    @Description("Test secondary process Stainless steel")
    public void secondaryProcessStainlessSteel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Stainless Steel Harden")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Stainless Steel Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5156"})
    @Description("Test secondary process High Speed Steel Harden")
    public void secondaryProcessHighSpeedSteel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("High Speed Steel Harden")
            .inputMasking("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("High Speed Steel Harden");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(3.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5158"})
    @Description("Test secondary process Low Temp Vacuum Anneal")
    public void secondaryProcessLowTempVacuumAnneal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("Low Temp Vacuum Anneal")
            .inputMasking("4")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .highlightSecondaryProcess("Low Temp Vacuum Anneal");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(4.0);
        softAssertions.assertAll();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5159", "5129"})
    @Description("Test secondary process High Temp Vacuum Anneal")
    public void secondaryProcessHighTempVacuumAnneal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("High Temp Vacuum Anneal")
            .inputMasking("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .highlightSecondaryProcess("High Temp Vacuum Anneal");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(2.0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5160"})
    @Description("Test secondary process Standard Temper")
    public void secondaryProcessStandardTemper() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .selectSecondaryProcess("Standard Temper")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .highlightSecondaryProcess("Standard Temper");

        softAssertions.assertThat(secondaryProcessPage.getMasking()).isEqualTo(1.0);
        softAssertions.assertAll();
    }

    @Test
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"5122"})
    @Description("Selections are cleared when user cancels changes")
    public void selectionsCleared() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulding";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Xray Inspection")
            .cancel()
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("No Processes Selected...");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5127"})
    @Description("Validate if a secondary process fails to cost, entire part fails to cost")
    public void secondaryProcessCostFailed() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED)).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5133", "5134", "5138"})
    @Description("Validate the user can clear all secondary process selections")
    public void clearAllSP() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .reset()
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .reset()
            .cancel()
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("No Processes Selected...");

        evaluatePage = advancedPage.openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .deselectAll()
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .deselectAll()
            .cancel();

        softAssertions.assertThat(evaluatePage.getListOfSecondaryProcesses()).contains("No Processes Selected...");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5120", "5121", "5123"})
    @Description("Validate zero count when no secondary process is selected and Test secondary process xray")
    public void secondaryProcessXray() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulding";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS_10_GLASS.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("No Processes Selected...");

        evaluatePage = advancedPage.openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Xray Inspection")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Xray Inspection");

        advancedPage = evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("[Other Secondary Processes] Xray Inspection");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"9106", "9102"})
    @Description("Check that Powder Coat Conveyor is available in Surface Treatment")
    public void secondaryProcessPowderCoatConveyor() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint, Conveyor Processes")
            .selectSecondaryProcess("Powder Coat Conveyor")
            .submit(EvaluateToolbar.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Conveyor Powder Coating");

        advancedPage = evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getSecondaryProcesses()).contains("[Surface Treatment] Powder Coat Conveyor");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"8867"})
    @Description("Validate the user can select a different secondary DF for each type of secondary process")
    public void secondaryProcessNotDefaultDigitalFactory() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToMachiningTab()
            .selectSecondaryProcess("Deburr")
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Heat Treat Processes")
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Vibratory Finishing")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Cleaning")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openSecondaryDF()
            .usePrimaryDF("No")
            .selectDropdown("Heat Treatment", DigitalFactoryEnum.APRIORI_CHINA)
            .selectDropdown("Machining", DigitalFactoryEnum.APRIORI_MEXICO)
            .selectDropdown("Surface Treatment", DigitalFactoryEnum.APRIORI_INDIA)
            .selectDropdown("Other Secondary Processes", DigitalFactoryEnum.APRIORI_BRAZIL)
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab();

        assertThat(advancedPage.getListOfSecondaryDigitalFactory(), hasItems("[Heat Treatment] aPriori China", "[Machining] aPriori Mexico", "[Surface Treatment] aPriori India", "[Other Secondary Processes] aPriori Brazil"));
    }
}