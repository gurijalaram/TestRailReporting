package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.actions.ScenarioAction;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.utils.WorkOrderRequestEntity;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class DeletePublicScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public DeletePublicScenarioTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"587"})
    @Description("Test a public scenario can be deleted from the component table")
    public void testDeletePublicScenario() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("casting.prt"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filterCriteria()
            .filterPublicCriteria("Part", "Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class)
            .highlightScenario(testScenarioName, "casting");

        explorePage = new ExplorePage(driver);
        explorePage.delete()
            .deleteScenario()
            .openJobQueue()
            .checkJobQueueActionComplete(testScenarioName, "Delete");

        assertThat(explorePage.getListOfScenarios(testScenarioName, "casting") < 1, is(true));
    }

//TEST EXAMPLE OF USE FORCE DELETE SCENARIO
    @Test
    public void testForceDelete() {
        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("casting.prt"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());

        ScenarioAction.forceDelete(
            WorkOrderRequestEntity.defaultRequestByUserEnum(UsersEnum.CID_TE_CFRITH_ALLDATA, testScenarioName)
                .setWorkspace(WorkspaceEnum.PUBLIC_API));

    }
}