package com.apriori.cid.api.tests.evaluate;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.ITERATION_EQ;
import static com.apriori.css.api.enums.CssSearch.LAST_ACTION_EQ;
import static com.apriori.css.api.enums.CssSearch.LATEST_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cid.api.models.request.ForkRequest;
import com.apriori.cid.api.models.request.GroupPublishRequest;
import com.apriori.cid.api.models.response.ScenarioSuccessesFailures;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.PeopleUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.request.component.GroupItems;
import com.apriori.shared.util.models.request.component.PublishRequest;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class GroupPublishTests {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CssComponent cssComponent = new CssComponent();
    private ComponentInfoBuilder componentAssembly;
    private static UserCredentials currentUser;

    private int PUBLIC_WORKSPACE = 0;

    final String chargerBase = "titan charger base";
    final String chargerLead = "titan charger lead";
    final String chargerUpper = "titan charger upper";
    final String componentExtension = ".SLDPRT";
    final String assemblyName = "titan charger ass";
    final String assemblyExtension = ".SLDASM";

    final List<String> subComponentNames = Arrays.asList(chargerBase, chargerLead, chargerUpper);
    final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

    @BeforeEach
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {11851, 11852})
    @Description("Publish a single private sub-component with no public counterpart")
    public void testGroupPublishPrivateSubcomponent() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final List<String> subComponentNames = Arrays.asList(chargerBase, chargerLead);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase);

        SoftAssertions softAssertions = new SoftAssertions();

        cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + chargerBase, SCENARIO_NAME_EQ.getKey() + scenarioName, LAST_ACTION_EQ.getKey() + " publish")
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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, componentAssembly, chargerLead);

        cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + chargerLead, SCENARIO_NAME_EQ.getKey() + newScenarioName, LAST_ACTION_EQ.getKey() + "PUBLISH")
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11853})
    @Description("Publish a multiple private sub-components with no public counterpart")
    public void testGroupPublishPrivateSubcomponents() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase, chargerLead, chargerUpper);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(componentName ->
            cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName, LAST_ACTION_EQ.getKey() + " publish")
                .forEach(scenario -> softAssertions.assertThat(scenario.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11854})
    @Description("Publish multiple private sub-components with no public counterparts Setting Override to false and supplying a scenario name")
    public void testGroupPublishPrivateSubcomponentsFalseOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase, chargerLead, chargerUpper);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(componentName ->
            cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() +
                    newScenarioName, LAST_ACTION_EQ.getKey() + " publish")
                .forEach(scenario -> softAssertions.assertThat(scenario.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        subComponentNames.forEach(componentName -> cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + componentName,
                SCENARIO_NAME_EQ.getKey() + newScenarioName)
            .forEach(scenario -> softAssertions.assertThat(scenario.getScenarioIterationKey().getWorkspaceId()).isNotEqualTo(user.getCustomAttributes().getWorkspaceId())));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11855})
    @Description("Publish private sub-component with public counterpart Setting Override to true")
    public void testGroupPublishPrivateSubcomponentTrueOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final List<String> subComponentNames = Collections.singletonList(chargerBase);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + chargerBase, SCENARIO_NAME_EQ.getKey() + scenarioName,
                ITERATION_EQ.getKey() + " 1", LATEST_EQ.getKey() + "false")
            .size()).isGreaterThanOrEqualTo(1);

        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + chargerBase, SCENARIO_NAME_EQ.getKey() + scenarioName,
                ITERATION_EQ.getKey() + " 2", LATEST_EQ.getKey() + "true")
            .size()).isGreaterThanOrEqualTo(1);

        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + chargerBase, SCENARIO_NAME_EQ.getKey() + scenarioName)
                .stream()
                .noneMatch(o -> o.getScenarioIterationKey().getWorkspaceId().equals(user.getCustomAttributes().getWorkspaceId())))
            .isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11856})
    @Description("Publish private sub-component with public counterpart Setting Override to false")
    public void testGroupPublishPrivateSubcomponentFalseOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final List<String> subComponentNames = Arrays.asList(chargerBase);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(false)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, componentAssembly, chargerBase);

        SoftAssertions softAssertions = new SoftAssertions();


        cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + chargerBase, SCENARIO_NAME_EQ.getKey() + scenarioName,
                ITERATION_EQ.getKey() + " 1", LATEST_EQ.getKey() + "true")
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE));

        cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + chargerBase, SCENARIO_NAME_EQ.getKey() + newScenarioName,
                ITERATION_EQ.getKey() + " 1", LATEST_EQ.getKey() + "true")
            .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11857})
    @Description("Publish multiple private sub-components with public counterparts Setting Override to true")
    public void testGroupPublishPrivateSubcomponentsTrueOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase, chargerLead, chargerUpper);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase, chargerLead, chargerUpper);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, componentAssembly, chargerBase, chargerLead, chargerUpper);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(subComponent ->
            cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName, ITERATION_EQ.getKey() + " 1",
                    LATEST_EQ.getKey() + " false")
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        subComponentNames.forEach(subComponent ->
            cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName, ITERATION_EQ.getKey() + " 2",
                    LATEST_EQ.getKey() + " true")
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11858})
    @Description("Publish multiple private sub-components with public counterparts Setting Override to false and supplying a scenario name")
    public void testGroupPublishPrivateSubcomponentsFalseOverrideNewScenario() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase, chargerLead, chargerUpper);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase, chargerLead, chargerUpper);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, componentAssembly, chargerBase, chargerLead, chargerUpper);

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(subComponent ->
            cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName2, ITERATION_EQ.getKey() + " 1",
                    LATEST_EQ.getKey() + " true")
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        subComponentNames.forEach(subComponent ->
            cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName2, ITERATION_EQ.getKey() + " 1",
                    LATEST_EQ.getKey() + " true")
                .forEach(o -> softAssertions.assertThat(o.getScenarioIterationKey().getWorkspaceId()).isEqualTo(PUBLIC_WORKSPACE)));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11859})
    @Description("Attempt to use pre-existing scenario name for public component when publishing with create new option")
    public void testGroupPublishPrivateSubAttemptExistingScenarioName() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final List<String> subComponentNames = Collections.singletonList(chargerBase);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, componentAssembly, chargerBase);

        PublishRequest publishRequest3 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(false)
            .status("New")
            .scenarioName(scenarioName2)
            .build();

        GroupPublishRequest groupPublishRequest3 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest3)
            .scenarioPublished(true)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> publishSuccessFailure = scenariosUtil.postPublishGroupScenarios(groupPublishRequest3, componentAssembly, chargerBase);

        SoftAssertions softAssertions = new SoftAssertions();

        publishSuccessFailure.getResponseEntity()
            .getFailures().forEach(o -> softAssertions.assertThat(o.getError()).isEqualTo("Scenario '" + scenarioName2 + "' has been published, scenario can not be published"));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11935})
    @Description("Attempt to use publish a public scenario")
    public void testAttemptPublishPublic() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final List<String> subComponentNames = Collections.singletonList(chargerBase);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

        PublishRequest publishRequest2 = PublishRequest.builder()
            .assignedTo(user.getIdentity())
            .costMaturity("Initial")
            .override(true)
            .status("New")
            .build();

        GroupPublishRequest groupPublishRequest2 = GroupPublishRequest.builder()
            .componentInfo(componentAssembly)
            .publishRequest(publishRequest2)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> publishSuccessFailure = scenariosUtil.postPublishGroupScenarios(groupPublishRequest2, componentAssembly, chargerBase);

        SoftAssertions softAssertions = new SoftAssertions();

        publishSuccessFailure.getResponseEntity()
            .getFailures().forEach(o -> softAssertions.assertThat(o.getError()).isEqualTo("Scenario '" + scenarioName + "' has been published, scenario can not be published"));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11936})
    @Description("Attempt to publish a scenario that does not exist (incorrect scenario name or component name)")
    public void testAttemptPublicScenarioNotExist() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final List<String> subComponentNames = Collections.singletonList(chargerBase);

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
            .build();

        scenariosUtil.postPublishGroupScenarios(groupPublishRequest, componentAssembly, chargerBase);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

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
