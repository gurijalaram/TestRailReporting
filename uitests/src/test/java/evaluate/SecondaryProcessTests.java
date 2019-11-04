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
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class SecondaryProcessTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private SecondaryProcessPage secondaryProcessPage;
    private ProcessRoutingPage processRoutingPage;
    private ProcessSetupOptionsPage processSetupOptionsPage;

    public SecondaryProcessTests() {
        super();
    }

    @After
    public void resetToleranceSettings() {
        new AfterTestUtil(driver).resetToleranceSettings();
    }

    @Test
    @TestRail(testCaseId = {"679"})
    @Description("Test secondary process leak test")
    public void secondaryProcessLeakTest() {
        loginPage = new LoginPage(driver);
        processSetupOptionsPage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .openSecondaryProcess()
            .highlightSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Hydrostatic Leak Testing")
            .selectOverrideButton()
            .setPartThickness("0.21");

        secondaryProcessPage = new SecondaryProcessPage(driver);
        evaluatePage = secondaryProcessPage.apply()
            .costScenario();
        assertThat(evaluatePage.isProcessRoutingDetails("Hydrostatic Leak Testing"), is(true));

        evaluatePage = new EvaluatePage(driver);
        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Hydrostatic Leak Testing")
            .selectOptions();

        assertThat(processSetupOptionsPage.isPartThickness("0.21"), is(true));


    }

    @Test
    @Description("Test secondary process xray")
    public void secondaryProcessXray() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Xray Inspection"), is(true));
    }

    @Test
    @Description("Test secondary process Carburize")
    public void secondaryProcessCarburize() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carburize")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Carburize"), is(true));
    }

    @Test
    @Description("Test secondary process Atmosphere Oil Harden")
    public void secondaryProcessAtmosphereOilHarden() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Atmosphere Oil Harden"), is(true));
    }

    @Test
    @Description("Test secondary process Standard Anneal")
    public void secondaryProcessStandardAnneal() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Standard Anneal")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Standard Anneal"), is(true));
    }

    @Test
    @Description("Test secondary process Vacuum Temper")
    public void secondaryProcessVacuumTemper() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Vacuum Temper"), is(true));
    }

    @Test
    @Description("Test secondary process Stress Relief")
    public void secondaryProcessStressRelief() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes", "Stress Relief")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Stress Relief"), is(true));
    }

    @Test
    @Description("Test secondary process Anodize")
    public void secondaryProcessAnodize() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Anodize"), is(true));
    }

    @Test
    @Description("Test secondary process Certification")
    public void secondaryProcessCertification() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment", "Certification")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Certification"), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1616"})
    @Description("Test secondary process Paint")
    public void secondaryProcessPaint() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Powder Coat Cart"), is(true));

        processRoutingPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Powder Coat Cart");

        assertThat(processRoutingPage.getProcessPercentage(), hasItem("38 (77%)"));


    }

    @Test
    @Description("Test secondary process Passivation")
    public void secondaryProcessPassivation() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Passivation"), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1614"})
    @Description("Multiple Secondary Processes before Costing")
    public void multiSecondaryProcessBeforeCost() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Passivation / Carton Forming / Pack & Load"), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1614"})
    @Description("Multiple Secondary Processes after Costing")
    public void multiSecondaryProcessAfterCost() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
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

        assertThat(evaluatePage.isProcessRoutingDetails("Anodize / Carton Forming / Pack & Load"), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1615"})
    @Description("secondary process automatically added by aPriori")
    public void cannotDeselectSP() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        secondaryProcessPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSecondaryProcess()
            .findSecondaryProcess("High Pressure Die Cast", "Trim");

        assertThat(secondaryProcessPage.getCheckboxStatus("Trim"), containsString("disabled"));
    }
}