package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ToleranceEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.designguidance.GuidancePage;
import pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import pageobjects.pages.evaluate.designguidance.tolerances.TolerancePage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.pages.settings.SettingsPage;
import pageobjects.pages.settings.ToleranceSettingsPage;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DTCMachiningTests extends TestBase {

    private CidLoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private InvestigationPage investigationPage;
    private UserCredentials currentUser;
    private TolerancePage tolerancePage;

    private File resourceFile;

    public DTCMachiningTests() {
        super();
    }

    @After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @Description("Testing DTC Machining Keyseat Mill")
    public void testDTCKeyseat() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issue_KeyseatMillAccessibility.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues", "Keyseat Mill Accessibility", "Slot:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("There is no available Groove milling tool that can fit inside the Slot."));
    }

    @Test
    @TestRail(testCaseId = {"1800"})
    @Description("Testing DTC Machining Sharp Corner on a Curved Surface")
    public void testDTCCurvedSurface() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues, Sharp Corner", "Curved Surfaces", "CurvedSurface:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Contouring: Feature contains a sharp corner that would require a zero tool diameter"));
    }

    @Test
    @TestRail(testCaseId = {"1798"})
    @Description("Testing DTC Machining Sharp Corner - Planar Face - Contouring")
    public void testDTCSharpCorner() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issue_SharpCorner-PlanarFace.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues", "Sharp Corner", "PlanarFace:5");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Contouring: Feature contains a sharp corner that would require a zero tool diameter."));
    }

    @Test
    @TestRail(testCaseId = {"1804"})
    @Description("Testing DTC Machining Side Milling L/D Ratio")
    public void testDTCSideMilling() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issue_SideMillingLengthDia.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues", "Side Milling L/D", "CurvedWall:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Required tool exceeds the max L/D Ratio"));
    }

    @Test
    @TestRail(testCaseId = {"1803"})
    @Description("Testing DTC Machining Missing Setups")
    public void testDTCMissingSetup() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issues_MissingSetups_CurvedWall-PlanarFace.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues", "Missing Setups", "PlanarFace:6");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Setup Axis was not automatically assigned"));
    }

    @Test
    @TestRail(testCaseId = {"1801"})
    @Description("Verify obstructed surfaces on planar faces")
    public void obstructedSurfacePlanarFace() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues, Obstructed Surfaces", "Planar Faces", "PlanarFace:9");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Feature is obstructed"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1799", "1802", "1806", "1593"})
    @Description("Ensure that  'Guidance' includes: - Issue type count - DTC Messaging for each guidance instance")
    public void stockMachiningDTC() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "nist_ftc_06_asme1_sw1500_rd.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
                .openSettings()
                .openTolerancesTab()
                .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues, Sharp Corner", "Planar Faces", "PlanarFace:10");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Facing: Feature contains a sharp corner"));

        guidancePage = new GuidancePage(driver);
        guidancePage.selectIssueTypeAndGCD("Machining Issues, Obstructed Surfaces", "Curved Walls", "CurvedWall:21");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Side Milling: Feature is obstructed. Override operation feasibility, select a specialized machining operation, or modify CAD geometry."));
        assertThat(guidancePage.getGuidanceCell("Curved Walls", "Count"), is(equalTo("2")));

        guidancePage.selectIssueTypeAndGCD("Slow Machining Operations, Contoured Surface", "Curved Surfaces", "CurvedSurface:2");
        assertThat(guidancePage.getGuidanceCell("Curved Surfaces", "Count"), is(equalTo("2")));
    }

    @Test
    @TestRail(testCaseId = {"1797"})
    @Description("Verify Sharp corners on curved walls are highlighted")
    public void sharpCornerCurvedWall() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "1379344_BEFORE_DTC.stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Machining Issues, Sharp Corner", "Curved Walls", "CurvedWall:22");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Contouring: Feature contains a sharp corner"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1792", "1795", "1796"})
    @Description("Verify the investigate tab correctly presents features & conditions which impact cost")
    public void stockMachineDTC() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "nist_ftc_06_asme1_sw1500_rd.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
                .openSettings()
                .openTolerancesTab()
                .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        investigationPage = settingsPage.save(ExplorePage.class)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openInvestigationTab()
                .selectInvestigationTopic("Holes and Fillets");

        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "Tool Count"), is(equalTo("4")));
        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "GCD Count"), is(equalTo("12")));
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "Tool Count"), is(equalTo("2")));
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "GCD Count"), is(equalTo("25")));

        investigationPage.selectInvestigationTopic("Machining Setups");
        assertThat(investigationPage.getInvestigationCell("SetupAxis:4", "GCD Count"), is(equalTo("2")));
        assertThat(investigationPage.getInvestigationCell("SetupAxis:12", "GCD Count"), is(equalTo("100")));
        assertThat(investigationPage.getInvestigationCell("SetupAxis:13", "GCD Count"), is(equalTo("34")));
    }

    @Test
    @TestRail(testCaseId = {"1808", "1809"})
    @Description("Verify tolerances which induce an additional operation are correctly respected in CI Design geometry tab")
    public void toleranceInducingTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
                .openSettings()
                .openTolerancesTab()
                .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openTolerancesTab()
                .selectToleranceType(ToleranceEnum.DIAMTOLERANCE.getToleranceName())
                .selectGcd("CurvedWall:99");
        assertThat(tolerancePage.getGCDCell("CurvedWall:99", "Operation"), Matchers.is(Matchers.equalTo("Contouring / Bulk Milling Surface / General Grinding")));

        tolerancePage.selectGcd("SimpleHole:13");
        assertThat(tolerancePage.getGCDCell("SimpleHole:13", "Operation"), Matchers.is(Matchers.equalTo("Waterjet Cutting")));
    }
}