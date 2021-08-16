package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SecondaryProcessTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SecondaryPage secondaryPage;
    private MaterialProcessPage materialProcessPage;

    private File resourceFile;
    private UserCredentials currentUser;
    private SecondaryProcessesPage secondaryProcessPage;

    public SecondaryProcessTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5140", "5115", "5132"})
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Hydrostatic Leak Testing")
            .inputOptionsOverride("0.21")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Hydrostatic Leak Testing"));

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Hydrostatic Leak Testing")
            .selectOptionsTab();

        assertThat(materialProcessPage.getAverageWallThickness(), is(0.21));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Secondary Processes has not went in yet")
    @TestRail(testCaseId = {"5120", "5121", "5123"})
    @Description("Validate zero count when no secondary process is selected and Test secondary process xray")
    public void secondaryProcessXray() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulding";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS, 10% Glass")
            .submit(EvaluatePage.class)
            .goToSecondaryTab();

        assertThat(secondaryPage.getSecondaryProcesses(), is(empty()));

        evaluatePage = secondaryPage.openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Xray Inspection")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Xray Inspection"));

        secondaryPage = evaluatePage.goToSecondaryTab();

        assertThat(secondaryPage.getSecondaryProcesses(), hasItems("Xray", " Packaging"));
    }

    @Test
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
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .selectSecondaryProcess("Carburize")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Carburize"));

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

        assertThat(materialProcessPage.getCaseDepth(), is(equalTo(0.46)));
        assertThat(materialProcessPage.getMasking(), is(equalTo(1.0)));
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
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Atmosphere Oil Harden")
            .inputMaskingOverride("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Atmosphere Oil Harden");

        assertThat(secondaryProcessPage.getMasking(), is(2.0));
    }

    @Test
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
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("Standard Anneal")
            .inputMaskingOverride("1")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Standard Anneal"));

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Standard Anneal")
            .selectOptionsTab();

        assertThat(materialProcessPage.getMasking(), is(1.0));
    }

    @Test
    @TestRail(testCaseId = {"5161"})
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
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .selectSecondaryProcess("Vacuum Temper")
            .inputMaskingOverride("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree(" Heat Treat Processes, Temper")
            .selectSecondaryProcess("Vacuum Temper");

        assertThat(secondaryProcessPage.getMasking(), is(3.0));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Secondary Processes has not went in yet")
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
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes")
            .selectSecondaryProcess("Stress Relief")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Stress Relief"));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Secondary Processes has not went in yet")
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
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Anodize, Anodizing Tank")
            .selectSecondaryProcess("Anodize:Anodize Type I")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Anodize"));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Secondary Processes has not went in yet")
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
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Certification")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Certification"));
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
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Powder Coat Cart")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Powder Coat Cart"));

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Powder Coat Cart");


        assertThat(materialProcessPage.getProcessPercentage("Powder Coat Cart"), hasItem("38.15s (11.35%)"));
    }

    @Test
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
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Paint")
            .selectSecondaryProcess("Powder Coat Cart")
            .inputFractionOverride("0.30")
            .selectNoMasking()
            .inputCompPaintCart("414")
            .inputBatchSizeOverride("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Powder Coat Cart")
            .selectOptionsTab();

        assertThat(materialProcessPage.getFractionPainted(), is(0.30));
        assertThat(materialProcessPage.isNoMaskingSelected(), is(true));
        assertThat(materialProcessPage.getComponentsPaintCart(), is(414.0));
        assertThat(materialProcessPage.getPaintedBatchSize(), is(2.0));
    }

    @Test
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
            .goToSecondaryTab()
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

        assertThat(materialProcessPage.getFractionPainted(), is(0.40));
        assertThat(materialProcessPage.getMaskedFeature(), is(1.0));
        assertThat(materialProcessPage.getFractionPainted(), is(.4));
        assertThat(materialProcessPage.getComponentsLoadBar(), is(1.0));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Secondary Processes has not went in yet")
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
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Passivation"));
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
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Passivation / Carton Forming / Pack & Load"));

        evaluatePage.publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Scenario Name", "Equals", scenarioName)
            .submit(ExplorePage.class)
            .openScenario("SheetMetal", scenarioName);

        assertThat(evaluatePage.isSecondaryProcessButtonEnabled(), is(false));
    }

    @Category({SmokeTests.class, IgnoreTests.class})
    @Test
    @Ignore("Secondary Processes has not went in yet")
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
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .expandSecondaryProcessTree("Anodize, Anodizing Tank")
            .search("Anodize:Anodize Type I")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Anodize / Carton Forming / Pack & Load"));
    }

    @Test
    @TestRail(testCaseId = {"5131"})
    @Description("secondary process automatically added by aPriori")
    public void cannotDeselectSP() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryProcessPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .expandSecondaryProcessTree("High Pressure Die Cast, Trim");

        assertThat(secondaryProcessPage.isCheckboxSelected("Trim"), is(true));
    }

    @Test
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
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .selectSecondaryProcess("Carbonitride")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Surface Harden")
            .highlightSecondaryProcess("Carbonitride");

        assertThat(secondaryProcessPage.getMasking(), is("1"));
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
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Vacuum Air Harden")
            .inputMasking("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Vacuum Air Harden");

        assertThat(secondaryProcessPage.getMasking(), is(2.0));
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
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Vacuum Air Harden with High Temper")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Vacuum Air Harden with High Temper");

        assertThat(secondaryProcessPage.getMasking(), is(1.0));
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Spring Steel Harden")
            .inputMasking("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Spring Steel Harden");

        assertThat(secondaryProcessPage.getMasking(), is(3.0));
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("Stainless Steel Harden")
            .inputMasking("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("Stainless Steel Harden");

        assertThat(secondaryProcessPage.getMasking(), is(1.0));
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .selectSecondaryProcess("High Speed Steel Harden")
            .inputMasking("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Through Harden")
            .highlightSecondaryProcess("High Speed Steel Harden");

        assertThat(secondaryProcessPage.getMasking(), is(3.0));
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("Low Temp Vacuum Anneal")
            .inputMasking("4")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .highlightSecondaryProcess("Low Temp Vacuum Anneal");

        assertThat(secondaryProcessPage.getMasking(), is(4.0));
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Anneal")
            .selectSecondaryProcess("High Temp Vacuum Anneal")
            .inputMasking("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Heat Treat Processes, Anneal")
            .highlightSecondaryProcess("High Temp Vacuum Anneal");

        assertThat(secondaryProcessPage.getMasking(), is(2.0));
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .selectSecondaryProcess("Standard Temper")
            .inputMaskedFeatures("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .expandSecondaryProcessTree("Heat Treat Processes, Temper")
            .selectSecondaryProcess("Standard Temper");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(1.0));
    }

    @Test
    @Ignore("Secondary Processes have not been implemented yet")
    @Category({SmokeTests.class, IgnoreTests.class})
    @TestRail(testCaseId = {"5122"})
    @Description("Selections are cleared when user cancels changes")
    public void selectionsCleared() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PlasticMoulding";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        secondaryPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .expandSecondaryProcessTree("Testing and Inspection")
            .selectSecondaryProcess("Xray Inspection")
            .cancel()
            .costScenario()
            .goToSecondaryTab();

        assertThat(secondaryPage.getSecondaryProcesses(), is(empty()));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Secondary Processes has not went in yet")
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
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED), is(true));
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
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .reset()
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getListOfSecondaryProcesses(), hasItems("No Processes Selected..."));

        evaluatePage.goToSecondaryTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Passivation")
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .deselectAll()
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getListOfSecondaryProcesses(), is("No Processes Selected..."));
    }
}