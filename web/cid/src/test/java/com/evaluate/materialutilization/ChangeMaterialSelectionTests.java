package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.PublishPage;
import com.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.pageobjects.pages.login.CidLoginPage;
import com.testsuites.suiteinterface.SmokeTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

public class ChangeMaterialSelectionTests extends TestBase {

    private CidLoginPage loginPage;
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_Additive.prt.1");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Aluminum AlSi10Mg"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Duraform ProX GF")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Duraform ProX GF"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Bar & Tube Fab, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestBarandTubeFab() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_BarAndTube.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Hot Worked, AISI 1010"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, AISI 316")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Stainless Steel, Stock, AISI 316"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Aluminum, Cast, ANSI AL380.0"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("ISO JMB 800-1")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("ISO JMB 800-1"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario(5);

        assertThat(evaluatePage.getMaterialInfo(), is("Aluminum, Cast, ANSI AL380.0"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Brass, Cast, Yellow 270")
            .apply()
            .costScenario(5);

        assertThat(evaluatePage.getMaterialInfo(), is("Brass, Cast, Yellow 270"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Aluminum, Cast, ANSI AL380.0"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Copper, Cast, UNS C28000")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Copper, Cast, UNS C28000"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Forging, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_Forging.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Cold Worked, AISI 1010"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Titanium, Ti-5Al-2.5Sn")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Titanium, Ti-5Al-2.5Sn"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap DFM.CATPart");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("ABS"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("PET 30% Glass")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("PET 30% Glass"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Powder Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPowderMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Powder Metal.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("F-0005"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("FLN2-4405")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("FLN2-4405"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Rapid Prototyping, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestRapidPrototyping() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap DFM.CATPart");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Default"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("870 Black")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("870 Black"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Roto & Blow Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestRotoBlowMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ROTO_BLOW_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap DFM.CATPart");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Polyethylene, High Density (HDPE)"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Nylon, Type 46")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Nylon, Type 46"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Cold Worked, AISI 1020"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Inconel 625")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Inconel 625"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal - Hydroforming, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetalHydroforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_HYDROFORMING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Hydroform Sample Banana.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Aluminum, Stock, ANSI 2017"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Copper, Stock, UNS C11000")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Copper, Stock, UNS C11000"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Metal Transfer Die, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetalTransferDie() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Cold Worked, AISI 1020"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("C60E")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("C60E"));
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sheet Plastic, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetPlastic() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "2038646_Sheet_plastic.prt.1");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Polyethylene, HDPE Extrusion Sheet"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Acrylic, PMMA")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Acrylic, PMMA"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"864", "866", "867", "889"})
    @Description("Test making changes to the Material for Stock Machining, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestStockMachining() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Hot Worked, AISI 1010"));

        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(scenarioName, "bracket_basic");

        assertThat(evaluatePage.getMaterialInfo(), is("Polyetheretherketone (PEEK)"));
    }

    @Test
    @TestRail(testCaseId = {"865"})
    @Description("Test re-selecting same material and the scenario can be recost")
    public void changeMaterialSelectionTestReSelect() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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

        assertThat(evaluatePage.getMaterialInfo(), is("Polyetheretherketone (PEEK)"));
    }

    @Test
    @TestRail(testCaseId = {"868", "875"})
    @Description("Test de-selecting the material, previous material applied and the scenario can be cost")
    public void changeMaterialSelectionTestDeSelect() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyetheretherketone (PEEK)")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Polyetheretherketone (PEEK)"));
    }

    @Test
    @TestRail(testCaseId = {"869"})
    @Description("Test closing and opening Material Properties, information within correct")
    public void changeMaterialSelectionTestMaterialProperties() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machined Box AMERICAS.SLDPRT");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "MACHINED BOX AMERICAS");

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Hot Worked, AISI 1095"));
    }

    @Test
    @TestRail(testCaseId = {"885"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMINotExist() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machined Box AMERICAS IronCast.SLDPRT");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialCompositionTable()
            .method("MCAD <material not found - VPE default used>")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Hot Worked, AISI 1010"));
    }

    @Test
    @TestRail(testCaseId = {"905"})
    @Description("Test opening material selection and selecting apply without making a selection")
    public void changeMaterialSelectionTestNoChange() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialCompositionTable()
            .apply()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Cold Worked, AISI 1020"));
    }

    @Test
    @TestRail(testCaseId = {"905"})
    @Description("Test opening material selection and selecting cancel after making a selection")
    public void changeMaterialSelectionTestCancel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Stainless Steel, Stock, AISI 316")
            .cancel()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo(), is("Steel, Cold Worked, AISI 1020"));
    }
}