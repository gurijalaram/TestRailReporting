package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class NewScenarioNameTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    public NewScenarioNameTests() {
        super();
    }

    @Category(SmokeTests.class)
    @Test
    @TestRail(testCaseId = {"5424"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    public void testEnterNewScenarioName() {

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, "partbody_2.stp");
        String testScenarioName = generateStringUtil.generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .createScenario()
            .enterScenarioName(testScenarioName)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(testScenarioName));
    }

    @Category(SmokeTests.class)
    @Test
    @TestRail(testCaseId = {"5950", "5951", "5952"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, "partbody_2.stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String testNewScenarioName = generateStringUtil.generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.CREATED), is(true));

        evaluatePage.costScenario()
            .publishScenario()
            .publish(ExplorePage.class)
            .highlightScenario("partbody_2", testScenarioName)
            .createScenario()
            .enterScenarioName(testNewScenarioName)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(testNewScenarioName));
    }

    @Category(SmokeTests.class)
    @Test
    @TestRail(testCaseId = {"5953"})
    @Description("Ensure a previously uploaded CAD File of the same name can be uploaded subsequent times with a different scenario name")
    public void multipleUpload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp");
        String scenarioA = generateStringUtil.generateScenarioName();
        String scenarioB = generateStringUtil.generateScenarioName();
        String scenarioC = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioA, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .uploadComponentAndSubmit(scenarioB, FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp"), EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .uploadComponentAndSubmit(scenarioC, FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp"), EvaluatePage.class)
            .inputProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .inputName(filterName)
            .saveAs()
            .addCriteriaWithOption("Component Name", "Contains", "MultiUpload")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioA), equalTo(1));
        assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioB), equalTo(1));
        assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioC), equalTo(1));
    }
}