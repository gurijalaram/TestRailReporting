package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
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

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public ReCostScenarioTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "6101")
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Case_011_-_Team_350385.prt.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Material Stock"));

        evaluatePage.inputVpe(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @TestRail(testCaseId = "6102")
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_002_00400016-003M10_A.STP");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("4 Axis Mill"));

        evaluatePage.inputVpe(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @TestRail(testCaseId = "6103")
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "14100640.stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("4 Axis Mill"));

        evaluatePage.inputVpe(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @TestRail(testCaseId = "6104")
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_010_lam_15-435508-00.prt.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Band Saw"));

        evaluatePage.inputVpe(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @TestRail(testCaseId = "6105")
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_066_SpaceX_00128711-001_A.stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Band Saw"));

        evaluatePage.inputVpe(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @TestRail(testCaseId = "6106")
    @Description("Test recosting a cad file - Slot Examples")
    public void testRecostSlotExamples() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_007_SpaceX_00088481-001_C.stp");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Material Stock"));

        evaluatePage.inputVpe(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }
}