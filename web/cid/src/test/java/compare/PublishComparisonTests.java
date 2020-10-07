package compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.common.ScenarioTablePage;
import pageobjects.pages.compare.ComparePage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.toolbars.GenericHeader;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishComparisonTests extends TestBase {

    private CidLoginPage loginPage;
    private ComparePage comparePage;
    private ExplorePage explorePage;
    private GenericHeader genericHeader;

    private File resourceFile;

    public PublishComparisonTests() {
        super();
    }

    @Test
    @Issue("AP-61539")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"421", "434"})
    @Description("Test a private comparison can be published from comparison page")
    public void testPublishComparisonComparePage() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Casting")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "Casting")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .openComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.toggleLock()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class);

        assertThat(comparePage.isComparisonLockStatus("lock"), is(true));

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.toggleLock()
            .openJobQueue()
            .checkJobQueueRow("okay")
            .closeJobQueue(ComparePage.class);

        assertThat(comparePage.isComparisonLockStatus("unlock"), is(true));
    }


    @Test
    @Issue("AP-61539")
    @Category({SanityTests.class})
    @TestRail(testCaseId = {"421"})
    @Description("Test a private comparison can be published from explore page")
    public void testPublishComparisonExplorePage() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Casting")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "CASTING")
            .apply(GenericHeader.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Set Children to Comparison", "okay")
            .closeJobQueue(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfComparisons(testComparisonName), is(equalTo(1)));
    }
}