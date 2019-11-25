package compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.jobqueue.JobQueuePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;


public class SaveAsComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;

    public SaveAsComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"419"})
    @Description("Test a private comparison can be have Save As performed on it")
    public void testSaveAsPrivateComparison() {

        String scenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();
        String testSaveAsComparisonName = new Util().getComparisonName();
        String testSaveAsComparisonDescription = "Save As Comparison Description";

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .selectScenario(scenarioName, "Push Pin")
            .apply();

        new GenericHeader(driver).saveAs()
            .inputName(testSaveAsComparisonName)
            .inputDescription(testSaveAsComparisonDescription)
            .selectCreateButton();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .openJobQueue()
            .checkJobQueueActionStatus("Initial", "Save Comparison As", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.RECENT.getWorkspace());

        assertThat(explorePage.findComparison(testSaveAsComparisonName).isDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"419"})
    @Description("Test a public comparison can be have Save As performed on it")
    public void testSaveAsPublicComparison() {

        String testComparisonName = new Util().getComparisonName();
        String testSaveAsComparisonName = new Util().getComparisonName();
        String testSaveAsComparisonDescription = "Save As Comparison Description";

        loginPage = new LoginPage(driver);

        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        comparePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .openComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);

        comparePage = genericHeader.saveAs()
            .inputName(testSaveAsComparisonName)
            .inputDescription(testSaveAsComparisonDescription)
            .selectCreateButton();

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.selectExploreButton()
            .openJobQueue()
            .checkJobQueueActionStatus("Initial", "Save Comparison As", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.RECENT.getWorkspace());

        assertThat(explorePage.findComparison(testSaveAsComparisonName).isDisplayed(), is(true));
    }

    @Test
    @Issue("BA-874")
    @TestRail(testCaseId = {"413"})
    @Description("Attempt to create a new comparison with a name that already exists")
    public void comparisonNameExists() {

        String scenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .selectScenario(scenarioName, "Push Pin")
            .apply();

        genericHeader = new GenericHeader(driver);
        jobQueuePage = genericHeader.selectExploreButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ExplorePage.class)
            .openJobQueue();

       assertThat(jobQueuePage.getJobQueueRow("stop"), containsString("Comparison " + testComparisonName + " already exists"));
    }
}