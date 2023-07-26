package com.evaluate.dtc;

import static com.apriori.enums.ProcessGroupEnum.STOCK_MACHINING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.TolerancesPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ToleranceEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DTCMachiningTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private UserCredentials currentUser;
    private TolerancesPage tolerancesPage;
    private SoftAssertions softAssertions = new SoftAssertions();

    private File resourceFile;

    public DTCMachiningTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @TestRail(id = {7751})
    @Description("Testing DTC Machining Keyseat Mill")
    public void testDTCKeyseat() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "Machining-DTC_Issue_KeyseatMillAccessibility";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("AISI 1010")
                .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario()
                .openDesignGuidance()
                .selectIssueTypeGcd("Machining Issues, Keyseat Mill Accessibility", "Slot", "Slot:3");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("There is no available Groove milling tool that can fit inside the Slot."));
    }

    @Test
    @TestRail(id = {6441})
    @Description("Testing DTC Machining Sharp Corner on a Curved Surface")
    public void testDTCCurvedSurface() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("AISI 1010")
                .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario()
                .openDesignGuidance()
                .selectIssueTypeGcd("Machining Issues, Sharp Corner", "Curved Surface", "CurvedSurface:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Contouring: Feature contains a sharp corner that would require a zero tool diameter"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(id = {6439})
    @Description("Testing DTC Machining Sharp Corner - Planar Face - Contouring")
    public void testDTCSharpCorner() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "Machining-DTC_Issue_SharpCorner-PlanarFace";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("AISI 1010")
                .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario()
                .openDesignGuidance()
                .selectIssueTypeGcd("Machining Issues, Sharp Corner", "Planar Face", "PlanarFace:5");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Facing: Feature contains a sharp corner that would require a zero tool diameter. " +
                "If sharp corner was intentional, try activating a new setup or changing process/operation. If sharp corner was unintentional, update CAD model or override operation feasibility rule."));
    }

    @Test
    @TestRail(id = {6445})
    @Description("Testing DTC Machining Side Milling L/D Ratio")
    public void testDTCSideMilling() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "Deep hole";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(STOCK_MACHINING)
                .openMaterialSelectorTable()
                .search("AISI 1010")
                .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario()
                .openDesignGuidance()
                .selectIssueTypeGcd("Machining Issues, Side Milling L/D", "Curved Wall", "CurvedWall:6");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Side Milling: Required tool exceeds the max L/D Ratio"));
    }

    @Test
    @TestRail(id = {6444})
    @Description("Testing DTC Machining Missing Setups")
    public void testDTCMissingSetup() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "Machining-DTC_Issues_MissingSetups_CurvedWall-PlanarFace";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("AISI 1010")
                .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario()
                .openDesignGuidance()
                .selectIssueTypeGcd("Machining Issues, Missing Setups", "Planar Face", "PlanarFace:6");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Setup Axis was not automatically assigned"));
    }

    @Test
    @TestRail(id = {6442})
    @Description("Verify obstructed surfaces on planar faces")
    public void obstructedSurfacePlanarFace() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("AISI 1010")
                .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario()
                .openDesignGuidance()
                .selectIssueTypeGcd("Machining Issues, Obstructed Surfaces", "Planar Face", "PlanarFace:9");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Facing: Feature is not accessible by an active setup. Activate a new Setup, override operation feasibility, or select a specialized machining operation."));
    }

    /*    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(id = {6440", "6443", "6447"})
    @Description("Ensure that  'Guidance' includes: - Issue type count - DTC Messaging for each guidance instance")
    public void stockMachiningDTC() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "nist_ftc_06_asme1_sw1500_rd.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidanceIssuesPage = settingsPage.save(ExplorePage.class)
            .uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Machining Issues, Sharp Corner", "Planar Faces", "PlanarFace:10");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Facing: Feature contains a sharp corner"));

        guidanceIssuesPage = new guidanceIssuesPage(driver);
        guidanceIssuesPage.selectIssueTypeGcd("Machining Issues, Obstructed Surfaces", "Curved Walls", "CurvedWall:21");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Rounding: Feature is not accessible by an active Setup. Activate a new Setup, override operation feasibility, or select a specialized machining operation."));
        assertThat(guidanceIssuesPage.getGuidanceCell("Curved Walls", "Count"), is(equalTo("2")));

        guidanceIssuesPage.selectIssueTypeGcd("Slow Machining Operations, Contoured Surface", "Curved Surfaces", "CurvedSurface:2");
        assertThat(guidanceIssuesPage.getGuidanceCell("Curved Surfaces", "Count"), is(equalTo("5")));
    }*/

    @Test
    @TestRail(id = {6438})
    @Description("Verify Sharp corners on curved walls are highlighted")
    public void sharpCornerCurvedWall() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "1379344_BEFORE_DTC";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("AISI 1010")
                .selectMaterial(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario(3)
                .openDesignGuidance()
                .selectIssueTypeGcd("Machining Issues, Sharp Corner", "Curved Wall", "CurvedWall:22");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Side Milling: Feature contains a sharp corner that would require a zero tool diameter. " +
                "If sharp corner was intentional, try activating a new setup or changing process/operation. If sharp corner was unintentional, update CAD model or override operation feasibility rule."));
    }

    /*    @Test
    @TestRail(id = {6433", "6436", "6437"})
    @Description("Verify the investigate tab correctly presents features & conditions which impact cost")
    public void stockMachineDTC() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "nist_ftc_06_asme1_sw1500_rd.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        investigationPage = settingsPage.save(ExplorePage.class)
            .uploadComponentAndOpen(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Holes and Fillets");

        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "Tool Count"), is(equalTo("4")));
        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "GCD Count"), is(equalTo("12")));
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "Tool Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "GCD Count"), is(equalTo("22")));

        investigationPage.selectInvestigationTopic("Machining Setups");
        assertThat(investigationPage.getInvestigationCell("SetupAxis:1", "GCD Count"), is(equalTo("38")));
        assertThat(investigationPage.getInvestigationCell("SetupAxis:2", "GCD Count"), is(equalTo("59")));
        assertThat(investigationPage.getInvestigationCell("SetupAxis:4", "GCD Count"), is(equalTo("39")));
    }*/

    @Test
    @TestRail(id = {6452})
    @Description("Verify tolerances which induce an additional operation are correctly respected in CI Design geometry tab")
    public void toleranceInducingTest() {
        final ProcessGroupEnum processGroupEnum = STOCK_MACHINING;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
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

        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Critical");

        tolerancesPage = evaluatePage.openDesignGuidance()
                .openTolerancesTab()
                .selectIssueTypeGcd(ToleranceEnum.DIAMTOLERANCE.getToleranceName(), "CurvedWall:99");

        softAssertions.assertThat(tolerancesPage.getOperation("CurvedWall:99").contains("Contouring / Bulk Milling Surface / General Grinding"));
        softAssertions.assertThat(tolerancesPage.getOperation("SimpleHole:13").contains("Waterjet Cutting"));

        softAssertions.assertAll();
    }

}