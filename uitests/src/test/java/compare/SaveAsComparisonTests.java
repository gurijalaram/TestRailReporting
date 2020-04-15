package compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.header.PageHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.jobqueue.JobQueuePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
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
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.AdhocTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;


public class SaveAsComparisonTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;
    private PageHeader pageHeader;

    private File resourceFile;

    public SaveAsComparisonTests() {
        super();
    }

    @Test
    @Category({AdhocTests.class, SmokeTests.class})
    @TestRail(testCaseId = {"419"})
    @Description("Test a private comparison can be have Save As performed on it")
    public void testSaveAsPrivateComparison() {

        resourceFile = new FileResourceUtil().getResourceFile("Push Pin.stp");
        String scenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();
        String testSaveAsComparisonName = new Util().getComparisonName();
        String testSaveAsComparisonDescription = "Save As Comparison Description";

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .selectComparisonScenario(scenarioName, "Push Pin")
            .apply(GenericHeader.class)
            .saveAs()
            .inputName(testSaveAsComparisonName)
            .inputDescription(testSaveAsComparisonDescription)
            .selectCreateButton();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testSaveAsComparisonName, "Initial", "Save Comparison As", "okay")
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

        new CIDLoginPage(driver).login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        new GenericHeader(driver).publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .openComparison(testComparisonName);

        new GenericHeader(driver).saveAs()
            .inputName(testSaveAsComparisonName)
            .inputDescription(testSaveAsComparisonDescription)
            .selectCreateButton();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testSaveAsComparisonName, "Initial", "Save Comparison As", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.RECENT.getWorkspace());

        assertThat(explorePage.findComparison(testSaveAsComparisonName).isDisplayed(), is(true));
    }

    @Test
    @Issue("BA-919")
    @TestRail(testCaseId = {"413"})
    @Description("Attempt to create a new comparison with a name that already exists")
    public void comparisonNameExists() {

        resourceFile = new FileResourceUtil().getResourceFile("Push Pin.stp");
        String scenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(scenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        new ComparePage(driver).addScenario()
            .selectComparisonScenario(scenarioName, "Push Pin")
            .apply(GenericHeader.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Set Children to Comparison", "okay")
            .closeJobQueue(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        jobQueuePage = genericHeader.selectExploreButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ExplorePage.class)
            .openJobQueue();

        assertThat(jobQueuePage.getJobQueueIconMessage("stop"), containsString("Comparison " + testComparisonName + " already exists"));
    }
}