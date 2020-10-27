package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.evaluate.EvaluatePage;
import pageobjects.evaluate.materialutilization.MaterialUtilizationPage;
import pageobjects.login.CidAppLoginPage;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ChangeMaterialSelectionTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private MaterialUtilizationPage materialUtilizationPage;

    public ChangeMaterialSelectionTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Additive Manufacturing, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestAdditive() {

        resourceFile = FileResourceUtil.getResourceAsFile("BasicScenario_Additive.prt.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum AlSi10Mg"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Duraform ProX GF")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Duraform ProX GF"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Bar & Tube Fab, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestBarandTubeFab() {

        resourceFile = FileResourceUtil.getResourceAsFile("BasicScenario_BarAndTube.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1010"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, AISI 316")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Stainless Steel, Stock, AISI 316"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestCasting() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("ISO JMB 800-1")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("ISO JMB 800-1"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario(5);

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Brass, Cast, Yellow 270")
            .select()
            .costScenario(5);

        assertThat(evaluatePage.isMaterialInfoDisplayed("Brass, Cast, Yellow 270"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Copper, Cast, UNS C28000")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Copper, Cast, UNS C28000"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Forging, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestForging() {

        resourceFile = FileResourceUtil.getResourceAsFile("BasicScenario_Forging.stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1010"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Titanium, Ti-5Al-2.5Sn")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Titanium, Ti-5Al-2.5Sn"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {

        resourceFile = FileResourceUtil.getResourceAsFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("ABS"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("PET 30% Glass")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("PET 30% Glass"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Powder Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPowderMetal() {

        resourceFile = FileResourceUtil.getResourceAsFile("Powder Metal.stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("F-0005"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("FLN2-4405")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("FLN2-4405"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Rapid Prototyping, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestRapidPrototyping() {

        resourceFile = FileResourceUtil.getResourceAsFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Default"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("870 Black")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("870 Black"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Roto & Blow Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestRotoBlowMolding() {

        resourceFile = FileResourceUtil.getResourceAsFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Polyethylene, High Density (HDPE)"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Nylon, Type 46")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Nylon, Type 46"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetal() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1020"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Inconel 625")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Inconel 625"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal - Hydroforming, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetalHydroforming() {

        resourceFile = FileResourceUtil.getResourceAsFile("Hydroform Sample Banana.stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum, Stock, ANSI 2017"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Copper, Stock, UNS C11000")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Copper, Stock, UNS C11000"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal Transfer Die, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetalTransferDie() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1020"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("C60E")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("C60E"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Plastic, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetPlastic() {

        resourceFile = FileResourceUtil.getResourceAsFile("2038646_Sheet_plastic.prt.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Polyethylene, HDPE Extrusion Sheet"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Acrylic, PMMA")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Acrylic, PMMA"), is(true));
    }

//    @Test
//    @Category(SmokeTests.class)
//    @TestRail(testCaseId = {"864", "866", "867", "889"})
//    @Description("Test making changes to the Material for Stock Machining, the change is respected and the scenario can be cost")
//    public void changeMaterialSelectionTestStockMachining() {
//
//        String scenarioName = new GenerateStringUtil().generateScenarioName();
//        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");
//
//        loginPage = new CidAppLoginPage(driver);
//        evaluatePage = loginPage.login(UserUtil.getUser())
//            .uploadComponentAndOk(scenarioName, resourceFile, EvaluatePage.class)
//            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
//            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
//            .costScenario();
//
//        assertThat(evaluatePage.getMaterialInfo("Steel, Hot Worked, AISI 1010"), is(true));
//
//        evaluatePage.openMaterialSelectorTable()
//            .selectMaterial("Polyetheretherketone (PEEK)")
//            .select()
//            .costScenario()
//            // TODO: 27/10/2020 not fully implemented
//            .publishScenario(PublishPage.class)
//            .selectPublishButton()
//            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
//            .openScenario(scenarioName, "bracket_basic");
//
//        assertThat(evaluatePage.getMaterialInfo("Polyetheretherketone (PEEK)"), is(true));
//    }

    @Test
    @TestRail(testCaseId = {"865"})
    @Description("Test re-selecting same material and the scenario can be recost")
    public void changeMaterialSelectionTestReSelect() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("Polyetheretherketone (PEEK)")
            .select()
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("Polyetheretherketone (PEEK)")
            .selectMaterial("Polyetheretherketone (PEEK)")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"868", "875"})
    @Description("Test de-selecting the material, previous material applied and the scenario can be cost")
    public void changeMaterialSelectionTestDeSelect() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("Polyetheretherketone (PEEK)")
            .select()
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("Polyetheretherketone (PEEK)")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"869"})
    @Description("Test closing and opening Material Properties, information within correct")
    public void changeMaterialSelectionTestMaterialProperties() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("Inconel 625")
            .select()
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo("Inconel 625")));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"884", "888"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMI() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getResourceAsFile("Machined Box AMERICAS.SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .clickExplore()
            .openScenario(scenarioName, "MACHINED BOX AMERICAS");

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1095"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"885"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMINotExist() {

        resourceFile = FileResourceUtil.getResourceAsFile("Machined Box AMERICAS IronCast.SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .selectionMethod("MCAD <material not found - VPE default used>")
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1010"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"905"})
    @Description("Test opening material selection and selecting apply without making a selection")
    public void changeMaterialSelectionTestNoChange() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .openMaterialSelectorTable()
            .select()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1020"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"905"})
    @Description("Test opening material selection and selecting cancel after making a selection")
    public void changeMaterialSelectionTestCancel() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Stainless Steel, Stock, AISI 316")
            .cancel()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1020"), is(true));
    }
}