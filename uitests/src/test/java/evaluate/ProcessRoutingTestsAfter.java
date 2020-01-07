package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

public class ProcessRoutingTestsAfter extends TestBase {

    private CIDLoginPage loginPage;
    private ProcessRoutingPage processRoutingPage;
    private SettingsPage settingsPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private EvaluatePage evaluatePage;


    public ProcessRoutingTestsAfter() {
        super();
    }

    @After
    public void resetToleranceSettings() {
        new AfterTestUtil(driver).resetToleranceSettings();
    }

    @Test
    @TestRail(testCaseId = {"645", "269", "647", "649"})
    @Description("View detailed information about costed process")
    public void testViewProcessDetails() {
        loginPage = new CIDLoginPage(driver);
        toleranceSettingsPage = loginPage.login(UserUtil.getUser())
            .openSettings()
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        processRoutingPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getSelectionTableDetails(), containsString("Cycle Time (s): 54.05, Piece Part Cost (USD): 0.63, Fully Burdened Cost (USD): 1.05, Total Capital Investments (USD): 11,741.62"));
    }

    @Test
    @TestRail(testCaseId = {"1672"})
    @Description("Validate behaviour when Adding/Editing tolerances that may require additional machining.")
    public void routingTolerances() {
        loginPage = new CIDLoginPage(driver);
        toleranceSettingsPage = loginPage.login(UserUtil.getUser())
            .openSettings()
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Melting / High Pressure Die Casting / Trim / 3 Axis Mill / Drill Press / Cylindrical Grinder / Reciprocating Surface Grinder"), is(true));

       processRoutingPage = evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Gravity Die Cast")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getRoutingLabels(), hasItems("3 Axis Mill", "Drill Press", "Cylindrical Grinder", "Reciprocating Surface Grinder"));
   }
}