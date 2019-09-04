package test.java.evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.constants.Constants;
import main.java.enums.CurrencyEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.process.ProcessPage;
import main.java.pages.login.LoginPage;
import main.java.pages.settings.SettingsPage;
import main.java.pages.settings.ToleranceSettingsPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

public class ProcessRoutingTests extends TestBase {

    private LoginPage loginPage;
    private ProcessPage processPage;
    private EvaluatePage evaluatePage;
    private SettingsPage settingsPage;
    private ToleranceSettingsPage toleranceSettingsPage;

    public ProcessRoutingTests() {
        super();
    }

    @Test
    @Description("Validate the user can Change the process routing in CI Design")
    public void testAlternateRoutingSelection() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"))
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
    @TestRail(testCaseId = {"645"})
    @Description("View detailed information about costed process")
    public void testViewProcessDetails() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .openSettings()
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openTolerancesTab()
            .selectAssumeTolerance();

        settingsPage = new SettingsPage(driver);
        processPage = settingsPage.save(EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processPage.getSelectionTableDetails(), arrayContaining("Cycle Time (s): 29.67", "Piece Part Cost (USD): 0.43",
            "Fully Burdened Cost (USD): 0.82", "Total Capital Investments (USD): 10,732.01"));
    }

    @Test
    @TestRail(testCaseId = {"646"})
    @Description("View individual process steps")
    public void testViewProcessSteps() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(Constants.scenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();

        settingsPage = new SettingsPage(driver);
        processPage = settingsPage.save(EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processPage.getRoutingLabels(), hasItems("Material Stock", "Turret Press", "Bend Brake"));
    }
}