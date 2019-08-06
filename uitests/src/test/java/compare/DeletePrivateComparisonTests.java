package test.java.compare;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class DeletePrivateComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public DeletePrivateComparisonTests() {
        super();
    }

    @Test
    @Description("Test a private comparison can be deleted from the comparison page")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePrivateScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.createNewComparison()
            .enterComparisonName("DeletePrivateComparison")
            .save()
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "DTCCASTINGISSUES")
            .apply(ComparisonTablePage.class)
            .selectComparison("Scenario b", "DTCCASTINGISSUES")
            .apply();

        explorePage = new ExplorePage(driver);
        explorePage.delete()
            .deleteComparison()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.getListOfComparisons("DeletePrivateComparison11") < 1, is(true));
    }
}