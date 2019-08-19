package test.java.compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.header.GenericHeader;
import main.java.pages.compare.ComparePage;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

import java.time.LocalDateTime;

public class DeletePublicComparisonTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private EvaluatePage evaluatePage;

    public DeletePublicComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"C430, C442"}, tags = {"smoke"})
    @Description("Test deleting a public comparison from explore tab")
    public void testPublicComparisonDelete() {
        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .costScenario()
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("DeletePublicComparison1")
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
            .highlightComparison("DeletePublicComparison1")
            .delete()
            .deleteExploreComparison();

        assertThat(explorePage.getListOfComparisons("DeletePublicComparison1") < 1, is(true));
    }

    @Test
    @TestRail(testCaseId = {"C443"}, tags = {"smoke"})
    @Description("Delete a public comparison from comparison page")
    @Severity(SeverityLevel.NORMAL)
    public void deletePublicComparisonPage() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .costScenario()
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("DeletePublicComparisonTest")
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
            .filterPublicCriteria("Comparison", "Part Name", "Contains", "DeletePublicComparisonTest")
            .apply(ExplorePage.class)
            .openComparison("DeletePublicComparisonTest");

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.delete().deleteComparison()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.getListOfComparisons("DeletePublicComparisonTest") < 1, is(true));
    }
}
