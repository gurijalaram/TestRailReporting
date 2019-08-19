package test.java.compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
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

import java.time.LocalDateTime;

public class DeletePrivateComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    public DeletePrivateComparisonTests() {
        super();
    }

    @Test
    @Description("Test a private comparison can be deleted from the explore page")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePrivateScenario() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .costScenario()
            .selectExploreButton()
            .createNewComparison()
            .enterComparisonName("DeletePrivateComparisonA")
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ComparisonTablePage.class)
            .selectComparison(testScenarioName, "Machined Box AMERICAS")
            .apply();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison("DeletePrivateComparisonA")
            .delete()
            .deleteExploreComparison();

        assertThat(explorePage.getListOfComparisons("DeletePrivateComparisonA") < 1, is(true));
    }

    @Test
    @TestRail(testCaseId = {"C430"}, tags = {"smoke"})
    @Description("Test a private comparison can be deleted from the comparison page")
    @Severity(SeverityLevel.NORMAL)
    public void deletePrivateComparison() {

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .createNewComparison()
            .enterComparisonName("comparisonScenarioName")
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        genericHeader.delete().deleteComparison();

        explorePage = new ExplorePage(driver);
        explorePage.selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.getListOfComparisons("comparisonScenarioName") < 1, is(true));
    }
}