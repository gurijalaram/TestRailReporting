package evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.ToleranceEditPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.TolerancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceValueSettingsPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ToleranceEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

public class ToleranceTests extends TestBase {

    private CIDLoginPage loginPage;
    private ToleranceEditPage toleranceEditPage;
    private TolerancePage tolerancePage;
    private WarningPage warningPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private DesignGuidancePage designGuidancePage;
    private ToleranceValueSettingsPage toleranceValueSettingsPage;
    private UserCredentials currentUser;
    private ExplorePage explorePage;

    public ToleranceTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if(currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"707", "1607", "1285"})
    @Description("Validate the user can edit multiple tolerances for a GCD in a private workspace scenario")
    public void testEditTolerances() {

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
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
            .setTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "0.16")
            .apply(TolerancePage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        toleranceEditPage = evaluatePage.costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.PROFILESURFACE.getToleranceName(), "PlanarFace:74")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "0.23"), is(true));
        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "0.16"), is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"708", "1607"})
    @Description("Validate a user can remove an applied tolerance")
    public void testRemoveTolerance() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
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
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"716", "1608"})
    @Description("Validate JUNK values can not be added in the edit tolerance table")
    public void testNoJunkTolerances() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        warningPage = settingsPage.save(ExplorePage.class)
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

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"717", "1608"})
    @Description("Validate value 0 can not be added in the edit tolerance table")
    public void testNoJunkTolerance0() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        warningPage = settingsPage.save(ExplorePage.class)
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
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"726", "712", "1295", "1297"})
    @Description("Validate a tolerance edit of a PMI imported tolerance is maintained when the user switches MATERIAL")
    public void testMaintainingToleranceChangeMaterial() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPART"))
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
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
        toleranceEditPage = evaluatePage.openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.STRAIGHTNESS.getToleranceName(), "PlanarFace:78")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.44"), is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1595"})
    @Description("Ensure the Tolerance Tab displays all applied tolerance types & tolerance counts")
    public void toleranceCounts() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .expandGuidancePanel()
            .openTolerancesTab();

        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.CIRCULARITY.getToleranceName()), "1"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.CONCENTRICITY.getToleranceName()), "1"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.CYLINDRICITY.getToleranceName()), "1"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.FLATNESS.getToleranceName()), "2"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.PARALLELISM.getToleranceName()), "1"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.PERPENDICULARITY.getToleranceName()), "1"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.PROFILESURFACE.getToleranceName()), "2"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.ROUGHNESSRA.getToleranceName()), "1"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.ROUGHNESSRZ.getToleranceName()), "2"), is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.RUNOUT.getToleranceName()), "1"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"724", "725", "729"})
    @Description("Validate applied tolerances are maintained after changing the scenario process group")
    public void testMaintainingToleranceChangePG() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CIRCULARITY.getToleranceName(), "CurvedWall:5")
            .selectEditButton()
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "2.16")
            .apply(TolerancePage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        toleranceEditPage = evaluatePage.selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CIRCULARITY.getToleranceName(), "CurvedWall:5")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "2.16"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"730", "709", "713", "714", "722"})
    @Description("Validate tolerance edits are maintained when user adds a secondary process group")
    public void testMaintainingSecondaryPG() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CYLINDRICITY.getToleranceName(), "CurvedWall:6")
            .selectEditButton()
            .setTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01")
            .apply(TolerancePage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        toleranceEditPage = evaluatePage.openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CYLINDRICITY.getToleranceName(), "CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01"), is(true));
        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), ""), is(true));

        new ToleranceEditPage(driver).setTolerance(ToleranceEnum.RUNOUT.getToleranceName(), "87")
            .cancel();

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        toleranceEditPage = evaluatePage.openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CYLINDRICITY.getToleranceName(), "CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01"), is(true));
        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.RUNOUT.getToleranceName(), ""), is(true));
    }

    @Test
    @Issue("AP-57941")
    @TestRail(testCaseId = {"723"})
    @Description("Validate tolerance edits when default values set")
    public void specificDefaultTolerances() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectSpecificDefaultValues()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.4")
            .setTolerance(ToleranceEnum.SYMMETRY.getToleranceName(), "2.5")
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "1.3")
            .save(ToleranceSettingsPage.class);

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Case_001_-_Rockwell_2075-0243G.stp"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CIRCULARITY.getToleranceName(), "CurvedWall:6")
            .selectEditButton()
            .setTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01")
            .apply(TolerancePage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        toleranceEditPage = evaluatePage.openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CIRCULARITY.getToleranceName(), "CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01"), is(true));
        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), ""), is(true));

        new ToleranceEditPage(driver).setTolerance(ToleranceEnum.RUNOUT.getToleranceName(), "87")
            .cancel();

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        toleranceEditPage = evaluatePage.openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CYLINDRICITY.getToleranceName(), "CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01"), is(true));
        assertThat(toleranceEditPage.isTolerance(ToleranceEnum.RUNOUT.getToleranceName(), ""), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1291"})
    @Description("Verify PMI data is not extracted ")
    public void assumeTolerances() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getGcdTolerancesCount("0"), is(true));
    }

    @Test
    @Issue("AP-57941")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1286"})
    @Description(" All tolerances types can be selected & edited")
    public void specificTolerances() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .editValues()
            .setTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "0.1")
            .setTolerance(ToleranceEnum.ROUGHNESSRZ.getToleranceName(), "0.2")
            .setTolerance(ToleranceEnum.DIAMTOLERANCE.getToleranceName(), "0.3")
            .setTolerance(ToleranceEnum.TRUEPOSITION.getToleranceName(), "0.4")
            .setTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), "0.5")
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "0.6")
            .setTolerance(ToleranceEnum.CONCENTRICITY.getToleranceName(), "0.7")
            .setTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "0.8")
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.9")
            .setTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "1.0")
            .setTolerance(ToleranceEnum.PERPENDICULARITY.getToleranceName(), "1.1")
            .setTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "1.2")
            .setTolerance(ToleranceEnum.RUNOUT.getToleranceName(), "1.3")
            .setTolerance(ToleranceEnum.TOTALRUNOUT.getToleranceName(), "1.4")
            .setTolerance(ToleranceEnum.STRAIGHTNESS.getToleranceName(), "1.5")
            .setTolerance(ToleranceEnum.SYMMETRY.getToleranceName(), "1.6")
            .save(ToleranceSettingsPage.class);

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .expandGuidancePanel()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CIRCULARITY.getToleranceName(), "CurvedWall:1");

        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.CIRCULARITY.getToleranceName(), "Count"), is(equalTo("12")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.CONCENTRICITY.getToleranceName(), "Count"), is(equalTo("12")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.CYLINDRICITY.getToleranceName(), "Count"), is(equalTo("12")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.DIAMTOLERANCE.getToleranceName(), "Count"), is(equalTo("16")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.FLATNESS.getToleranceName(), "Count"), is(equalTo("14")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.PARALLELISM.getToleranceName(), "Count"), is(equalTo("26")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.PERPENDICULARITY.getToleranceName(), "Count"), is(equalTo("26")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.PROFILESURFACE.getToleranceName(), "Count"), is(equalTo("27")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "Count"), is(equalTo("30")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.ROUGHNESSRZ.getToleranceName(), "Count"), is(equalTo("30")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.RUNOUT.getToleranceName(), "Count"), is(equalTo("30")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.STRAIGHTNESS.getToleranceName(), "Count"), is(equalTo("26")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.SYMMETRY.getToleranceName(), "Count"), is(equalTo("30")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.TOTALRUNOUT.getToleranceName(), "Count"), is(equalTo("12")));
        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.TRUEPOSITION.getToleranceName(), "Count"), is(equalTo("30")));
    }

    @Test
    @TestRail(testCaseId = {"1287", "750"})
    @Description("tolerance Policy Panel functionality in CI Design-JUNK values are prevented")
    public void tolerancePolicyJunk() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        warningPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .editValues()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "abcd")
            .save(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1294"})
    @Description("Validate PMI is off when use specific is selected")
    public void specificTolerancesNoPMI() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .editValues()
            .setTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "1.2")
            .save(ToleranceSettingsPage.class);

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .expandGuidancePanel()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "CurvedWall:2");

        assertThat(tolerancePage.getToleranceCell(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "Count"), is(equalTo("30")));
    }

    @Test
    @TestRail(testCaseId = {"1289"})
    @Description("Validate Tolerance Policy updates to System Unit User preferences")
    public void toleranceUnits() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .editValues()
            .setTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "0.3")
            .setTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), "1.2")
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "8.9")
            .setTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "1.9")
            .save(ToleranceSettingsPage.class);

        settingsPage = new SettingsPage(driver);
        toleranceValueSettingsPage = settingsPage.save(ExplorePage.class)
            .openSettings()
            .changeDisplayUnits(UnitsEnum.ENGLISH.getUnit())
            .save(ExplorePage.class)
            .openSettings()
            .openTolerancesTab()
            .editValues();

        assertThat(toleranceValueSettingsPage.isTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "11.81102"), is(true));
        assertThat(toleranceValueSettingsPage.isTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), "1.2"), is(true));
        assertThat(toleranceValueSettingsPage.isTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "0.35039"), is(true));
        assertThat(toleranceValueSettingsPage.isTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "0.0748"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1296", "1288"})
    @Description("Validate 'Replace values less than' button")
    public void replaceValuesButton() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel()
            .replaceValues("0.2", "0.35");

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .expandGuidancePanel()
            .openTolerancesTab()
            .selectToleranceTypeAndGCD(ToleranceEnum.CIRCULARITY.getToleranceName(), "CurvedWall:5");

        assertThat(tolerancePage.getGCDCell("CurvedWall:5", "Current"), is(equalTo("0.35000")));
    }

    @Test
    @TestRail(testCaseId = {"1299"})
    @Description("Validate conditions used for original costing are maintained between different users")
    public void tolerancesDiffUsers() {

        String testScenarioName = new Util().getScenarioName();
        UserCredentials testUser1 = UserUtil.getUser();
        UserCredentials testUser2 = UserUtil.getUser();
        currentUser = testUser1;

        new CIDLoginPage(driver).login(testUser1)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getGcdTolerancesCount("11"), is(true));

        new EvaluatePage(driver).publishScenario(PublishPage.class)
            .selectPublishButton()
            .openAdminDropdown()
            .selectLogOut();

        new CIDLoginPage(driver).login(testUser2);

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "PMI_AllTolTypesCatia");

        assertThat(evaluatePage.getGcdTolerancesCount("11"), is(true));
    }
}