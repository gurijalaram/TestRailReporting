package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserDataUtil;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class RevertScenarioTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public RevertScenarioTests() {
        super();
    }

    @Test
    @Description("Test revert saved scenario")
    @TestRail(testCaseId = {"585"})
    public void testRevertSavedScenario() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .revert()
            .revertScenario(EvaluatePage.class);

        assertThat(evaluatePage.isProcessGroupSelected(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup()), is(true));
    }

    @Test
    @Description("Test revert unsaved scenario")
    @TestRail(testCaseId = {"586"})
    public void testRevertUnsavedScenario() {

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("testpart-4.prt"))
            .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .revert()
            .revertScenario(EvaluatePage.class);

        assertThat(evaluatePage.isProcessGroupSelected(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup()), is(true));
    }
}