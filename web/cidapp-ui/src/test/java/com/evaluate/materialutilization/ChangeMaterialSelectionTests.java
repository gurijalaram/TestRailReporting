package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialUtilizationPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.Locale;

public class ChangeMaterialSelectionTests extends TestBase {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private MaterialUtilizationPage materialUtilizationPage;

    public ChangeMaterialSelectionTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6186"})
    @Issue("BA-1758")
    @Description("Test making changes to the Material for Sand Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSandCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"6187"})
    @Description("Test making changes to the Material for Die Casting, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"6188"})
    @Description("Test making changes to the Material for Plastic Molding, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestPlasticMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Plastic moulded cap DFM";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"6189"})
    @Description("Test making changes to the Material for Sheet Metal, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"6190"})
    @Description("Test making changes to the Material for Stock Machining, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestStockMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1010"), is(true));

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Polyetheretherketone (PEEK)")
            .submit()
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .openScenario(componentName, scenarioName);

        assertThat(evaluatePage.isMaterialInfoDisplayed("Polyetheretherketone (PEEK)"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6191"})
    @Description("Test re-selecting same material and the scenario can be recost")
    public void changeMaterialSelectionTestReSelect() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"6192"})
    @Issue("BA-1758")
    @Description("Test closing and opening Material Properties, information within correct")
    public void changeMaterialSelectionTestMaterialProperties() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialUtilizationPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
            .openMaterialProcess()
            .openMaterialUtilizationTab();

        assertThat(materialUtilizationPage.getUtilizationInfo("Name"), is(equalTo("Inconel 625")));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6193"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMI() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "Machined Box AMERICAS";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName, scenarioName);

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1095"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6194"})
    @Description("Test opening a CAD part with material PMI, selecting and costing with MCAD option")
    public void changeMaterialSelectionTestPMINotExist() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "Machined Box AMERICAS IronCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .openMaterialSelectorTable()
            .selectionMethod("MCAD <material not found - VPE default used>")
            .submit()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfoDisplayed("Steel, Hot Worked, AISI 1010"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6195"})
    @Description("Test opening material selection and selecting apply without making a selection")
    public void changeMaterialSelectionTestNoChange() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    @TestRail(testCaseId = {"6196"})
    @Issue("BA-1758")
    @Description("Test opening material selection and selecting cancel after making a selection")
    public void changeMaterialSelectionTestCancel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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