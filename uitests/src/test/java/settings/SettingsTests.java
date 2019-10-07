package settings;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

public class SettingsTests extends TestBase {
    private LoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioNotesPage scenarioNotesPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private ProductionDefaultPage productionDefaultPage;

    @Test
    @TestRail(testCaseId = {"1609"})
    @Description("User can change the default Production Defaults")
    public void changeProductionDefaults() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openProdDefaultTab()
            .enterScenarioName("MP Auto Test")
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectMaterialCatalog(VPEEnum.APRIORI_EASTERN_EUROPE.getVpe())
            .selectMaterial("ABS, Plating")
            .enterAnnualVolume("3000")
            .enterProductionLife("7")
            .selectBatchManual()
            .enterBatchInput("50");
        settingsPage = new SettingsPage(driver);
        explorePage = settingsPage.save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        productionDefaultPage = explorePage.openSettings()
            .openProdDefaultTab();

        assertThat(productionDefaultPage.getScenarioName("MP Auto Test"), Matchers.is(true));
        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(VPEEnum.APRIORI_BRAZIL.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(VPEEnum.APRIORI_EASTERN_EUROPE.getVpe()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("ABS, Plating"), is(true));


            productionDefaultPage.enterScenarioName("Initial")
            .selectProcessGroup("<No default specified>")
            .selectVPE("<No default specified>")
            .selectMaterialCatalog("<No default specified>")
            .selectMaterial("<No default specified>")
            .enterAnnualVolume("")
            .enterProductionLife("")
            .selectBatchAuto();
        settingsPage = new SettingsPage(driver);
        explorePage = settingsPage.save(ExplorePage.class);
    }


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
        evaluatePage = explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .costScenario()
            .publishScenario()
            .openJobQueue()
            .openScenarioLink(testScenarioName, "bracket_basic", "publish");
        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), CoreMatchers.is(true));

        evaluatePage = new EvaluatePage(driver);
        productionDefaultPage = evaluatePage.openSettings()
            .openProdDefaultTab()
            .selectProcessGroup("<No default specified>");
        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(EvaluatePage.class);
    }
}
