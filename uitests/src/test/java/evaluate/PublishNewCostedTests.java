package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class PublishNewCostedTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public PublishNewCostedTests() {
        super();
    }

    @Test
    @Description("Publish a new scenario from the Private Workspace to the Public Workspace")
    @TestRail(testCaseId = {"386", "388"})
    public void testPublishNewCostedScenario() {

        String testScenarioName = new Util().getScenarioName();
        String partName = "Testpart-4";

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile(partName + ".prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario();

        assertThat(explorePage.findScenario(testScenarioName,partName).isDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"400", "401", "402", "404"})
    @Description("Publish a part and add an assignee, cost maturity and status")
    public void testPublishWithStatus() {

        String testScenarioName = new Util().getScenarioName();
        String partName = "Testpart-4";

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile(partName + ".prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario("Analysis", "Low", "Abe Chaves")
            .selectPublishButton();

        assertThat(explorePage.findScenario(testScenarioName, partName).isDisplayed(), is(true));
    }
}