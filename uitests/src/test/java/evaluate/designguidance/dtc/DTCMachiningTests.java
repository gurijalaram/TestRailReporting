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
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class DTCMachiningTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private GuidancePage guidancePage;

    public DTCMachiningTests() {
        super();
    }

    @Test
    @Description("Testing DTC Machining Keyseat Mill")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCKeyseat() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_KeyseatMillAccessibility.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Keyseat Mill Accessibility", "Slot:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("There is no available Groove milling tool that can fit inside the Slot."));
    }

    @Test
    @Description("Testing DTC Machining Curved Surface")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCCurvedSurface() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues, Sharp Corner", "CurvedSurface", "CurvedSurface:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Feature contains a sharp corner"));
    }

    @Test
    @Description("Testing DTC Machining Sharp Corner")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCSharpCorner() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner-PlanarFace.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Sharp Corner", "PlanarFace:5");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Feature contains a sharp corner"));
    }

    @Test
    @Description("Testing DTC Machining Side Milling")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCSideMilling() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SideMillingLengthDia.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Side Milling L/D", "CurvedWall:3");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Required tool exceeds the max L/D Ratio"));
    }

    @Test
    @Description("Testing DTC Machining Missing Setups")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCMissingSetup() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issues_MissingSetups_CurvedWall-PlanarFace.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Missing Setups", "PlanarFace:6");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Setup Axis was not automatically assigned"));
    }

    @Test
    @Description("Testing DTC Machining Planar Face")
    @Severity(SeverityLevel.NORMAL)
    public void testDTCPlanarFace() {
        loginPage = new LoginPage(driver);
        guidancePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues, Obstructed Surfaces", "PlanarFace", "PlanarFace:9");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Feature is obstructed"));
    }
}