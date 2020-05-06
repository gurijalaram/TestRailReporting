package compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.common.ScenarioTablePage;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.GenericHeader;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;


public class DeleteComparisonTests extends TestBase {

    private final String noComponentMessage = "You have no components that match the selected filter";
    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private ScenarioTablePage scenarioTablePage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public DeleteComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"433"})
    @Description("Test a private comparison can be deleted from the explore page")
    public void testDeletePrivateComparisonExplore() {

        resourceFile = new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT");
        String testScenarioName = new Util().generateScenarioName();
        String testComparisonName = new Util().generateComparisonName();

        new CIDLoginPage(driver).login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ScenarioTablePage.class);

        scenarioTablePage = new ScenarioTablePage(driver);

        new ScenarioTablePage(driver).selectComparisonScenario(testScenarioName, "Machined Box AMERICAS")
            .apply(GenericHeader.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Set Children to Comparison", "okay")
            .closeJobQueue(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .filterCriteria()
            .filterPrivateCriteria("Comparison", "Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(new ExplorePage(driver).getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"430"})
    @Description("Test a private comparison can be deleted from the comparison page")
    public void testDeletePrivateComparison() {

        String testComparisonName = new Util().generateComparisonName();

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.delete()
            .deleteComparison()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"3838", "430", "432", "442", "448"})
    @Description("Test deleting a public comparison from explore tab")
    public void testPublicComparisonDeleteExplore() {

        resourceFile = new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT");
        String testScenarioName = new Util().generateScenarioName();
        String testComparisonName = new Util().generateComparisonName();

        loginPage = new CIDLoginPage(driver);

        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-critical-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Critical"));

        explorePage = evaluatePage.publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "MACHINED BOX AMERICAS")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Delete", "okay")
            .closeJobQueue(ExplorePage.class)
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"443"})
    @Description("Delete a public comparison from comparison page")
    public void deletePublicComparisonPage() {

        resourceFile = new FileResourceUtil().getResourceFile("testpart-4.prt");
        String testScenarioName = new Util().generateScenarioName();
        String testComparisonName = new Util().generateComparisonName();

        loginPage = new CIDLoginPage(driver);

        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "testpart-4")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "testpart-4")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class)
            .openComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.delete()
            .deleteComparison()
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @TestRail(testCaseId = {"431"})
    @Description("In comparison view, the user can delete the currently open comparison and any matching public or private comparisons")
    public void deletePublicPrivateComparison() {

        resourceFile = new FileResourceUtil().getResourceFile("testpart-4.prt");
        String testScenarioName = new Util().generateScenarioName();
        String testComparisonName = new Util().generateComparisonName();

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "testpart-4")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "testpart-4")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .editScenario(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.delete()
            .selectIterationsCheckbox()
            .deleteComparison()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Delete", "okay")
            .closeJobQueue(ExplorePage.class)
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), containsString("You have no components that match the selected filter"));
    }
}