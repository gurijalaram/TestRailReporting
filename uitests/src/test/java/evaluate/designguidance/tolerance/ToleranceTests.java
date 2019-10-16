package evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.ToleranceEditPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.TolerancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ToleranceEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class ToleranceTests extends TestBase {

    private LoginPage loginPage;
    private ToleranceEditPage toleranceEditPage;
    private TolerancePage tolerancePage;
    private WarningPage warningPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private DesignGuidancePage designGuidancePage;

    public ToleranceTests() {
        super();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @Issue("AP-56493")
    @TestRail(testCaseId = {"707", "1607"})
    @Description("Validate the user can edit multiple tolerances for a GCD in a private workspace scenario")
    public void testEditTolerances() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.PROFILESURFACE.getToleranceName(), "PlanarFace:74")
            .selectEditButton()
            .setTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "0.23")
            .setTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "0.16")
            .apply(TolerancePage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.PROFILESURFACE.getToleranceName(), "PlanarFace:74")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "0.23"), is(true));
        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "0.16"), is(true));

        toleranceEditPage.cancel();

        evaluatePage = new EvaluatePage(driver);
        toleranceSettingsPage = evaluatePage.openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();

        new SettingsPage(driver).save(EvaluatePage.class);
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @Issue("AP-56493")
    @TestRail(testCaseId = {"708", "1607"})
    @Description("Validate a user can remove an applied tolerance")
    public void testRemoveTolerance() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        toleranceEditPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getToleranceName(), "PlanarFace:35")
            .selectEditButton()
            .removeTolerance(ToleranceEnum.FLATNESS.getToleranceName())
            .apply(TolerancePage.class)
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.FLATNESS.getToleranceName(), ""), is(true));

        toleranceEditPage.cancel();

        evaluatePage = new EvaluatePage(driver);
        toleranceSettingsPage = evaluatePage.openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();

        new SettingsPage(driver).save(EvaluatePage.class);
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @Issue("AP-56493")
    @TestRail(testCaseId = {"716", "1608"})
    @Description("Validate JUNK values can not be added in the edit tolerance table")
    public void testNoJunkTolerancea() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getToleranceName(), "PlanarFace:35")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "abcd")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"717", "1608"})
    @Description("Validate value 0 can not be added in the edit tolerance table")
    public void testNoJunkTolerance0() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getToleranceName(), "PlanarFace:35")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Issue("AP-56493")
    @TestRail(testCaseId = "726")
    @Description("Validate a tolerance edit of a PMI imported tolerance is maintained when the user switches MATERIAL")
    public void testMaintainingToleranceChangeMaterial() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.STRAIGHTNESS.getToleranceName(), "PlanarFace:78")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.44")
            .apply(TolerancePage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.STRAIGHTNESS.getToleranceName(), "PlanarFace:78")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.44"), is(true));

        toleranceEditPage.cancel();

        evaluatePage = new EvaluatePage(driver);
        toleranceSettingsPage = evaluatePage.openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();

        new SettingsPage(driver).save(EvaluatePage.class);

    }
}