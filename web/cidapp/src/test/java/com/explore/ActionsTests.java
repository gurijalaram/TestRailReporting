package com.explore;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static utils.ColumnsEnum.COST_MATURITY;
import static utils.ColumnsEnum.STATUS;

import com.apriori.pageobjects.navtoolbars.AssignPage;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.PreviewPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;
import utils.DirectionEnum;

import java.io.File;

public class ActionsTests extends TestBase {
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private InfoPage infoPage;
    private PreviewPage previewPage;
    private AssignPage assignPage;

    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    public ActionsTests() {
        super();
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"7185", "7257", "7264", "7263", "7268"})
    @Description("Validate user can add notes to a scenario")
    public void addScenarioNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "M3CapScrew.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .highlightScenario("M3CapScrew", testScenarioName)
            .info()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("Qa Description")
            .inputNotes("QA Notes Test\n \u2022 MP Testing\n \u2022 Add and remove notes") //Unicode characters
            .submit(ExplorePage.class)
            .highlightScenario("M3CapScrew", testScenarioName)
            .info();

        assertThat(infoPage.getStatus(), is(equalTo("New")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("Low")));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7197", "7198"})
    @Description("Validate status and cost maturity columns can be added")
    public void addStatusColumn() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "M3CapScrew.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .highlightScenario("M3CapScrew", testScenarioName)
            .info()
            .inputStatus("Analysis")
            .inputCostMaturity("Medium")
            .inputDescription("Qa Description")
            .inputNotes("Adding QA notes")
            .submit(ExplorePage.class)
            .configure()
            .selectColumn(COST_MATURITY)
            .selectColumn(STATUS)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        assertThat(explorePage.getTableHeaders(), hasItems(STATUS.getColumns(), COST_MATURITY.getColumns()));

        explorePage.configure()
            .selectColumn(COST_MATURITY)
            .selectColumn(STATUS)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class);
    }

    @Test
    @Category({CustomerSmokeTests.class})
    @TestRail(testCaseId = {"7902"})
    @Description("User can lock and unlock a scenario")
    public void lockUnlockScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .highlightScenario("bracket_basic", testScenarioName)
            .lock(ExplorePage.class)
            .highlightScenario("bracket_basic", testScenarioName)
            .previewPanel();

        assertThat(previewPage.isIconDisplayed(StatusIconEnum.LOCK), is(true));

        evaluatePage = previewPage.openScenario();

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK), is(true));

        evaluatePage.unlock(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK), is(true));
    }

    @Test
    @TestRail(testCaseId = {"7259", "7265", "7269", "7272"})
    @Description("User can add scenario info and notes from action on evaluate page")
    public void actionsEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_002_006-8611543_prt.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .info()
            .inputStatus("Complete")
            .inputCostMaturity("Medium")
            .inputDescription("Qa Auto Test")
            .inputNotes("Uploaded and costed via automation")
            .submit(EvaluatePage.class)
            .costScenario(1)
            .info();

        assertThat(infoPage.getStatus(), is(equalTo("Complete")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("Medium")));
        assertThat(infoPage.getDescription(), is(equalTo("Qa Auto Test")));
        assertThat(infoPage.getNotes(), is(equalTo("Uploaded and costed via automation")));
    }

    @Test
    @TestRail(testCaseId = {"7258", "7263", "7267", "7270"})
    @Description("User can add scenario info and notes from input & notes tile")
    public void infoNotesPanel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_Forging.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .info()
            .inputStatus("New")
            .inputCostMaturity("High")
            .inputDescription("infoNotesPanel")
            .inputNotes("Panel Test")
            .submit(EvaluatePage.class)
            .costScenario()
            .info();

        assertThat(infoPage.getStatus(), is(equalTo("New")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("High")));
        assertThat(infoPage.getDescription(), is("infoNotesPanel"));
        assertThat(infoPage.getNotes(), is("Panel Test"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7172", "7175"})
    @Description("Validate ASSIGN action can operate directly on Public Workspace without requiring a Private Workspace Edit")
    public void actionsAssign() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .highlightScenario("PowderMetalShaft", testScenarioName)
            .assign()
            .inputAssignee("Moya Parker")
            .submit(ExplorePage.class)
            .openScenario(testScenarioName, "PowderMetalShaft")
            .info();

        assertThat(infoPage.getScenarioInfo("Assignee"), is("Moya Parker"));
    }

    @Test
    @TestRail(testCaseId = {"7174", "7173"})
    @Description("Validate the user can select an ASSIGN action in the Evaluate page view without opening for Edit")
    public void actionsAssignEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        assignPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .openScenario("PowderMetalShaft", testScenarioName)
            .assign()
            .inputAssignee("Sinead Plunkett")
            .submit(EvaluatePage.class)
            .assign();

        assertThat(assignPage.isAssigneeDisplayed("Sinead Plunkett"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"7178", "7262", "7910"})
    @Description("Validate Assignee is an available search criteria")
    public void filterAssignee() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputAssignee("Ciene Frith")
            .publish(EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Assignee", "In", "Ciene Frith")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Push Pin", testScenarioName), equalTo(1));
    }

    @Test
    @TestRail(testCaseId = {"7187"})
    @Description("Validate User can edit notes to a scenario")
    public void editNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_Forging.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .highlightScenario("BasicScenario_Forging", testScenarioName)
            .info()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .openScenario("BasicScenario_Forging", testScenarioName)
            .info()
            .editNotes("Testing QA notes validating the ability to edit notes")
            .submit(EvaluatePage.class)
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes validating the ability to edit notes"));
    }

    @Test
    @TestRail(testCaseId = {"7188"})
    @Description("Validate User can edit notes to a scenario but then cancel out without saving changes")
    public void cancelEditNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String testComponentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, testComponentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .inputFilter("Recent")
            .enterKeySearch(testComponentName.toUpperCase())
            .highlightScenario(testComponentName, testScenarioName)
            .info()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .inputFilter("Recent")
            .enterKeySearch(testComponentName.toUpperCase())
            .openScenario(testComponentName, testScenarioName)
            .info()
            .editNotes("Validating the ability to edit notes")
            .cancel(EvaluatePage.class)
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7186", "7191"})
    @Description("Validate User can delete notes to a scenario")
    public void deleteNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String testComponentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, testComponentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .inputFilter("Recent")
            .enterKeySearch(testComponentName.toUpperCase())
            .highlightScenario(testComponentName, testScenarioName)
            .info()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .inputFilter("Recent")
            .enterKeySearch(testComponentName.toUpperCase())
            .openScenario(testComponentName, testScenarioName)
            .info()
            .editNotes("")
            .submit(EvaluatePage.class)
            .info();

        assertThat(infoPage.getNotes(), is(""));
    }

    @Test
    @TestRail(testCaseId = {"7911"})
    @Description("Be able to view and read notes added by other users")
    public void readUsersNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String testComponentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, testComponentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials testUser1 = UserUtil.getUser();
        UserCredentials testUser2 = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(testUser1)
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .inputFilter("Recent")
            .enterKeySearch(testComponentName.toUpperCase())
            .highlightScenario(testComponentName, testScenarioName)
            .info()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .logout()
            .login(testUser2)
            .inputFilter("Recent")
            .enterKeySearch(testComponentName.toUpperCase())
            .openScenario(testComponentName, testScenarioName)
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
    @TestRail(testCaseId = {"7199", "7912"})
    @Description("Validate Status & Cost maturity are searchable attributes")
    public void filterStatusCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Rapid Prototyping.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .inputStatus("Complete")
            .inputCostMaturity("Medium")
            .inputAssignee("Moya Parker")
            .publish(EvaluatePage.class)
            .clickExplore()
            .filter()
            .inputName(filterName)
            .addCriteriaWithOption("Status", "In", "Complete")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "RAPID PROTOTYPING"), equalTo(1));

        explorePage.filter()
            .inputName(filterName2)
            .addCriteriaWithOption("Cost Maturity", "In", "Medium")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Rapid Prototyping", testScenarioName), equalTo(1));
    }

    @Test
    @TestRail(testCaseId = {"7266", "7913"})
    @Description("Validate the user can add a description in scenario information & notes, then delete the description text & progress")
    public void deleteDescription() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .highlightScenario("Push Pin", testScenarioName)
            .info()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QAutomation Test Remove Description")
            .inputNotes("")
            .submit(ExplorePage.class)
            .openScenario("Push Pin", testScenarioName)
            .info()
            .editDescription("")
            .submit(EvaluatePage.class)
            .info();

        assertThat(infoPage.getDescription(), is(""));
    }

    @Ignore
    @Test
    @TestRail(testCaseId = {"6727"})
    @Description("Ensure scripts cannot be entered into text input fields")
    public void cannotUseScript() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class)
            .clickExplore()
            .highlightScenario("Push Pin", testScenarioName)
            .info()
            .inputStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("<script src=http://www.example.com/malicious-code.js></script>")
            .inputNotes("<script>alert(document.cookie)</script>")
            .submit(ExplorePage.class);

        // TODO: 07/05/2021 remove comment
        //assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }
}