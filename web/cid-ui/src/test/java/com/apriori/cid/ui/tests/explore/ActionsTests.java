package com.apriori.cid.ui.tests.explore;

import static com.apriori.cid.ui.utils.ColumnsEnum.ASSIGNEE;
import static com.apriori.cid.ui.utils.ColumnsEnum.COST_MATURITY;
import static com.apriori.cid.ui.utils.ColumnsEnum.STATUS;
import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.LAST_ACTION_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.enums.MaterialNameEnum.ABS;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.UpdateCadFilePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.explore.PreviewPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.AssignPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.InfoPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColourEnum;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.DirectionEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

// TODO: 13/12/2023 cn - almost all these tests are selecting a material. do we really need to? need to revise this.

public class ActionsTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private InfoPage infoPage;
    private PreviewPage previewPage;
    private AssignPage assignPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder component;
    private SoftAssertions softAssertions = new SoftAssertions();
    private UpdateCadFilePage updateCadFilePage;

    public ActionsTests() {
        super();
    }

    @Test
    @TestRail(id = {7185, 7257, 7264, 7263, 7268, 6342})
    @Description("Validate user can add notes to a scenario")
    public void addScenarioNotes() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickSearch(component.getComponentName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("Qa Description")
            .inputNotes("QA Notes Test \u2022 MP Testing \u2022 Add and remove notes") //Unicode characters
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(component.getComponentName())
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {7197, 7198, 7200})
    @Description("Validate status and cost maturity columns can be added")
    public void addStatusColumn() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(component.getComponentName())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
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
    @Tag(SMOKE)
    @TestRail(id = {7902, 5436})
    @Description("User can lock and unlock a scenario")
    public void lockUnlockScenario() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.FORGING);

        previewPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(component.getComponentName())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .lock(ExplorePage.class)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
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
    @TestRail(id = {7259, 7265, 7269, 7272, 7189})
    @Description("User can add scenario info and notes from action on evaluate page")
    public void actionsEvaluatePage() {
        final String bulletPointNotes = "• Automation notes 1" +
            "• Automation notes 2" +
            "• Automation notes 3" +
            "• Automation notes 4";

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
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
            .clickCostButton()
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getNotes()).isEqualTo(bulletPointNotes);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7258, 7263, 7267, 7270})
    @Description("User can add scenario info and notes from input & notes tile")
    public void infoNotesPanel() {
        component = new ComponentRequestUtil().getComponent();

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
    @Tag(SMOKE)
    @TestRail(id = {7172, 7175, 5437})
    @Description("Validate ASSIGN action can operate directly on Public Workspace without requiring a Private Workspace Edit")
    public void actionsAssign() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.POWDER_METAL);

        assignPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .assign();

        String scenarioCreatedByName = scenariosUtil.getScenarioCompleted(component).getCreatedByName();

        infoPage = assignPage.selectAssignee(scenarioCreatedByName)
            .submit(ExplorePage.class)
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info();

        assertThat(infoPage.isScenarioInfo("Assignee", scenarioCreatedByName), is(true));
    }

    @Test
    @TestRail(id = {7174, 7173})
    @Description("Validate the user can select an ASSIGN action in the Evaluate page view without opening for Edit")
    public void actionsAssignEvaluatePage() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.POWDER_METAL);

        assignPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .assign();

        String scenarioCreatedByName = scenariosUtil.getScenarioCompleted(component).getCreatedByName();

        assignPage.selectAssignee(scenarioCreatedByName)
            .submit(EvaluatePage.class)
            .clickActions()
            .assign();

        assertThat(assignPage.isAssigneeDisplayed(scenarioCreatedByName), is(true));
    }

    @Test
    @TestRail(id = {7178, 7262, 7910})
    @Description("Validate Assignee is an available search criteria")
    public void filterAssignee() {
        String filterName = generateStringUtil.generateFilterName();

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectStatus("New")
            .selectCostMaturity("Low")
            .selectAssignee(component.getUser())
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.ASSIGNEE, OperationEnum.IN, scenariosUtil.getScenarioCompleted(component).getCreatedByName())
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName()), equalTo(1));
    }

    @Test
    @TestRail(id = {7187, 7271, 6199, 6339, 5438})
    @Description("Validate User can edit notes to a scenario")
    public void editNotes() {
        final String editedNotes = "Testing QA notes validating the ability to edit notes";

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.FORGING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .getCssComponents(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getComponentName(), SCENARIO_NAME_EQ.getKey() + component.getScenarioName(),
                LAST_ACTION_EQ.getKey() + "UPDATE", SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_COMPLETE)
            .refresh()
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");

        infoPage.cancel(ExplorePage.class)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(ExplorePage.class)
            .getCssComponents(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getComponentName(), SCENARIO_NAME_EQ.getKey() + component.getScenarioName(),
                LAST_ACTION_EQ.getKey() + " UPDATE", SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.COST_COMPLETE)
            .refresh()
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Medium");

        infoPage.cancel(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .openScenario(component.getComponentName(), component.getScenarioName())
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
    @TestRail(id = 7188)
    @Description("Validate User can edit notes to a scenario but then cancel out without saving changes")
    public void cancelEditNotes() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.FORGING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName().toUpperCase())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName().toUpperCase())
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .editNotes("Validating the ability to edit notes")
            .cancel(EvaluatePage.class)
            .clickActions()
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
    @TestRail(id = {7186, 7191})
    @Description("Validate User can delete notes to a scenario")
    public void deleteNotes() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName().toUpperCase())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName().toUpperCase())
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .editNotes("")
            .submit(EvaluatePage.class)
            .clickActions()
            .info();

        assertThat(infoPage.getNotes(), is(""));
    }

    @Test
    @TestRail(id = {7911})
    @Description("Be able to view and read notes added by other users")
    public void readUsersNotes() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName().toUpperCase())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .logout()
            .login(UserUtil.getUser())
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName().toUpperCase())
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info();

        assertThat(infoPage.getNotes(), is("Testing QA notes"));
    }

    @Test
    @TestRail(id = {7199, 7912})
    @Description("Validate Status & Cost maturity are searchable attributes")
    public void filterStatusCost() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        component = new ComponentRequestUtil().getComponent();

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial("Default")
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectStatus("Complete")
            .selectCostMaturity("Medium")
            .selectAssignee(component.getUser())
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteria(PropertyEnum.STATUS, OperationEnum.IN, "Complete")
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios("RAPID PROTOTYPING", component.getScenarioName())).isEqualTo(1);

        explorePage.filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .submit(ExplorePage.class)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getListOfScenarios("Rapid Prototyping", component.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7266, 7913})
    @Description("Validate the user can add a description in scenario information & notes, then delete the description text & progress")
    public void deleteDescription() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName())
            .highlightScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QAutomation Test Remove Description")
            .inputNotes("")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .enterKeySearch(component.getComponentName().toUpperCase())
            .openScenario(component.getComponentName(), component.getScenarioName())
            .clickActions()
            .info()
            .editDescription("")
            .submit(EvaluatePage.class)
            .clickActions()
            .info();

        assertThat(infoPage.getDescription(), is(""));
    }

    @Test
    @TestRail(id = {7177})
    @Description("Validate assignee is displayed in the explore view")
    public void actionsAssignValidateAssignee() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        explorePage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .selectMaterial(ABS.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickActions()
            .assign()
            .selectAssignee(scenariosUtil.getScenarioCompleted(component).getCreatedByName())
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
    @TestRail(id = {7190})
    @Description("Validate notes can be read by different users")
    public void notesReadOtherUsers() {
        final String testDescription = "QA Notes to be read by different user";
        final String testNotes = "Testing QA notes notes to be read by different user";

        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.FORGING);

        infoPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1010")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(component, EvaluatePage.class)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription(testDescription)
            .inputNotes(testNotes)
            .submit(EvaluatePage.class)
            .logout()
            .login(UserUtil.getUser())
            .navigateToScenario(component)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getDescription()).isEqualTo(testDescription);
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo(testNotes);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6207, 6208})
    @Description("Validate users can select rows in a sequence by using shift/ctrl buttons")
    public void shiftControlHighlightScenarios() {
        component = new ComponentRequestUtil().getComponent();

        ComponentInfoBuilder component2 = component;
        component2.setScenarioName(new GenerateStringUtil().generateScenarioName());

        ComponentInfoBuilder component3 = component;
        component3.setScenarioName(new GenerateStringUtil().generateScenarioName());

        ComponentInfoBuilder component4 = component;
        component4.setScenarioName(new GenerateStringUtil().generateScenarioName());

        explorePage = new CidAppLoginPage(driver).login(component.getUser())
            .uploadComponentAndOpen(component)
            .uploadComponentAndOpen(component2)
            .uploadComponentAndOpen(component3)
            .uploadComponentAndOpen(component4)
            .clickExplore()
            .selectFilter("Private")
            .shiftHighlightScenario(component.getComponentName(), component.getScenarioName())
            .controlHighlightScenario(component2.getComponentName(), component2.getScenarioName())
            .shiftHighlightScenario(component3.getComponentName(), component3.getScenarioName())
            .controlHighlightScenario(component4.getComponentName(), component4.getScenarioName());

        softAssertions.assertThat(explorePage.getCellColour(component.getComponentName(), component.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());
        softAssertions.assertThat(explorePage.getCellColour(component2.getComponentName(), component2.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());
        softAssertions.assertThat(explorePage.getCellColour(component3.getComponentName(), component3.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());
        softAssertions.assertThat(explorePage.getCellColour(component4.getComponentName(), component4.getScenarioName())).isEqualTo(ColourEnum.PLACEBO_BLUE.getColour());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5440})
    @Description("User cannot update the 3D CAD with a differently named 3D CAD file")
    public void updateWithDifferentCADFile() {
        component = new ComponentRequestUtil().getComponent("Bishop");
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent("Machined Box AMERICAS");

        loginPage = new CidAppLoginPage(driver);
        updateCadFilePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .clickActions()
            .updateCadFile(componentB.getResourceFile());

        final String expectedError = "The supplied CAD file (" + componentB.getComponentName() + componentB.getExtension() + ") cannot be used for this scenario. The name of the file must be " + component.getComponentName() + component.getExtension();
        assertThat(updateCadFilePage.getFileInputError(), containsString(expectedError));
    }

    @Test
    @TestRail(id = {5443})
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
