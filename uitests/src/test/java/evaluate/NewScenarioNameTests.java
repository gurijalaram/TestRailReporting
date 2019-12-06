package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.jobqueue.JobQueuePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class NewScenarioNameTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private JobQueuePage jobQueuePage;
    private WarningPage warningPage;

    public NewScenarioNameTests() {
        super();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"577"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    public void testEnterNewScenarioName() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .createNewScenario()
            .enterScenarioName(testScenarioName)
            .save();

        assertThat(evaluatePage.getCurrentScenarioName(testScenarioName), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1576", "1586", "1587", "1589"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {

        String testScenarioName = new Util().getScenarioName();
        String testNewScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("partbody_2.stp"));

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

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1588"})
    @Issue("AP-56845")
    @Description("Ensure a previously uploaded CAD File of the same name can be uploaded subsequent times with a different scenario name")
    public void multipleUpload() {

        String ScenarioA = new Util().getScenarioName();
        String ScenarioB = new Util().getScenarioName();
        String ScenarioC = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());
        explorePage = new ExplorePage(driver);
        explorePage = explorePage.uploadFile(ScenarioA, new FileResourceUtil().getResourceFile("MultiUpload.stp"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .refreshCurrentPage()
            .uploadFile(ScenarioB, new FileResourceUtil().getResourceFile("MultiUpload.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .refreshCurrentPage()
            .uploadFile(ScenarioC, new FileResourceUtil().getResourceFile("MultiUpload.stp"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "MultiUpload")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(ScenarioA, "MultiUpload"), equalTo(1));
        assertThat(explorePage.getListOfScenarios(ScenarioB, "MultiUpload"), equalTo(1));
        assertThat(explorePage.getListOfScenarios(ScenarioC, "MultiUpload"), equalTo(1));
    }
}