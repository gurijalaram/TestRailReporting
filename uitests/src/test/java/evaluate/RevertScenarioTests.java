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
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class RevertScenarioTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    public RevertScenarioTests() {
        super();
    }

    @Test
    @Description("Test revert saved scenario")
    public void testRevertSavedScenario() {
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
    public void testRevertUnsavedScenario() {
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