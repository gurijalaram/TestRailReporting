package test.java.evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.designguidance.tolerances.TolerancePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class ToleranceTests extends TestBase {

    private LoginPage loginPage;
    private TolerancePage tolerancePage;

    public ToleranceTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "389")
    @Description("Tolerance can be edited")
    public void testEditTolerance() {
        loginPage = new LoginPage(driver);
        tolerancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD("Flatness", "PlanarFace:35");

        assertThat(tolerancePage.)
    }
}