package test.java.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.constants.Constants;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

public class ReCostScenarioTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public ReCostScenarioTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"C578, C584,C598"}, tags = {"smoke"})
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("Case_011_-_Team_350385.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @TestRail(testCaseId = {"C578, C584,C598"}, tags = {"smoke"})
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("case_002_00400016-003M10_A.STP"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @TestRail(testCaseId = {"C578, C584,C598"}, tags = {"smoke"})
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("14100640.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @TestRail(testCaseId = {"C578, C584,C598"}, tags = {"smoke"})
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {

        String testScenarioName = Constants.scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("case_010_lam_15-435508-00.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @TestRail(testCaseId = {"C578, C584,C598"}, tags = {"smoke"})
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("case_066_SpaceX_00128711-001_A.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }

    @Test
    @TestRail(testCaseId = {"C578, C584,C598"}, tags = {"smoke"})
    @Description("Test recosting a cad file - Slot Examples")
    public void testRecostSlotExamples() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("case_007_SpaceX_00088481-001_C.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(), is(equalTo(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())));
    }
}