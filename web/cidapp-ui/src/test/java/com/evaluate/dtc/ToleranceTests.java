package com.evaluate.dtc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.TolerancesPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.ToleranceDefaultsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ToleranceEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.EvaluateDfmIconEnum;
import com.utils.OverridesEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ToleranceTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private TolerancesPage tolerancesPage;
    private ToleranceDefaultsPage toleranceDefaultsPage;
    SoftAssertions softAssertions = new SoftAssertions();

    private File resourceFile;
    private ComponentInfoBuilder cidComponentItem;

    public ToleranceTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @Ignore("Cannot edit tolerances this release")
    @Category({IgnoreTests.class})
    @TestRail(testCaseId = {"6464", "7811", "6964"})
    @Description("Validate the user can edit multiple tolerances for a GCD in a private workspace scenario")
    public void testEditTolerances() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "DTCCastingIssues";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        /*assertThat(evaluatePage.getDfmRiskIcon(), is("Critical"));
        assertThat(evaluatePage.getDfmRisk(), is("Critical"));

        toleranceEditPage = evaluatePage.openDesignGuidance()
            .openTolerancesTab()

            .selectIssueTypeGcd("Profile of Surface", "PlanarFace:74")
            .selectEditButton()
            .setTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "0.23")
            .setTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "0.16")
            .apply(TolerancePage.class)
            .closePanel()
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.PROFILESURFACE.getToleranceName())
            .selectGcd("PlanarFace:74")
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName()), containsString("0.23"));
        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.PARALLELISM.getToleranceName()), containsString("0.16"));*/
    }

    /*    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"7812"})
    @Description("Validate a user can remove an applied tolerance")
    public void testRemoveTolerance() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        new SettingsPage(driver).save(ExplorePage.class);
        assertThat(new APIValue().getToleranceValueFromEndpoint(currentUser.getUsername(), "toleranceMode"), is(CAD")));

        explorePage = new ExplorePage(driver);
        toleranceEditPage = explorePage.uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.FLATNESS.getToleranceName())
            .selectGcd("PlanarFace:35")
            .selectEditButton()
            .removeTolerance(ToleranceEnum.FLATNESS.getToleranceName())
            .apply(TolerancePage.class)
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.FLATNESS.getToleranceName()), containsString(""));
    }*/

    /*@Category({CustomerSmokeTests.class})
    @Test
    @TestRail(testCaseId = {"7820"})
    @Description("Validate JUNK values can not be added in the edit tolerance table")
    public void testNoJunkTolerances() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        new SettingsPage(driver).save(ExplorePage.class);
        assertThat(new APIValue().getToleranceValueFromEndpoint(currentUser.getUsername(), "toleranceMode"), is(CAD")));

        explorePage = new ExplorePage(driver);
        warningPage = explorePage.uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.FLATNESS.getToleranceName())
            .selectGcd("PlanarFace:35")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "abcd")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }*/


    /*@Category({CustomerSmokeTests.class})
    @Test
    @TestRail(testCaseId = {"7821"})
    @Description("Validate value 0 can not be added in the edit tolerance table")
    public void testNoJunkTolerance0() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        new SettingsPage(driver).save(ExplorePage.class);
        assertThat(new APIValue().getToleranceValueFromEndpoint(currentUser.getUsername(), "toleranceMode"), is(CAD")));

        explorePage = new ExplorePage(driver);
        warningPage = explorePage.uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.FLATNESS.getToleranceName())
            .selectGcd("PlanarFace:35")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }*/


    /*@Test
    @TestRail(testCaseId = {"7830", "7816", "6974", "6976"})
    @Description("Validate a tolerance edit of a PMI imported tolerance is maintained when the user switches MATERIAL")
    public void testMaintainingToleranceChangeMaterial() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        new SettingsPage(driver).save(ExplorePage.class);
        assertThat(new APIValue().getToleranceValueFromEndpoint(currentUser.getUsername(), "toleranceMode"), is(CAD")));

        toleranceEditPage = new ExplorePage(driver).uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectVPE(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.STRAIGHTNESS.getToleranceName())
            .selectGcd("PlanarFace:78")
            .selectEditButton()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.44")
            .apply(TolerancePage.class)
            .closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 1050A")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.STRAIGHTNESS.getToleranceName())
            .selectGcd("PlanarFace:78")
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.FLATNESS.getToleranceName()), containsString("0.44"));
    }*/

    @Test
    @Issue("COST-685")
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"6455"})
    @Description("Ensure the Tolerance Tab displays all applied tolerance types & tolerance counts")
    public void toleranceCounts() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "PMI_AllTolTypesCatia";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.MEDIUM.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Medium");

        tolerancesPage = evaluatePage.openDesignGuidance()
            .openTolerancesTab();

        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.CIRCULARITY)).isEqualTo(1);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.CONCENTRICITY)).isEqualTo(1);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.CYLINDRICITY)).isEqualTo(1);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.FLATNESS)).isEqualTo(2);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.PARALLELISM)).isEqualTo(1);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.PERPENDICULARITY)).isEqualTo(1);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.PROFILESURFACE)).isEqualTo(2);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.ROUGHNESSRA)).isEqualTo(1);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.ROUGHNESSRZ)).isEqualTo(2);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.RUNOUT)).isEqualTo(1);

        softAssertions.assertAll();
    }

    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7828", "7829", "7833"})
    @Description("Validate applied tolerances are maintained after changing the scenario process group")
    public void testMaintainingToleranceChangePG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PMI_AllTolTypesCatia.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        new SettingsPage(driver).save(ExplorePage.class);
        assertThat(new APIValue().getToleranceValueFromEndpoint(currentUser.getUsername(), "toleranceMode"), is(CAD")));

        explorePage = new ExplorePage(driver);
        toleranceEditPage = explorePage.uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CIRCULARITY.getToleranceName())
            .selectGcd("CurvedWall:5")
            .selectEditButton()
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "2.16")
            .apply(TolerancePage.class)
            .closePanel()
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CIRCULARITY.getToleranceName())
            .selectGcd("CurvedWall:5")
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.CIRCULARITY.getToleranceName()), containsString("2.16"));
    }

    @Test
    @TestRail(testCaseId = {"7834", "7813", "7817", "7818", "7826"})
    @Description("Validate tolerance edits are maintained when user adds a secondary process group")
    public void testMaintainingSecondaryPG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PMI_AllTolTypesCatia.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        new SettingsPage(driver).save(ExplorePage.class);
        assertThat(new APIValue().getToleranceValueFromEndpoint(currentUser.getUsername(), "toleranceMode"), is(CAD")));

        explorePage = new ExplorePage(driver);
        toleranceEditPage = explorePage.uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CYLINDRICITY.getToleranceName())
            .selectGcd("CurvedWall:6")
            .selectEditButton()
            .setTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01")
            .apply(TolerancePage.class)
            .closePanel()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CYLINDRICITY.getToleranceName())
            .selectGcd("CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName()), containsString("4.01"));
        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.PARALLELISM.getToleranceName()), containsString(""));

        toleranceEditPage.setTolerance(ToleranceEnum.RUNOUT.getToleranceName(), "87")
            .cancel()
            .closePanel()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CYLINDRICITY.getToleranceName())
            .selectGcd("CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName()), containsString("4.01"));
        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.RUNOUT.getToleranceName()), containsString(""));
    }

    @Test
    @TestRail(testCaseId = {"7827"})
    @Description("Validate tolerance edits when default values set")
    public void specificDefaultTolerances() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Case_001_-_Rockwell_2075-0243G.stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectSpecificDefaultValues()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "0.4")
            .setTolerance(ToleranceEnum.SYMMETRY.getToleranceName(), "2.5")
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "1.3")
            .save(ToleranceSettingsPage.class);

        settingsPage = new SettingsPage(driver);
        toleranceEditPage = settingsPage.save(ExplorePage.class)
            .uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CIRCULARITY.getToleranceName())
            .selectGcd("CurvedWall:6")
            .selectEditButton()
            .setTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "4.01")
            .apply(TolerancePage.class)
            .closePanel()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CIRCULARITY.getToleranceName())
            .selectGcd("CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName()), containsString("4.01"));
        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.PARALLELISM.getToleranceName()), containsString(""));

        toleranceEditPage.setTolerance(ToleranceEnum.RUNOUT.getToleranceName(), "87")
            .cancel()
            .closePanel()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab()
            .selectToleranceType(ToleranceEnum.CYLINDRICITY.getToleranceName())
            .selectGcd("CurvedWall:6")
            .selectEditButton();

        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName()), containsString("4.01"));
        assertThat(toleranceEditPage.getTolerance(ToleranceEnum.RUNOUT.getToleranceName()), containsString(""));
    }*/

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6970"})
    @Description("Verify PMI data is not extracted ")
    public void assumeTolerances() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PMI_AllTolTypesCatia";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getGuidanceResult("GCDs with Tolerances"), is("0"));
    }

    @Test
    @TestRail(testCaseId = {"6965"})
    @Description(" All tolerances types can be selected & edited")
    public void specificTolerances() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PMI_AllTolTypesCatia";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        tolerancesPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .editSpecificValues()
            .inputOverride(OverridesEnum.ROUGHNESS_RA, "0.1")
            .inputOverride(OverridesEnum.ROUGHNESS_RZ, "0.2")
            .inputOverride(OverridesEnum.DIAMETER_TOLERANCE, "0.3")
            .inputOverride(OverridesEnum.POSITION_TOLERANCE, "0.4")
            .inputOverride(OverridesEnum.BEND_ANGLE, "0.5")
            .inputOverride(OverridesEnum.CIRCULARITY, "0.6")
            .inputOverride(OverridesEnum.CONCENTRICITY, "0.7")
            .inputOverride(OverridesEnum.CYLINDRICITY, "0.8")
            .inputOverride(OverridesEnum.FLATNESS, "0.9")
            .inputOverride(OverridesEnum.PARALLELISM, "1.0")
            .inputOverride(OverridesEnum.PERPENDICULARITY, "1.1")
            .inputOverride(OverridesEnum.PROFILE_OF_SURFACE, "1.2")
            .inputOverride(OverridesEnum.RUNOUT, "1.3")
            .inputOverride(OverridesEnum.TOTAL_RUNOUT, "1.4")
            .inputOverride(OverridesEnum.STRAIGHTNESS, "1.5")
            .inputOverride(OverridesEnum.SYMMETRY, "1.6")
            .submit()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab();

        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.CIRCULARITY)).isEqualTo(12);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.CONCENTRICITY)).isEqualTo(12);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.CYLINDRICITY)).isEqualTo(12);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.DIAMTOLERANCE)).isEqualTo(16);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.PARALLELISM)).isEqualTo(26);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.PERPENDICULARITY)).isEqualTo(26);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.PROFILESURFACE)).isEqualTo(27);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.ROUGHNESSRA)).isEqualTo(30);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.ROUGHNESSRZ)).isEqualTo(30);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.RUNOUT)).isEqualTo(30);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.STRAIGHTNESS)).isEqualTo(26);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.SYMMETRY)).isEqualTo(30);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.TOTALRUNOUT)).isEqualTo(12);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.TRUEPOSITION)).isEqualTo(30);

        softAssertions.assertAll();
    }

    /*@Test
    @TestRail(testCaseId = {"6966", "7276"})
    @Description("tolerance Policy Panel functionality in CI Design-JUNK values are prevented")
    public void tolerancePolicyJunk() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        warningPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .editValues()
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "abcd")
            .save(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }*/

    @Test
    @TestRail(testCaseId = {"6973"})
    @Description("Validate PMI is off when use specific is selected")
    public void specificTolerancesNoPMI() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PMI_AllTolTypesCatia";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        tolerancesPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .editSpecificValues()
            .inputOverride(OverridesEnum.ROUGHNESS_RA, "1.2")
            .submit()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab();

        assertThat(tolerancesPage.getGcdCount(ToleranceEnum.ROUGHNESSRA), is(30));
    }

    /*@Test
    @TestRail(testCaseId = {"6968", "7832"})
    @Description("Validate Tolerance Policy updates to System Unit User preferences")
    public void toleranceUnits() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .editValues()
            .setTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "0.3")
            .setTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), "1.2")
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "8.9")
            .setTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "1.9")
            .save(ToleranceSettingsPage.class);

        settingsPage = new SettingsPage(driver);
        toleranceValueSettingsPage = settingsPage.save(ExplorePage.class)
            .openSettings()
            .selectSystem(MetricEnum.ENGLISH.getMetricUnit())
            .save(ExplorePage.class)
            .openSettings()
            .goToToleranceTab()
            .editValues();

        assertThat(toleranceValueSettingsPage.getTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName()), is("11.81102"));
        assertThat(toleranceValueSettingsPage.getTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName()), containsString("1.2"));
        assertThat(toleranceValueSettingsPage.getTolerance(ToleranceEnum.CIRCULARITY.getToleranceName()), is("0.35039"));
        assertThat(toleranceValueSettingsPage.getTolerance(ToleranceEnum.PARALLELISM.getToleranceName()), containsString("0.0748"));
    }*/

    @Test
    @Issue("COST-685")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6975", "6967"})
    @Description("Validate 'Replace values less than' button")
    public void replaceValuesButton() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PMI_AllTolTypesCatia";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        tolerancesPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .replaceValues("0.2", "0.35")
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab()
            .selectIssueTypeGcd(ToleranceEnum.CIRCULARITY.getToleranceName(), "CurvedWall:5");

        assertThat(tolerancesPage.getCurrent("CurvedWall:5"), is("0.35mm"));
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"6465", "6978", "7814"})
    @Description("Validate conditions used for original costing are maintained between different users")
    public void tolerancesDiffUsers() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "PMI_AllTolTypesCatia";
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials testUser1 = UserUtil.getUser();
        UserCredentials testUser2 = UserUtil.getUser();
        currentUser = testUser1;
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(testUser1)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponent(componentName, testScenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .costScenario(3);

        softAssertions.assertThat(evaluatePage.getGuidanceResult("GCDs with Tolerances")).isEqualTo("13");
        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.HIGH.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("High");

        evaluatePage.publishScenario(PublishPage.class)
            .publish(cidComponentItem,  EvaluatePage.class)
            .logout()
            .login(testUser2)
            .selectFilter("Public")
            .clickSearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName, testScenarioName);

        softAssertions.assertThat(evaluatePage.getGuidanceResult("GCDs with Tolerances")).isEqualTo("13");

        softAssertions.assertAll();

        /*tolerancesPage = evaluatePage.openDesignGuidance()
            .openTolerancesTab()
            .selectToleranceType(ToleranceEnum.CIRCULARITY.getToleranceName())
            .selectGcd("CurvedWall:4");

        assertThat(tolerancePage.isEditButtonEnabled(), is(false));*/
    }

    /*@Test
    @TestRail(testCaseId = {"6978"})
    @Description("Validate conditions used for original costing are maintained between different users")
    public void toleranceThresholdMaintains() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad();

        new SettingsPage(driver).save(ExplorePage.class);

        assertThat(new APIValue().getToleranceValueFromEndpoint(currentUser.getUsername(), "toleranceMode"), is(CAD")));
    }*/

    @Test
    @TestRail(testCaseId = {"6979"})
    @Description("Ensure tolerance preferences are maintained for the user")
    public void tolerancesMaintained() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceDefaultsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .logout()
            .login(currentUser)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openSettings()
            .goToToleranceTab();

        assertThat(toleranceDefaultsPage.isCadSelected(), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6977"})
    @Description("Ensure tolerance policy is for single user.  User 1 preferences should not impact User 2 preferences")
    public void tolerancesSingleUser() {
        UserCredentials testUser1 = UserUtil.getUser();
        UserCredentials testUser2 = UserUtil.getUser();
        currentUser = testUser1;

        loginPage = new CidAppLoginPage(driver);
        toleranceDefaultsPage = loginPage.login(testUser1)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .logout()
            .login(testUser2)
            .openSettings()
            .goToToleranceTab();

        assertThat(toleranceDefaultsPage.isAssumeSelected(), is(true));
    }

    /*@Test
    @TestRail(testCaseId = {"7824", "7825"})
    @Description("Validate bend angle tolerance is only available for Bar & Tube process group")
    public void bendAngle() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_BarAndTube.prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .editValues()
            .setTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), "8.5")
            .save(ToleranceSettingsPage.class);

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .goToToleranceTab();

        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName()), "3"), is(true));

        evaluatePage = tolerancePage.closePanel()
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getGcdTolerancesCount(), is("0"));
    }*/
}