package test.java.evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.Util;
import org.junit.Test;

public class DTCMouldingEdgeRadiusTests extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;

    public DTCMouldingEdgeRadiusTests() {
        super();
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius Internal")
    public void testMouldingEdgeInternal() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap edge Radius.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Radii  Issue", "Minimum Internal Edge Radius", "SharpEdge:8");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Internal Edge Radius is less than the minimum limit"));
    }

    @Test
    @Description("Testing DTC Plastic Moulding Edge Radius External")
    public void testMouldingEdgeExternal() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap edge Radius.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Radii  Issue", "Minimum External Edge Radius", "SharpEdge:9");

        assertThat(guidancePage.getGuidanceMessage(), containsString("External Edge Radius is less than the minimum limit"));
    }
}