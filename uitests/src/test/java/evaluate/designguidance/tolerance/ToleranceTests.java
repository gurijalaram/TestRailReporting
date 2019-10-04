package evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.ToleranceEditPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ToleranceEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

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

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.50"), is(true));
    }
}