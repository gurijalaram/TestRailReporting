package main.java.evaluate.designguidance.tolerance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.investigation.InvestigationPage;
import main.java.pages.evaluate.designguidance.investigation.ThreadingPage;
import main.java.pages.evaluate.designguidance.tolerances.WarningPage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.pages.settings.SettingsPage;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Scanner;

public class TolerancesTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;
    private SettingsPage settingsPage;

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
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getEditButton().isEnabled(), Matchers.is(false));
    }

    /**
     * Test to check thread length persist
     */
    @Test
    public void editThread() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:2")
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
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:2")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.28")
            .apply(InvestigationPage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:2");

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
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:2")
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
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.64")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("0.64")));
    }

    /**
     * Testing warning message displayed when thread length is removed
     */
    @Test
    public void costedThreadLengthRemoved() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:5")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.25")
            .apply(InvestigationPage.class);

        investigationPage = new InvestigationPage(driver);
        investigationPage.selectEditButton()
            .removeThreadLength()
            .apply(WarningPage.class);

        assertThat(new WarningPage(driver).getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    /**
     * Testing changing the thread value and cancelling doesn't remove the value
     */
    @Test
    public void changeThreadValueCancel() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("3.50")
            .apply(InvestigationPage.class);

        investigationPage = new InvestigationPage(driver);
        investigationPage.selectEditButton()
            .enterThreadLength("1.70")
            .cancel();

        new InvestigationPage(driver).selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("3.50")));
    }

    /**
     * Testing that adding text values in the thread length shows a warning message
     */
    @Test
    public void junkValuesCharTest() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:5")
            .selectThreadDropdown("Yes")
            .enterThreadLength("apriori")
            .apply(WarningPage.class);

        assertThat(new WarningPage(driver).getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }


    /**
     * Testing that adding no value in the thread shows a warning message
     */
    @Test
    public void junkValueTest() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:3")
            .selectThreadDropdown("Yes")
            .enterThreadLength("")
            .apply(WarningPage.class);

        assertThat(new WarningPage(driver).getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    /**
     * Testing a public thread cannot be edited
     */
    @Test
    public void cannotEditPublicThread() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .publishScenario();

        explorePage = new ExplorePage(driver);
        explorePage.openScenario("DTCCastingIssues", "Scenario b")
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getEditButton().isEnabled(), is(false));
    }

    /**
     * Testing thread length persist when attributes are changed
     */
    @Test
    public void maintainingThreadChangeAttributes() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.CATPart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:3")
            .selectThreadDropdown("Yes")
            .enterThreadLength("4.85")
            .apply(InvestigationPage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 2007")
            .apply()
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:3");

        assertThat(new ThreadingPage(driver).getThreadLength(), equalTo("4.85"));
    }

    /**
     * Testing thread units persist when changed to inches
     */
    @Test
    public void validateThreadUnitsInches() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        explorePage.openSettings()
            .changeDisplayUnits("English")
            .save(EvaluatePage.class);

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getThreadHeader(), containsString("(in)"));
    }

    /**
     * Testing thread units persist when changed to millimetres
     */
    @Test
    public void validateThreadUnitsMM() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        explorePage.openSettings()
            .changeDisplayUnits("System")
            .save(EvaluatePage.class);

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getThreadHeader(), containsString("mm"));
    }

    /**
     * Testing threading persist when secondary process is added
     */
    @Test
    public void maintainingThreadSecondaryProcessGroup() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:2")
            .selectThreadDropdown("Yes")
            .enterThreadLength("4.85")
            .apply(InvestigationPage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:2");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }

    /**
     * Testing compatible thread length for DTC files
     */
    @Test
    public void threadsCompatibleCadDTC() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }

    /**
     * Testing compatible thread length for NX files
     */
    @Test
    public void threadsCompatibleCadNX() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:3");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }

    /**
     * Testing compatible thread length for Creo files
     */
    @Test
    public void threadsCompatibleCadCreo() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", filePath, "PMI_ALLTOLTYPESCATIA.catpart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:3");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }
}