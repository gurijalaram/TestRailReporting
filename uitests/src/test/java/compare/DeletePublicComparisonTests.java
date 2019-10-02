package compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import pages.jobqueue.JobQueuePage;

public class DeletePublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;

    public DeletePublicComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"430", "432", "442", "448"})
    @Description("Test deleting a public comparison from explore tab")
    @Issue("AP-56464")
    public void testPublicComparisonDelete() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .costScenario()
            .publishScenario()
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
        jobQueuePage = genericHeader.publishScenario()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .openJobQueue()
            .checkJobQueueActionComplete(testScenarioName, "Delete");

        assertThat(new ExplorePage(driver).getListOfComparisons(testComparisonName) < 1, is(true));
    }

    @Test
    @TestRail(testCaseId = {"443"})
    @Description("Delete a public comparison from comparison page")
    @Issue("AP-56464")
    public void deletePublicComparisonPage() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .costScenario()
            .publishScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "testpart-4")
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "testpart-4")
            .apply();

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario()
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class)
            .openComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        jobQueuePage = genericHeader.delete()
            .deleteComparison()
            .openJobQueue()
            .checkJobQueueActionComplete(testScenarioName, "Delete");

        explorePage = new ExplorePage(driver);
        explorePage.selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.getListOfComparisons(testComparisonName) < 1, is(true));
    }
}