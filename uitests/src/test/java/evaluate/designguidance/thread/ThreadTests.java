package evaluate.designguidance.thread;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.ThreadingPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.LengthEnum;
import com.apriori.utils.enums.MetricEnum;
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
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ThreadTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;
    private ThreadingPage threadingPage;
    private WarningPage warningPage;
    private DesignGuidancePage designGuidancePage;
    private UserCredentials currentUser;

    private File resourceFile;

    public ThreadTests() {
        super();
    }

    @After
    public void resetPreferences() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @Description("Test to check edit thread button is disabled")
    public void threadButtonDisabled() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getEditButton().isEnabled(), is(false));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"28", "1631"})
    @Description("C28 Test to check thread length persist")
    public void editThread() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap noDraft.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
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

        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issue_KeyseatMillAccessibility.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.28")
            .apply(InvestigationPage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        threadingPage = designGuidancePage.closePanel()
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1");

        assertThat(threadingPage.isThreadLength("0.28"), is(true));
    }

    @Test
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"29"})
    @Description("Test to set dropdown value to no")
    public void setDropdownValueNo() {

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
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
    @TestRail(testCaseId = {"3847"})
    @Description("Test to set dropdown value to yes")
    public void setDropdownValueYes() {

        resourceFile = new FileResourceUtil().getResourceFile("CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario(3);

        assertThat(evaluatePage.isDFMRiskIcon("dtc-high-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));

        threadingPage = evaluatePage.openDesignGuidance()
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

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:24")
            .selectThreadDropdown("Yes")
            .enterThreadLength("0.25")
            .apply(InvestigationPage.class)
            .selectEditButton();

        assertThat(threadingPage.isThreadLength("0.25"), is(true));
        warningPage = threadingPage.removeThreadLength()
            .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"32", "33"})
    @Description("Testing changing the thread value and cancelling doesn't remove the value")
    public void changeThreadValueCancel() {

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
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

        resourceFile = new FileResourceUtil().getResourceFile("CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        warningPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
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

        resourceFile = new FileResourceUtil().getResourceFile("CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        warningPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
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

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        warningPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
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
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"30"})
    @Description("Testing a public thread cannot be edited")
    public void cannotEditPublicThread() {

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = new FileResourceUtil().getResourceFile("CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
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

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:26")
            .selectThreadDropdown("Yes")
            .enterThreadLength("4.85")
            .apply(InvestigationPage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        threadingPage = designGuidancePage.closePanel()
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
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
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"267", "268", "39", "294", "285"})
    @Description("Testing thread units persist when changed to inches")
    public void validateThreadUnitsInches() {

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM.getUnits())
            .selectSystem(MetricEnum.ENGLISH.getMetricUnit())
            .selectLength(LengthEnum.INCHES.getLength())
            .save(EvaluatePage.class)
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getThreadHeader(), containsString("(in)"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"42"})
    @Description("Testing thread units persist when changed to millimetres")
    public void validateThreadUnitsMM() {

        resourceFile = new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openSettings()
            .selectSystem(MetricEnum.METRIC.getMetricUnit())
            .selectLength(LengthEnum.MILLIMETER.getLength())
            .save(EvaluatePage.class)
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getThreadHeader(), containsString("(mm)"));

        investigationPage.editThread("Simple Holes", "SimpleHole:1");

        assertThat(new ThreadingPage(driver).isThreadLength("20"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"37", "41"})
    @Description("Testing threading persist when secondary process is added")
    public void maintainingThreadSecondaryProcessGroup() {

        resourceFile = new FileResourceUtil().getResourceFile("M3CapScrew.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("4.85")
            .apply(InvestigationPage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        threadingPage = designGuidancePage.closePanel()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Curved Walls", "CurvedWall:1");

        assertThat(threadingPage.isThreadLength("4.85"), is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"44", "1632"})
    @Description("Testing compatible thread length for DTC files")
    public void threadsCompatibleCadDTC() {

        resourceFile = new FileResourceUtil().getResourceFile("CatiaPMIThreads.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.isThreadLength("10.00"), is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"44", "1632"})
    @Description("Testing compatible thread length for NX files")
    public void threadsCompatibleCadNX() {

        resourceFile = new FileResourceUtil().getResourceFile("100plusThreads.prt");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:15");

        assertThat(threadingPage.isThreadLength("15.00"), is(true));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"44", "1632"})
    @Description("Testing compatible thread length for Creo files")
    public void threadsCompatibleCadCreo() {

        resourceFile = new FileResourceUtil().getResourceFile("CREO-PMI-Threads.prt.1");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:13");

        assertThat(threadingPage.isThreadLength("4.06"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"64"})
    @Description("Validate thread filter behaves correctly in Investigation tab.")
    public void threadFilter() {

        resourceFile = new FileResourceUtil().getResourceFile("CREO-PMI-Threads.prt.1");
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .selectFilterDropdown("Threaded in CAD")
            .selectGcdTypeAndGcd("Simple Holes", "SimpleHole:13");

        assertThat(investigationPage.getGcdRow("SimpleHole:13"), hasItems("CAD", "4.06"));

        investigationPage.selectEditButton()
            .enterThreadLength("7")
            .apply(InvestigationPage.class);

        designGuidancePage = new DesignGuidancePage(driver);
        investigationPage = designGuidancePage.closePanel()
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .selectFilterDropdown("Overridden GCDs")
            .selectGcdTypeAndGcd("Simple Holes", "SimpleHole:13");

        assertThat(investigationPage.getGcdRow("SimpleHole:13"), hasItems("Manual", "7.00"));
    }
}