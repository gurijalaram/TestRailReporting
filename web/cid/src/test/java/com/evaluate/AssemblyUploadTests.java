package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColumnsEnum;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.ComponentsPage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.inputs.VPESelectionPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.explore.FileOpenError;
import com.pageobjects.pages.explore.FileUploadPage;
import com.pageobjects.pages.login.CidLoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class AssemblyUploadTests extends TestBase {

    private final String noComponentMessage = "You have no components that match the selected filter";
    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private VPESelectionPage vpeSelectionPage;
    private ComponentsPage componentsPage;
    private File resourceFile;
    private File resourceFile2;
    private File resourceFile3;
    private File resourceFile4;
    private String scenarioName;
    private FileUploadPage fileUploadPage;

    public AssemblyUploadTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2628", "2647", "2653"})
    @Description("Assembly File Upload - STEP")
    public void testAssemblyFormatSTEP() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_INCOMPLETE.getCostingText()), is(true));
        assertThat(evaluatePage.getTotalComponents(), is("4"));
        assertThat(evaluatePage.getUniqueComponents(), is("4"));
        assertThat(evaluatePage.getWarningsCount(), is("4"));
        assertThat(evaluatePage.getCycleTimeCount(), is(0.0));
    }

    @Test
    @Issue("AP-60916")
    @TestRail(testCaseId = {"2655", "2647", "2643"})
    @Description("Uploaded STEP assembly and components can be recosted")
    public void costAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Assembly2.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openScenario(scenarioName, "PART0001")
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "PART0002")
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "PART0003")
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "PART0004")
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "PART0005A")
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "PART0005B")
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openAssembly(scenarioName, "ASSY03A")
            .costScenario()
            .selectExploreButton()
            .openAssembly(scenarioName, "ASSY03")
            .costScenario()
            .selectExploreButton()
            .openAssembly(scenarioName, "ASSY02")
            .costScenario()
            .selectExploreButton()
            .openAssembly(scenarioName, "ASSEMBLY01")
            .costScenario()
            .selectExploreButton()
            .openAssembly(scenarioName, "ASSEMBLY2")
            .costScenario();

        assertThat(evaluatePage.getTotalComponents(), is("22"));
        assertThat(evaluatePage.getUniqueComponents(), is("10"));
        assertThat(evaluatePage.isUncostedUnique("0"), is(true));
        assertThat(evaluatePage.getFinishMass(), is(closeTo(0.80, 1)));
        assertThat(evaluatePage.getTargetMass(), is("0.00"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2651"})
    @Description("User can delete STEP Assembly Pre-Costing")
    public void testSTEPAssemblyDeletePreCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .delete()
            .deleteScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Assembly")
            .setRowOne("Scenario Name", "Contains", scenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Issue("BA-1103")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2652", "1351", "1353", "1354"})
    @Description("User can delete STEP Assembly Post-Costing")
    public void testSTEPAssemblyDeletePostCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .enterAnnualVolume("3126")
            .enterAnnualYears("9")
            .costScenario();

        assertThat(evaluatePage.getSelectedVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe()), is(true));
        assertThat(evaluatePage.getAnnualVolume(), is("3,126"));
        assertThat(evaluatePage.getProductionLife(), is("9.00"));

        explorePage = evaluatePage.delete()
            .deleteScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Assembly")
            .setRowOne("Scenario Name", "Contains", scenarioName)
            .setRowTwo("Part Name", "Contains", "Piston_assembly")
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Category({SanityTests.class})
    @TestRail(testCaseId = {"2648", "1352", "1355"})
    @Description("User can cost STEP Assembly with Powder Coat Cart Secondary Processes")
    public void testSTEPAssemblyPowderCoatCart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        vpeSelectionPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .openSecondaryProcess()
            .selectSecondaryProcess("Surface Treatment, Paint", "Powder Coat Cart")
            .apply()
            .openMoreInputs()
            .selectVPEButton()
            .saveChanges()
            .closePanel()
            .costScenario()
            .openMoreInputs()
            .selectVPEButton();

        assertThat(vpeSelectionPage.isUsePrimaryVPESelected("checked"), is("true"));

        evaluatePage = new VPESelectionPage(driver).close()
            .closePanel();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Powder Coat Cart"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1341", "1342", "1402", "1405", "1410"})
    @Description("Validate error message and cost status appears, when assembly cost is out of date")
    public void smallAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Hinge assembly.STEP");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openScenario(scenarioName, "SMALL RING")
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "BIG RING")
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openScenario(scenarioName, "PIN")
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .openAssembly(scenarioName, "Hinge assembly");

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_OUT_OF_DATE.getCostingText()), is(true));

        componentsPage = evaluatePage.clickCostStatus(ComponentsPage.class)
            .selectComponentsView("Tree View")
            .openColumnsTable()
            .addColumn(ColumnsEnum.PIECE_PART_COST.getColumns())
            .selectSaveButton();

        assertThat(componentsPage.getColumnHeaderNames(), hasItems(ColumnsEnum.PIECE_PART_COST.getColumns(), ColumnsEnum.PROCESS_GROUP.getColumns()));

        componentsPage.openColumnsTable()
            .removeColumn(ColumnsEnum.PIECE_PART_COST.getColumns())
            .selectSaveButton();
    }

    @Test
    @TestRail(testCaseId = {"1404", "1434", "1435"})
    @Description("Validate quantity column is correct")
    public void treeViewTests() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Assembly2.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        componentsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openComponentsTable()
            .selectComponentsView("Tree View")
            .expandAssembly(scenarioName, "ASSY02")
            .highlightSubcomponent(scenarioName, "PART0002");

        assertThat(componentsPage.getComponentCell("PART0002", "Qty"), is(equalTo("2")));

        evaluatePage = componentsPage.openSubcomponent(scenarioName, "PART0002")
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getPartCost(), is(closeTo(1.02, 1)));

        componentsPage = evaluatePage.selectExploreButton()
            .openAssembly(scenarioName, "Assembly2")
            .openComponentsTable()
            .selectComponentsView("Tree View")
            .openColumnsTable()
            .addColumn(ColumnsEnum.PIECE_PART_COST.getColumns())
            .selectSaveButton()
            .expandAssembly(scenarioName, "ASSY02");

        assertThat(componentsPage.getComponentCell("PART0002", "Piece Part Cost (USD)"), is(equalTo("2.05")));

        componentsPage.openColumnsTable()
            .removeColumn(ColumnsEnum.PIECE_PART_COST.getColumns())
            .selectSaveButton();
    }

    @Test
    @TestRail(testCaseId = {"2637"})
    @Description("Ensure CAD based assemblies are prevented")
    public void testFailCADAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "multidiscclutch.asm.6");

        loginPage = new CidLoginPage(driver);
        fileUploadPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile);

        assertThat(new FileOpenError(driver).getErrorText(), containsString("The selected file type is not supported"));
    }

    @Test
    @TestRail(testCaseId = {"2631", "2632"})
    @Description("Upload multiple STEP assemblies and parts at once")
    public void multiAssyUpload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Assembly2.stp");
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        resourceFile3 = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        resourceFile4 = FileResourceUtil.getCloudFile(processGroupEnum, "CastedPart.CATPart");

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, new File(resourceFile + "\n" + resourceFile2 + "\n" + resourceFile3 + "\n" + resourceFile4), ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("Assembly2", scenarioName, "Translate", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.RECENT.getWorkspace())
            .sortColumnDescending(ColumnsEnum.LAST_SAVED.getColumns());

        assertThat(explorePage.getListOfAssemblies(scenarioName, "ASSEMBLY2"), is(CoreMatchers.equalTo(1)));
    }
}