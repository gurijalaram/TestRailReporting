package com.evaluate;

import static com.apriori.css.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.entity.enums.CssSearch.ITERATION_EQ;
import static com.apriori.css.entity.enums.CssSearch.LAST_ACTION_EQ;
import static com.apriori.css.entity.enums.CssSearch.LATEST_EQ;
import static com.apriori.css.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.entity.enums.CssSearch.SCENARIO_STATE_EQ;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.request.GroupItems;
import com.apriori.cidappapi.entity.request.GroupPublishRequest;
import com.apriori.cidappapi.entity.request.PublishRequest;
import com.apriori.cidappapi.entity.response.ScenarioSuccessesFailures;
import com.apriori.cidappapi.entity.response.User;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.PeopleUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupPublishTests {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CssComponent cssComponent = new CssComponent();
    private ComponentInfoBuilder componentAssembly;
    private static UserCredentials currentUser;

    private int PUBLIC_WORKSPACE = 0;

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = {"11851", "11852"})
    @Description("Publish a single private sub-component with no public counterpart")
    public void testGroupPublishPrivateSubcomponent() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND, DRIVE);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("new")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_NAME_EQ.getKey() + scenarioName, LAST_ACTION_EQ.getKey() + " publish")
            .getResponseEntity()
            .getItems()
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE));

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("new")
            .scenarioName(newScenarioName)
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, DRIVE + "," + scenarioName);

        cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + DRIVE, SCENARIO_NAME_EQ.getKey() + scenarioName, LAST_ACTION_EQ.getKey() + "  publish")
            .getResponseEntity()
            .getItems()
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11853"})
    @Description("Publish a multiple private sub-components with no public counterpart")
    public void testGroupPublishPrivateSubcomponents() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("new")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(componentName ->
            cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName, LAST_ACTION_EQ.getKey() + " publish")
                .getResponseEntity()
                .getItems()
                .forEach(scenario -> softAssertions.assertThat(scenario.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11854"})
    @Description("Publish multiple private sub-components with no public counterparts Setting Override to false and supplying a scenario name")
    public void testGroupPublishPrivateSubcomponentsFalseOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial".toUpperCase())
            .override(false)
            .status("New".toUpperCase())
            .scenarioName(newScenarioName)
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(componentName ->
            cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() +
                    scenarioName, LAST_ACTION_EQ.getKey() + " publish")
                .getResponseEntity()
                .getItems()
                .forEach(scenario -> softAssertions.assertThat(scenario.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        subComponentNames.forEach(componentName -> cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentName,
                SCENARIO_NAME_EQ.getKey() + newScenarioName).getResponseEntity()
            .getItems()
            .forEach(scenario -> softAssertions.assertThat(scenario.getScenarioIterationKey().getWorkspaceId()).isNotEqualTo(user.getCustomAttributes().getWorkspaceId())));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11855"})
    @Description("Publish private sub-component with public counterpart Setting Override to true")
    public void testGroupPublishPrivateSubcomponentTrueOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND + "," + scenarioName);

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_STATE_EQ.getKey() + scenarioName,
                ITERATION_EQ.getKey() + " 1", LATEST_EQ.getKey() + "false")
            .getResponseEntity()
            .getItems()
            .size()).isGreaterThanOrEqualTo(1);

        softAssertions.assertThat(cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_STATE_EQ.getKey() + scenarioName,
                ITERATION_EQ.getKey() + " 2", LATEST_EQ.getKey() + "true")
            .getResponseEntity()
            .getItems()
            .size()).isGreaterThanOrEqualTo(1);

        softAssertions.assertThat(cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_STATE_EQ.getKey() + scenarioName)
                .getResponseEntity()
                .getItems()
                .stream()
                .noneMatch(o -> o.getScenarioIterationKey().getWorkspaceId().equals(user.getCustomAttributes().getWorkspaceId())))
            .isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11856"})
    @Description("Publish private sub-component with public counterpart Setting Override to false")
    public void testGroupPublishPrivateSubcomponentFalseOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(false)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND + "," + scenarioName);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .scenarioName(newScenarioName)
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, STAND + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();


        cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_STATE_EQ.getKey() + scenarioName,
                ITERATION_EQ.getKey() + " 1", LATEST_EQ.getKey() + "true")
            .getResponseEntity()
            .getItems()
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE));

        cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_STATE_EQ.getKey() + newScenarioName,
                ITERATION_EQ.getKey() + " 1", LATEST_EQ.getKey() + "true")
            .getResponseEntity()
            .getItems()
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11857"})
    @Description("Publish multiple private sub-components with public counterparts Setting Override to true")
    public void testGroupPublishPrivateSubcomponentsTrueOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(subComponent ->
            cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName, ITERATION_EQ + " 1",
                    LATEST_EQ.getKey() + " false")
                .getResponseEntity()
                .getItems()
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        subComponentNames.forEach(subComponent ->
            cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName, ITERATION_EQ.getKey() + " 2",
                    LATEST_EQ.getKey() + " true")
                .getResponseEntity()
                .getItems()
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11858"})
    @Description("Publish multiple private sub-components with public counterparts Setting Override to false and supplying a scenario name")
    public void testGroupPublishPrivateSubcomponentsFalseOverrideNewScenario() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND, DRIVE, JOINT);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .scenarioName(scenarioName2)
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(subComponent ->
            cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName, ITERATION_EQ.getKey() + " 1",
                    LATEST_EQ.getKey() + " true")
                .getResponseEntity()
                .getItems()
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        subComponentNames.forEach(subComponent ->
            cssComponent.getCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName2, ITERATION_EQ.getKey() + " 1",
                    LATEST_EQ.getKey() + " true")
                .getResponseEntity()
                .getItems()
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11859"})
    @Description("Attempt to use pre-existing scenario name for public component when publishing with create new option")
    public void testGroupPublishPrivateSubAttemptExistingScenarioName() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND + "," + scenarioName);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .scenarioName(scenarioName2)
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, STAND + "," + scenarioName);

        PublishRequest publishRequest3 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .scenarioName(scenarioName)
            .build();

        GroupPublishRequest groupPublishRequest3 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest3)
            .workspaceId(PUBLIC_WORKSPACE)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> publishSuccessFailure = scenariosUtil.postPublishGroupScenarios(groupPublishRequest3, STAND + "," + scenarioName);

        publishSuccessFailure.getResponseEntity()
            .getFailures().forEach(o -> assertThat(o.getError(), equalTo("Scenario '" + scenarioName + "' has been published, scenario can not be published")));
    }

    @Test
    @TestRail(testCaseId = {"11935"})
    @Description("Attempt to use publish a public scenario")
    public void testAttemptPublishPublic() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND);
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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND + "," + scenarioName);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .workspaceId(PUBLIC_WORKSPACE)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> publishSuccessFailure = scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, STAND + "," + scenarioName);

        publishSuccessFailure.getResponseEntity()
            .getFailures().forEach(o -> assertThat(o.getError(), equalTo("Scenario '" + scenarioName + "' has been published, scenario can not be published")));
    }

    @Test
    @TestRail(testCaseId = {"11936"})
    @Description("Attempt to publish a scenario that does not exist (incorrect scenario name or component name)")
    public void testAttemptPublicScenarioNotExist() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";

        final String UNKNOWN_COMPONENT_ID = "41EBF4GGGGGG";
        final String UNKNOWN_SCENARIO_ID = "41EBF4FFFFFF";

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

        User user = new PeopleUtil().getCurrentUser(currentUser);

        PublishRequest publishRequest = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest)
            .workspaceId(user.getCustomAttributes().getWorkspaceId())
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, STAND + "," + scenarioName);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND + "," + scenarioName);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .groupItems(Collections.singletonList(GroupItems.builder()
                .componentIdentity(UNKNOWN_COMPONENT_ID)
                .scenarioIdentity(componentAssembly.getSubComponents().stream().findFirst().get().getScenarioIdentity())
                .build()))
            .user(componentAssembly.getUser())
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> publishSuccessFailure = scenariosUtil.postSimplePublishGroupScenarios(publishRequest2);

        SoftAssertions softAssertions = new SoftAssertions();

        publishSuccessFailure.getResponseEntity()
            .getFailures().forEach(o -> softAssertions.assertThat(o.getError()).isEqualTo("Resource 'Component' with identity '" + UNKNOWN_COMPONENT_ID + "' was not found"));

        PublishRequest publishRequest3 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .groupItems(Collections.singletonList(GroupItems.builder()
                .componentIdentity(componentAssembly.getSubComponents().stream().findFirst().get().getComponentIdentity())
                .scenarioIdentity(UNKNOWN_SCENARIO_ID)
                .build()))
            .user(componentAssembly.getUser())
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> publishSuccessFailure2 = scenariosUtil.postSimplePublishGroupScenarios(publishRequest3);

        publishSuccessFailure2.getResponseEntity()
            .getFailures().forEach(o -> softAssertions.assertThat(o.getError()).isEqualTo("Resource 'Scenario' with identity '" + UNKNOWN_SCENARIO_ID + "' was not found"));

        softAssertions.assertAll();
    }
}
