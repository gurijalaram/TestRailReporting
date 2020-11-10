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
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.designguidance.GuidancePage;
import pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.pages.settings.SettingsPage;
import pageobjects.pages.settings.ToleranceSettingsPage;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SheetMetalDTCTests extends TestBase {

    private CidLoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;
    private UserCredentials currentUser;

    private File resourceFile;

    public SheetMetalDTCTests() {
        super();
    }

    @After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @TestRail(testCaseId = {"1839", "1842", "1843"})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCHoles() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SheMetDTC.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

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
                .selectIssueTypeAndGCD("Hole Issue", "Diameter", "SimpleHole:2");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Hole is too small to be cut with Plasma Cutting"));

        guidancePage.selectIssueTypeAndGCD("Hole Issue", "Property", "SimpleHole:4");
        assertThat(guidancePage.getGuidanceMessage(), containsString("This is a blind hole and cannot be cut with Plasma Cutting"));

        guidancePage.selectIssueTypeAndGCD("Machined GCDs", "Facing", "PlanarFace:13");
        assertThat(guidancePage.getGuidanceCell("Facing", "Count"), is(equalTo("1")));

        guidancePage.selectIssueTypeAndGCD("Machined GCDs", "Center Drilling / Pecking", "SimpleHole:2");
        assertThat(guidancePage.getGuidanceCell("Center Drilling / Pecking", "Count"), is(equalTo("1")));

        guidancePage.selectIssueTypeAndGCD("Machined GCDs", "Center Drilling / Drilling", "SimpleHole:3");
        assertThat(guidancePage.getGuidanceCell("Center Drilling / Drilling", "Count"), is(equalTo("3")));
    }

    @Test
    @TestRail(testCaseId = {"1840", "1841"})
    @Description("Verify Proximity Issues Are Highlighted")
    public void sheetMetalProximity() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SheetMetalTray.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        guidancePage = loginPage.login(currentUser)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openGuidanceTab()
                .selectIssueTypeAndGCD("Proximity Warning, Distance", "Complex Holes", "ComplexHole:10");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Hole is too close to to the following bend(s): StraightBend:3"));

        guidancePage.selectIssueTypeAndGCD("Proximity Warning, Distance", "Simple Holes", "SimpleHole:2");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Hole is too close to the following hole(s): SimpleHole:3, SimpleHole:1"));
    }

    @Test
    @TestRail(testCaseId = {"1838", "1844"})
    @Description("Verify Bend Issues Are Highlighted")
    public void sheetMetalBends() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "extremebends.prt.1");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

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
                .selectIssueTypeAndGCD("Bend Issue", "Intersects", "StraightBend:4");

        assertThat(guidancePage.getGuidanceMessage(), containsString("There is an intersection with a form and therefore cannot be made with Bending"));

        guidancePage.selectIssueTypeAndGCD("Bend Issue", "Length", "StraightBend:3");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Bend flap is too small to be formed with Bending"));

        guidancePage.selectIssueTypeAndGCD("Bend Issue", "Radius", "StraightBend:3");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Bend radius is too small"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1829"})
    @Description("Verify the Design Guidance tile presents the correct counts for number of GCDs, warnings, guidance issues, & tolerances for a part")
    public void tileDTC() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "extremebends.prt.1");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
                .openSettings()
                .openTolerancesTab()
                .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
                .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario();

        assertThat(evaluatePage.getWarningsCount(), is("5"));
        assertThat(evaluatePage.getGuidanceIssuesCount(), is("9"));
        assertThat(evaluatePage.getGcdTolerancesCount(), is("22"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1834", "1835", "1836", "1837"})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCInvestigation() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SheMetDTC.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

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

        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "Tool Count"), is(equalTo("2")));
        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "GCD Count"), is(equalTo("4")));

        investigationPage.selectInvestigationTopic("Distinct Sizes Count");

        assertThat(investigationPage.getInvestigationCell("Bend Radius", "Tool Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("Bend Radius", "GCD Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("Hole Size", "Tool Count"), is(equalTo("2")));
        assertThat(investigationPage.getInvestigationCell("Hole Size", "GCD Count"), is(equalTo("4")));

        investigationPage.selectInvestigationTopic("Machining Setups");

        assertThat(investigationPage.getInvestigationCell("SetupAxis:1", "GCD Count"), is(equalTo("14")));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1845", "719"})
    @Description("Verify tolerances which induce an additional operation")
    public void toleranceAdditionalOp() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic_matPMI.prt.1");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

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
                .selectIssueTypeAndGCD("GCDs With Special Finishing", "Reaming", "SimpleHole:2");

        assertThat(guidancePage.getGCDGuidance("SimpleHole:2", "Current"), is(equalTo("0.02")));
        assertThat(guidancePage.getGCDGuidance("SimpleHole:2", "Basic Machining Threshold"), is(equalTo("0.06")));
    }
}
