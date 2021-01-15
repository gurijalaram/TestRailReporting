package com.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class NewScenarioNameTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public NewScenarioNameTests() {
        super();
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"577"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    public void testEnterNewScenarioName() {

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, "partbody_2.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .createScenario()
            .enterScenarioName(testScenarioName)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(testScenarioName));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1576", "1586", "1587", "1589"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, "partbody_2.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testNewScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), is(true));

        evaluatePage.costScenario()
            .publishScenario()
            .publish(ExplorePage.class)
            .highlightComponent(testScenarioName, "partbody_2")
            .createScenario()
            .enterScenarioName(testNewScenarioName)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(testNewScenarioName));
    }

//    @Category({CustomerSmokeTests.class, SmokeTests.class})
//    @Test
//    @TestRail(testCaseId = {"1588"})
//    @Description("Ensure a previously uploaded CAD File of the same name can be uploaded subsequent times with a different scenario name")
//    public void multipleUpload() {
//        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
//
//        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp");
//        String scenarioA = new GenerateStringUtil().generateScenarioName();
//        String scenarioB = new GenerateStringUtil().generateScenarioName();
//        String scenarioC = new GenerateStringUtil().generateScenarioName();
//
//        loginPage = new CidAppLoginPage(driver);
//        explorePage = loginPage.login(UserUtil.getUser())
//            .uploadComponentAndSubmit(scenarioA, resourceFile, EvaluatePage.class)
//            .selectProcessGroup(processGroupEnum.getProcessGroup())
//            .costScenario()
//            .publishScenario()
//            .publish(EvaluatePage.class)
//            .uploadComponentAndSubmit(scenarioB, FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp"), EvaluatePage.class)
//            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
//            .costScenario()
//            .publishScenario()
//            .publish(EvaluatePage.class)
//            .uploadComponentAndSubmit(scenarioC, FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp"), EvaluatePage.class)
//            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
//            .costScenario()
//            .publishScenario()
//            .publish(ExplorePage.class);
//            .filter()
//            .setWorkspace("Public")
//            .setScenarioType("Part")
//            .setRowOne("Part Name", "Contains", "MultiUpload")
//            .apply(ExplorePage.class);

//        assertThat(explorePage.getListOfComponents(scenarioA, "MultiUpload"), equalTo(1));
//        assertThat(explorePage.getListOfComponents(scenarioB, "MultiUpload"), equalTo(1));
//        assertThat(explorePage.getListOfComponents(scenarioC, "MultiUpload"), equalTo(1));
//    }
}