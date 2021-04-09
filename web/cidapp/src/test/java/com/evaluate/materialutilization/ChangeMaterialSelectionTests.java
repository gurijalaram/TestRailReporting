package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
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
    @TestRail(testCaseId = "6186")
    @Issue("BA-1758")
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .search("270")
            .selectMaterial("Brass, Cast, Yellow 270")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Brass, Cast, Yellow 270"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6187")
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Aluminum, Cast, ANSI AL380.0"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .search("C28000")
            .selectMaterial("Copper, Cast, UNS C28000")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Copper, Cast, UNS C28000"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6188")
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap DFM.CATPart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("ABS"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .search("PET")
            .selectMaterial("PET 30% Glass")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("PET 30% Glass"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6189")
    @Description("Test making changes to the Material for Sheet Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1020"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .search("625")
            .selectMaterial("Inconel 625")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Inconel 625"), is(true));
    }


    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "6190")
    @Description("Test making changes to the Material for Stock Machining, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestStockMachining() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1010"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Polyetheretherketone (PEEK)")
            .submit()
            .costScenario()
            .publishScenario()
            .publish(ExplorePage.class)
            .openScenario(scenarioName, "bracket_basic");

        assertThat(evaluatePage.isMaterialInfoDisplayed("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6191")
    @Description("Test re-selecting same material and the scenario can be recost")
    public void changeMaterialSelectionTestReSelect() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit()
            .costScenario()
            .openMaterialSelectorTable()
            .search("PEEK")
            .selectMaterial("Polyetheretherketone (PEEK)")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6192")
    @Issue("BA-1758")
    @Description("Test closing and opening Material Properties, information within correct")
    public void changeMaterialSelectionTestMaterialProperties() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario()
            .openMaterialSelectorTable()
            .search("625")
            .selectMaterial("Inconel 625")
            .submit()
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo("Inconel 625")));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "6193")
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMI() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getResourceAsFile("Machined Box AMERICAS.SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .clickExplore()
            .openScenario(scenarioName, "MACHINED BOX AMERICAS");

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1095"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6194")
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMINotExist() {

        resourceFile = FileResourceUtil.getResourceAsFile("Machined Box AMERICAS IronCast.SLDPRT");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .selectionMethod("MCAD <material not found - VPE default used>")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1010"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6195")
    @Description("Test opening material selection and selecting apply without making a selection")
    public void changeMaterialSelectionTestNoChange() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .openMaterialSelectorTable()
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1020"), is(true));
    }

    @Test
    @TestRail(testCaseId = "6196")
    @Issue("BA-1758")
    @Description("Test opening material selection and selecting cancel after making a selection")
    public void changeMaterialSelectionTestCancel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .openMaterialSelectorTable()
            .search("316")
            .selectMaterial("Stainless Steel, Stock, AISI 316")
            .cancel()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Cold Worked, AISI 1020"), is(true));
    }
}