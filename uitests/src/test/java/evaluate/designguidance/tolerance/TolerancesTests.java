package test.java.evaluate.designguidance.tolerance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.header.PageHeader;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.designguidance.DesignGuidancePage;
import main.java.pages.evaluate.designguidance.investigation.InvestigationPage;
import main.java.pages.evaluate.designguidance.investigation.ThreadingPage;
import main.java.pages.evaluate.designguidance.tolerances.WarningPage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.hamcrest.Matchers;
import org.junit.Test;

public class TolerancesTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private DesignGuidancePage designGuidancePage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;
    private PageHeader pageHeader;

    public TolerancesTests() {
        super();
    }

    @Test
    @Description("Test to check edit thread button is disabled")
    @Severity(SeverityLevel.CRITICAL)
    public void threadButtonDisabled() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getEditButton().isEnabled(), Matchers.is(false));
    }

    @Test
    @Description("Test to check thread length persist")
    @Severity(SeverityLevel.CRITICAL)
    public void editThread() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
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

    @Test
    @Description("Test to verify costed thread")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyCostedThread() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
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

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:10");

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("0.28")));
    }

    @Test
    @Description("Test to set dropdown value to no")
    @Severity(SeverityLevel.CRITICAL)
    public void setDropdownValueNo() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1")
            .selectThreadDropdown("No")
            .apply(InvestigationPage.class);

        new InvestigationPage(driver).selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("")));
    }

    @Test
    @Description("Test to set dropdown value to yes")
    @Severity(SeverityLevel.CRITICAL)
    public void setDropdownValueYes() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:23")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.64")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(new ThreadingPage(driver).getThreadLength(), Matchers.is(equalTo("0.64")));
    }

    @Test
    @Description("Testing warning message displayed when thread length is removed")
    @Severity(SeverityLevel.CRITICAL)
    public void costedThreadLengthRemoved() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:24")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.25")
            .apply(InvestigationPage.class);

        investigationPage = new InvestigationPage(driver);
        investigationPage.selectEditButton()
            .removeThreadLength()
            .apply(WarningPage.class);

        assertThat(new WarningPage(driver).getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Description("Testing changing the thread value and cancelling doesn't remove the value")
    @Severity(SeverityLevel.CRITICAL)
    public void changeThreadValueCancel() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:25")
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

    @Test
    @Description("Testing that adding text values in the thread length shows a warning message")
    @Severity(SeverityLevel.CRITICAL)
    public void junkValuesCharTest() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:25")
            .selectThreadDropdown("Yes")
            .enterThreadLength("apriori")
            .apply(WarningPage.class);

        assertThat(new WarningPage(driver).getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Description("Testing that adding no value in the thread shows a warning message")
    @Severity(SeverityLevel.CRITICAL)
    public void junkValueTest() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:25")
            .selectThreadDropdown("Yes")
            .enterThreadLength("")
            .apply(WarningPage.class);

        assertThat(new WarningPage(driver).getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Description("Testing a public thread cannot be edited")
    @Severity(SeverityLevel.CRITICAL)
    public void cannotEditPublicThread() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .publishScenario();

        explorePage = new ExplorePage(driver);
        explorePage.openScenario("Scenario b", "DTCCastingIssues")
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getEditButton().isEnabled(), is(false));
    }

    @Test
    @Description("Testing thread length persist when attributes are changed")
    @Severity(SeverityLevel.CRITICAL)
    public void maintainingThreadChangeAttributes() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:26")
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
            .editThread("Curved Walls", "CurvedWall:26");

        assertThat(new ThreadingPage(driver).getThreadLength(), equalTo("4.85"));
    }

    @Test
    @Description("Testing thread units persist when changed to inches")
    @Severity(SeverityLevel.CRITICAL)
    public void validateThreadUnitsInches() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openSettings()
            .changeDisplayUnits("English")
            .save(EvaluatePage.class);

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getThreadHeader(), containsString("(in)"));
    }

    @Test
    @Description("Testing thread units persist when changed to millimetres")
    @Severity(SeverityLevel.CRITICAL)
    public void validateThreadUnitsMM() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openSettings()
            .changeDisplayUnits("System")
            .save(EvaluatePage.class);

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(new InvestigationPage(driver).getThreadHeader(), containsString("(mm)"));
    }

    @Test
    @Description("Testing threading persist when secondary process is added")
    @Severity(SeverityLevel.CRITICAL)
    public void maintainingThreadSecondaryProcessGroup() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
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
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:27");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }

    @Test
    @Description("Testing compatible thread length for DTC files")
    @Severity(SeverityLevel.CRITICAL)
    public void threadsCompatibleCadDTC() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:28");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }

    @Test
    @Description("Testing compatible thread length for NX files")
    @Severity(SeverityLevel.CRITICAL)
    public void threadsCompatibleCadNX() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:29");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }

    @Test
    @Description("Testing compatible thread length for Creo files")
    @Severity(SeverityLevel.CRITICAL)
    public void threadsCompatibleCadCreo() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("Scenario b", new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel())
            .openDesignGuidance();

        designGuidancePage = new DesignGuidancePage(driver);
        designGuidancePage.openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:30");

        assertThat(new ThreadingPage(driver).getThreadLength(), is(equalTo("4.85")));
    }
}