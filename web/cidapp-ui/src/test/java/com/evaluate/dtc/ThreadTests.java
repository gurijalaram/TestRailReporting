package com.evaluate.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.InvestigationPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.ThreadsPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.LengthEnum;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ThreadTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InvestigationPage investigationPage;
    private ThreadsPage threadingPage;
    private GuidanceIssuesPage designGuidancePage;
    private UserCredentials currentUser;

    private File resourceFile;

    public ThreadTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @Description("Test to check edit thread button is disabled")
    public void threadButtonDisabled() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap noDraft.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openInvestigationTab()
                .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getEditButton().isEnabled(), is(false));
    }*/

    /*@Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"28", "1631"})
    @Description("C28 Test to check thread length persist")
    public void editThread() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap noDraft.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    }*/

    @Test
    @TestRail(testCaseId = {"8902"})
    @Description("Testing to verify costed thread with attribute change")
    public void selectScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .costScenario(7)
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getThreaded("SimpleHole:1"), containsString("check"));

        threadingPage.closePanel()
                .selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("11000")
                .selectMaterial("Copper, Stock, UNS C11000")
                .submit(EvaluatePage.class)
                .costScenario(7)
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getThreaded("SimpleHole:1"), containsString("check"));
    }

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @TestRail(testCaseId = {"29"})
    @Description("Test to set dropdown value to no")
    public void setDropdownValueNo() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openInvestigationTab()
                .selectInvestigationTopic("Threading")
                .editThread("Simple Holes", "SimpleHole:1")
                .selectThreadDropdown("No")
                .apply(InvestigationPage.class)
                .selectEditButton();

        assertThat(threadingPage.isThreadingStatus("No"), is(true));
    }*/

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @TestRail(testCaseId = {"3847"})
    @Description("Test to set dropdown value to yes")
    public void setDropdownValueYes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .selectVPE(DigitalFactoryEnum.APRIORI_USA.getVpe())
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
    }*/

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @Description("Testing warning message displayed when thread length is removed")
    public void costedThreadLengthRemoved() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    }*/

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @TestRail(testCaseId = {"32", "33"})
    @Description("Testing changing the thread value and cancelling doesn't remove the value")
    public void changeThreadValueCancel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    }*/

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @TestRail(testCaseId = {"32", "34"})
    @Description("Testing that adding text values in the thread length shows a warning message")
    public void junkValuesCharTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        warningPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openInvestigationTab()
                .selectInvestigationTopic("Threading")
                .editThread("Curved Walls", "CurvedWall:1")
                .selectThreadDropdown("Yes")
                .enterThreadLength("apriori")
                .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }*/

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @Description("Testing that adding no value in the thread shows a warning message")
    public void junkValueTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        warningPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openInvestigationTab()
                .selectInvestigationTopic("Threading")
                .editThread("Curved Walls", "CurvedWall:1")
                .selectThreadDropdown("Yes")
                .enterThreadLength("")
                .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }*/

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @TestRail(testCaseId = {"35"})
    @Description("Testing that adding a value of 0 in the thread shows a warning message")
    public void zeroValueTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        warningPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .openDesignGuidance()
                .openInvestigationTab()
                .selectInvestigationTopic("Threading")
                .editThread("Curved Walls", "CurvedWall:25")
                .selectThreadDropdown("Yes")
                .enterThreadLength("0")
                .apply(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }*/

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"30"})
    @Description("Testing a public thread cannot be edited")
    public void cannotEditPublicThread() {
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CurvedWall.CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
                .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .openScenario(testScenarioName, "CurvedWall")
                .openDesignGuidance()
                .openInvestigationTab()
                .selectInvestigationTopic("Threading");

        assertThat(investigationPage.getEditButton().isEnabled(), is(false));
    }*/

    @Test
    @TestRail(testCaseId = {"8903"})
    @Description("Testing thread length persist when attributes are changed from process group")
    public void maintainingThreadChangeAttributes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .costScenario(7)
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("20.00mm"));

        threadingPage.closePanel()
                .selectProcessGroup(processGroupEnum.SHEET_METAL)
                .openMaterialSelectorTable()
                .search("1095")
                .selectMaterial("Steel, Hot Worked, AISI 1095")
                .submit(EvaluatePage.class)
                .costScenario(7)
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("20.00mm"));
    }

    @Test
    @TestRail(testCaseId = {"8904"})
    @Description("Testing thread units persist when changed to inches")
    public void validateThreadUnitsInches() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .costScenario()
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("20.00mm"));

        threadingPage.closePanel()
                .openSettings()
                .selectUnits(UnitsEnum.CUSTOM)
                .setSystem("Imperial")
                .selectLength(LengthEnum.INCHES)
                .submit(EvaluatePage.class)
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("0.79in"));

    }

    @Test
    @TestRail(testCaseId = {"8905"})
    @Description("Testing thread units persist when changed to centimetres")
    public void validateThreadUnitsCM() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .costScenario()
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("20.00mm"));

        threadingPage.closePanel()
                .openSettings()
                .selectUnits(UnitsEnum.CUSTOM)
                .setSystem("Metric")
                .selectLength(LengthEnum.CENTIMETER)
                .submit(EvaluatePage.class)
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("2.00cm"));
    }

    @Test
    @TestRail(testCaseId = {"8906"})
    @Description("Testing threading persist when secondary process is added")
    public void maintainingThreadSecondaryProcessGroup() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.STOCK_MACHINING)
                .costScenario()
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("20.00mm"));

        threadingPage.closePanel()
                .selectProcessGroup(processGroupEnum.CASTING_DIE)
                .goToSecondaryTab()
                .openSecondaryProcesses()
                .goToOtherSecProcessesTab()
                .selectSecondaryProcess("Packaging")
                .submit(EvaluatePage.class)
                .costScenario()
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("20.00mm"));
    }

    @Test
    @TestRail(testCaseId = {"8268"})
    @Description("Testing compatible thread length for DTC files")
    public void threadsCompatibleCadDTC() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "CatiaPMIThreads";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum)
                .costScenario()
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:1");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("10.00mm"));
    }

    @Test
    @TestRail(testCaseId = {"8268"})
    @Description("Testing compatible thread length for NX files")
    public void threadsCompatibleCadNX() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "100plusThreads";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.SHEET_METAL)
                .costScenario(5)
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:15");

        assertThat(threadingPage.getLength("SimpleHole:1"), is("15.00mm"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"8268"})
    @Description("Testing compatible thread length for Creo files")
    public void threadsCompatibleCadCreo() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "CREO-PMI-Threads";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        threadingPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.SHEET_METAL)
                .costScenario()
                .openDesignGuidance()
                .openThreadsTab()
                .selectIssueTypeGcd("Simple Holes", "SimpleHole:13");

        assertThat(threadingPage.getLength("SimpleHole:13"), is("4.06mm"));
    }

    // TODO: 11/08/2021 cn - test commented as edit functionality hasn't been implemented
    /*@Test
    @TestRail(testCaseId = {"64"})
    @Description("Validate thread filter behaves correctly in Investigation tab.")
    public void threadFilter() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CREO-PMI-Threads.prt.1");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        investigationPage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
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
    }*/
}
