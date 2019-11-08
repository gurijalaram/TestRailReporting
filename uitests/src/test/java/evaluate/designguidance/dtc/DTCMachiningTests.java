package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class DTCMachiningTests extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private InvestigationPage investigationPage;

    public DTCMachiningTests() {
        super();
    }

    @After
    public void resetToleranceSettings() {
        new AfterTestUtil(driver).resetToleranceSettings();
    }

    @Test
    @Description("Testing DTC Machining Keyseat Mill")
    public void testDTCKeyseat() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_KeyseatMillAccessibility.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner-PlanarFace.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SideMillingLengthDia.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issues_MissingSetups_CurvedWall-PlanarFace.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .expandGuidancePanel()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues, Obstructed Surfaces", "Planar Faces", "PlanarFace:9");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Feature is obstructed"));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1799", "1802", "1806", "1593"})
    @Description("Ensure that  'Guidance' includes: - Issue type count - DTC Messaging for each guidance instance")
    public void stockMachiningDTC() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UserUtil.getUser())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("nist_ftc_06_asme1_sw1500_rd.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .expandGuidancePanel()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues, Sharp Corner", "Planar Faces", "PlanarFace:10");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Facing: Feature contains a sharp corner"));

        guidancePage = new GuidancePage(driver);
        guidancePage.selectIssueTypeAndGCD("Machining Issues, Obstructed Surfaces", "Curved Walls", "CurvedWall:21");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Rounding: Feature is not accessible by an active Setup. "));
        assertThat(guidancePage.getGuidanceCell("Curved Walls", "Count"), is(equalTo("2")));

        guidancePage.selectIssueTypeAndGCD("Slow Machining Operations, Contoured Surface", "Curved Surfaces", "CurvedSurface:2");
        assertThat(guidancePage.getGuidanceCell("Curved Surfaces", "Count"), is(equalTo("2")));
    }

    @Test
    @TestRail(testCaseId = {"1797"})
    @Description("Verify Sharp corners on curved walls are highlighted")
    public void sharpCornerCurvedWall() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("1379344_BEFORE_DTC.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues, Sharp Corner", "Curved Walls", "CurvedWall:22");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Contouring: Feature contains a sharp corner"));
    }

    @Test
    @TestRail(testCaseId = {"1792", "1795", "1796"})
    @Description("Verify the investigate tab correctly presents features & conditions which impact cost")
    public void stockMachineDTC() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UserUtil.getUser())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        investigationPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("nist_ftc_06_asme1_sw1500_rd.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .expandGuidancePanel()
            .openInvestigationTab()
            .selectInvestigationTopic("Holes and Fillets")
            .findIssueType("Hole - Standard");

        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "Tool Count"), is(equalTo("4")));
        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "GCD Count"), is(equalTo("12")));

        investigationPage.findIssueType("Fillet Radius - Standard");
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "Tool Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "GCD Count"), is(equalTo("20")));

        investigationPage.selectInvestigationTopic("Machining Setups")
            .findIssueType("SetupAxis:1");
        assertThat(investigationPage.getInvestigationCell("SetupAxis:1", "GCD Count"), is(equalTo("46")));

        investigationPage.findIssueType("SetupAxis:2");
        assertThat(investigationPage.getInvestigationCell("SetupAxis:2", "GCD Count"), is(equalTo("56")));

        investigationPage.findIssueType("SetupAxis:4");
        assertThat(investigationPage.getInvestigationCell("SetupAxis:4", "GCD Count"), is(equalTo("34")));
    }
}