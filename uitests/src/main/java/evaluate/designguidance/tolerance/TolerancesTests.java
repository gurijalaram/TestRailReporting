package main.java.evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;

import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.investigation.InvestigationPage;
import main.java.pages.evaluate.designguidance.investigation.ThreadingPage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Scanner;

public class TolerancesTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;
    private String filePath = new Scanner(TolerancesTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public TolerancesTests() {
        super();
    }

    /**
     * Test to check edit thread button is disabled
     */
    @Test
    public void threadButtonDisabled() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "DTCCastingIssues.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getEditButton().isEnabled(), Matchers.is(true));
    }

    /**
     * Test to check thread length persist
     */
    @Test
    public void editThread() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "DTCCastingIssues.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        /*explorePage = new ExplorePage(driver);
        explorePage.selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openScenario("Dtccastingissues", "Scenario b")
            .openDesignGuidance();*/

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:10")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.28")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(Matchers.equalTo("0.28")));
    }
}