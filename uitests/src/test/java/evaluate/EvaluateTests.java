package test.java.evaluate;

import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class EvaluateTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;
    private String filePath = new Scanner(EvaluateTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public EvaluateTests() {
        super();
    }

    /**
     * Test successfully costing a scenario
     */
    @Test
    public void testCostScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Scenario A", filePath, "testpart-42.prt");
        evaluatePage = new EvaluatePage(driver);
    }

    /**
     * Test successfully costing and publishing a scenario
     */
    @Test
    public void testPublishScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Scenario A", filePath, "testpart-4.prt");
        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(null)
            .publishScenario();
    }

    /**
     * Test successful costing, change vpe and process group
     */
    @Test
    public void testCostVPE() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Scenario A", filePath, "testpart-4.prt");
        evaluatePage = new EvaluatePage(driver);
        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .costScenario(null);
    }
}