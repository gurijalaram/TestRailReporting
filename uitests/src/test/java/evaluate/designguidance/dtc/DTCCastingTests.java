package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class DTCCastingTests extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;

    public DTCCastingTests() {
        super();
    }

    @Test
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
}