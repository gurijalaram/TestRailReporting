package test.java.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

import java.time.LocalDateTime;

public class RevertScenarioTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public RevertScenarioTests() {
        super();
    }

    @Test
    @Description("Test revert saved scenario")
    @TestRail(testCaseId = ("{C585}"))
    public void testRevertSavedScenario() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectProcessGroup(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .revert()
            .revertScenario(EvaluatePage.class);

        assertThat(evaluatePage.getSelectedProcessGroup(), is(equalTo(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())));
    }

    @Test
    @Description("Test revert unsaved scenario")
    @TestRail(testCaseId = ("{C586}"))
    public void testRevertUnsavedScenario() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectProcessGroup(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .revert()
            .revertScenario(EvaluatePage.class);

        assertThat(evaluatePage.getSelectedProcessGroup(), is(equalTo(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())));
    }
}