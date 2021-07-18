package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.navtoolbars.EvaluateToolbar;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.SecondaryProcessesPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
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
    private SecondaryProcessesPage secondaryProcessPage;

    private File resourceFile;
    private UserCredentials currentUser;

    public SecondaryProcessTests() {
        super();
    }

    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5140", "5115", "5132"})
    @Description("Test secondary process leak test - edit wall thickness PSO and validate the process chart")
    public void secondaryProcessLeakTest() {

        resourceFile = FileResourceUtil.getResourceAsFile("PlasticMoulding.CATPart");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .openSecondaryProcesses()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Hydrostatic Leak Testing")
            .selectOverrideButton()
            .setPartThickness("0.21");

        evaluatePage = new SecondaryProcessPage(driver).apply()
            .costScenario();
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Hydrostatic Leak Testing"));

        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Hydrostatic Leak Testing")
            .selectOptions();

        assertThat(processSetupOptionsPage.getPartThickness(), is("0.21"));
    }*/

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
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS, 10% Glass")
            .submit();
        assertThat(evaluatePage.getSecondaryProcesses(), is(empty()));

        evaluatePage.openSecondaryProcesses()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Xray Inspection"));
        assertThat(evaluatePage.getSecondaryProcesses(), hasItems("Xray", " Packaging"));
    }

    /*@Test
    @TestRail(testCaseId = {"5142", "5149"})
    @Description("Test secondary process Carburize")
    public void secondaryProcessCarburize() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carburize")
            .submit()
            .costScenario();
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Carburize"));

        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Carburize")
            .selectOptions()
            .setCaseOverrideInput("0.46")
            .setMaskedFeaturesInput("1")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Carburize")
            .selectOptions();

        assertThat(processSetupOptionsPage.getCaseOverride(), is("0.46"));
        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5151"})
    @Description("Test secondary process Atmosphere Oil Harden")
    public void secondaryProcessAtmosphereOilHarden() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden")
            .setMaskedFeaturesInput("2");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("2"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5157"})
    @Description("Test secondary process Standard Anneal")
    public void secondaryProcessStandardAnneal() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Standard Anneal")
            .selectNumMaskedFeaturesButton()
            .setMaskedFeaturesInput("1");

        evaluatePage = new SecondaryProcessPage(driver).apply()
            .costScenario();
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Standard Anneal"));

        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Standard Anneal")
            .selectOptions();
        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5161"})
    @Description("Test secondary process Vacuum Temper")
    public void secondaryProcessVacuumTemper() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 7075")
            .submit()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper")
            .setMaskedFeaturesInput("3");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("3"));
    }*/

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
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes", "Stress Relief")
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
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I")
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
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Heat Treatment", "Certification")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Certification"));
    }

    /*
    @Test
    @TestRail(testCaseId = {"5132"})
    @Description("Test secondary process Paint")
    public void secondaryProcessPaint() {

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .submit()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Powder Coat Cart"));

        processRoutingPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Powder Coat Cart");

        assertThat(processRoutingPage.getProcessPercentage(), hasItem("38 (77%)"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5141", "5142", "5143"})
    @Description("Test secondary process powder coat cart PSO")
    public void psoPowderCoatCart() {

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .selectFractionButton()
            .setFractionPainted("0.30")
            .selectNoMaskingButton()
            .setSpecifyPainted("414")
            .setSpecifiedInput("2");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Powder Coat Cart")
            .selectOptions();

        assertThat(processSetupOptionsPage.getFractionPainted(), is("0.30"));
        assertThat(processSetupOptionsPage.isNoMaskingSelected("checked"), is("true"));
        assertThat(processSetupOptionsPage.getSpecifyPainted(), is("414"));
        assertThat(processSetupOptionsPage.getSpecified(), is("2"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5144", "5145", "5146", "5147"})
    @Description("Test secondary process wet coat line PSO")
    public void psoWetCoatLine() {

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Paint", "Wet Coat Line")
            .submit()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Surface Treatment, Paint", "Wet Coat Line")
            .selectFractionButton()
            .setFractionPainted("0.40")
            .selectEnterNumberOfMaskedFeaturesButton()
            .setMaskFeatures("1")
            .setSpecifyPainted("254")
            .setComponentsPerLoad("1");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Wet Coat Line")
            .selectOptions();

        assertThat(processSetupOptionsPage.getFractionPainted(), is("0.40"));
        assertThat(processSetupOptionsPage.getTheNumberOfMaskedFeatures(), is("1"));
        assertThat(processSetupOptionsPage.getSpecifyPainted(), is("254"));
        assertThat(processSetupOptionsPage.getComponentsPerLoad(), is("1"));
    }*/

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
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Passivation"));
    }

    /*
    @Test
    @TestRail(testCaseId = {"5116", "5119"})
    @Description("Multiple Secondary Processes before Costing")
    public void multiSecondaryProcessBeforeCost() {

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, 440B")
            .submit()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .submit()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Passivation / Carton Forming / Pack & Load"));

        evaluatePage.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(scenarioName, "SheetMetal");

        assertThat(evaluatePage.isSecondaryProcessButtonEnabled(), is(false));
    }*/

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
            .submit()
            .costScenario()
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Anodize / Carton Forming / Pack & Load"));
    }

    /*
    @Test
    @TestRail(testCaseId = {"5131"})
    @Description("secondary process automatically added by aPriori")
    public void cannotDeselectSP() {

        resourceFile = FileResourceUtil.getResourceAsFile("DTCCastingIssues.catpart");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        secondaryProcessPage = settingsPage.save(ExplorePage.class)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcesses()
            .findSecondaryProcess("High Pressure Die Cast", "Trim");

        assertThat(secondaryProcessPage.getCheckboxStatus("Trim"), containsString("disabled"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5150"})
    @Description("Test secondary process Carbonitride")
    public void secondaryProcessCarbonitride() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit()
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carbonitride")
            .setMaskedFeaturesInput("1");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carbonitride");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5152"})
    @Description("Test secondary process Vacuum air harden")
    public void secondaryProcessVacuumAirHarden() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit()
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden")
            .setMaskedFeaturesInput("2");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("2"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5153", "5162"})
    @Description("Test secondary process Vacuum Air Harden with High Temper")
    public void secondaryProcessVacuumAirHardenHighTemp() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit()
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden with High Temper")
            .setMaskedFeaturesInput("1");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden with High Temper");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5154"})
    @Description("Test secondary process Spring steel")
    public void secondaryProcessSpringSteel() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Spring Steel Harden")
            .setMaskedFeaturesInput("3");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Spring Steel Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("3"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5155"})
    @Description("Test secondary process Stainless steel")
    public void secondaryProcessStainlessSteel() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Stainless Steel Harden")
            .setMaskedFeaturesInput("1");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Stainless Steel Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5156"})
    @Description("Test secondary process High Speed Steel Harden")
    public void secondaryProcessHighSpeedSteel() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "High Speed Steel Harden")
            .setMaskedFeaturesInput("3");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "High Speed Steel Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("3"));
    }
*/
    /*@Test
    @TestRail(testCaseId = {"5158"})
    @Description("Test secondary process Low Temp Vacuum Anneal")
    public void secondaryProcessLowTempVacuumAnneal() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Low Temp Vacuum Anneal")
            .setMaskedFeaturesInput("4");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Low Temp Vacuum Anneal");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("4"));
    }*/

    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"5159", "5129"})
    @Description("Test secondary process High Temp Vacuum Anneal")
    public void secondaryProcessHighTempVacuumAnneal() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "High Temp Vacuum Anneal")
            .setMaskedFeaturesInput("2");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "High Temp Vacuum Anneal");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("2"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5160"})
    @Description("Test secondary process Standard Temper")
    public void secondaryProcessStandardTemper() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcesses()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Standard Temper")
            .setMaskedFeaturesInput("1");

        processSetupOptionsPage = new SecondaryProcessPage(driver).apply()
            .costScenario()
            .openSecondaryProcesses()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Standard Temper");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }*/

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
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openSecondaryProcesses()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection")
            .cancel()
            .costScenario();

        assertThat(evaluatePage.getSecondaryProcesses(), is(empty()));
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
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .submit(EvaluateToolbar.class)
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED), is(true));
    }

    /*@Test
    @TestRail(testCaseId = {"5133", "5134", "5138"})
    @Description("Validate the user can clear all secondary process selections")
    public void clearAllSP() {

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openSecondaryProcesses()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .reset()
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getSecondaryProcesses(), is("0 Selected"));

        evaluatePage.openProcessDetails()
            .selectSecondaryProcessButton()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .selectClearAll()
            .apply()
            .costScenario();

        assertThat(evaluatePage.getSecondaryProcesses(), is("0 Selected"));
    }*/
}