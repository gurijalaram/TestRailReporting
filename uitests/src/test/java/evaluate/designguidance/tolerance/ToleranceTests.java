package test.java.evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.ToleranceEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.designguidance.tolerances.ToleranceEditPage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class ToleranceTests extends TestBase {

    private LoginPage loginPage;
    private ToleranceEditPage toleranceEditPage;

    public ToleranceTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
    public void testEditTolerance() {
        loginPage = new LoginPage(driver);
        toleranceEditPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD("Flatness", "PlanarFace:35")
            .editTolerance();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.FLATNESS.getTolerance(), "0.50"), is(true));
    }
}