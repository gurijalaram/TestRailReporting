package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.process.ProcessPage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class ProcessRoutingTests extends TestBase {

    private LoginPage loginPage;
    private ProcessPage processPage;
    private EvaluatePage evaluatePage;

    public ProcessRoutingTests() {
        super();
    }

    @Test
    @Description("Validate the user can Change the process routing in CI Design")
    @Severity(SeverityLevel.CRITICAL)
    public void testAlternateRoutingSelection() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile("Alternate Routing", new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"))
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("3 Axis Mill")
            .apply();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("3 Axis Mill"), is(true));
    }

    @Test
    @Description("C645 View detailed information about costed process")
    @Severity(SeverityLevel.CRITICAL)
    public void viewProcessDetails(){
        loginPage = new LoginPage(driver);
        
    }

}