package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateNameUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ReCostScenarioTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public ReCostScenarioTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"578", "584", "598"})
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {

        resourceFile = new FileResourceUtil().getResourceFile("Case_011_-_Team_350385.prt.1");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
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

        resourceFile = new FileResourceUtil().getResourceFile("case_002_00400016-003M10_A.STP");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
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

        resourceFile = new FileResourceUtil().getResourceFile("14100640.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
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


        resourceFile = new FileResourceUtil().getResourceFile("case_010_lam_15-435508-00.prt.1");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
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


        resourceFile = new FileResourceUtil().getResourceFile("case_066_SpaceX_00128711-001_A.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
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


        resourceFile = new FileResourceUtil().getResourceFile("case_007_SpaceX_00088481-001_C.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }
}