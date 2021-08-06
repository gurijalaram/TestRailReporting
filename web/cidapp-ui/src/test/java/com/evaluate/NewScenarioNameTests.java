package com.evaluate;

import static com.apriori.utils.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static com.apriori.utils.enums.ProcessGroupEnum.STOCK_MACHINING;
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
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class NewScenarioNameTests extends TestBase {

    UserCredentials currentUser;
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();
        String testScenarioName = generateStringUtil.generateScenarioName();
        String testScenarioName2 = generateStringUtil.generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .createScenario()
            .enterScenarioName(testScenarioName2)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(testScenarioName2));
    }

    @Category(IgnoreTests.class)
    @Test
    @Ignore("At the moment a new scenario name cannot be created from a public scenario")
    @TestRail(testCaseId = {"5950", "5951", "5952"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.WITHOUT_PG, componentName + ".stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String testNewScenarioName = generateStringUtil.generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED), is(true));

        evaluatePage.selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(componentName)
            .highlightScenario(componentName, testScenarioName)
            .createScenario()
            .enterScenarioName(testNewScenarioName)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.getCurrentScenarioName(), is(testNewScenarioName));
    }

    @Test
    @TestRail(testCaseId = {"5953"})
    @Description("Ensure a previously uploaded CAD File of the same name can be uploaded subsequent times with a different scenario name")
    public void multipleUpload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "MultiUpload";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioA = generateStringUtil.generateScenarioName();
        String scenarioB = generateStringUtil.generateScenarioName();
        String scenarioC = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();


        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioA, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .uploadComponentAndOpen(componentName, scenarioB, resourceFile, currentUser)
            .selectProcessGroup(STOCK_MACHINING)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Hot Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .uploadComponentAndOpen(componentName, scenarioC, resourceFile, currentUser)
            .selectProcessGroup(PLASTIC_MOLDING)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", "MultiUpload")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioA), equalTo(1));
        assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioB), equalTo(1));
        assertThat(explorePage.getListOfScenarios("MultiUpload", scenarioC), equalTo(1));
    }
}