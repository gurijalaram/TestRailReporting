package main.java.evaluate.designguidance.tolerance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.investigation.InvestigationPage;
import main.java.pages.evaluate.designguidance.investigation.ThreadingPage;
import main.java.pages.evaluate.designguidance.tolerances.WarningPage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Scanner;

public class TolerancesTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;

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

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:20")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.28")
            .apply(InvestigationPage.class);

        new InvestigationPage(driver).selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("0.28")));
    }

    /**
     * Test to verify costed thread
     */
    @Test
    public void verifyCostedThread() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "DTCCastingIssues.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:21")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.28")
            .apply(InvestigationPage.class);

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:10");

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("0.28")));
    }

    /**
     * Test to set dropdown value to no
     */
    @Test
    public void setDropdownValueNo() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "DTCCastingIssues.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:22")
            .selectThreadDropdown("No")
            .apply(InvestigationPage.class);

        new InvestigationPage(driver).selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("")));
    }

    /**
     * Test to set dropdown value to yes
     */
    @Test
    public void setDropdownValueYes() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "DTCCastingIssues.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:23")
            .selectThreadDropdown("Yes")
            .enterThreadLength("")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("")));
    }

    /**
     * Testing warning message displayed when thread length is removed
     */
    @Test
    public void costedThreadLengthRemoved() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "DTCCastingIssues.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:2")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.25")
            .apply(InvestigationPage.class);

        investigationPage = new InvestigationPage(driver);
        investigationPage.selectEditButton()
            .removeThreadLength()
            .apply(WarningPage.class);

        assertThat(new WarningPage(driver).getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }
}