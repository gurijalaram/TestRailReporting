package com.evaluate;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.request.GroupItems;
import com.apriori.cidappapi.entity.response.ScenarioSuccessesFailures;
import com.apriori.cidappapi.utils.AssemblyUtils;
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


public class GroupEditTests {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder componentAssembly;
    private static UserCredentials currentUser;
    private CssComponent cssComponent = new CssComponent();
    private SoftAssertions softAssertions = new SoftAssertions();


    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = {"10947", "10948"})
    @Description("Edit public sub-component with no Private counterparts")
    public void testEditPublicSubcomponentWithNoPrivateCounterpart() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName);

        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_NAME_EQ.getKey() + scenarioName,
            "scenarioPublished[EQ], false")).hasSize(1);
        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_NAME_EQ.getKey() + scenarioName,
            "scenarioPublished[EQ], true")).hasSize(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10949"})
    @Description("Edit multiple public sub-components with no Private counterparts (Overwrite)")
    public void testGroupEditPublicSubcomponentsOverride() {
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

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        subComponentNames.forEach(subComponent -> {
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                "scenarioPublished[EQ], false")).hasSizeGreaterThan(0);
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                "scenarioPublished[EQ], true")).hasSizeGreaterThan(0);
        });

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10950"})
    @Description("Edit multiple public sub-components with no Private counterparts (Create)")
    public void testGroupEditPublicSubcomponentsRename() {
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

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        subComponentNames.forEach(subComponent -> {
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + newScenarioName,
                "scenarioPublished[EQ], false")).hasSizeGreaterThan(0);
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                "scenarioPublished[EQ], true")).hasSizeGreaterThan(0);
        });

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10951"})
    @Description("Edit public sub-component with Private counterpart (Overwrite)")
    public void testEditPublicSubcomponentOverride() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest2,
            STAND + "," + scenarioName);

        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_NAME_EQ.getKey() + scenarioName,
            "scenarioPublished[EQ], false")).hasSize(1);
        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_NAME_EQ.getKey() + scenarioName,
            "scenarioPublished[EQ], true")).hasSize(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10952"})
    @Description("Edit public sub-component with Private counterpart (Create)")
    public void testEditPublicSubcomponentRename() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest2,
            STAND + "," + scenarioName);

        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_NAME_EQ.getKey() + newScenarioName,
            "scenarioPublished[EQ], false")).hasSize(1);
        softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + STAND, SCENARIO_NAME_EQ.getKey() + scenarioName,
            "scenarioPublished[EQ], true")).hasSize(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10953"})
    @Description("Edit multiple public sub-components with Private counterparts (Overwrite)")
    public void testGroupEditPublicSubcomponentsWithPrivateCounterpartOverride() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse2 = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest2,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        subComponentNames.forEach(subComponent -> {
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                "scenarioPublished[EQ], false")).hasSize(1);
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                "scenarioPublished[EQ], true")).hasSize(1);
        });

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10954"})
    @Description("Edit multiple public sub-components with Private counterparts (Create)")
    public void testGroupEditPublicSubcomponentsWithPrivateCounterpartRename() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse2 = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest2,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName, JOINT + "," + scenarioName);

        subComponentNames.forEach(subComponent -> {
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + newScenarioName,
                "scenarioPublished[EQ], false")).hasSize(1);
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                "scenarioPublished[EQ], true")).hasSize(1);
        });

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10955"})
    @Description("Attempt to edit a sub-component that does not exist")
    public void testEditSubcomponentThatDoesNotExist() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        final List<String> subComponentNames = Arrays.asList(STAND);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";

        final String UNKNOWN_SCENARIO_ID = "41EBF4GGGGGG";
        final String UNKNOWN_COMPONENT_ID = "A5C6KLGMOYA";


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

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(false)
            .scenarioName(scenarioName)
            .groupItems(Collections.singletonList(GroupItems.builder()
                .componentIdentity(UNKNOWN_COMPONENT_ID)
                .scenarioIdentity(UNKNOWN_SCENARIO_ID)
                .build()))
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> errorResponse = scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName);


        errorResponse.getResponseEntity()
            .getFailures().forEach(o -> softAssertions.assertThat(o.getError()).isEqualTo("Bad Request"));

        softAssertions.assertAll();
    }
}