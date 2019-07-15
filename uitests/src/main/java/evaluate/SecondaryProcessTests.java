package main.java.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.explore.FileUploadTests;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class SecondaryProcessTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private final String COSTING_SUCCESS = "Success";

    public SecondaryProcessTests() {
        super();
    }

    private String filePath = new Scanner(FileUploadTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    @Test
    @Description("Test secondary process leak test")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessLeakTest() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("LeakTest", filePath, "PlasticMoulding.CATPart")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Hydrostatic Leak Testing");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Hydrostatic Leak Testing"));
    }

    @Test
    @Description("Test secondary process xray")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessXray() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Xray Inspection", filePath, "PlasticMoulding.CATPart")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ABS, 10% Glass")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes, Testing and Inspection", "Xray Inspection");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Xray Inspection"));
    }

    @Test
    @Description("Test secondary process Carburize")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessCarburize() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Carburize", filePath, "Casting.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Carburize");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Carburize"));
    }

    @Test
    @Description("Test secondary process Atmosphere Oil Harden")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessAtmosphereOilHarden() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Atmosphere Oil Harden", filePath, "Casting.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Surface Harden", "Atmosphere Oil Harden");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Atmosphere Oil Harden"));
    }

    @Test
    @Description("Test secondary process Standard Anneal")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessStandardAnneal() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Standard Anneal", filePath, "Casting.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Anneal", "Standard Anneal");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Standard Anneal"));
    }

    @Test
    @Description("Test secondary process Vacuum Temper")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessVacuumTemper() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Vacuum Temper", filePath, "Casting.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes, Temper", "Vacuum Temper");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Vacuum Temper"));
    }

    @Test
    @Description("Test secondary process Stress Relief")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessStressRelief() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Stress Relief", filePath, "Casting.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("ASTM A148 Grade 105-85")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment, Heat Treat Processes", "Stress Relief");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Stress Relief"));
    }

    @Test
    @Description("Test secondary process Anodize")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessAnodize() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Anodize", filePath, "Casting.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Anodize, Anodizing Tank", "Anodize:Anodize Type I");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Anodize"));
    }

    @Test
    @Description("Test secondary process Paint")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessCertification() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Certification", filePath, "SheetMetal.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Heat Treatment", "Certification");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Certification"));
    }

    @Test
    @Description("Test secondary process Paint")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessPaint() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Paint", filePath, "SheetMetal.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Powder Coat Cart"));
    }

    @Test
    @Description("Test secondary process Passivation")
    @Severity(SeverityLevel.NORMAL)
    public void secondaryProcessPassivation() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Passivation", filePath, "SheetMetal.prt")
            .costScenario(COSTING_SUCCESS)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, 440B")
            .apply()
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment", "Passivation");

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(COSTING_SUCCESS);
        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Passivation"));
    }
}