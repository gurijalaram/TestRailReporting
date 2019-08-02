package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.properties.reader.FileResourceReader;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class SecondaryProcessTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private final String COSTING_SUCCESS = "Success";

    public SecondaryProcessTests() {
        super();
    }

    @Test
    @Description("Test secondary process leak test")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessLeakTest() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("LeakTest", new FileResourceReader().getResourceFile("PlasticMoulding.CATPart"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Hydrostatic Leak Testing")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Hydrostatic Leak Testing"), is(true));
    }

    @Test
    @Description("Test secondary process xray")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessXray() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Xray Inspection", new FileResourceReader().getResourceFile("PlasticMoulding.CATPart"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Xray Inspection"), is(true));
    }

    @Test
    @Description("Test secondary process Carburize")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessCarburize() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Carburize", new FileResourceReader().getResourceFile("Casting.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carburize")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Carburize"), is(true));
    }

    @Test
    @Description("Test secondary process Atmosphere Oil Harden")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessAtmosphereOilHarden() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Atmosphere Oil Harden", new FileResourceReader().getResourceFile("Casting.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Atmosphere Oil Harden")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Atmosphere Oil Harden"), is(true));
    }

    @Test
    @Description("Test secondary process Standard Anneal")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessStandardAnneal() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Standard Anneal", new FileResourceReader().getResourceFile("Casting.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Standard Anneal")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Standard Anneal"), is(true));
    }

    @Test
    @Description("Test secondary process Vacuum Temper")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessVacuumTemper() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Vacuum Temper", new FileResourceReader().getResourceFile("Casting.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Vacuum Temper"), is(true));
    }

    @Test
    @Description("Test secondary process Stress Relief")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessStressRelief() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Stress Relief", new FileResourceReader().getResourceFile("Casting.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes", "Stress Relief")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Stress Relief"), is(true));
    }

    @Test
    @Description("Test secondary process Anodize")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessAnodize() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Anodize", new FileResourceReader().getResourceFile("Casting.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Anodize"), is(true));
    }

    @Test
    @Description("Test secondary process Paint")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessCertification() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Certification", new FileResourceReader().getResourceFile("SheetMetal.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment", "Certification")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Certification"), is(true));
    }

    @Test
    @Description("Test secondary process Paint")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessPaint() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Paint", new FileResourceReader().getResourceFile("SheetMetal.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Powder Coat Cart"), is(true));
    }

    @Test
    @Description("Test secondary process Passivation")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessPassivation() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("Passivation", new FileResourceReader().getResourceFile("SheetMetal.prt"))
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation")
            .apply()
            .costScenario(COSTING_SUCCESS);

        assertThat(evaluatePage.getProcessRoutingDetails("Passivation"), is(true));
    }
}