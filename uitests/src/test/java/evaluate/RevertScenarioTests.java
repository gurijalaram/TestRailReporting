package test.java.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class RevertScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    private String filePath = new Scanner(DeletePrivateScenarioTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public RevertScenarioTests() {
        super();
    }

    @Test
    @Description("Test revert saved scenario")
    @Severity(SeverityLevel.NORMAL)
    public void testRevertSavedScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("RevertSavedScenario", filePath, "testpart-4.prt")
            .selectProcessGroup(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .revert()
            .revertScenario(EvaluatePage.class);

        assertThat(new EvaluatePage(driver).getProcessGroup(), is(equalTo(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())));
    }

    @Test
    @Description("Test revert unsaved scenario")
    @Severity(SeverityLevel.NORMAL)
    public void testRevertUnsavedScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("RevertUnsavedScenario", filePath, "testpart-4.prt")
            .selectProcessGroup(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .revert()
            .revertScenario(EvaluatePage.class);

        assertThat(new EvaluatePage(driver).getProcessGroup(), is(equalTo(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())));
    }
}
