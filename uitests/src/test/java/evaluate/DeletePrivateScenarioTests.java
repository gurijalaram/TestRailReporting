package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class DeletePrivateScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public DeletePrivateScenarioTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"393", "587"})
    @Description("Test a private scenario can be deleted from the component table")
    public void testDeletePrivateScenario() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("casting.prt"))
            .selectExploreButton()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class)
            .highlightScenario(testScenarioName, "casting");

        explorePage = new ExplorePage(driver);
        explorePage.delete()
            .deleteScenario()
            .openJobQueue()
            .checkJobQueueActionComplete(testScenarioName, "Delete");

        assertThat(explorePage.getListOfScenarios(testScenarioName, "casting") < 1, is(true));
    }
}