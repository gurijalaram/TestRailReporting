package com.apriori.cid.ui.tests.evaluate.dtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.TolerancesPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ToleranceEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class DTCMachiningTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private TolerancesPage tolerancesPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public DTCMachiningTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (component.getUser() != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @TestRail(id = {7751})
    @Description("Testing DTC Machining Keyseat Mill")
    public void testDTCKeyseat() {
        component = new ComponentRequestUtil().getComponent("Machining-DTC_Issue_KeyseatMillAccessibility");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
    @Tag(SMOKE)
    @TestRail(id = {6439})
    @Description("Testing DTC Machining Sharp Corner - Planar Face - Contouring")
    public void testDTCSharpCorner() {
        component = new ComponentRequestUtil().getComponent("Machining-DTC_Issue_SharpCorner-PlanarFace");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("Deep hole");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("Machining-DTC_Issues_MissingSetups_CurvedWall-PlanarFace");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("1379344_BEFORE_DTC");

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        component = new ComponentRequestUtil().getComponent("DTCCastingIssues");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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