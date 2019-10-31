package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;

public class AssemblyUploadTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public AssemblyUploadTests() {
        super();
    }

    @Test
    @Issue("AP-56584")
    @TestRail(testCaseId = {"2628"})
    @Description("Assembly File Upload - STEP")
    public void testAssemblyFormatSTEP() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Piston_assembly.stp"))
            .selectProcessGroup(AssemblyProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_INCOMPLETE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"2655"})
    @Description("Uploaded STEP assembly and components can be recosted")
    public void costAssembly() {

        String scenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
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
}