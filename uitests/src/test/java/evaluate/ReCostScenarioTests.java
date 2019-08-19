package test.java.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class ReCostScenarioTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    public ReCostScenarioTests() {
        super();
    }

    @Test
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Case_001_-_Rockwell_2075-0243G.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "Case_001_-_Rockwell_2075-0243G")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_002_00400016-003M10_A.STP"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "case_002_00400016-003M10_A")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("14100640.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "14100640")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("03229_0032_002_A.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "03229_0032_002_A")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_066_SpaceX_00128711-001_A.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "case_066_SpaceX_00128711-001_A")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @Description("Test recosting a cad file - Slot Examples")
    public void testRecostSlotExamples() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("case_007_SpaceX_00088481-001_C.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "case_007_SpaceX_00088481-001_C")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }
}