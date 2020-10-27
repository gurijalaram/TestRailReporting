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
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .select()
            .costScenario(5);

        assertThat(evaluatePage.getMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Brass, Cast, Yellow 270")
            .select()
            .costScenario(5);

        assertThat(evaluatePage.getMaterialInfo("Brass, Cast, Yellow 270"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Copper, Cast, UNS C28000")
            .select()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo("Copper, Cast, UNS C28000"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("ABS"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("PET 30% Glass")
            .select()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo("PET 30% Glass"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("Steel, Cold Worked, AISI 1020"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Inconel 625")
            .select()
            .costScenario();

        assertThat(evaluatePage.getMaterialInfo("Inconel 625"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("Polyetheretherketone (PEEK)"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("Polyetheretherketone (PEEK)"), is(true));
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

        assertThat(materialUtilizationPage.getMaterialInfo("Name"), is(equalTo("Inconel 625")));
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

        assertThat(evaluatePage.getMaterialInfo("Steel, Hot Worked, AISI 1095"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("Steel, Hot Worked, AISI 1010"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("Steel, Cold Worked, AISI 1020"), is(true));
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

        assertThat(evaluatePage.getMaterialInfo("Steel, Cold Worked, AISI 1020"), is(true));
    }
}