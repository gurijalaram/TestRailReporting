package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.SetInputStatusPage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class EditAssembliesTest extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private EditScenarioStatusPage editScenarioStatusPage;
    private ComponentsListPage componentsListPage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public EditAssembliesTest() {
        super();
    }

    @Test
    @TestRail(testCaseId = "10810")
    @Description("Shallow Edit an assembly with scenarios uncosted")
    public void testUploadPublishAssemblyAndEdit() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadPublishAndOpenAssembly(
                subComponentNames,
                componentExtension,
                processGroupEnum,
                assemblyName,
                assemblyExtension,
                scenarioName,
                currentUser)
            .editScenario()
            .close(EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION), is(true));
        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE), is(true));
    }

    @Test
    @TestRail(testCaseId = {"10799", "10768"})
    @Description("Shallow Edit assembly and scenarios that was cost in CI Design")
    public void testUploadCostPublishAssemblyAndEdit() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final String mode = "Manual";
        final String material = "Steel, Cold Worked, AISI 1010";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .uploadCostPublishAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                mode,
                material,
                currentUser)
            .editScenario()
            .close(EvaluatePage.class);
    }

    @Test
    @TestRail(testCaseId = {"12037", "12039"})
    @Description("Validate I can switch between public sub components")
    public void testSwitchBetweenPublicSubcomponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String editedComponentScenarioName = new GenerateStringUtil().generateScenarioName();
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
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly);

        double initialTotalCost = evaluatePage.getCostResults("Total Cost");
        double initialComponentsCost = evaluatePage.getCostResults("Components Cost");

        evaluatePage.openComponents()
            .multiSelectSubcomponents(PIN + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsListPage.class)
            .multiSelectSubcomponents(PIN + "," + scenarioName)
            .setInputs()
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .applyAndCost(SetInputStatusPage.class)
            .close(ComponentsListPage.class)
            .multiSelectSubcomponents(PIN + "," + scenarioName)
            .publishSubcomponent()
            .changeName(editedComponentScenarioName)
            .clickContinue(PublishPage.class)
            .publish(ComponentsListPage.class);

        assertThat(componentsListPage.getRowDetails(PIN, editedComponentScenarioName), is(StatusIconEnum.PUBLIC.getStatusIcon()));

        evaluatePage = componentsListPage.closePanel()
            .costScenario()
            .costScenarioConfirmation("Yes");

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(initialTotalCost).isGreaterThan(modifiedTotalCost);
        softAssertions.assertThat(initialComponentsCost).isGreaterThan(modifiedComponentsCost);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "12038")
    @Description("Validate I can switch between private sub components")
    public void testSwitchingBetweenPrivateSubcomponents() {
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioNameChange = new GenerateStringUtil().generateScenarioName();

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
            .selectFilter("Recent")
            .clickSearch(BOLT)
            .multiSelectScenarios("" + BOLT + ", " + scenarioName + "")
            .createScenario()
            .enterScenarioName(scenarioNameChange)
            .submit(EvaluatePage.class)
            .navigateToScenario(componentAssembly);

        double initialTotalCost = evaluatePage.getCostResults("Total Cost");
        double initialComponentsCost = evaluatePage.getCostResults("Components Cost");

        evaluatePage.openComponents()
            .switchScenarioName(BOLT, scenarioNameChange)
            .multiSelectSubcomponents(BOLT + "," + scenarioNameChange)
            .setInputs()
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .applyAndCost(SetInputStatusPage.class)
            .close(ComponentsListPage.class)
            .switchScenarioName(BOLT, scenarioNameChange)
            .closePanel()
            .costScenario();

        double modifiedTotalCost = evaluatePage.getCostResults("Total Cost");
        double modifiedComponentsCost = evaluatePage.getCostResults("Components Cost");

        softAssertions.assertThat(initialTotalCost).isGreaterThan(modifiedTotalCost);
        softAssertions.assertThat(initialComponentsCost).isGreaterThan(modifiedComponentsCost);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "12040")
    @Description("Validate I can switch between public sub components when private iteration is deleted")
    public void testSwitchingPublicSubcomponentsWithDeletedPrivateIteration() {
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

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
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BOLT + "," + scenarioName)
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(EvaluatePage.class)
            .clickRefresh(ComponentsListPage.class)
            .multiSelectSubcomponents(BOLT + "," + scenarioName)
            .setInputs()
            .selectProcessGroup(ProcessGroupEnum.CASTING)
            .applyAndCost(SetInputStatusPage.class)
            .close(EvaluatePage.class)
            .clickRefresh(ComponentsListPage.class);

        subComponentNames.forEach(componentName ->
            assertThat(componentsListPage.getScenarioState(componentName, scenarioName, currentUser, ScenarioStateEnum.COST_COMPLETE),
                is(ScenarioStateEnum.COST_COMPLETE.getState())));

        componentsListPage.closePanel()
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(BOLT)
            .multiSelectScenarios("" + BOLT + ", " + scenarioName + "")
            .delete()
            .submit(ExplorePage.class)
            .navigateToScenario(componentAssembly)
            .openComponents();

        softAssertions.assertThat(componentsListPage.getRowDetails(BOLT, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10895", "10897"})
    @Description("Edit public sub-component with Private counterpart (Override)")
    public void testEditPublicAndOverridePrivateSubcomponent() {
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
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName, SMALL_RING + "," + scenarioName)
            .editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsListPage = editScenarioStatusPage.close(ComponentsListPage.class);

        softAssertions.assertThat(componentsListPage.getRowDetails(BIG_RING, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails(SMALL_RING, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10896", "10898"})
    @Description("Edit public sub-component with Private counterpart (Override)")
    public void testEditPublicAndRenamePrivateSubcomponent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
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
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + scenarioName, SMALL_RING + "," + scenarioName)
            .editSubcomponent(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(newScenarioName)
            .clickContinue(EditScenarioStatusPage.class);

        assertThat(editScenarioStatusPage.getEditScenarioMessage(), containsString("All private scenarios created."));

        componentsListPage = editScenarioStatusPage.close(ComponentsListPage.class);

        softAssertions.assertThat(componentsListPage.getListOfScenarios(BIG_RING, newScenarioName)).isEqualTo(1);
        softAssertions.assertThat(componentsListPage.getListOfScenarios(SMALL_RING, newScenarioName)).isEqualTo(1);

        softAssertions.assertAll();
    }
}
