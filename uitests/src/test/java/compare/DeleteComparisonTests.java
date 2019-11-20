package compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.jobqueue.JobQueuePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;

import org.junit.Test;


public class DeleteComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;

    private final String noComponentMessage = "You have no components that match the selected filter";

    public DeleteComparisonTests() {
        super();
    }

    @Test
    @Issue("BA-841")
    @TestRail(testCaseId = {"433"})
    @Description("Test a private comparison can be deleted from the explore page")
    public void testDeletePrivateComparisonExplore() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .costScenario()
            .selectExploreButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "Machined Box AMERICAS")
            .apply();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .filterCriteria()
            .filterPrivateCriteria("Comparison", "Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @TestRail(testCaseId = {"430"})
    @Description("Test a private comparison can be deleted from the comparison page")
    public void testDeletePrivateComparison() {

        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
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
    @TestRail(testCaseId = {"430", "432", "442", "448"})
    @Description("Test deleting a public comparison from explore tab")
    @Issue("BA-841")
    public void testPublicComparisonDeleteExplore() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);

        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "Machined Box AMERICAS")
            .apply();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .openJobQueue()
            .checkJobQueueActionStatus("Initial", "Delete", "okay")
            .closeJobQueue(ExplorePage.class)
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @TestRail(testCaseId = {"443"})
    @Description("Delete a public comparison from comparison page")
    @Issue("BA-839")
    public void deletePublicComparisonPage() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);

        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "testpart-4")
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "testpart-4")
            .apply();

        new GenericHeader(driver).publishScenario(PublishPage.class)
            .selectPublishButton()
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
}