package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserDataUtil;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class ReCostScenarioTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public ReCostScenarioTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"578", "584", "598"})
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Case_011_-_Team_350385.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"578", "584", "598"})
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("case_002_00400016-003M10_A.STP"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"578", "584", "598"})
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("14100640.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"578", "584", "598"})
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("case_010_lam_15-435508-00.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"578", "584", "598"})
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("case_066_SpaceX_00128711-001_A.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"578", "584", "598"})
    @Description("Test recosting a cad file - Slot Examples")
    public void testRecostSlotExamples() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("case_007_SpaceX_00088481-001_C.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }
}