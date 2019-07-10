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

public class DTCMouldingTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;

    private String filePath = new Scanner(TolerancesTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public DTCMouldingTests() {
        super();
    }

    /**
     * Testing DTC Plastic Moulding Edge Radius Internal
     */
    @Test
    public void testMouldingEdgeInternal() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("ScenarioEdgeRadiusInternal", filePath, "Plastic moulded cap edge Radius.CATPart")
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Radii  Issue", "Minimum Internal Edge Radius", "SharpEdge:8");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Internal Edge Radius is less than the minimum limit"));
    }

    /**
     * Testing DTC Plastic Moulding Edge Radius External
     */
    @Test
    public void testMouldingEdgeExternal() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("ScenarioEdgeRadiusExternal", filePath, "Plastic moulded cap edge Radius.CATPart")
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Radii  Issue", "Minimum External Edge Radius", "SharpEdge:9");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("External Edge Radius is less than the minimum limit"));
    }
}