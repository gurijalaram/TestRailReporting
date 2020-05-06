package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateNameUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class NewScenarioNameTests extends TestBase {

    private CIDLoginPage loginPage;
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

        resourceFile = new FileResourceUtil().getResourceFile("partbody_2.stp");
        String testScenarioName = new GenerateNameUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateNameUtil().generateScenarioName(), resourceFile)
            .createNewScenario()
            .enterScenarioName(testScenarioName)
            .save();

        assertThat(evaluatePage.getCurrentScenarioName(testScenarioName), is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1576", "1586", "1587", "1589"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {

        resourceFile = new FileResourceUtil().getResourceFile("partbody_2.stp");
        String testScenarioName = new GenerateNameUtil().generateScenarioName();
        String testNewScenarioName = new GenerateNameUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile(testScenarioName, resourceFile);

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), CoreMatchers.is(true));

        evaluatePage.costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "partbody_2");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.createNewScenario()
            .enterScenarioName(testNewScenarioName)
            .save();

        assertThat(evaluatePage.getCurrentScenarioName(testNewScenarioName), is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1588"})
    @Description("Ensure a previously uploaded CAD File of the same name can be uploaded subsequent times with a different scenario name")
    public void multipleUpload() {

        resourceFile = new FileResourceUtil().getResourceFile("MultiUpload.stp");
        String scenarioA = new GenerateNameUtil().generateScenarioName();
        String scenarioB = new GenerateNameUtil().generateScenarioName();
        String scenarioC = new GenerateNameUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());
        explorePage = new ExplorePage(driver);
        explorePage = explorePage.uploadFile(scenarioA, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .refreshCurrentPage()
            .uploadFile(scenarioB, new FileResourceUtil().getResourceFile("MultiUpload.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .refreshCurrentPage()
            .uploadFile(scenarioC, new FileResourceUtil().getResourceFile("MultiUpload.stp"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "MultiUpload")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(scenarioA, "MultiUpload"), equalTo(1));
        assertThat(explorePage.getListOfScenarios(scenarioB, "MultiUpload"), equalTo(1));
        assertThat(explorePage.getListOfScenarios(scenarioC, "MultiUpload"), equalTo(1));
    }
}