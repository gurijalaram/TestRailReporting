package test.java.evaluate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CurrencyEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.process.ProcessPage;
import main.java.pages.login.LoginPage;
import main.java.pages.settings.SettingsPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Currency;

public class ProcessRoutingTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ProcessPage processPage;
    private EvaluatePage evaluatePage;
    private SettingsPage settingsPage;

    public ProcessRoutingTests() {
        super();
    }

    @Test
    @Description("Validate the user can Change the process routing in CI Design")
    @Severity(SeverityLevel.CRITICAL)
    public void testAlternateRoutingSelection() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"))
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
    public void testViewProcessDetails() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
                .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
                .openSettings()
                .changeCurrency(CurrencyEnum.USD.getCurrency())
                .save(EvaluatePage.class)
                .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                .selectVPE(VPEEnum.APRIORI_USA.getVpe())
                .costScenario()
                .openProcessDetails();

        assertThat(processPage.getSelectionTableDetails(), containsString("Cycle Time (s): 29.67\n" +
                "Piece Part Cost (USD): 0.44\n" +
                "Fully Burdened Cost (USD): 0.83\n" +
                "Total Capital Investments (USD): 10,709.39"));
    }

    @Test
    @Description("C646 View individual process steps")
    @Severity(SeverityLevel.NORMAL)
    public void testViewProcessSteps() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
                .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
                .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
                .selectVPE(VPEEnum.APRIORI_USA.getVpe())
                .costScenario()
                .openProcessDetails();

        assertThat(processPage.getRoutingLabels(), hasItems("Material Stock", "Turret Press", "Bend Brake"));
    }
}