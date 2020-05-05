package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ChangeMaterialSelectionTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private MaterialUtilizationPage materialUtilizationPage;

    private File resourceFile;

    public ChangeMaterialSelectionTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Additive Manufacturing, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestAdditive() {

        resourceFile = new FileResourceUtil().getResourceFile("BasicScenario_Additive.prt.1");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum AlSi10Mg"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Duraform ProX GF")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Duraform ProX GF"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Bar & Tube Fab, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestBarandTubeFab() {

        resourceFile = new FileResourceUtil().getResourceFile("BasicScenario_BarAndTube.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Hot Worked, AISI 1010"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, AISI 316")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Stainless Steel, Stock, AISI 316"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestCasting() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("ISO JMB 800-1")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("ISO JMB 800-1"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario(5);

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Brass, Cast, Yellow 270")
            .apply()
            .costScenario(5);

        assertThat(evaluatePage.isMaterialInfo("Brass, Cast, Yellow 270"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Copper, Cast, UNS C28000")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Copper, Cast, UNS C28000"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Forging, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestForging() {

        resourceFile = new FileResourceUtil().getResourceFile("BasicScenario_Forging.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Cold Worked, AISI 1010"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Titanium, Ti-5Al-2.5Sn")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Titanium, Ti-5Al-2.5Sn"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("ABS"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("PET 30% Glass")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("PET 30% Glass"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Powder Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPowderMetal() {

        resourceFile = new FileResourceUtil().getResourceFile("Powder Metal.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("F-0005"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("FLN2-4405")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("FLN2-4405"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Rapid Prototyping, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestRapidPrototyping() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Default"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("870 Black")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("870 Black"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Roto & Blow Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestRotoBlowMolding() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Polyethylene, High Density (HDPE)"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Nylon, Type 46")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Nylon, Type 46"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetal() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Cold Worked, AISI 1020"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Inconel 625")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Inconel 625"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal - Hydroforming, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetalHydroforming() {

        resourceFile = new FileResourceUtil().getResourceFile("Hydroform Sample Banana.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum, Stock, ANSI 2017"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Copper, Stock, UNS C11000")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Copper, Stock, UNS C11000"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal Transfer Die, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetalTransferDie() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Cold Worked, AISI 1020"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("C60E")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("C60E"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Plastic, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetPlastic() {

        resourceFile = new FileResourceUtil().getResourceFile("2038646_Sheet_plastic.prt.1");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Polyethylene, HDPE Extrusion Sheet"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Acrylic, PMMA")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Acrylic, PMMA"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"864", "866", "867", "889"})
    @Description("Test making changes to the Material for Stock Machining, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestStockMachining() {

        String scenarioName = new Util().getScenarioName();
        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Hot Worked, AISI 1010"), is(true));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(scenarioName, "bracket_basic");

        assertThat(evaluatePage.isMaterialInfo("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"865"})
    @Description("Test re-selecting same material and the scenario can be recost")
    public void changeMaterialSelectionTestReSelect() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"868", "875"})
    @Description("Test de-selecting the material, previous material applied and the scenario can be cost")
    public void changeMaterialSelectionTestDeSelect() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"869"})
    @Description("Test closing and opening Material Properties, information within correct")
    public void changeMaterialSelectionTestMaterialProperties() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Inconel 625")
            .apply()
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getMaterialInfo("Name"), is("Inconel 625"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"884", "888"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMI() {

        String scenarioName = new Util().getScenarioName();
        resourceFile = new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialCompositionTable()
            .method("MCAD <material not found - VPE default used>")
            .apply()
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "MACHINED BOX AMERICAS");

        assertThat(evaluatePage.isMaterialInfo("Steel, Hot Worked, AISI 1095"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"885"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMINotExist() {

        resourceFile = new FileResourceUtil().getResourceFile("Machined Box AMERICAS IronCast.SLDPRT");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialCompositionTable()
            .method("MCAD <material not found - VPE default used>")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Hot Worked, AISI 1010"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"905"})
    @Description("Test opening material selection and selecting apply without making a selection")
    public void changeMaterialSelectionTestNoChange() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .openMaterialCompositionTable()
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Cold Worked, AISI 1020"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"905"})
    @Description("Test opening material selection and selecting cancel after making a selection")
    public void changeMaterialSelectionTestCancel() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, AISI 316")
            .cancel()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Steel, Cold Worked, AISI 1020"), is(true));
    }
}