package test.java.evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class DTCMouldingEdgeRadiusTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private GuidancePage guidancePage;

    public DTCMouldingEdgeRadiusTests() {
        super();
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius Internal")
    @Severity(SeverityLevel.NORMAL)
    public void testMouldingEdgeInternal() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        guidancePage = explorePage.uploadFile("ScenarioEdgeRadiusInternal", new FileResourceUtil().getResourceFile("Plastic moulded cap edge Radius.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Radii Issue", "Minimum Internal Edge Radius", "SharpEdge:8");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Internal Edge Radius is less than the minimum limit"));
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius External")
    @Severity(SeverityLevel.NORMAL)
    public void testMouldingEdgeExternal() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("ScenarioEdgeRadiusExternal", new FileResourceUtil().getResourceFile("Plastic moulded cap edge Radius.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Radii Issue", "Minimum External Edge Radius", "SharpEdge:9");

        assertThat(guidancePage.getGuidanceMessage(), containsString("External Edge Radius is less than the minimum limit"));
    }
}