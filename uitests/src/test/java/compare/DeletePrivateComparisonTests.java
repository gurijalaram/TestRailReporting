package test.java.compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.header.GenericHeader;
import main.java.pages.compare.ComparePage;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.jobqueue.JobQueuePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class DeletePrivateComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;

    public DeletePrivateComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"433"})
    @Description("Test a private comparison can be deleted from the explore page")
    public void testDeletePrivateScenario() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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
        jobQueuePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .openJobQueue()
            .checkJobQueueActionComplete(testScenarioName, "Delete");

        assertThat(new ExplorePage(driver).getListOfComparisons(testComparisonName) < 1, is(true));
    }

    @Test
    @TestRail(testCaseId = {"430"})
    @Description("Test a private comparison can be deleted from the comparison page")
    public void deletePrivateComparison() {

        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        genericHeader.delete()
            .deleteComparison()
            .openJobQueue()
            .checkJobQueueActionComplete(testComparisonName, "Delete");

        explorePage = new ExplorePage(driver);
        explorePage.selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.getListOfComparisons(testComparisonName) < 1, is(true));
    }
}