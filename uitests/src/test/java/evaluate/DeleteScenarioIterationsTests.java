package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class DeleteScenarioIterationsTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public DeleteScenarioIterationsTests() {
        super();
    }

    @Test
    @Description("Test a public scenario can be deleted from the component table")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePublicScenarioIteration() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePublicScenarioIteration", new FileResourceUtil().getResourceFile("casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("DeletePublicScenarioIteration", "casting");

        explorePage = new ExplorePage(driver);
        explorePage.editScenario()
            .delete()
            .deleteScenarioIteration()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());

        assertThat(explorePage.getListOfScenarios("DeletePublicScenarioIteration", "casting") < 1, is(true));
    }

    @Test
    @Description("Test a public scenario can be deleted from the component table")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePrivateScenarioIteration() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePrivateScenarioIteration", new FileResourceUtil().getResourceFile("casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("DeletePrivateScenarioIteration", "casting");

        explorePage = new ExplorePage(driver);
        explorePage.editScenario()
            .delete()
            .deleteScenarioIteration()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getListOfScenarios("DeletePrivateScenarioIteration", "casting") < 1, is(true));
    }
}