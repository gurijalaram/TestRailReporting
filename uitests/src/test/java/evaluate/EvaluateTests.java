package test.java.evaluate;

import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class EvaluateTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public EvaluateTests() {
        super();
    }

    /**
     * Test successfully costing a scenario
     */
    @Test
    public void testCostScenario() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Scenario A", new FileResourceUtil().getResourceFile("testpart-4.prt"));
        evaluatePage = new EvaluatePage(driver);
    }

    /**
     * Test successfully costing and publishing a scenario
     */
    @Test
    public void testPublishScenario() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Scenario A", new FileResourceUtil().getResourceFile("testpart-4.prt"));
        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(null)
            .publishScenario();
    }

    /**
     * Test successful costing, change vpe and process group
     */
    @Test
    public void testCostVPE() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Scenario A", new FileResourceUtil().getResourceFile("testpart-4.prt"));
        evaluatePage = new EvaluatePage(driver);
        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .costScenario(null);
    }
}