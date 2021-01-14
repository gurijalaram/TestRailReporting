package com.evaluate;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
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

//        assertThat(evaluatePage.getCurrentScenarioName(testScenarioName), is(true));
    }

//    @Category({CustomerSmokeTests.class, SmokeTests.class})
//    @Test
//    @TestRail(testCaseId = {"1576", "1586", "1587", "1589"})
//    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
//    public void testPublishEnterNewScenarioName() {
//
//        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, "partbody_2.stp");
//        String testScenarioName = new GenerateStringUtil().generateScenarioName();
//        String testNewScenarioName = new GenerateStringUtil().generateScenarioName();
//
//        loginPage = new CidAppLoginPage(driver);
//        evaluatePage = loginPage.login(UserUtil.getUser())
//            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class);
//
//        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), is(true));
//
//        evaluatePage.costScenario()
//            .publishScenario(PublishPage.class)
//            .selectPublishButton()
//            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
//            .highlightScenario(testScenarioName, "partbody_2");
//
//        explorePage = new ExplorePage(driver);
//        evaluatePage = explorePage.createNewScenario()
//            .enterScenarioName(testNewScenarioName)
//            .save(EvaluatePage.class);
//
//        assertThat(evaluatePage.getCurrentScenarioName(testNewScenarioName), is(true));
//    }
//
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
//            .publishScenario(PublishPage.class)
//            .selectPublishButton()
//            .refreshCurrentPage()
//            .uploadFileAndOk(scenarioB, FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp"), EvaluatePage.class)
//            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
//            .costScenario()
//            .publishScenario(PublishPage.class)
//            .selectPublishButton()
//            .refreshCurrentPage()
//            .uploadFileAndOk(scenarioC, FileResourceUtil.getCloudFile(processGroupEnum, "MultiUpload.stp"), EvaluatePage.class)
//            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
//            .costScenario()
//            .publishScenario(PublishPage.class)
//            .selectPublishButton()
//            .filter()
//            .setWorkspace("Public")
//            .setScenarioType("Part")
//            .setRowOne("Part Name", "Contains", "MultiUpload")
//            .apply(ExplorePage.class);
//
//        assertThat(explorePage.getListOfScenarios(scenarioA, "MultiUpload"), equalTo(1));
//        assertThat(explorePage.getListOfScenarios(scenarioB, "MultiUpload"), equalTo(1));
//        assertThat(explorePage.getListOfScenarios(scenarioC, "MultiUpload"), equalTo(1));
//    }
}