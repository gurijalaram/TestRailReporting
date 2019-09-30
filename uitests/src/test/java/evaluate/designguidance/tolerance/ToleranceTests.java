package test.java.evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.ToleranceEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.evaluate.designguidance.tolerances.ToleranceEditPage;
import main.java.pages.evaluate.designguidance.tolerances.TolerancePage;
import main.java.pages.evaluate.designguidance.tolerances.WarningPage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.pages.settings.SettingsPage;
import main.java.pages.settings.ToleranceSettingsPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class ToleranceTests extends TestBase {

    private LoginPage loginPage;
    private ToleranceEditPage toleranceEditPage;
    private TolerancePage tolerancePage;
    private WarningPage warningPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private GuidancePage guidancePage;
    private EvaluatePage evaluatePage;
    private DesignGuidancePage designGuidancePage;

    public ToleranceTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Validate Tolerances can be edited")
    public void testEditTolerance() {
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
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getTolerance(), "PlanarFace:35")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getTolerance(), "0.23")
            .apply(TolerancePage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getTolerance(), "PlanarFace:35")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.FLATNESS.getTolerance(), "0.23"), is(true));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
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
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getTolerance(), "PlanarFace:35")
            .selectEditButton()
            .removeTolerance(ToleranceEnum.FLATNESS.getTolerance())
            .apply(TolerancePage.class)
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.FLATNESS.getTolerance(), ""), is(true));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
    public void testNoJunkTolerancea() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getTolerance(), "PlanarFace:35")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getTolerance(), "abcd")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
    public void testNoJunkTolerance0() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.FLATNESS.getTolerance(), "PlanarFace:35")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getTolerance(), "0")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
    public void testEditButtonDisabled() {
        loginPage = new LoginPage(driver);
        tolerancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab();

        assertThat(tolerancePage.getEditButton().isEnabled(), is(false));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
    public void testMaintainValuesChangeAttributes() {
        loginPage = new LoginPage(driver);
        tolerancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab();

        assertThat(tolerancePage.isToleranceCount(ToleranceEnum.FLATNESS.getTolerance(), "18"), is(true));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
    public void testMaintainingToleranceChangeMaterial() {
        loginPage = new LoginPage(driver);
        tolerancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab();

        assertThat(tolerancePage.isToleranceCount(ToleranceEnum.FLATNESS.getTolerance(), "18"), is(true));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Tolerance can be edited")
    public void testCannotEditPublicTolerance() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        tolerancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .publishScenario()
            .openScenario(testScenarioName, "DTCCastingIssues")
            .openDesignGuidance()
            .openTolerancesTab();

        assertThat(tolerancePage.getEditButton().isEnabled(), is(false));
    }
}