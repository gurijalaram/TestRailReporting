package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.analysis.PropertiesDialogPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GeometryPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.TolerancePage;
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
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class DTCCastingTests extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private GeometryPage geometryPage;
    private PropertiesDialogPage propertiesDialogPage;
    private TolerancePage tolerancePage;

    public DTCCastingTests() {
        super();
    }

    @Test
    @Issue("AP-55576")
    @TestRail(testCaseId = {"1045", "1050", "1054", "1056", "1058", "1049", "286"})
    @Description("Testing DTC Casting - Sand Casting")
    public void sandCastingDTC() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario(5)
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft Issue, Draft Angle", "Curved Walls", "CurvedWall:18");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));

        guidancePage.selectIssueTypeAndGCD("Material Issue", "Minimum Wall Thickness", "Component:1");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Radius Issue", "Minimum Internal Edge Radius", "SharpEdge:25");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. Internal Edge Radius is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Hole Issue", "Minimum Hole Diameter", "SimpleHole:10");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. Hole Diameter is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Hole Issue, Maximum Hole Depth", "Simple Holes", "SimpleHole:2");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. The Hole Depth is greater than the maximum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Machining Issues", "Obstructed Surfaces", "PlanarFace:4");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Facing: Feature is obstructed. Override operation feasibility, select a specialized machining operation, or modify CAD geometry."));

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1596"})
    @Description("Ensure that the Geometry tab section is expandable table of GCDs to third hierarchical level with total at GCD type level")
    public void geometryTest() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        geometryPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario(3)
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Surfaces", "Planar Faces", "PlanarFace:1");

        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Properties");
        assertThat(propertiesDialogPage.getProperties("Finished Area (mm^2)"), containsString("85.62"));

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();
    }

    @Test
    @TestRail(testCaseId = {"1042", "1046", "1051", "1053", "1055", "1057"})
    @Description("Min & Max DTC checks for Die Casted Part")
    public void highPressureDieCasting() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario(3)
            .openDesignGuidance()
            .expandGuidanceTab()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft  Issue, Draft Angle", "Curved Walls", "CurvedWall:6");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));
        assertThat(guidancePage.getGuidanceCell("Curved Walls", "Count"), is(equalTo("89")));

        guidancePage.selectIssueTypeAndGCD("Material  Issue", "Minimum Wall Thickness", "Component:1");
        assertThat(guidancePage.getGuidanceMessage(), containsString("High Pressure Die Casting is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Material  Issue", "Maximum Wall Thickness", "Component:1");
        assertThat(guidancePage.getGuidanceMessage(), containsString("High Pressure Die Casting is not feasible. Part Thickness is more than the maximum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Radius  Issue", "Minimum Internal Edge Radius", "SharpEdge:38");
        assertThat(guidancePage.getGuidanceMessage(), containsString("High Pressure Die Casting is not feasible. Internal Edge Radius is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Hole  Issue", "Minimum Hole Diameter", "SimpleHole:12");
        assertThat(guidancePage.getGuidanceMessage(), containsString("High Pressure Die Casting is not feasible. Hole Diameter is less than the minimum limit with this material."));
    }

    @Test
    @TestRail(testCaseId = {"1046", "1051", "1055"})
    @Description("Ensure that the Geometry tab section is expandable table of GCDs to third hierarchical level with total at GCD type level")
    public void gravityDieCasting() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario(3)
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Gravity Die Cast")
            .apply()
            .closeProcessPanel()
            .costScenario(2)
            .openDesignGuidance()
            .expandGuidanceTab()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft  Issue, Draft Angle", "Curved Walls", "CurvedWall:7");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));

        guidancePage.selectIssueTypeAndGCD("Material  Issue", "Minimum Wall Thickness", "Component:1");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Gravity Die Casting is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Radius  Issue", "Minimum Internal Edge Radius", "SharpEdge:38");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Gravity Die Casting is not feasible. Internal Edge Radius is less than the minimum limit with this material."));
    }

    @Test
    @TestRail(testCaseId = {"1044"})
    @Description("Validate Tolerance counts are correct")
    public void dtcTolerances() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .expandGuidanceTab()
            .openTolerancesTab();

        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.DIAMTOLERANCE.getToleranceName()), "9"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.FLATNESS.getToleranceName()), "5"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.PROFILESURFACE.getToleranceName()), "6"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.ROUGHNESSRA.getToleranceName()), "3"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.STRAIGHTNESS.getToleranceName()), "3"), Matchers.is(true));
    }
}