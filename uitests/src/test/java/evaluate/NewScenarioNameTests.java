package test.java.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.Util;
import org.junit.Test;

public class NewScenarioNameTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    public NewScenarioNameTests() {
        super();
    }

    @Test
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    public void testEnterNewScenarioName() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .createNewScenario()
            .enterScenarioName(testScenarioName)
            .save();

        assertThat(evaluatePage.getCurrentScenarioName(testScenarioName), is(true));
    }

    @Test
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {

        String testScenarioName = new Util().getScenarioName();
        String testNewScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "partbody_2");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.createNewScenario()
            .enterScenarioName(testNewScenarioName)
            .save();

        assertThat(evaluatePage.getCurrentScenarioName(testNewScenarioName), is(true));
    }
}