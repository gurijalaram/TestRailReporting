package settings;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class SettingsTests extends TestBase {
    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioNotesPage scenarioNotesPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private ProductionDefaultPage productionDefaultPage;

    @Test
    @TestRail(testCaseId = {"274", "1609"})
    @Description("User can change the default Process group")
    public void defaultPG() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup());

        settingsPage = new SettingsPage(driver);
        explorePage = settingsPage.save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .costScenario()
            .publishScenario()
            .openJobQueue()
            .openScenarioLink(testScenarioName, "bracket_basic", "Publish");

        //assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), is(true));

        evaluatePage = new EvaluatePage(driver);
        productionDefaultPage = evaluatePage.openSettings()
            .openProdDefaultTab()
            .selectProcessGroup("<No default specified>");

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(EvaluatePage.class);
    }
}
