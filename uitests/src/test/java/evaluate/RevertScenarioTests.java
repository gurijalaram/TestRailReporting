package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class RevertScenarioTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public RevertScenarioTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Test revert saved scenario")
    @TestRail(testCaseId = {"3848","585"})
    public void testRevertSavedScenario() {

        resourceFile = new FileResourceUtil().getResourceFile("testpart-4.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario(3)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-medium-risk-icon"));
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));

        new EvaluatePage(driver).revert()
            .revertScenario();

        assertThat(evaluatePage.isProcessGroupSelected(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup()), is(true));
    }

    @Test
    @Issue("AP-59845")
    @Category(SmokeTests.class)
    @Description("Test revert unsaved scenario")
    @TestRail(testCaseId = {"586"})
    public void testRevertUnsavedScenario() {

        resourceFile = new FileResourceUtil().getResourceFile("testpart-4.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario(3)
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .revert()
            .revertScenario();

        assertThat(evaluatePage.isProcessGroupSelected(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup()), is(true));
    }
}