package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ButtonTypeEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PublishAssembliesTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemC;
    private File subComponentA;
    private File subComponentB;
    private File assembly;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private PublishPage publishPage;
    private ComponentsTablePage componentsTablePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ExplorePage explorePage;
    private InfoPage infoPage;

    public PublishAssembliesTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10763", "10768"})
    @Description("Publish an assembly with no missing sub-components")
    public void shallowPublishAssemblyTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String subComponentAName = "titan battery release";
        String subComponentBName = "titan battery";
        String assemblyName = "titan battery ass";

        subComponentA = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, subComponentAName + ".SLDPRT");
        subComponentB = FileResourceUtil.getCloudFile(ProcessGroupEnum.STOCK_MACHINING, subComponentBName + ".SLDPRT");
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(subComponentAName, scenarioName, subComponentA, currentUser);

        cidComponentItemB = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItem, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(subComponentBName, scenarioName, subComponentB, currentUser);

        cidComponentItemC = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario(4)
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemB, EvaluatePage.class)
            .clickExplore()
            .uploadComponent(assemblyName, scenarioName, assembly, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .selectProcessGroup(ProcessGroupEnum.ASSEMBLY)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(cidComponentItemC, EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @TestRail(testCaseId = {"11812", "6042"})
    @Description("Verify publish scenario modal appears when publish button is clicked")
    public void testIncludeSubcomponentsAndCost() {
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String publishModalMessage = "Public scenarios will be created for each scenario in your selection." +
            " If you wish to retain existing public scenarios, change the scenario name, otherwise they will be overridden.";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        publishPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(FLANGE + ", " + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, FLANGE)
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "")
            .publishSubcomponent();

        assertThat(publishPage.getConflictMessage(), is(equalTo(publishModalMessage)));
    }

    @Test
    @TestRail(testCaseId = "11811")
    @Description("Publish button becomes unavailable when public sub-component selected alongside private sub-component(s)")
    public void testPublishButtonAvailability() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(FLANGE + ", " + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, FLANGE)
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "");

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        componentsTablePage = componentsTablePage.multiSelectSubcomponents(FLANGE + ", " + scenarioName);

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11828")
    @Description("Validate an error message appears if any issues occur")
    public void testPublishWithExistingScenarioName() {
        String preExistingScenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        ComponentInfoBuilder preExistingComponentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            preExistingScenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(preExistingComponentAssembly)
            .uploadAssembly(preExistingComponentAssembly);
        assemblyUtils.costSubComponents(preExistingComponentAssembly)
            .costAssembly(preExistingComponentAssembly);
        assemblyUtils.publishSubComponents(preExistingComponentAssembly)
            .publishAssembly(preExistingComponentAssembly);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName + "", SMALL_RING + "," + scenarioName + "")
            .publishSubcomponent()
            .changeName(preExistingScenarioName)
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class);

        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(BIG_RING, scenarioName, ScenarioStateEnum.PROCESSING_FAILED)).isEqualTo(true);
        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(SMALL_RING, scenarioName, ScenarioStateEnum.PROCESSING_FAILED)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11829")
    @Description("Validate a public iteration of the sub component is created")
    public void testCreatingPublicIterationOfSubcomponent() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        String publishingMessage = "All scenarios are publishing..Close";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        publishPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(FLANGE + ", " + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .checkSubcomponentState(componentAssembly, FLANGE)
            .multiSelectSubcomponents(BOLT + "," + scenarioName + "", NUT + "," + scenarioName + "")
            .publishSubcomponent()
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class);

        assertThat(publishPage.getPublishingMessage(), is(equalTo(publishingMessage)));
    }

    @Test
    @TestRail(testCaseId = {"11813", "11814", "11808", "6051"})
    @Description("Validate public scenarios are overridden from publish modal")
    public void testOverridePublicScenarios() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(PIN + "," + scenarioName);

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.EDIT)).isEqualTo(false);

        componentsTablePage = componentsTablePage.publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName + "", SMALL_RING + "," + scenarioName + "")
            .publishSubcomponent()
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class);

        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(BIG_RING, scenarioName, ScenarioStateEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(componentsTablePage.getListOfScenariosWithStatus(SMALL_RING, scenarioName, ScenarioStateEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(componentsTablePage.getRowDetails(BIG_RING, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTablePage.getRowDetails(SMALL_RING, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        explorePage = componentsTablePage.closePanel()
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(assemblyName);

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10773", "10775"})
    @Description("Shallow Publish correctly publishes to Public Workspace")
    public void testShallowPublishInPublicWorkspace() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .selectFilter("Private")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(assemblyName);

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(1);

        explorePage.selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .editScenario(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(assemblyName);

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10771", "10772", "10776", "10777", "10778", "6746", "6615", "6616", "6617", "6056", "6057"})
    @Description("Modify the Status/ Cost Maturity/ Assignee/ Lock during a Shallow Publish")
    public void testShallowPublishWithModifiedFeatures() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        String scenarioCreatedByName = scenariosUtil.getScenarioRepresentationCompleted(componentAssembly).getCreatedByName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .selectFilter("Private")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(assemblyName);

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(0);

        infoPage = explorePage.checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
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
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .clickActions()
            .lock(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .clickActions()
            .assign()
            .selectAssignee(scenarioCreatedByName)
            .submit(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
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
    @TestRail(testCaseId = "10770")
    @Description("Retain the Status/ Cost Maturity/ Lock during a Shallow Publish")
    public void testShallowPublishWithRetainedFeatures() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        infoPage = loginPage.login(currentUser)
            .selectFilter("Private")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .clickActions()
            .info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .submit(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .clickActions()
            .lock(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .publishScenario(PublishPage.class)
            .publish(ExplorePage.class)
            .refresh()
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("New");
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.isIconDisplayed(StatusIconEnum.LOCK)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10787", "10789"})
    @Description("Shallow Publish over existing Public Scenarios")
    public void testShallowPublishOverExistingPublicScenario() {
        String preExistingScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        ComponentInfoBuilder preExistingComponentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            preExistingScenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(preExistingComponentAssembly)
            .uploadAssembly(preExistingComponentAssembly);
        assemblyUtils.costSubComponents(preExistingComponentAssembly)
            .costAssembly(preExistingComponentAssembly);
        assemblyUtils.publishSubComponents(preExistingComponentAssembly)
            .publishAssembly(preExistingComponentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + preExistingScenarioName)
            .editScenario(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(preExistingComponentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + preExistingScenarioName)
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .cancel(ExplorePage.class);

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, preExistingScenarioName)).isEqualTo(1);

        explorePage.publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(ExplorePage.class)
            .checkComponentStateRefresh(preExistingComponentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(assemblyName);

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, preExistingScenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "10786")
    @Description("Attempt to Shallow Publish over existing Public locked scenarios")
    public void testShallowPublishExistingPublicLockedScenario() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        String publishingMessage = "A public scenario with this name already exists." +
            " The public scenario is locked and cannot be overridden, please supply a different scenario name or cancel the operation.";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        publishPage = loginPage.login(currentUser)
            .selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .clickActions()
            .lock(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Public")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .editScenario(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly, ScenarioStateEnum.COST_COMPLETE)
            .selectFilter("Private")
            .clickSearch(assemblyName)
            .multiSelectScenarios(assemblyName + "," + scenarioName)
            .publishScenario(PublishPage.class);

        assertThat(publishPage.getConflictMessage(), is(equalTo(publishingMessage)));
    }

    @Test
    @TestRail(testCaseId = "10780")
    @Description("Shallow Publish an assembly with Out of Date cost results")
    public void testShallowPublishWithOutOfDateCostResults() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .selectFilter("Private")
            .clickSearch(BOLT)
            .multiSelectScenarios(BOLT + "," + scenarioName)
            .openScenario(BOLT, scenarioName)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .costScenario()
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .clickExplore()
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(FLANGE + "," + scenarioName, NUT + "," + scenarioName)
            .publishSubcomponent()
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ComponentsTablePage.class)
            .checkManifestComplete(componentAssembly, FLANGE)
            .closePanel()
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 1)
            .clickRefresh(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));
    }

    @Test
    @TestRail(testCaseId = {"11094", "11095"})
    @Description("Validate when I select a sub components in a processing state the set inputs button is disabled until the scenario is unselected")
    public void testInputsEnabledDisabled() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(STAND + ", " + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .multiSelectSubcomponents(STAND + "," + scenarioName + "", DRIVE + "," + scenarioName + "");

        softAssertions.assertThat(componentsTablePage.getRowDetails(STAND, scenarioName)).contains("gear");
        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).isFalse();

        componentsTablePage.checkSubcomponentState(componentAssembly, STAND)
            .multiSelectSubcomponents(STAND + "," + scenarioName + "", DRIVE + "," + scenarioName + "")
            .multiSelectSubcomponents(JOINT + "," + scenarioName + "", DRIVE + "," + scenarioName + "");

        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11824", "11825"})
    @Description("Validate when I select any sub components in a processing state the publish button is disabled")
    public void testPublishButtonDisabledEnabled() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String assemblyName = "titan charger ass";
        final String assemblyExtension = ".SLDASM";
        final List<String> subComponentNames = Arrays.asList("titan charger base", "titan charger lead", "titan charger upper");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents("titan charger base" + "," + scenarioName)
            .publishSubcomponent()
            .publish(ComponentsTablePage.class)
            .multiSelectSubcomponents("titan charger base" + "," + scenarioName + "", "titan charger lead" + "," + scenarioName + "");

        softAssertions.assertThat(componentsTablePage.getRowDetails("titan charger base", scenarioName)).contains("gear");
        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);

        componentsTablePage.multiSelectSubcomponents("titan charger base" + "," + scenarioName);

        softAssertions.assertThat(componentsTablePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        softAssertions.assertAll();
    }
}

