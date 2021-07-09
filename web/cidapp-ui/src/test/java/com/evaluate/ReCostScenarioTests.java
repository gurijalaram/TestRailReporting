package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ReCostScenarioTests extends TestBase {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;

    public ReCostScenarioTests() {
        super();
    }

    @Test
    @Issue("MIC-3071")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6101"})
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Case_011_-_Team_350385";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Material Stock"));

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @Issue("MIC-3071")
    @TestRail(testCaseId = {"6102"})
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_002_00400016-003M10_A";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STP");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("4 Axis Mill"));

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_CHINA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @Issue("MIC-3071")
    @TestRail(testCaseId = {"6103"})
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "14100640";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @Issue("MIC-3071")
    @TestRail(testCaseId = {"6104"})
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_010_lam_15-435508-00";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Band Saw"));

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @Issue("MIC-3071")
    @TestRail(testCaseId = {"6105"})
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_066_SpaceX_00128711-001_A";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Band Saw"));

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }

    @Test
    @Issue("MIC-3071")
    @TestRail(testCaseId = {"6106"})
    @Description("Test recosting a cad file - Slot Examples")
    public void testRecostSlotExamples() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "case_007_SpaceX_00088481-001_C";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Material Stock"));

        evaluatePage.selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL.getVpe())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_UP_TO_DATE), is(true));
    }
}