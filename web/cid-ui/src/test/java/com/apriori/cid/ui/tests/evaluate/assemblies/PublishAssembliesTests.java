package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.InfoPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ButtonTypeEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dto.AssemblyDTORequest;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PublishAssembliesTests extends TestBaseUI {

    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private PublishPage publishPage;
    private ComponentsTablePage componentsTablePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ExplorePage explorePage;
    private InfoPage infoPage;

    public PublishAssembliesTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (componentAssembly.getUser() != null) {
            new UserPreferencesUtil().resetSettings(componentAssembly.getUser());
        }
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {10763, 10768})
    @Description("Publish an assembly with no missing sub-components")
    public void shallowPublishAssemblyTest() {
        String preferPublic = "Prefer Public Scenarios";

        componentAssembly = new AssemblyDTORequest().getAssembly("titan battery ass");
        ComponentInfoBuilder subComponentA = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("titan battery release")).findFirst().get();
        ComponentInfoBuilder subComponentB = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("titan battery")).findFirst().get();


        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .openSettings()
            .goToAssemblyDefaultsTab()
            .selectAssemblyStrategy(preferPublic)
            .submit(EvaluatePage.class)
            .uploadComponentAndOpen(subComponentA)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(subComponentA, EvaluatePage.class)
            .clickExplore()
            .uploadComponentAndOpen(subComponentB)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario(4)
            .publishScenario(PublishPage.class)
            .publish(subComponentB, EvaluatePage.class)
            .clickExplore()
            .uploadComponentAndOpen(componentAssembly);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @TestRail(id = {11812, 6042})
    @Description("Verify publish scenario modal appears when publish button is clicked")
    public void testIncludeSubcomponentsAndCost() {
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String publishModalMessage = "Public scenarios will be created for each scenario in your selection." +
            " If you wish to retain existing public scenarios, change the scenario name, otherwise they will be overridden.";

        componentAssembly = new AssemblyDTORequest().getAssembly("flange c");
        ComponentInfoBuilder flangeSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(FLANGE)).findFirst().get();
        ComponentInfoBuilder nutSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(NUT)).findFirst().get();
        ComponentInfoBuilder boltSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(BOLT)).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        publishPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(flangeSubcomponent.getComponentName() + ", " + flangeSubcomponent.getScenarioName())
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, flangeSubcomponent.getComponentName())
            .multiSelectSubcomponents(nutSubcomponent.getComponentName() + "," + componentAssembly.getScenarioName(), boltSubcomponent.getComponentName() + "," + componentAssembly.getScenarioName())
            .publishSubcomponent();

        assertThat(publishPage.getConflictMessage(), is(equalTo(publishModalMessage)));
    }

    @Test
    @TestRail(id = 11811)
    @Description("Publish button becomes unavailable when public sub-component selected alongside private sub-component(s)")
    public void testPublishButtonAvailability() {
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        componentAssembly = new AssemblyDTORequest().getAssembly("flange c");
        ComponentInfoBuilder flangeSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(FLANGE)).findFirst().get();
        ComponentInfoBuilder nutSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(NUT)).findFirst().get();
        ComponentInfoBuilder boltSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(BOLT)).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(flangeSubcomponent.getComponentName() + ", " + flangeSubcomponent.getScenarioName())
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, flangeSubcomponent.getComponentName())
            .multiSelectSubcomponents(boltSubcomponent.getComponentName() + "," + boltSubcomponent.getScenarioName(), nutSubcomponent.getComponentName() + "," + nutSubcomponent.getScenarioName());

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        componentsTablePage = componentsTablePage.multiSelectSubcomponents(flangeSubcomponent.getComponentName() + ", " + flangeSubcomponent.getScenarioName());

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11828)
    @Description("Validate an error message appears if any issues occur")
    public void testPublishWithExistingScenarioName() {
        final String BIG_RING = "big ring";
        final String SMALL_RING = "small ring";

        ComponentInfoBuilder preExistingComponentAssembly = new AssemblyDTORequest().getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(preExistingComponentAssembly)
            .uploadAssembly(preExistingComponentAssembly);
        assemblyUtils.costSubComponents(preExistingComponentAssembly)
            .costAssembly(preExistingComponentAssembly);
        assemblyUtils.publishSubComponents(preExistingComponentAssembly)
            .publishAssembly(preExistingComponentAssembly);

        componentAssembly = new AssemblyDTORequest().getAssembly("Hinge assembly");
        ComponentInfoBuilder bigRingSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(BIG_RING)).findFirst().get();
        ComponentInfoBuilder smallRingSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(SMALL_RING)).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(bigRingSubcomponent.getComponentName() + "," + bigRingSubcomponent.getScenarioName(), smallRingSubcomponent.getComponentName() + "," + smallRingSubcomponent.getScenarioName())
            .publishSubcomponent()
            .changeName(preExistingComponentAssembly.getScenarioName())
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class);

        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(bigRingSubcomponent.getComponentName(), bigRingSubcomponent.getScenarioName(), ScenarioStateEnum.PROCESSING_FAILED)).isEqualTo(true);
        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(smallRingSubcomponent.getComponentName(), smallRingSubcomponent.getScenarioName(), ScenarioStateEnum.PROCESSING_FAILED)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11829)
    @Description("Validate a public iteration of the sub component is created")
    public void testCreatingPublicIterationOfSubcomponent() {
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String publishingMessage = "All scenarios are publishing...Close";

        componentAssembly = new AssemblyDTORequest().getAssembly("flange c");
        ComponentInfoBuilder flangeSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(FLANGE)).findFirst().get();
        ComponentInfoBuilder nutSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(NUT)).findFirst().get();
        ComponentInfoBuilder boltSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(BOLT)).findFirst().get();

        loginPage = new CidAppLoginPage(driver);
        publishPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(flangeSubcomponent.getComponentName() + ", " + flangeSubcomponent.getScenarioName())
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, flangeSubcomponent.getComponentName())
            .multiSelectSubcomponents(boltSubcomponent.getComponentName() + "," + boltSubcomponent.getScenarioName(), nutSubcomponent.getComponentName() + "," + nutSubcomponent.getScenarioName())
            .publishSubcomponent()
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class);

        assertThat(publishPage.getPublishingMessage(), is(equalTo(publishingMessage)));
    }

    @Test
    @TestRail(id = {11813, 11814, 11808, 6051})
    @Description("Validate public scenarios are overridden from publish modal")
    public void testOverridePublicScenarios() {
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        componentAssembly = new AssemblyDTORequest().getAssembly("Hinge assembly");
        ComponentInfoBuilder bigRingSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(BIG_RING)).findFirst().get();
        ComponentInfoBuilder smallRingSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(SMALL_RING)).findFirst().get();
        ComponentInfoBuilder pinSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(PIN)).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(pinSubcomponent.getComponentName() + "," + pinSubcomponent.getScenarioName());

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EDIT)).isEqualTo(false);

        componentsTablePage = componentsTablePage.publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .multiSelectSubcomponents(bigRingSubcomponent.getComponentName() + "," + bigRingSubcomponent.getScenarioName(), smallRingSubcomponent.getComponentName() + "," + bigRingSubcomponent.getScenarioName())
            .publishSubcomponent()
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class);

        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(bigRingSubcomponent.getComponentName(), bigRingSubcomponent.getScenarioName(), ScenarioStateEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(smallRingSubcomponent.getComponentName(), smallRingSubcomponent.getScenarioName(), ScenarioStateEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(componentsTablePage.getRowDetails(bigRingSubcomponent.getComponentName(), bigRingSubcomponent.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(smallRingSubcomponent.getComponentName(), smallRingSubcomponent.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        explorePage = componentsTablePage.closePanel()
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(componentAssembly.getComponentName());

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10773, 10775})
    @Description("Shallow Publish correctly publishes to Public Workspace")
    public void testShallowPublishInPublicWorkspace() {
        componentAssembly = new AssemblyDTORequest().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentAssembly.getUser())
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName());

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(1);

        explorePage.selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .editScenario(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName());

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10771, 10772, 10776, 10777, 10778, 6746, 6615, 6616, 6617, 6056, 6057})
    @Description("Modify the Status/ Cost Maturity/ Assignee/ Lock during a Shallow Publish")
    public void testShallowPublishWithModifiedFeatures() {
        componentAssembly = new AssemblyDTORequest().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        String scenarioCreatedByName = scenariosUtil.getScenarioCompleted(componentAssembly).getCreatedByName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentAssembly.getUser())
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName());

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(0);

        infoPage = explorePage.checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .lock(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .assign()
            .selectAssignee(scenarioCreatedByName)
            .submit(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getDescription()).isEqualTo("QA Test Description");
        softAssertions.assertThat(infoPage.getNotes()).isEqualTo("Testing QA notes");
        softAssertions.assertThat(infoPage.isScenarioInfo("Assignee", scenarioCreatedByName)).isEqualTo(true);
        softAssertions.assertThat(infoPage.isIconDisplayed(StatusIconEnum.LOCK)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10770)
    @Description("Retain the Status/ Cost Maturity/ Lock during a Shallow Publish")
    public void testShallowPublishWithRetainedFeatures() {
        componentAssembly = new AssemblyDTORequest().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(componentAssembly.getUser())
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .submit(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .lock(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.isIconDisplayed(StatusIconEnum.LOCK)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10787, 10789})
    @Description("Shallow Publish over existing Public Scenarios")
    public void testShallowPublishOverExistingPublicScenario() {

        componentAssembly = new AssemblyDTORequest().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentAssembly.getUser())
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .editScenario(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .cancel(ExplorePage.class);

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(1);

        explorePage.publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName());

        softAssertions.assertThat(explorePage.getListOfScenarios(componentAssembly.getComponentName(), componentAssembly.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10786)
    @Description("Attempt to Shallow Publish over existing Public locked scenarios")
    public void testShallowPublishExistingPublicLockedScenario() {
        String publishingWarning = "A public scenario with this name already exists." +
            " The public scenario is locked and cannot be overridden, please supply a different scenario name or cancel the operation.";

        componentAssembly = new AssemblyDTORequest().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        publishPage = loginPage.login(componentAssembly.getUser())
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .clickActions()
            .lock(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .editScenario(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(componentAssembly.getComponentName())
            .multiSelectScenarios(componentAssembly.getComponentName() + "," + componentAssembly.getScenarioName())
            .publishScenario(PublishPage.class);

        assertThat(publishPage.getConflictMessage(), is(equalTo(publishingWarning)));
    }

    @Test
    @TestRail(id = 10780)
    @Description("Shallow Publish an assembly with Out of Date cost results")
    public void testShallowPublishWithOutOfDateCostResults() {
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        componentAssembly = new AssemblyDTORequest().getAssembly("flange c");
        ComponentInfoBuilder flangeSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(FLANGE)).findFirst().get();
        ComponentInfoBuilder nutSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(NUT)).findFirst().get();
        ComponentInfoBuilder boltSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(BOLT)).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(componentAssembly.getUser())
            .selectFilter("Private")
            .clickSearch(boltSubcomponent.getComponentName())
            .multiSelectScenarios(boltSubcomponent.getComponentName() + "," + boltSubcomponent.getScenarioName())
            .openScenario(boltSubcomponent.getComponentName(), boltSubcomponent.getScenarioName())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .clickExplore()
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(flangeSubcomponent.getComponentName() + "," + flangeSubcomponent.getScenarioName(), nutSubcomponent.getComponentName() + "," + nutSubcomponent.getScenarioName())
            .publishSubcomponent()
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, flangeSubcomponent.getComponentName())
            .closePanel()
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 1)
            .clickRefresh(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @TestRail(id = {11094, 11095})
    @Description("Validate when I select a sub components in a processing state the set inputs button is disabled until the scenario is unselected")
    public void testInputsEnabledDisabled() {
        final String STAND = "stand";
        final String JOINT = "joint";

        componentAssembly = new AssemblyDTORequest().getAssembly("oldham");
        ComponentInfoBuilder standSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(STAND)).findFirst().get();
        ComponentInfoBuilder jointSubcomponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(JOINT)).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(standSubcomponent.getComponentName() + ", " + standSubcomponent.getScenarioName())
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .multiSelectSubcomponents(standSubcomponent.getComponentName() + "," + standSubcomponent.getScenarioName(), jointSubcomponent.getComponentName() + "," + jointSubcomponent.getScenarioName());

        softAssertions.assertThat(componentsTablePage.getRowDetails(standSubcomponent.getComponentName(), standSubcomponent.getScenarioName())).contains("gear");
        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).isFalse();

        componentsTablePage.checkSubcomponentState(componentAssembly, standSubcomponent.getComponentName());
        componentAssembly.getSubComponents().forEach(subcomponent ->
            componentsTablePage.multiSelectSubcomponents(subcomponent.getComponentName() + "," + subcomponent.getScenarioName()));

        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11824, 11825})
    @Description("Validate when I select any sub components in a processing state the publish button is disabled")
    public void testPublishButtonDisabledEnabled() {
        final String BASE = "titan charger base";
        final String LEAD = "titan charger lead";

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        componentAssembly = new AssemblyDTORequest().getAssembly("titan charger ass");
        ComponentInfoBuilder baseComponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(BASE)).findFirst().get();
        ComponentInfoBuilder leadComponent = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase(LEAD)).findFirst().get();

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(baseComponent.getComponentName() + "," + baseComponent.getComponentName())
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .multiSelectSubcomponents(baseComponent.getComponentName() + "," + baseComponent.getComponentName(), leadComponent.getComponentName() + "," + leadComponent.getScenarioName());

        softAssertions.assertThat(componentsTablePage.getRowDetails(baseComponent.getComponentName(), baseComponent.getScenarioName())).contains("gear");
        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);

        componentsTablePage.multiSelectSubcomponents(baseComponent.getComponentName() + "," + baseComponent.getComponentName());

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        softAssertions.assertAll();
    }
}

