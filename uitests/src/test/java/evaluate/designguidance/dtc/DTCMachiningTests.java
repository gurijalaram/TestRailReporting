package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DTCMachiningTests extends TestBase {

    private CIDLoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private InvestigationPage investigationPage;
    private UserCredentials currentUser;

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

        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issue_KeyseatMillAccessibility.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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
        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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

        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner-PlanarFace.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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

        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SideMillingLengthDia.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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

        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issues_MissingSetups_CurvedWall-PlanarFace.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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

        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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

        resourceFile = new FileResourceUtil().getResourceFile("nist_ftc_06_asme1_sw1500_rd.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
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

        resourceFile = new FileResourceUtil().getResourceFile("1379344_BEFORE_DTC.stp");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        guidancePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
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

        resourceFile = new FileResourceUtil().getResourceFile("nist_ftc_06_asme1_sw1500_rd.SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        investigationPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Holes and Fillets");

        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "Tool Count"), is(equalTo("4")));
        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "GCD Count"), is(equalTo("12")));
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "Tool Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("Fillet Radius - Standard", "GCD Count"), is(equalTo("20")));

        investigationPage.selectInvestigationTopic("Machining Setups");
        assertThat(investigationPage.getInvestigationCell("SetupAxis:1", "GCD Count"), is(equalTo("46")));
        assertThat(investigationPage.getInvestigationCell("SetupAxis:2", "GCD Count"), is(equalTo("56")));
        assertThat(investigationPage.getInvestigationCell("SetupAxis:4", "GCD Count"), is(equalTo("34")));
    }
}