package compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.compare.ComparePage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.jobqueue.JobQueuePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.toolbars.GenericHeader;
import pageobjects.toolbars.PageHeader;
import testsuites.suiteinterface.AdhocTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;


public class SaveAsComparisonTests extends TestBase {

    private CidLoginPage loginPage;
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testSaveAsComparisonName = new GenerateStringUtil().generateComparisonName();
        String testSaveAsComparisonDescription = "Save As Comparison Description";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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

        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testSaveAsComparisonName = new GenerateStringUtil().generateComparisonName();
        String testSaveAsComparisonDescription = "Save As Comparison Description";

        new CidLoginPage(driver).login(UserUtil.getUser())
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
    @TestRail(testCaseId = {"413"})
    @Description("Attempt to create a new comparison with a name that already exists")
    public void comparisonNameExists() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
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