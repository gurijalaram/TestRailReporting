package test.java.evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.pages.settings.SettingsPage;
import main.java.pages.settings.ToleranceSettingsPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import org.junit.Test;

import java.time.LocalDateTime;

public class DTCCastingTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;

    public DTCCastingTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"C1045, C1050, C1054, C1056, C1058, C1049"}, tags = {"smoke"})
    @Description("Testing DTC Casting - Sand Casting")
    public void sandCastingDTC() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Draft  Issue, Draft Angle", "Curved Walls", "CurvedWall:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Part of this surface is below the minimum recommended draft angle."));

        guidancePage.selectIssueTypeAndGCD("Radius  Issue", "Minimum Wall Thickness", "Component:1");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Material  Issue", "Minimum Internal Edge Radius", "SharpEdge:25");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. Internal Edge Radius is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Hole  Issue", "Minimum Hole Diameter", "SimpleHole:10");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. Hole Diameter is less than the minimum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Hole  Issue, Maximum Hole Depth", "Simple Holes", "SimpleHole:2");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Sand Casting is not feasible. The Hole Depth is greater than the maximum limit with this material."));

        guidancePage.selectIssueTypeAndGCD("Machining Issues", "Obstructed Surfaces", "PlanarFace:4");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Facing: Feature is obstructed. Override operation feasibility, select a specialized machining operation, or modify CAD geometry."));
    }

    @Test
    @TestRail(testCaseId = {"C1044"}, tags = {"smoke"})
    @Description("Validate Tolerance counts are correct for DTC Casting")
    public void tolCountCastingDTC() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();
        //assertThat(evaluatePage);
    }
}