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
import org.junit.Test;

import java.time.LocalDateTime;

public class DTCMouldingPartThicknessTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private GuidancePage guidancePage;

    public DTCMouldingPartThicknessTests() {
        super();
    }

    @Test
    @Description("Testing DTC Moulding Thickness Minimum")
    public void testDTCMouldingThicknessMin() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Minimum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is less than the minimum limit with this material."));
    }

    @Test
    @Description("Testing DTC Moulding Thickness Maximum")
    public void testDTCMouldingThicknessMax() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Plastic moulded cap thickPart.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issue", "Maximum Wall Thickness", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
    }
}