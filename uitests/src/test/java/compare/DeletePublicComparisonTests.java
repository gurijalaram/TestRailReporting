package test.java.compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.constants.Constants;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.header.GenericHeader;
import main.java.pages.compare.ComparePage;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

public class DeletePublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;

    public DeletePublicComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"430", "442"})
    @Description("Test deleting a public comparison from explore tab")
    public void testPublicComparisonDelete() {

        String testScenarioName = Constants.scenarioName;
        String testComparisonName = Constants.comparisonName;

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
        explorePage = genericHeader.publishScenario()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison();

        assertThat(explorePage.getListOfComparisons(testComparisonName) < 1, is(true));
    }

    @Test
    @TestRail(testCaseId = {"443"})
    @Description("Delete a public comparison from comparison page")
    @Severity(SeverityLevel.NORMAL)
    public void deletePublicComparisonPage() {

        String testScenarioName = Constants.scenarioName;
        String testComparisonName = Constants.comparisonName;

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
        explorePage = genericHeader.delete().deleteComparison()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.getListOfComparisons(testComparisonName) < 1, is(true));
    }
}