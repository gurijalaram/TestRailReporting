package main.java.evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.evaluate.designguidance.tolerance.TolerancesTests;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.GuidancePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class DTCMachiningTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;

    private String filePath = new Scanner(TolerancesTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public DTCMachiningTests() {
        super();
    }

    /**
     * Testing DTC Machining Keyseat Mill
     */
    @Test
    public void testDTCKeyseat() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Initial", filePath, "Machining-DTC_Issue_KeyseatMillAccessibility.CATPart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Keyseat Mill Accessibility", "Slot:3");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("There is no available Groove milling tool that can fit inside the Slot."));
    }

    /**
     * Testing DTC Machining Curved Surface
     */
    @Test
    public void testDTCCurvedSurface() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Initial", filePath, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues, Sharp Corner", "CurvedSurface", "CurvedSurface:1");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Feature contains a sharp corner"));
    }

    /**
     * Testing DTC Machining Sharp Corner
     */
    @Test
    public void testDTCSharpCorner() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Initial", filePath, "Machining-DTC_Issue_SharpCorner-PlanarFace.CATPart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Sharp Corner", "PlanarFace:5");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Feature contains a sharp corner"));
    }

    /**
     * Testing DTC Machining Side Milling
     */
    @Test
    public void testDTCSideMilling() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Initial", filePath, "Machining-DTC_Issue_SideMillingLengthDia.SLDPRT")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Side Milling L/D", "CurvedWall:3");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Required tool exceeds the max L/D Ratio"));
    }

    /**
     * Testing DTC Machining Missing Setups
     */
    @Test
    public void testDTCMissingSetup() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Initial", filePath, "Machining-DTC_Issues_MissingSetups_CurvedWall-PlanarFace.CATPart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues", "Missing Setups", "PlanarFace:6");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Setup Axis was not automatically assigned"));
    }

    /**
     * Testing DTC Machining Planar Face
     */
    @Test
    public void testDTCPlanarFace() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Initial", filePath, "Machining-DTC_Issues_ObstructedSurfaces_CurvedWall-PlanarFace.CATPart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Machining Issues, Obstructed Surfaces", "PlanarFace", "PlanarFace:9");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Feature is obstructed"));
    }
}