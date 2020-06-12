package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import com.apriori.pageobjects.pages.evaluate.process.secondaryprocess.SecondaryProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.pageobjects.toolbars.EvaluatePanelToolbar;
import com.apriori.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SecondaryProcessTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private SecondaryProcessPage secondaryProcessPage;
    private ProcessRoutingPage processRoutingPage;
    private ProcessSetupOptionsPage processSetupOptionsPage;
    private UserCredentials currentUser;
    private EvaluatePanelToolbar evaluatePanelToolbar;

    private File resourceFile;

    public SecondaryProcessTests() {
        super();
    }

    @After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"679", "653", "670"})
    @Description("Test secondary process leak test")
    public void secondaryProcessLeakTest() {

        resourceFile = new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Hydrostatic Leak Testing")
            .selectOverrideButton()
            .setPartThickness("0.21");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        evaluatePage = secondaryProcessPage.apply()
            .costScenario();
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Hydrostatic Leak Testing"));

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Hydrostatic Leak Testing")
            .selectOptions();

        assertThat(processSetupOptionsPage.getPartThickness(), is("0.21"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"658", "659", "661"})
    @Description("Test secondary process xray")
    public void secondaryProcessXray() {

        resourceFile = new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply();
        assertThat(evaluatePage.getSecondaryProcesses(), is("0 Selected"));

        new EvaluatePage(driver).openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Xray Inspection"));
        assertThat(evaluatePage.getSecondaryProcesses(), is("1 Selected"));
    }

    @Test
    @TestRail(testCaseId = {"687", "688"})
    @Description("Test secondary process Carburize")
    public void secondaryProcessCarburize() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carburize")
            .apply()
            .costScenario();
        assertThat(evaluatePage.getProcessRoutingDetails(), is("Carburize"));

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
    }

    @Test
    @TestRail(testCaseId = {"690"})
    @Description("Test secondary process Atmosphere Oil Harden")
    public void secondaryProcessAtmosphereOilHarden() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden")
            .setMaskedFeaturesInput("2");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("2"));
    }

    @Test
    @TestRail(testCaseId = {"696"})
    @Description("Test secondary process Standard Anneal")
    public void secondaryProcessStandardAnneal() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Standard Anneal")
            .selectNumMaskedFeaturesButton()
            .setMaskedFeaturesInput("1");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        evaluatePage = secondaryProcessPage.apply()
            .costScenario();
        assertThat(evaluatePage.getProcessRoutingDetails(), is("Standard Anneal"));

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Standard Anneal")
            .selectOptions();
        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }

    @Test
    @TestRail(testCaseId = {"700"})
    @Description("Test secondary process Vacuum Temper")
    public void secondaryProcessVacuumTemper() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper")
            .setMaskedFeaturesInput("3");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("3"));
    }

    @Test
    @Description("Test secondary process Stress Relief")
    public void secondaryProcessStressRelief() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes", "Stress Relief")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Stress Relief"));
    }

    @Test
    @Description("Test secondary process Anodize")
    public void secondaryProcessAnodize() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Anodize"));
    }

    @Test
    @Description("Test secondary process Certification")
    public void secondaryProcessCertification() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment", "Certification")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Certification"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1616"})
    @Description("Test secondary process Paint")
    public void secondaryProcessPaint() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Powder Coat Cart"));

        processRoutingPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Powder Coat Cart");

        assertThat(processRoutingPage.getProcessPercentage(), hasItem("38 (77%)"));
    }

    @Test
    @TestRail(testCaseId = {"680", "681", "682"})
    @Description("Test secondary process powder coat cart PSO")
    public void psoPowderCoatCart() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .selectFractionButton()
            .setFractionPainted("0.3")
            .selectNoMaskingButton()
            .setSpecifyPainted("414")
            .setSpecifiedInput("2");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Powder Coat Cart")
            .selectOptions();

        assertThat(processSetupOptionsPage.getFractionPainted(), is("0.3"));
        assertThat(processSetupOptionsPage.isNoMaskingSelected("checked"), is("true"));
        assertThat(processSetupOptionsPage.getSpecifyPainted(), is("414"));
        assertThat(processSetupOptionsPage.getSpecified("2"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"683", "684", "685", "686"})
    @Description("Test secondary process wet coat line PSO")
    public void psoWetCoatLine() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Paint", "Wet Coat Line")
            .apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Surface Treatment, Paint", "Wet Coat Line")
            .selectFractionButton()
            .setFractionPainted("0.40")
            .selectEnterNumberOfMaskedFeaturesButton()
            .setMaskFeatures("1")
            .setSpecifyPainted("254")
            .setComponentsPerLoad("1");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Wet Coat Line")
            .selectOptions();

        assertThat(processSetupOptionsPage.getFractionPainted(), is("0.4"));
        assertThat(processSetupOptionsPage.getTheNumberOfMaskedFeatures(), is("1"));
        assertThat(processSetupOptionsPage.getSpecifyPainted(), is("254"));
        assertThat(processSetupOptionsPage.getComponentsPerLoad(), is("1"));
    }

    @Test
    @Description("Test secondary process Passivation")
    public void secondaryProcessPassivation() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Passivation"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1614", "654"})
    @Description("Multiple Secondary Processes before Costing")
    public void multiSecondaryProcessBeforeCost() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Passivation / Carton Forming / Pack & Load"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1614", "655", "656"})
    @Description("Multiple Secondary Processes after Costing")
    public void multiSecondaryProcessAfterCost() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .costScenario()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Anodize / Carton Forming / Pack & Load"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1615", "669"})
    @Description("secondary process automatically added by aPriori")
    public void cannotDeselectSP() {

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        secondaryProcessPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .findSecondaryProcess("High Pressure Die Cast", "Trim");

        assertThat(secondaryProcessPage.getCheckboxStatus("Trim"), containsString("disabled"));
    }

    @Test
    @TestRail(testCaseId = {"689"})
    @Description("Test secondary process Carbonitride")
    public void secondaryProcessCarbonitride() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carbonitride")
            .setMaskedFeaturesInput("1");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carbonitride");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }

    @Test
    @TestRail(testCaseId = {"691"})
    @Description("Test secondary process Vacuum air harden")
    public void secondaryProcessVacuumAirHarden() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden")
            .setMaskedFeaturesInput("2");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("2"));
    }

    @Test
    @TestRail(testCaseId = {"692", "702"})
    @Description("Test secondary process Vacuum Air Harden with High Temper")
    public void secondaryProcessVacuumAirHardenHighTemp() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden with High Temper")
            .setMaskedFeaturesInput("1");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Vacuum Air Harden with High Temper");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }

    @Test
    @TestRail(testCaseId = {"693"})
    @Description("Test secondary process Spring steel")
    public void secondaryProcessSpringSteel() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Spring Steel Harden")
            .setMaskedFeaturesInput("3");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Spring Steel Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("3"));
    }

    @Test
    @TestRail(testCaseId = {"694"})
    @Description("Test secondary process Stainless steel")
    public void secondaryProcessStainlessSteel() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Stainless Steel Harden")
            .setMaskedFeaturesInput("1");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Stainless Steel Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }

    @Test
    @TestRail(testCaseId = {"695"})
    @Description("Test secondary process High Speed Steel Harden")
    public void secondaryProcessHighSpeedSteel() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "High Speed Steel Harden")
            .setMaskedFeaturesInput("3");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "High Speed Steel Harden");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("3"));
    }

    @Test
    @TestRail(testCaseId = {"697"})
    @Description("Test secondary process Low Temp Vacuum Anneal")
    public void secondaryProcessLowTempVacuumAnneal() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Low Temp Vacuum Anneal")
            .setMaskedFeaturesInput("4");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Low Temp Vacuum Anneal");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("4"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"698", "667"})
    @Description("Test secondary process High Temp Vacuum Anneal")
    public void secondaryProcessHighTempVacuumAnneal() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "High Temp Vacuum Anneal")
            .setMaskedFeaturesInput("2");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "High Temp Vacuum Anneal");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("2"));
    }

    @Test
    @TestRail(testCaseId = {"699"})
    @Description("Test secondary process Standard Temper")
    public void secondaryProcessStandardTemper() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        processSetupOptionsPage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .selectHighlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Standard Temper")
            .setMaskedFeaturesInput("1");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        processSetupOptionsPage = secondaryProcessPage.apply()
            .costScenario()
            .openSecondaryProcess()
            .highlightSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Standard Temper");

        assertThat(processSetupOptionsPage.getMaskedFeatures(), is("1"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"660"})
    @Description("Selections are cleared when user cancels changes")
    public void selectionsCleared() {

        resourceFile = new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection")
            .cancel()
            .costScenario();

        assertThat(evaluatePage.getSecondaryProcesses(), is("0 Selected"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"665"})
    @Description("Validate if a secondary process fails to cost, entire part fails to cost")
    public void secondaryProcessCostFailed() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), CoreMatchers.is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"671", "672", "677"})
    @Description("Validate the user can clear all secondary process selections")
    public void clearAllSP() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .selectClearAll()
            .apply()
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
    }
}