package com.evaluate;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.request.GroupItems;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.CssComponent;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
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

    /**
     * Asserts that after editing scenario one iteration of that scenario exists in public workspace and one iteration in the private one
     *
     * @param scenarioName       - the name of the published scenario
     * @param secondScenarioName - the name of the private scenario (it will be the same as scenarioName if override is true, and it will be different if option Rename is selected)
     * @param subComponentNames  - list of subcomponents
     */
    private void verifyEditAction(String scenarioName, String secondScenarioName, List<String> subComponentNames) {
        subComponentNames.forEach(subComponent -> {
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + secondScenarioName,
                "scenarioPublished[EQ], false")).hasSize(1);
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                "scenarioPublished[EQ], true")).hasSize(1);
        });

        softAssertions.assertAll();
    }

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

        final List<String> subComponentNames = Collections.singletonList(STAND);
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

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND);

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
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

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND, DRIVE, JOINT);

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
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

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND, DRIVE, JOINT);

        verifyEditAction(scenarioName, newScenarioName, subComponentNames);
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

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest2, STAND);

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
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

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest2, STAND);

        verifyEditAction(scenarioName, newScenarioName, subComponentNames);
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

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND, DRIVE, JOINT);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest2, STAND, DRIVE, JOINT);

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
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

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest, STAND, DRIVE, JOINT);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        scenariosUtil.postEditPublicGroupScenarios(componentAssembly, forkRequest2, STAND, DRIVE, JOINT);

        verifyEditAction(scenarioName, newScenarioName, subComponentNames);
    }

    @Test
    @TestRail(testCaseId = {"10955"})
    @Description("Attempt to edit a sub-component that does not exist")
    public void testEditSubcomponentThatDoesNotExist() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String STAND = "stand";
        final String JOINT = "joint";
        final String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";
        final List<String> subComponentNames = Arrays.asList(STAND, JOINT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";
        final String UNKNOWN_SCENARIO_ID = "41EBF4GGGGGG";
        final String UNKNOWN_COMPONENT_ID = "A5C6KLGMOYA";
        final String UNKNOWN_SCENARIO_ID2 = "41EBF4HJEKTU";
        final String UNKNOWN_COMPONENT_ID2 = "A5C6KLGZLATI";

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
            .groupItems(Arrays.asList(
                GroupItems.builder()
                    .componentIdentity(UNKNOWN_COMPONENT_ID)
                    .scenarioIdentity(UNKNOWN_SCENARIO_ID)
                    .build(),
                GroupItems.builder()
                    .componentIdentity(UNKNOWN_COMPONENT_ID2)
                    .scenarioIdentity(UNKNOWN_SCENARIO_ID2)
                    .build()))
            .build();

        SoftAssertions softAssertions = new SoftAssertions();

        ErrorMessage errorResponse = scenariosUtil.postSimpleEditPublicGroupScenarios(componentAssembly, forkRequest, ErrorMessage.class).getResponseEntity();

        subComponentNames.forEach(subcomponent -> {
            softAssertions.assertThat(errorResponse.getError()).isEqualTo("Bad Request");
            softAssertions.assertThat(errorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");
        });

        softAssertions.assertAll();
    }
}