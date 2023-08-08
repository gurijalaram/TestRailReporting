package com.apriori.evaluate;

import static com.apriori.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.enums.CssSearch.LATEST_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_PUBLISHED_EQ;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.request.ForkRequest;
import com.apriori.cidappapi.models.request.GroupItems;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.models.response.ErrorMessage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.commons.lang.SerializationUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GroupEditTests {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder clonedComponentAssembly;
    private static UserCredentials currentUser;
    private CssComponent cssComponent = new CssComponent();
    private SoftAssertions softAssertions = new SoftAssertions();

    final String chargerBase = "titan charger base";
    final String chargerLead = "titan charger lead";
    final String chargerUpper = "titan charger upper";
    final String componentExtension = ".SLDPRT";
    final String assemblyName = "titan charger ass";
    final String assemblyExtension = ".SLDASM";

    final List<String> subComponentNames = Arrays.asList(chargerBase, chargerLead, chargerUpper);
    final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

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
                SCENARIO_PUBLISHED_EQ.getKey() + false, LATEST_EQ.getKey() + true)).hasSize(1);
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + subComponent, SCENARIO_NAME_EQ.getKey() + scenarioName,
                SCENARIO_PUBLISHED_EQ.getKey() + true, LATEST_EQ.getKey() + true)).hasSize(1);
        });

        softAssertions.assertAll();
    }

    @BeforeEach
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {10947, 10948})
    @Description("Edit public sub-component with no Private counterparts")
    public void testEditPublicSubcomponentWithNoPrivateCounterpart() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
    }

    @Test
    @TestRail(id = {10949})
    @Description("Edit multiple public sub-components with no Private counterparts (Overwrite)")
    public void testGroupEditPublicSubcomponentsOverride() {
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

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        componentAssembly.getSubComponents().forEach(component -> scenariosUtil.getScenarioCompleted(component));

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase, chargerLead, chargerUpper);

        componentAssembly.getSubComponents().forEach(component -> scenariosUtil.getScenarioCompleted(component));

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
    }

    @Test
    @TestRail(id = {10950})
    @Description("Edit multiple public sub-components with no Private counterparts (Create)")
    public void testGroupEditPublicSubcomponentsRename() {
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

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        componentAssembly.getSubComponents().forEach(component -> scenariosUtil.getScenarioCompleted(component));

        ComponentInfoBuilder editedComponentAssembly = componentAssembly;
        editedComponentAssembly.setScenarioName(newScenarioName);
        editedComponentAssembly.getSubComponents().forEach(component -> component.setScenarioName(newScenarioName));

        scenariosUtil.postEditGroupScenarios(editedComponentAssembly, forkRequest, chargerBase, chargerLead, chargerUpper);

        editedComponentAssembly.getSubComponents().forEach(component -> scenariosUtil.getScenarioCompleted(component));

        verifyEditAction(scenarioName, newScenarioName, subComponentNames);
    }

    @Test
    @TestRail(id = {10951})
    @Description("Edit public sub-component with Private counterpart (Overwrite)")
    public void testEditPublicSubcomponentOverride() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        clonedComponentAssembly = (ComponentInfoBuilder) SerializationUtils.clone(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(clonedComponentAssembly, forkRequest2, chargerBase);

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
    }

    @Test
    @TestRail(id = {10952})
    @Description("Edit public sub-component with Private counterpart (Create)")
    public void testEditPublicSubcomponentRename() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        clonedComponentAssembly = (ComponentInfoBuilder) SerializationUtils.clone(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(clonedComponentAssembly, forkRequest2, chargerBase);

        verifyEditAction(scenarioName, newScenarioName, subComponentNames);
    }

    @Test
    @TestRail(id = {10953})
    @Description("Edit multiple public sub-components with Private counterparts (Overwrite)")
    public void testGroupEditPublicSubcomponentsWithPrivateCounterpartOverride() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        clonedComponentAssembly = (ComponentInfoBuilder) SerializationUtils.clone(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase, chargerLead, chargerUpper);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(clonedComponentAssembly, forkRequest2, chargerBase, chargerLead, chargerUpper);

        verifyEditAction(scenarioName, scenarioName, subComponentNames);
    }

    @Test
    @TestRail(id = {10954})
    @Description("Edit multiple public sub-components with Private counterparts (Create)")
    public void testGroupEditPublicSubcomponentsWithPrivateCounterpartRename() {
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
        assemblyUtils.uploadSubComponents(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        clonedComponentAssembly = (ComponentInfoBuilder) SerializationUtils.clone(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .override(true)
            .scenarioName(scenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest, chargerBase, chargerLead, chargerUpper);

        ForkRequest forkRequest2 = ForkRequest.builder()
            .override(false)
            .scenarioName(newScenarioName)
            .build();

        scenariosUtil.postEditGroupScenarios(clonedComponentAssembly, forkRequest2, chargerBase, chargerLead, chargerUpper);

        verifyEditAction(scenarioName, newScenarioName, subComponentNames);
    }

    @Test
    @TestRail(id = {10955})
    @Description("Attempt to edit a sub-component that does not exist")
    public void testEditSubcomponentThatDoesNotExist() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final List<String> subComponentNames = Arrays.asList(chargerBase, chargerLead);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

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

        ErrorMessage errorResponse = scenariosUtil.postSimpleEditGroupScenarios(componentAssembly, forkRequest, ErrorMessage.class).getResponseEntity();

        subComponentNames.forEach(subcomponent -> {
            softAssertions.assertThat(errorResponse.getError()).isEqualTo("Bad Request");
            softAssertions.assertThat(errorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");
        });

        softAssertions.assertAll();
    }
}