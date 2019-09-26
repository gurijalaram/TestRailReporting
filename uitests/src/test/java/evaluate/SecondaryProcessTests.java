package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class SecondaryProcessTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public SecondaryProcessTests() {
        super();
    }

    @Test
    @Description("Test secondary process leak test")
    public void secondaryProcessLeakTest() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Hydrostatic Leak Testing")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Hydrostatic Leak Testing"), is(true));
    }

    @Test
    @Description("Test secondary process xray")
    public void secondaryProcessXray() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Xray Inspection"), is(true));
    }

    @Test
    @Description("Test secondary process Carburize")
    public void secondaryProcessCarburize() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carburize")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Carburize"), is(true));
    }

    @Test
    @Description("Test secondary process Atmosphere Oil Harden")
    public void secondaryProcessAtmosphereOilHarden() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Through Harden", "Atmosphere Oil Harden")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Atmosphere Oil Harden"), is(true));
    }

    @Test
    @Description("Test secondary process Standard Anneal")
    public void secondaryProcessStandardAnneal() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Standard Anneal")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Standard Anneal"), is(true));
    }

    @Test
    @Description("Test secondary process Vacuum Temper")
    public void secondaryProcessVacuumTemper() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Vacuum Temper"), is(true));
    }

    @Test
    @Description("Test secondary process Stress Relief")
    public void secondaryProcessStressRelief() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 7075")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes", "Stress Relief")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Stress Relief"), is(true));
    }

    @Test
    @Description("Test secondary process Anodize")
    public void secondaryProcessAnodize() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Anodize"), is(true));
    }

    @Test
    @Description("Test secondary process Certification")
    public void secondaryProcessCertification() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment", "Certification")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Certification"), is(true));
    }

    @Test
    @Description("Test secondary process Paint")
    public void secondaryProcessPaint() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Powder Coat Cart"), is(true));
    }

    @Test
    @Description("Test secondary process Passivation")
    public void secondaryProcessPassivation() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Passivation"), is(true));
    }
}