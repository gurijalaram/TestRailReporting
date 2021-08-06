package com.explore;

import static com.utils.ColumnsEnum.COST_MATURITY;
import static com.utils.ColumnsEnum.STATUS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ActionsTests extends TestBase {
    UserCredentials currentUser;
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

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7185", "7257", "7264", "7263", "7268"})
    @Description("Validate user can add notes to a scenario")
    public void addScenarioNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .highlightScenario("M3CapScrew", scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("Qa Description")
            .inputNotes("QA Notes Test\n \u2022 MP Testing\n \u2022 Add and remove notes") //Unicode characters
            .submit(ExplorePage.class)
            .highlightScenario("M3CapScrew", scenarioName)
            .info();

        assertThat(infoPage.getStatus(), is(equalTo("New")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("Low")));
    }

    @Test
    @Issue("BA-1920")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7197", "7198"})
    @Description("Validate status and cost maturity columns can be added")
    public void addStatusColumn() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(componentName)
            .highlightScenario(componentName, scenarioName)
            .info()
            .selectStatus("Analysis")
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
    @Issue("BA-1920")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7902", "5436"})
    @Description("User can lock and unlock a scenario")
    public void lockUnlockScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(componentName)
            .highlightScenario(componentName, scenarioName)
            .lock(ExplorePage.class)
            .highlightScenario(componentName, scenarioName)
            .previewPanel();

        assertThat(previewPage.isIconDisplayed(StatusIconEnum.LOCK), is(true));

        evaluatePage = previewPage.openScenario();

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK), is(true));

        evaluatePage.unlock(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK), is(true));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7259", "7265", "7269", "7272"})
    @Description("User can add scenario info and notes from action on evaluate page")
    public void actionsEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "case_002_006-8611543_prt";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .info()
            .selectStatus("Complete")
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
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7258", "7263", "7267", "7270"})
    @Description("User can add scenario info and notes from input & notes tile")
    public void infoNotesPanel() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .info()
            .selectStatus("New")
            .inputCostMaturity("High")
            .inputDescription("infoNotesPanel")
            .inputNotes("Panel Test")
            .submit(EvaluatePage.class)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Cold Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .info();

        assertThat(infoPage.getStatus(), is(equalTo("New")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("High")));
        assertThat(infoPage.getDescription(), is("infoNotesPanel"));
        assertThat(infoPage.getNotes(), is("Panel Test"));
    }

    @Test
    @Issue("BA-1920")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7172", "7175", "5437"})
    @Description("Validate ASSIGN action can operate directly on Public Workspace without requiring a Private Workspace Edit")
    public void actionsAssign() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName, scenarioName)
            .assign()
            .selectAssignee("Moya Parker")
            .submit(ExplorePage.class)
            .openScenario(componentName, scenarioName)
            .info();

        assertThat(infoPage.getScenarioInfo("Assignee"), is("Moya Parker"));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7174", "7173"})
    @Description("Validate the user can select an ASSIGN action in the Evaluate page view without opening for Edit")
    public void actionsAssignEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        assignPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario("PowderMetalShaft", scenarioName)
            .assign()
            .selectAssignee("Sinead Plunkett")
            .submit(EvaluatePage.class)
            .assign();

        assertThat(assignPage.isAssigneeDisplayed("Sinead Plunkett"), is(true));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7178", "7262", "7910"})
    @Description("Validate Assignee is an available search criteria")
    public void filterAssignee() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .selectStatus("New")
            .selectCostMaturity("Low")
            .selectAssignee("Ciene Frith")
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Assignee", "In", "Ciene Frith")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Push Pin", scenarioName), equalTo(1));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7187"})
    @Description("Validate User can edit notes to a scenario")
    public void editNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Cold Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .highlightScenario("BasicScenario_Forging", scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .openScenario("BasicScenario_Forging", scenarioName)
            .info()
            .editNotes("Testing QA notes validating the ability to edit notes")
            .submit(EvaluatePage.class)
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes validating the ability to edit notes"));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7188"})
    @Description("Validate User can edit notes to a scenario but then cancel out without saving changes")
    public void cancelEditNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Cold Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .info()
            .editNotes("Validating the ability to edit notes")
            .cancel(EvaluatePage.class)
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7186", "7191"})
    @Description("Validate User can delete notes to a scenario")
    public void deleteNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .info()
            .editNotes("")
            .submit(EvaluatePage.class)
            .info();

        assertThat(infoPage.getNotes(), is(""));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7911"})
    @Description("Be able to view and read notes added by other users")
    public void readUsersNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials testUser1 = UserUtil.getUser();
        UserCredentials testUser2 = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(testUser1)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, testUser1)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .logout()
            .login(testUser2)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7199", "7912"})
    @Description("Validate Status & Cost maturity are searchable attributes")
    public void filterStatusCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;

        String componentName = "Rapid Prototyping";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();


        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("Default")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .selectStatus("Complete")
            .selectCostMaturity("Medium")
            .selectAssignee("Moya Parker")
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Status", "In", "Complete")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("RAPID PROTOTYPING", scenarioName), equalTo(1));

        explorePage.filter()
            .inputName(filterName2)
            .saveAs()
            .addCriteriaWithOption("Cost Maturity", "In", "Medium")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Rapid Prototyping", scenarioName), equalTo(1));
    }

    @Test
    @Issue("BA-1920")
    @TestRail(testCaseId = {"7266", "7913"})
    @Description("Validate the user can add a description in scenario information & notes, then delete the description text & progress")
    public void deleteDescription() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QAutomation Test Remove Description")
            .inputNotes("")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .info()
            .editDescription("")
            .submit(EvaluatePage.class)
            .info();

        assertThat(infoPage.getDescription(), is(""));
    }

    @Ignore("Not sure if this is allowed or not yet")
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6727"})
    @Description("Ensure scripts cannot be entered into text input fields")
    public void cannotUseScript() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario()
            .publish(currentUser, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("<script src=http://www.example.com/malicious-code.js></script>")
            .inputNotes("<script>alert(document.cookie)</script>")
            .submit(ExplorePage.class);

        // TODO: 07/05/2021 remove comment
        //assertThat(warningPage.getWarningText(), containsString("Some of the supplied inputs are invalid"));
    }
}