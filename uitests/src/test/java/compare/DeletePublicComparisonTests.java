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
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class DeletePublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public DeletePublicComparisonTests() {
        super();
    }

    @Test
    @Description("Test deleting a public comparison from the comparison table is not visible")
    @Severity(SeverityLevel.CRITICAL)
    public void testPublicComparisonDelete() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("PublicComparisonDelete", new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("PublicComparisonDelete1")
            .save()
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "DTCCASTINGISSUES")
            .apply(ComparisonTablePage.class)
            .selectComparison("Scenario b", "DTCCASTINGISSUES")
            .apply();

        explorePage = new ExplorePage(driver);
        explorePage.selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison("PublicComparisonDelete1")
            .delete()
            .deleteScenario();

        assertThat(explorePage.getListOfComparisons("PublicComparisonDelete1") < 1, is(true));
    }
}
