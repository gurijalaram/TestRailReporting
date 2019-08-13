package test.java.evaluate.designguidance.thread;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.investigation.InvestigationPage;
import main.java.pages.evaluate.designguidance.investigation.ThreadingPage;
import main.java.pages.evaluate.designguidance.tolerances.WarningPage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

public class ThreadTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private DesignGuidancePage designGuidancePage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;
    private ThreadingPage threadingPage;
    private WarningPage warningPage;

    public ThreadTests() {
        super();
    }

    @Test
    @Description("Test to check edit thread button is disabled")
    @Severity(SeverityLevel.CRITICAL)
    public void threadButtonDisabled() {
        loginPage = new LoginPage(driver);
        investigationPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getEditButton().isEnabled(), is(false));
    }

    @Test
    @Description("Test to check thread length persist")
    @Severity(SeverityLevel.CRITICAL)
    public void editThread() {
        loginPage = new LoginPage(driver);
        threadingPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.28")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(threadingPage.getThreadLength(), is(equalTo("0.28")));
    }

    @Test
    @Description("Test to verify costed thread")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyCostedThread() {
        loginPage = new LoginPage(driver);
        investigationPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Machining-DTC_Issue_KeyseatMillAccessibility.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.28")
            .apply(InvestigationPage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        threadingPage = evaluatePage.costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1");

        assertThat(threadingPage.getThreadLength(), is(equalTo("0.28")));
    }

    @Test
    @Description("Test to set dropdown value to no")
    @Severity(SeverityLevel.CRITICAL)
    public void setDropdownValueNo() {
        loginPage = new LoginPage(driver);
        threadingPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1")
            .selectThreadDropdown("No")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(threadingPage.getThreadLength(), is(equalTo("")));
    }

    @Test
    @Description("Test to set dropdown value to yes")
    @Severity(SeverityLevel.CRITICAL)
    public void setDropdownValueYes() {
        loginPage = new LoginPage(driver);
        threadingPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.64")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(threadingPage.getThreadLength(), is(equalTo("0.64")));
    }

    @Test
    @Description("Testing warning message displayed when thread length is removed")
    @Severity(SeverityLevel.CRITICAL)
    public void costedThreadLengthRemoved() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:24")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.25")
            .apply(InvestigationPage.class)
            .selectEditButton()
            .removeThreadLength()
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Description("Testing changing the thread value and cancelling doesn't remove the value")
    @Severity(SeverityLevel.CRITICAL)
    public void changeThreadValueCancel() {
        loginPage = new LoginPage(driver);
        threadingPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.26")
            .apply(InvestigationPage.class)
            .selectEditButton()
            .enterThreadLength("1.70")
            .cancel()
            .selectEditButton();

        assertThat(threadingPage.getThreadLength(), is(equalTo("0.26")));
    }

    @Test
    @Description("Testing that adding text values in the thread length shows a warning message")
    @Severity(SeverityLevel.CRITICAL)
    public void junkValuesCharTest() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("apriori")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Description("Testing that adding no value in the thread shows a warning message")
    @Severity(SeverityLevel.CRITICAL)
    public void junkValueTest() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Description("Testing that adding a value of 0 in the thread shows a warning message")
    @Severity(SeverityLevel.CRITICAL)
    public void zeroValueTest() {
        loginPage = new LoginPage(driver);
        warningPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:25")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0")
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Description("Testing a public thread cannot be edited")
    @Severity(SeverityLevel.CRITICAL)
    public void cannotEditPublicThread() {

        String testScenarioName = scenarioName;

        loginPage = new LoginPage(driver);
        investigationPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .openScenario(testScenarioName, "CurvedWall")
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getEditButton().isEnabled(), is(false));
    }

    @Test
    @Description("Testing thread length persist when attributes are changed")
    @Severity(SeverityLevel.CRITICAL)
    public void maintainingThreadChangeAttributes() {
        loginPage = new LoginPage(driver);
        investigationPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:26")
            .selectThreadDropdown("Yes")
            .enterThreadLength("4.85")
            .apply(InvestigationPage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 2007")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:26");

        assertThat(threadingPage.getThreadLength(), is(equalTo("4.85")));
    }

    @Test
    @Description("Testing thread units persist when changed to inches")
    @Severity(SeverityLevel.CRITICAL)
    public void validateThreadUnitsInches() {
        loginPage = new LoginPage(driver);
        investigationPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSettings()
            .changeDisplayUnits("English")
            .save(EvaluatePage.class)
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getThreadHeader(), containsString("(in)"));
    }

    @Test
    @Description("Testing thread units persist when changed to millimetres")
    @Severity(SeverityLevel.CRITICAL)
    public void validateThreadUnitsMM() {
        loginPage = new LoginPage(driver);
        investigationPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openSettings()
            .changeDisplayUnits("System")
            .save(EvaluatePage.class)
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getThreadHeader(), containsString("(mm)"));
    }

    @Test
    @Description("Testing threading persist when secondary process is added")
    @Severity(SeverityLevel.CRITICAL)
    public void maintainingThreadSecondaryProcessGroup() {
        loginPage = new LoginPage(driver);
        investigationPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:27")
            .selectThreadDropdown("Yes")
            .enterThreadLength("4.85")
            .apply(InvestigationPage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario();

        designGuidancePage = new DesignGuidancePage(driver);
        threadingPage = designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:27");

        assertThat(threadingPage.getThreadLength(), is(equalTo("4.85")));
    }

    @Test
    @Description("Testing compatible thread length for DTC files")
    @Severity(SeverityLevel.CRITICAL)
    public void threadsCompatibleCadDTC() {
        loginPage = new LoginPage(driver);
        threadingPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("CatiaPMIThreads.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getThreadLength(), is(equalTo("10.00")));
    }

    @Test
    @Description("Testing compatible thread length for NX files")
    @Severity(SeverityLevel.CRITICAL)
    public void threadsCompatibleCadNX() {
        loginPage = new LoginPage(driver);
        threadingPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("100plusThreads.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:15");

        assertThat(threadingPage.getThreadLength(), is(equalTo("15.00")));
    }

    @Test
    @Description("Testing compatible thread length for Creo files")
    @Severity(SeverityLevel.CRITICAL)
    public void threadsCompatibleCadCreo() {
        loginPage = new LoginPage(driver);
        threadingPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("CREO-PMI-Threads.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:13");

        assertThat(threadingPage.getThreadLength(), is(equalTo("4.06")));
    }
}