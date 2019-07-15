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

public class DTCMouldingPartThicknessTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;

    private String filePath = new Scanner(TolerancesTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public DTCMouldingPartThicknessTests() {
        super();
    }

    /**
     * Testing DTC Moulding Thickness Minimum
     */
    @Test
    public void testDTCMouldingThicknessMin() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("ScenarioMinThickness", filePath, "Plastic moulded cap thinPart.SLDPRT")
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issues", "Minimum Wall Thickness", "Component:1");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is less than the minimum limit with this material."));
    }

    /**
     * Testing DTC Moulding Thickness Maximum
     */
    @Test
    public void testDTCMouldingThicknessMax() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("ScenarioMaxThickness", filePath, "Plastic moulded cap thinPart.SLDPRT")
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openGuidanceTab()
            .selectIssueTypeAndGCD("Material Issues", "Maximum Wall Thickness", "Component:1");

        assertThat(new GuidancePage(driver).getGuidanceMessage(), containsString("Injection Mold is not feasible. Part Thickness is more than the maximum limit with this material."));
    }
}