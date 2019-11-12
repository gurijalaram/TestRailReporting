package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.DeletePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class AssemblyUploadTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private DeletePage deletePage;

    private String scenarioName;
    private final String noComponentMessage = "You have no components that match the selected filter";

    public AssemblyUploadTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"2628", "2647", "2653"})
    @Description("Assembly File Upload - STEP")
    public void testAssemblyFormatSTEP() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Piston_assembly.stp"))
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_INCOMPLETE.getCostingText()), is(true));
        assertThat(evaluatePage.isTotalComponents("4"), is(true));
        assertThat(evaluatePage.isUniqueComponents("4"), is(true));
        assertThat(evaluatePage.getWarningsCount("4"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("0.00"));
    }

    @Test
    @TestRail(testCaseId = {"2655", "2647", "2643"})
    @Description("Uploaded STEP assembly and components can be recosted")
    public void costAssembly() {

        String scenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Assembly2.stp"))
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
            .openAssembly(scenarioName, "Assembly2")
            .costScenario();

        assertThat(evaluatePage.isTotalComponents("22"), is(true));
        assertThat(evaluatePage.isUniqueComponents("10"), is(true));
        assertThat(evaluatePage.isUncostedUnique("0"), is(true));
        assertThat(evaluatePage.isFinishMass("0.80"), is(true));
        assertThat(evaluatePage.isTargetMass("0.00"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"2651"})
    @Description("User can delete STEP Assembly Pre-Costing")
    public void testSTEPAssemblyDeletePreCost() {

        scenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Piston_assembly.stp"))
            .delete()
            .deleteScenario()
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Scenario Name", "Contains", scenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @TestRail(testCaseId = {"2652"})
    @Description("User can delete STEP Assembly Post-Costing")
    public void testSTEPAssemblyDeletePostCost() {

        scenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Piston_assembly.stp"))
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario()
            .delete()
            .deleteScenario()
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Scenario Name", "Contains", scenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }
}