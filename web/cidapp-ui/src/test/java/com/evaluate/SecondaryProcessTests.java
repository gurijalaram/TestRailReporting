package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
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

        resourceFile = FileResourceUtil.getResourceAsFile("PlasticMoulding.CATPart");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .goToSecondaryTab()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Hydrostatic Leak Testing")
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
            .selectSecondaryProcess("Testing and Inspection", " Xray Inspection")
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

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden",  "Carburize")
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
        assertThat(materialProcessPage.getMasking(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5151"})
    @Description("Test secondary process Atmosphere Oil Harden")
    public void secondaryProcessAtmosphereOilHarden() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden")
            .inputMaskingOverride("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden");

        assertThat(secondaryProcessPage.getMasking(), is(2));
    }

    @Test
    @TestRail(testCaseId = {"5157"})
    @Description("Test secondary process Standard Anneal")
    public void secondaryProcessStandardAnneal() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Standard Anneal")
            .inputMaskingOverride("1")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Standard Anneal"));

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Standard Anneal")
            .selectOptionsTab();

        assertThat(materialProcessPage.getMasking(), is(1));
    }

    @Test
    @TestRail(testCaseId = {"5161"})
    @Description("Test secondary process Vacuum Temper")
    public void secondaryProcessVacuumTemper() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit(EvaluatePage.class)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper")
            .inputMaskingOverride("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper");

        assertThat(secondaryProcessPage.getMasking(), is(3));
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
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Heat Treat Processes", "Stress Relief")
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
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Anodize, Anodizing Tank", "Anodize:Anodize Type I")
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

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Powder Coat Cart"));

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Powder Coat Cart");

        assertThat(materialProcessPage.getProcessPercentage(), hasItem("38 (77%)"));
    }

    @Test
    @TestRail(testCaseId = {"5141", "5142", "5143"})
    @Description("Test secondary process powder coat cart PSO")
    public void psoPowderCoatCart() {

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
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
        assertThat(materialProcessPage.getComponentsPaintCart(), is(414));
        assertThat(materialProcessPage.getPaintedBatchSize(), is(2));
    }

    @Test
    @TestRail(testCaseId = {"5144", "5145", "5146", "5147"})
    @Description("Test secondary process wet coat line PSO")
    public void psoWetCoatLine() {

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Paint", "Wet Coat Line")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Paint", "Wet Coat Line")
            .inputFractionOverride("0.40")
            .inputMaskedFeatures("1")
            .inputBatchSizeOverride("254")
            .inputCompPaintCart("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Wet Coat Line")
            .selectOptionsTab();

        assertThat(materialProcessPage.getFractionPainted(), is(0.40));
        assertThat(materialProcessPage.getMaskedFeature(), is(1));
        assertThat(materialProcessPage.getFractionPainted(), is(254));
        assertThat(materialProcessPage.getComponentsPaintCart(), is(1));
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

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit(EvaluatePage.class)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
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
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .selectSecondaryProcess("Anodize, Anodizing Tank", "Anodize:Anodize Type I")
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

        resourceFile = FileResourceUtil.getResourceAsFile("DTCCastingIssues.catpart");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openSecondaryProcesses()
            .expandSecondaryProcessTree("High Pressure Die Cast, Trim");

        assertThat(secondaryProcessPage.getCheckboxStatus("Trim"), containsString("disabled"));
    }

    @Test
    @TestRail(testCaseId = {"5150"})
    @Description("Test secondary process Carbonitride")
    public void secondaryProcessCarbonitride() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carbonitride")
            .inputMaskedFeatures("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carbonitride");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is("1"));
    }

    @Test
    @TestRail(testCaseId = {"5152"})
    @Description("Test secondary process Vacuum air harden")
    public void secondaryProcessVacuumAirHarden() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden")
            .inputMaskedFeatures("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(2));
    }

    @Test
    @TestRail(testCaseId = {"5153", "5162"})
    @Description("Test secondary process Vacuum Air Harden with High Temper")
    public void secondaryProcessVacuumAirHardenHighTemp() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden with High Temper")
            .inputMaskedFeatures("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden with High Temper");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(1));
    }

    @Test
    @TestRail(testCaseId = {"5154"})
    @Description("Test secondary process Spring steel")
    public void secondaryProcessSpringSteel() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Spring Steel Harden")
            .inputMaskedFeatures("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Spring Steel Harden");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(3));
    }

    @Test
    @TestRail(testCaseId = {"5155"})
    @Description("Test secondary process Stainless steel")
    public void secondaryProcessStainlessSteel() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Stainless Steel Harden")
            .inputMaskedFeatures("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Stainless Steel Harden");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(1));
    }

    @Test
    @TestRail(testCaseId = {"5156"})
    @Description("Test secondary process High Speed Steel Harden")
    public void secondaryProcessHighSpeedSteel() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "High Speed Steel Harden")
            .inputMaskedFeatures("3")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "High Speed Steel Harden");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(3));
    }

    @Test
    @TestRail(testCaseId = {"5158"})
    @Description("Test secondary process Low Temp Vacuum Anneal")
    public void secondaryProcessLowTempVacuumAnneal() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Low Temp Vacuum Anneal")
            .inputMaskedFeatures("4")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Low Temp Vacuum Anneal");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(4));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5159", "5129"})
    @Description("Test secondary process High Temp Vacuum Anneal")
    public void secondaryProcessHighTempVacuumAnneal() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "High Temp Vacuum Anneal")
            .inputMaskedFeatures("2")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "High Temp Vacuum Anneal");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(2));
    }

    @Test
    @TestRail(testCaseId = {"5160"})
    @Description("Test secondary process Standard Temper")
    public void secondaryProcessStandardTemper() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        secondaryProcessPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Standard Temper")
            .inputMaskedFeatures("1")
            .submit(EvaluatePage.class)
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Standard Temper");

        assertThat(secondaryProcessPage.getMaskedFeatures(), is(1));
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
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Testing and Inspection", "Xray Inspection")
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

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .reset()
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getListOfSecondaryProcesses(), hasItems("No Processes Selected..."));

        evaluatePage.goToSecondaryTab()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .deselectAll()
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getListOfSecondaryProcesses(), is("No Processes Selected..."));
    }
}