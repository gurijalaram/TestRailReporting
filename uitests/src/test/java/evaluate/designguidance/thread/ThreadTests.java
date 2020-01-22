package evaluate.designguidance.thread;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.ThreadingPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class ThreadTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;
    private ThreadingPage threadingPage;
    private WarningPage warningPage;
    private UserCredentials currentUser;

    public ThreadTests() {
        super();
    }

    @After
    public void resetPreferences() {
        new AfterTestUtil().resetAllSettings(currentUser.getUsername());
    }

    @Test
    @Description("Test to check edit thread button is disabled")
    public void threadButtonDisabled() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        investigationPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getEditButton().isEnabled(), is(false));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"28", "1631"})
    @Description("C28 Test to check thread length persist")
    public void editThread() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        threadingPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart"))
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

        assertThat(threadingPage.isThreadLength("0.28"), is(true));
    }

    @Test
    @Description("Test to verify costed thread")
    public void selectScenario() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        investigationPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_KeyseatMillAccessibility.CATPart"))
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

        assertThat(threadingPage.isThreadLength("0.28"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"29"})
    @Description("Test to set dropdown value to no")
    public void setDropdownValueNo() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        threadingPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1")
            .selectThreadDropdown("No")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(threadingPage.isThreadingStatus("No"), is(true));
    }

    @Test
    @Description("Test to set dropdown value to yes")
    public void setDropdownValueYes() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        threadingPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.64")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(threadingPage.isThreadLength("0.64"), is(true));
    }

    @Test
    @Description("Testing warning message displayed when thread length is removed")
    public void costedThreadLengthRemoved() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        warningPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
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
    @TestRail(testCaseId = {"32", "33"})
    @Description("Testing changing the thread value and cancelling doesn't remove the value")
    public void changeThreadValueCancel() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        threadingPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
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

        assertThat(threadingPage.isThreadLength("0.26"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"32", "34"})
    @Description("Testing that adding text values in the thread length shows a warning message")
    public void junkValuesCharTest() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        warningPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
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
    public void junkValueTest() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        warningPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
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
    @TestRail(testCaseId = {"35"})
    @Description("Testing that adding a value of 0 in the thread shows a warning message")
    public void zeroValueTest() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        warningPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
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
    @TestRail(testCaseId = {"30"})
    @Description("Testing a public thread cannot be edited")
    public void cannotEditPublicThread() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        investigationPage = loginPage.login(currentUser)
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openScenario(testScenarioName, "CurvedWall")
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getEditButton().isEnabled(), is(false));
    }

    @Test
    @TestRail(testCaseId = {"38", "40", "43", "584", "598"})
    @Description("Testing thread length persist when attributes are changed")
    public void maintainingThreadChangeAttributes() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        investigationPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.CATPart"))
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
        threadingPage = evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_MEXICO.getVpe())
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum, Cast, ANSI 2007")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:26");

        assertThat(threadingPage.isThreadLength("4.85"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"267", "268", "39", "294", "285"})
    @Description("Testing thread units persist when changed to inches")
    public void validateThreadUnitsInches() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        investigationPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSettings()
            .changeDisplayUnits(UnitsEnum.ENGLISH.getUnit())
            .save(EvaluatePage.class)
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getThreadHeader("(in)"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"42"})
    @Description("Testing thread units persist when changed to millimetres")
    public void validateThreadUnitsMM() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        investigationPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSettings()
            .changeDisplayUnits(UnitsEnum.SYSTEM.getUnit())
            .save(EvaluatePage.class)
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getThreadHeader("(mm)"), is(true));

        investigationPage = new InvestigationPage(driver);
        threadingPage = investigationPage.editThread("Simple Holes", "SimpleHole:1");
        assertThat(threadingPage.isThreadLength("20"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"37", "41"})
    @Description("Testing threading persist when secondary process is added")
    public void maintainingThreadSecondaryProcessGroup() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        investigationPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("M3CapScrew.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("4.85")
            .apply(InvestigationPage.class);

        new DesignGuidancePage(driver).closeDesignGuidance();

        evaluatePage = new EvaluatePage(driver);
        threadingPage = evaluatePage.openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1");

        assertThat(threadingPage.isThreadLength("4.85"), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"44", "1632"})
    @Description("Testing compatible thread length for DTC files")
    public void threadsCompatibleCadDTC() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        threadingPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("CatiaPMIThreads.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.isThreadLength("10.00"), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"44", "1632"})
    @Description("Testing compatible thread length for NX files")
    public void threadsCompatibleCadNX() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        threadingPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("100plusThreads.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:15");

        assertThat(threadingPage.isThreadLength("15.00"), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"44", "1632"})
    @Description("Testing compatible thread length for Creo files")
    public void threadsCompatibleCadCreo() {
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        threadingPage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("CREO-PMI-Threads.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:13");

        assertThat(threadingPage.isThreadLength("4.06"), is(true));
    }
}