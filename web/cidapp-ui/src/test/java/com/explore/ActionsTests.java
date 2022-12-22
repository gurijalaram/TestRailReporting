package com.explore;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.LAST_ACTION_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.utils.ColumnsEnum.ASSIGNEE;
import static com.utils.ColumnsEnum.COST_MATURITY;
import static com.utils.ColumnsEnum.STATUS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.pageobjects.navtoolbars.AssignPage;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.UpdateCadFilePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.PreviewPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColourEnum;
import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ActionsTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private InfoPage infoPage;
    private PreviewPage previewPage;
    private AssignPage assignPage;
    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();
    private UpdateCadFilePage updateCadFilePage;

    public ActionsTests() {
        super();
    }

    @Test
    @Issue("BA-2043")
    @TestRail(testCaseId = {"7185", "7257", "7264", "7263", "7268", "6342"})
    @Description("Validate user can add notes to a scenario")
    public void addScenarioNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName, scenarioName)
            .clickSearch(componentName)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("Qa Description")
            .inputNotes("QA Notes Test\n \u2022 MP Testing\n \u2022 Add and remove notes") //Unicode characters
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .openScenario(componentName, scenarioName)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");

        softAssertions.assertAll();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7197", "7198", "7200"})
    @Description("Validate status and cost maturity columns can be added")
    public void addStatusColumn() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .highlightScenario(componentName, scenarioName)
            .clickActions()
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
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        previewPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(componentName)
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .lock(ExplorePage.class)
            .highlightScenario(componentName, scenarioName)
            .openPreviewPanel();

        softAssertions.assertThat(previewPage.isIconDisplayed(StatusIconEnum.LOCK)).isEqualTo(true);

        evaluatePage = previewPage.openScenario();

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.LOCK)).isEqualTo(true);

        evaluatePage.clickActions()
            .unlock(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.UNLOCK)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7259", "7265", "7269", "7272", "7189"})
    @Description("User can add scenario info and notes from action on evaluate page")
    public void actionsEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "case_002_006-8611543_prt";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String bulletPointNotes = "• Automation notes 1\n" +
            "• Automation notes 2\n" +
            "• Automation notes 3\n" +
            "• Automation notes 4";

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .clickActions()
            .info()
            .selectStatus("Complete")
            .inputCostMaturity("Medium")
            .inputDescription("Qa Auto Test")
            .inputNotes("Uploaded and costed via automation")
            .submit(EvaluatePage.class)
            .costScenario(1)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Complete");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Medium");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("Qa Auto Test");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Uploaded and costed via automation");

        infoPage.inputNotes(bulletPointNotes)
            .submit(EvaluatePage.class)
            .costScenario()
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getNotes()).isEqualTo(bulletPointNotes);

        softAssertions.assertAll();
    }

    @Test
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
            .selectProcessGroup(ProcessGroupEnum.FORGING)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("High")
            .inputDescription("infoNotesPanel")
            .inputNotes("Panel Test")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("High");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("infoNotesPanel");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Panel Test");

        softAssertions.assertAll();
    }

    @Test
    @Issue("CIS-531")
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
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        String scenarioCreatedByName = scenariosUtil.getScenarioRepresentationCompleted(cidComponentItem).getCreatedByName();

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .assign()
            .selectAssignee(scenarioCreatedByName)
            .submit(ExplorePage.class)
            .openScenario(componentName, scenarioName)
            .clickActions()
            .info();

        assertThat(infoPage.isScenarioInfo("Assignee", scenarioCreatedByName), is(true));
    }

    @Test
    @TestRail(testCaseId = {"7174", "7173"})
    @Description("Validate the user can select an ASSIGN action in the Evaluate page view without opening for Edit")
    public void actionsAssignEvaluatePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        String scenarioCreatedByName = scenariosUtil.getScenarioRepresentationCompleted(cidComponentItem).getCreatedByName();

        assignPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario("PowderMetalShaft", scenarioName)
            .clickActions()
            .assign()
            .selectAssignee(scenarioCreatedByName)
            .submit(EvaluatePage.class)
            .clickActions()
            .assign();

        assertThat(assignPage.isAssigneeDisplayed(scenarioCreatedByName), is(true));
    }

    @Test
    @Issue("BA-2148")
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
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        String scenarioCreatedByName = scenariosUtil.getScenarioRepresentationCompleted(cidComponentItem).getCreatedByName();

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectStatus("New")
            .selectCostMaturity("Low")
            .selectAssignee(currentUser)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.ASSIGNEE, OperationEnum.IN, scenarioCreatedByName)
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Push Pin", scenarioName), equalTo(1));
    }

    @Test
    @TestRail(testCaseId = {"7187", "7271", "6199", "6339", "5438"})
    @Description("Validate User can edit notes to a scenario")
    public void editNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        final String editedNotes = "Testing QA notes validating the ability to edit notes";

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName, LAST_ACTION_EQ.getKey() + "UPDATE",
                SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_COMPLETE)
            .refresh()
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");

        infoPage.cancel(ExplorePage.class)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(ExplorePage.class)
            .getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName, LAST_ACTION_EQ.getKey() + " UPDATE",
                SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_COMPLETE)
            .refresh()
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Medium");

        infoPage.cancel(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .editNotes(editedNotes)
            .submit(EvaluatePage.class)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getNotes()).isEqualTo(editedNotes);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7188"})
    @Description("Validate User can edit notes to a scenario but then cancel out without saving changes")
    public void cancelEditNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .editNotes("Validating the ability to edit notes")
            .cancel(EvaluatePage.class)
            .clickActions()
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
    @TestRail(testCaseId = {"7186", "7191"})
    @Description("Validate User can delete notes to a scenario")
    public void deleteNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .editNotes("")
            .submit(EvaluatePage.class)
            .clickActions()
            .info();

        assertThat(infoPage.getNotes(), is(""));
    }

    @Test
    @TestRail(testCaseId = {"7911"})
    @Description("Be able to view and read notes added by other users")
    public void readUsersNotes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials testUser1 = UserUtil.getUser();
        UserCredentials testUser2 = UserUtil.getUser();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .logout()
            .login(testUser2)
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .clickActions()
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
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
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("Default")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectStatus("Complete")
            .selectCostMaturity("Medium")
            .selectAssignee(currentUser)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.STATUS, OperationEnum.IN, "Complete")
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios("RAPID PROTOTYPING", scenarioName)).isEqualTo(1);

        explorePage.filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios("Rapid Prototyping", scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7266", "7913"})
    @Description("Validate the user can add a description in scenario information & notes, then delete the description text & progress")
    public void deleteDescription() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName)
            .highlightScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QAutomation Test Remove Description")
            .inputNotes("")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(componentName.toUpperCase())
            .openScenario(componentName, scenarioName)
            .clickActions()
            .info()
            .editDescription("")
            .submit(EvaluatePage.class)
            .clickActions()
            .info();

        assertThat(infoPage.getDescription(), is(""));
    }

    @Test
    @TestRail(testCaseId = {"7177"})
    @Description("Validate assignee is displayed in the explore view")
    public void actionsAssignValidateAssignee() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        String scenarioCreatedByName = scenariosUtil.getScenarioRepresentationCompleted(cidComponentItem).getCreatedByName();

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .selectMaterial("ABS")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickActions()
            .assign()
            .selectAssignee(scenarioCreatedByName)
            .submit(EvaluatePage.class)
            .clickExplore()
            .configure()
            .selectColumn(ColumnsEnum.ASSIGNEE)
            .moveColumn(DirectionEnum.RIGHT)
            .moveToTop(ColumnsEnum.ASSIGNEE)
            .submit(ExplorePage.class);

        assertThat(explorePage.getTableHeaders(), hasItems(ASSIGNEE.getColumns()));

        explorePage.configure()
            .selectColumn(ASSIGNEE)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class);
    }

    @Test
    @TestRail(testCaseId = {"7190"})
    @Description("Validate notes can be read by different users")
    public void notesReadOtherUsers() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        final String testDescription = "QA Notes to be read by different user";
        final String testNotes = "Testing QA notes notes to be read by different user";

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        infoPage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial("Steel, Cold Worked, AISI 1010")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription(testDescription)
            .inputNotes(testNotes)
            .submit(EvaluatePage.class)
            .logout()
            .login(UserUtil.getUser())
            .navigateToScenario(cidComponentItem)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getDescription()).isEqualTo(testDescription);
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo(testNotes);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6207", "6208"})
    @Description("Validate users can select rows in a sequence by using shift/ctrl buttons")
    public void shiftControlHighlightScenarios() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "BasicScenario_Forging";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        String scenarioName4 = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName, scenarioName2, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName, scenarioName3, resourceFile, currentUser)
            .uploadComponentAndOpen(componentName, scenarioName4, resourceFile, currentUser)
            .clickExplore()
            .selectFilter("Private")
            .shiftHighlightScenario(componentName, scenarioName)
            .controlHighlightScenario(componentName, scenarioName2)
            .shiftHighlightScenario(componentName, scenarioName3)
            .controlHighlightScenario(componentName, scenarioName4);

        softAssertions.assertThat(explorePage.getCellColour(componentName, scenarioName)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());
        softAssertions.assertThat(explorePage.getCellColour(componentName, scenarioName2)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());
        softAssertions.assertThat(explorePage.getCellColour(componentName, scenarioName3)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());
        softAssertions.assertThat(explorePage.getCellColour(componentName, scenarioName4)).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5440"})
    @Description("User can not update the 3D CAD with a differently named 3D CAD file")
    public void updateWithDifferentCADFile() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "Bishop";
        final String extension = ".SLDPRT";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String componentName2 = "Machined Box AMERICAS";
        File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + extension);

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        updateCadFilePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickActions()
            .updateCadFile(resourceFile2);

        final String expectedError = "The supplied CAD file (" + componentName2 + extension + ") cannot be used for this scenario. The name of the file must be " + componentName + extension;
        assertThat(updateCadFilePage.getFileInputError(), containsString(expectedError));
    }

    @Test
    @TestRail(testCaseId = {"5443"})
    @Description("Toolbar options are disabled correctly in appropriate contexts")
    public void buttonsDisabledByDefault() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isFalse();
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isFalse();
        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isFalse();
        softAssertions.assertThat(explorePage.isActionsDropdownEnabled()).isFalse();
        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isFalse();

        softAssertions.assertAll();
    }
}