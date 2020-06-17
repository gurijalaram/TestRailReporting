package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.pageobjects.pages.evaluate.ComponentsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.inputs.VPESelectionPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.ColumnsEnum;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class AssemblyUploadTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private VPESelectionPage vpeSelectionPage;
    private ComponentsPage componentsPage;

    private File resourceFile;
    private String scenarioName;
    private final String noComponentMessage = "You have no components that match the selected filter";

    public AssemblyUploadTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2628", "2647", "2653"})
    @Description("Assembly File Upload - STEP")
    public void testAssemblyFormatSTEP() {

        resourceFile = new FileResourceUtil().getResourceFile("Piston_assembly.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_INCOMPLETE.getCostingText()), is(true));
        assertThat(evaluatePage.getTotalComponents(), is("4"));
        assertThat(evaluatePage.getUniqueComponents(), is("4"));
        assertThat(evaluatePage.getWarningsCount(), is("4"));
        assertThat(evaluatePage.getCycleTimeCount(), is("0.00"));
    }

    @Test
    @Issue("AP-60916")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2655", "2647", "2643"})
    @Description("Uploaded STEP assembly and components can be recosted")
    public void costAssembly() {

        resourceFile = new FileResourceUtil().getResourceFile("Assembly2.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
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
        assertThat(evaluatePage.getUncostedUnique(), is("0"));
        assertThat(evaluatePage.getFinishMass(), is(closeTo(0.80, 1)));
        assertThat(evaluatePage.getTargetMass(), is("0.00"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2651"})
    @Description("User can delete STEP Assembly Pre-Costing")
    public void testSTEPAssemblyDeletePreCost() {

        resourceFile = new FileResourceUtil().getResourceFile("Piston_assembly.stp");
        scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .delete()
            .deleteScenario()
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Scenario Name", "Contains", scenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Issue("BA-1103")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"2652", "1351", "1353", "1354"})
    @Description("User can delete STEP Assembly Post-Costing")
    public void testSTEPAssemblyDeletePostCost() {

        resourceFile = new FileResourceUtil().getResourceFile("Piston_assembly.stp");
        scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .enterAnnualVolume("3126")
            .enterAnnualYears("9")
            .costScenario();

        assertThat(evaluatePage.getSelectedVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe()), is(true));
        assertThat(evaluatePage.getAnnualVolume(), is("3126"));
        assertThat(evaluatePage.getProductionLife(), is("9"));

        explorePage = evaluatePage.delete()
            .deleteScenario()
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Scenario Name", "Contains", scenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Category({SanityTests.class})
    @TestRail(testCaseId = {"2648", "1352", "1355"})
    @Description("User can cost STEP Assembly with Powder Coat Cart Secondary Processes")
    public void testSTEPAssemblyPowderCoatCart() {

        resourceFile = new FileResourceUtil().getResourceFile("Piston_assembly.stp");
        scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        vpeSelectionPage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
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

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Powder Coat Cart"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1341", "1342", "1402", "1405", "1410"})
    @Description("Validate error message and cost status appears, when assembly cost is out of date")
    public void smallAssembly() {

        resourceFile = new FileResourceUtil().getResourceFile("Hinge assembly.STEP");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
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

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_OUT_OF_DATE.getCostingText()), Matchers.is(true));

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
    @TestRail(testCaseId = {"2655", "2647", "2643"})
    @Description("Uploaded STEP assembly and components can be recosted")
    public void treeViewTests() {

        resourceFile = new FileResourceUtil().getResourceFile("Assembly2.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        componentsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario()
            .openComponentsTable()
            .selectComponentsView("Tree View")
            .expandAssembly(scenarioName, "ASSY02")
            .highlightSubcomponent(scenarioName, "PART0002");

        assertThat(componentsPage.getComponentCell("PART0002", "Qty"), Matchers.is(Matchers.equalTo("2")));
    }
}