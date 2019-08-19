package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class PublishNewCostedTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public PublishNewCostedTests() {
        super();
    }

    @Test
    @Description("Edit & publish an existing unlocked scenario from the Public Workspace back to the Public Workspace")
    public void testPublishNewCostedScenario() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario();

        assertThat(explorePage.findComparison(testScenarioName).isDisplayed(), is(true));
    }
}