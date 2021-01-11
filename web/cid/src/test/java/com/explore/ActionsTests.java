package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColumnsEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.common.ScenarioTablePage;
import com.pageobjects.pages.compare.ComparePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.PublishPage;
import com.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.pageobjects.pages.explore.AssignPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.explore.ScenarioNotesPage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.toolbars.GenericHeader;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.MigrationTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ActionsTests extends TestBase {
    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private ScenarioNotesPage scenarioNotesPage;
    private EvaluatePage evaluatePage;
    private GenericHeader genericHeader;
    private AssignPage assignPage;
    private WarningPage warningPage;
    private ComparePage comparePage;

    private File resourceFile;

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @Issue("AP-61858")
    @TestRail(testCaseId = {"545", "731", "738", "1610", "742"})
    @Description("Validate user can add notes to a scenario")
    public void addScenarioNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "M3CapScrew.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "M3CapScrew");

        explorePage = new ExplorePage(driver);
        explorePage.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("New", "Low", "Qa Description", "\u2022 QA Notes Test\n \u2022 MP Testing\n \u2022 Add and remove notes") //Unicode characters
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("M3CapScrew", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        scenarioNotesPage = explorePage.selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"554", "555"})
    @Description("Validate status and cost maturity columns can be added")
    public void addStatusColumn() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "M3CapScrew.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "M3CapScrew");

        explorePage = new ExplorePage(driver);
        explorePage.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Analysis", "Medium", "Qa Description", "Adding QA notes")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("M3CapScrew", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openColumnsTable()
            .addColumn(ColumnsEnum.COST_MATURITY.getColumns())
            .addColumn(ColumnsEnum.STATUS.getColumns())
            .selectSaveButton(ExplorePage.class);

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.STATUS.getColumns(), ColumnsEnum.COST_MATURITY.getColumns()));

        explorePage.openColumnsTable()
            .removeColumn(ColumnsEnum.COST_MATURITY.getColumns())
            .removeColumn(ColumnsEnum.STATUS.getColumns())
            .selectSaveButton(ExplorePage.class);
    }

    @Test
    @Issue("AP-61858")
    @Category({CustomerSmokeTests.class, SmokeTests.class, MigrationTests.class})
    @TestRail(testCaseId = {"1610", "592", "593"})
    @Description("User can lock and unlock a scenario")
    public void lockUnlockScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "bracket_basic");

        new GenericHeader(driver).toggleLock();
        genericHeader = new GenericHeader(driver);
        genericHeader.selectActions();

        assertThat(genericHeader.isActionLockedStatus("Unlock"), is(true));
        genericHeader.selectActions();

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.openScenario(testScenarioName, "bracket_basic");

        assertThat(evaluatePage.isLockedStatus("Locked"), is(true));

        new GenericHeader(driver).toggleLock();

        assertThat(new EvaluatePage(driver).isLockedStatus("Unlocked"), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"733", "739", "743", "746"})
    @Description("User can add scenario info and notes from action on evaluate page")
    public void actionsEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_002_006-8611543_prt.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        scenarioNotesPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Complete", "Medium", "Qa Auto Test", "Uploaded and costed via automation")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("case_002_006-8611543_prt", scenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .costScenario(1)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("Complete"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Medium"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("Qa Auto Test"));
        assertThat(scenarioNotesPage.getScenarioNotes(), is("Uploaded and costed via automation"));
    }

    @Test
    @Category({SmokeTests.class, MigrationTests.class})
    @TestRail(testCaseId = {"732", "737", "741", "744"})
    @Description("User can add scenario info and notes from input & notes tile")
    public void infoNotesPanel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_Forging.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        scenarioNotesPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectInfoNotes()
            .enterScenarioInfoNotes("New", "High", "infoNotesPanel", "Panel Test")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("BasicScenario_Forging", scenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .costScenario()
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("High"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("infoNotesPanel"));
        assertThat(scenarioNotesPage.getScenarioNotes(), is("Panel Test"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"526", "529"})
    @Description("Validate ASSIGN action can operate directly on Public Workspace without requiring a Private Workspace Edit")
    public void actionsAssign() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "PowderMetalShaft");

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectAssignScenario()
            .selectAssignee("Moya Parker")
            .update(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("PowderMetalShaft", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openScenario(testScenarioName, "PowderMetalShaft")
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isAssignee(), is("Moya Parker"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"528", "527"})
    @Description("Validate the user can select an ASSIGN action in the Evaluate page view without opening for Edit")
    public void actionsAssignEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "PowderMetalShaft");

        genericHeader = new GenericHeader(driver);
        genericHeader.selectAssignScenario()
            .selectAssignee("Sinead Plunkett")
            .update(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("PowderMetalShaft", testScenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class);

        genericHeader = new GenericHeader(driver);
        assignPage = genericHeader.selectAssignScenario();

        assertThat(assignPage.isAssignee("Sinead Plunkett"), is(true));
    }

    @Test
    @Category({SmokeTests.class, MigrationTests.class})
    @TestRail(testCaseId = {"532", "736", "734"})
    @Description("Validate Assignee is an available search criteria")
    public void filterAssignee() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario("New", "Low", "Ciene Frith")
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Assignee", "is", "Ciene Frith")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin"), equalTo(1));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"543"})
    @Description("Validate User can edit notes to a scenario")
    public void editNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_Forging.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "BasicScenario_Forging");

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Select Status", "Select Cost Maturity", "QA Test Description", "Testing QA notes")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("BasicScenario_Forging", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openScenario(testScenarioName, "BasicScenario_Forging")
            .selectInfoNotes()
            .editNotes("Testing QA notes validating the ability to edit notes")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueRow("okay")
            .closeJobQueue(EvaluatePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.getScenarioNotes(), is("Testing QA notes validating the ability to edit notes"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"544"})
    @Description("Validate User can edit notes to a scenario but then cancel out without saving changes")
    public void cancelEditNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "BasicScenario_Forging.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "BasicScenario_Forging");

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Select Status", "Select Cost Maturity", "QA Test Description", "Testing QA notes")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("BasicScenario_Forging", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openScenario(testScenarioName, "BasicScenario_Forging")
            .selectInfoNotes()
            .editNotes("Validating the ability to edit notes")
            .cancel(EvaluatePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.getScenarioNotes(), is("Testing QA notes"));
    }

    @Test
    @Category(SmokeTests.class)
    @Issue("BA-932")
    @TestRail(testCaseId = {"542", "546"})
    @Description("Validate User can delete notes to a scenario")
    public void deleteNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "Push Pin");

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Select Status", "Select Cost Maturity", "QA Test Description", "Testing QA notes")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("PUSH PIN", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openScenario(testScenarioName, "Push Pin")
            .selectInfoNotes()
            .editNotes("")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueRow("okay")
            .closeJobQueue(EvaluatePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.getScenarioNotes(), is(""));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("Be able to view and read notes added by other users")
    public void readUsersNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials testUser1 = UserUtil.getUser();
        UserCredentials testUser2 = UserUtil.getUser();

        new CidLoginPage(driver).login(testUser1)
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "Push Pin");

        new GenericHeader(driver).selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Select Status", "Select Cost Maturity", "QA Test Description", "Testing QA notes")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("Push Pin", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openAdminDropdown()
            .selectLogOut();

        new CidLoginPage(driver).login(testUser2);

        explorePage = new ExplorePage(driver);
        scenarioNotesPage = explorePage.selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, "Push Pin")
            .selectInfoNotes();

        assertThat(scenarioNotesPage.getScenarioNotes(), is("Testing QA notes"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"556", "594"})
    @Description("Validate Status & Cost maturity are searchable attributes")
    public void filterStatusCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Rapid Prototyping.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario("Complete", "Medium", "Moya Parker")
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Status", "is", "Complete")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "RAPID PROTOTYPING"), equalTo(1));

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Cost Maturity", "is", "Medium")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Rapid Prototyping"), equalTo(1));
    }

    @Test
    @Issue("BA-932")
    @TestRail(testCaseId = {"740", "595"})
    @Description("Validate the user can add a description in scenario information & notes, then delete the description text & progress")
    public void deleteDescription() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "Push Pin");

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Select Status", "Select Cost Maturity", "QAutomation Test Remove Description", "")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("PUSH PIN", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openScenario(testScenarioName, "Push Pin")
            .selectInfoNotes()
            .deleteDescription()
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueRow("okay")
            .closeJobQueue(EvaluatePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.getDescription(), is(""));
    }

    @Test
    @TestRail(testCaseId = {"2317"})
    @Description("Ensure scripts cannot be entered into text input fields")
    public void cannotUseScript() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "Push Pin");

        genericHeader = new GenericHeader(driver);
        warningPage = genericHeader.selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Select Status", "Select Cost Maturity", "<script src=http://www.example.com/malicious-code.js></script>", "<script>alert(document.cookie)</script>")
            .save(WarningPage.class);

        assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"436"})
    @Description("In comparison view, the user can assign the currently open public comparison")
    public void actionsAssignComparison() {

        String testComparisonName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .openComparison(testComparisonName);

        new GenericHeader(driver).selectAssignScenario()
            .selectAssignee("Nataliia Valieieva")
            .update(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isAssignee(), is("Nataliia Valieieva"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"437"})
    @Description("In explore view, the user can assign the currently selected public comparison")
    public void actionsAssignComparisonExploreView() {

        String testComparisonName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .selectAssignScenario()
            .selectAssignee("Nataliia Valieieva")
            .update(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openComparison(testComparisonName)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isAssignee(), is("Nataliia Valieieva"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"438"})
    @Description("In private comparison view, the user can add Info & notes to the currently open comparison")
    public void addInfoNotesInPrivateComparisonView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Assembly")
            .setRowOne("Part Name", "Contains", testAssemblyName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, testAssemblyName)
            .apply(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class)
            .selectInfoNotes()
            .enterScenarioInfoNotesForComparison("New", "Low", "QA Test Description")
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"438"})
    @Description("In public comparison view, the user can add Info & notes to the currently open comparison")
    public void addInfoNotesInPublicComparisonView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Assembly")
            .setRowOne("Part Name", "Contains", testAssemblyName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, testAssemblyName)
            .apply(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .openComparison(testComparisonName)
            .selectInfoNotes()
            .enterScenarioInfoNotesForComparison("Analysis", "Initial", "QA Test Description")
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("Analysis"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Initial"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"439"})
    @Description("In explore view, the user can add Info & notes to the currently selected private comparison")
    public void addInfoNotesFofPrivateComparisonExploreView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String partName = "PowderMetalShaft";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectExploreButton()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("Analysis", "Initial", "QA Test Description")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("Analysis"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Initial"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"439"})
    @Description("In explore view, the user can add Info & notes to the currently selected public comparison")
    public void addInfoNotesFofComparisonExploreView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String partName = "PowderMetalShaft";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("New", "Low", "QA Test Description")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }

    @Test
    @TestRail(testCaseId = {"440"})
    @Description("For private comparisons, all characters should be tested in Info & notes")
    public void testAllCharactersInfoNotesPrivateComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectExploreButton()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("Analysis", "Initial", "!£$%^&()_+{}~`1-=[]#';@")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("Analysis"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Initial"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("!£$%^&()_+{}~`1-=[]#';@"));
    }

    @Test
    @TestRail(testCaseId = {"440"})
    @Description("For public comparisons, all characters should be tested in Info & notes")
    public void testAllCharactersInfoNotesPublicComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("New", "Low", "!£$%^&()_+{}~`1-=[]#';@")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("!£$%^&()_+{}~`1-=[]#';@"));
    }
}