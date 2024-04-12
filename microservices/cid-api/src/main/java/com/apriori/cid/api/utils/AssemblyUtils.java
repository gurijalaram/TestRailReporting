package com.apriori.cid.api.utils;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AssemblyUtils {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentsUtil componentsUtil = new ComponentsUtil();

    /**
     * Associates subcomponents with assemblies then uploads and publishes them
     *
     * @param subComponentNames        - the subcomponent names
     * @param subComponentExtension    - the subcomponent extension
     * @param subComponentProcessGroup - the subcomponent process group
     * @param scenarioName             - the scenario name
     * @param currentUser              - the current user
     * @return component object
     */
    private List<ComponentInfoBuilder> buildSubComponents(List<String> subComponentNames,
                                                          String subComponentExtension,
                                                          ProcessGroupEnum subComponentProcessGroup,
                                                          String scenarioName,
                                                          UserCredentials currentUser) {

        return subComponentNames.stream().map(
            subComponentName -> ComponentInfoBuilder.builder()
                .componentName(subComponentName)
                .extension(subComponentExtension)
                .resourceFile(FileResourceUtil.getCloudFile(subComponentProcessGroup, subComponentName + subComponentExtension))
                .scenarioName(scenarioName)
                .processGroup(subComponentProcessGroup)
                .user(currentUser)
                .build()).collect(Collectors.toList());
    }

    /**
     * Associate sub-components with assemblies
     *
     * @param assemblyName             - the assembly name
     * @param assemblyExtension        - the assembly extension
     * @param assemblyProcessGroup     - the assembly process group
     * @param subComponentNames        - the subcomponent names
     * @param subComponentExtension    - the subcomponent extension
     * @param subComponentProcessGroup - the subcomponent process group
     * @param scenarioName             - the scenario name
     * @param currentUser              - the current user
     * @return component info builder object
     */
    // TODO: 02/01/2024 cn - this method needs updated or removed. any tests currently using this method will more than likely fail due to 'resourceFile' being null in postCadFiles() line#63
    public ComponentInfoBuilder associateAssemblyAndSubComponents(String assemblyName,
                                                                  String assemblyExtension,
                                                                  ProcessGroupEnum assemblyProcessGroup,
                                                                  List<String> subComponentNames,
                                                                  String subComponentExtension,
                                                                  ProcessGroupEnum subComponentProcessGroup,
                                                                  String scenarioName,
                                                                  UserCredentials currentUser) {

        return ComponentInfoBuilder.builder()
            .componentName(assemblyName)
            .extension(assemblyExtension)
            .scenarioName(scenarioName)
            .processGroup(assemblyProcessGroup)
            .resourceFile(FileResourceUtil.getCloudFile(assemblyProcessGroup, assemblyName + assemblyExtension))
            .subComponents(buildSubComponents(subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser))
            .user(currentUser)
            .build();
    }

    /**
     * Upload sub-components
     *
     * @return current object
     */
    public AssemblyUtils uploadSubComponents(ComponentInfoBuilder componentAssembly) {

        componentAssembly.getSubComponents()
            .forEach(subcomponent -> {
                if (subcomponent.getSubComponents() != null) {
                    uploadSubComponents(subcomponent);
                }
            });

        componentsUtil.postCadUploadComponentSuccess(componentAssembly.getSubComponents());

        return this;
    }

    /**
     * Upload assembly
     *
     * @return component info builder object
     */
    public ComponentInfoBuilder uploadAssembly(ComponentInfoBuilder componentAssembly) {
        List<ComponentInfoBuilder> assemblies = componentsUtil.postCadUploadComponentSuccess(List.of(componentAssembly));
        return assemblies.stream().findFirst().get();
    }

    /**
     * Publish assembly
     *
     * @param assembly - the assembly
     * @return scenario object
     */
    public ScenarioResponse publishAssembly(ComponentInfoBuilder assembly) {
        return scenariosUtil.postPublishScenario(assembly);
    }

    /**
     * Publish sub-components
     *
     * @param componentAssembly - assembly
     * @return current object
     */
    public AssemblyUtils publishSubComponents(ComponentInfoBuilder componentAssembly) {

        List<ComponentInfoBuilder> subAssemblies = componentAssembly.getSubComponents().stream()
            .filter(component -> component.getSubComponents() != null).collect(Collectors.toList());

        subAssemblies.forEach(this::publishSubComponents);

        componentAssembly.getSubComponents().forEach(subComponent -> scenariosUtil.postPublishScenario(subComponent));
        return this;
    }

    /**
     * Upload component expecting an error
     *
     * @param component - the component
     * @return - scenario object
     */
    public ResponseWrapper<ScenarioResponse> publishAssemblyExpectError(ComponentInfoBuilder component) {
        return scenariosUtil.publishScenario(component, ErrorMessage.class, HttpStatus.SC_CONFLICT);
    }

    /**
     * Cost sub-components
     *
     * @param assemblySubComponent the sub-components
     * @return current object
     */
    public AssemblyUtils costSubComponents(ComponentInfoBuilder assemblySubComponent) {
        assemblySubComponent.getSubComponents().forEach(subComponent -> scenariosUtil.postCostScenario(subComponent));
        return this;
    }

    /**
     * Cost assembly
     *
     * @param assembly - the assembly
     * @return list of scenario item
     */
    public AssemblyUtils costAssembly(ComponentInfoBuilder assembly) {

        List<ComponentInfoBuilder> subAssemblies = assembly.getSubComponents().stream()
            .filter(subComponent -> subComponent.getSubComponents() != null).toList();

        subAssemblies.forEach(subAsm -> scenariosUtil.postGroupCostScenarios(subAsm));

        scenariosUtil.postGroupCostScenarios(assembly);
        return this;
    }

    /**
     * Uploads an assembly with all subcomponents, cost and publish all
     *
     * @param assemblyName             - the assembly name
     * @param assemblyExtension        - the assembly extension
     * @param assemblyProcessGroup     - the assembly process group
     * @param subComponentNames        - the subComponent names
     * @param subComponentExtension    - the subComponent extension
     * @param subComponentProcessGroup - the subComponent process group
     * @param scenarioName             - the scenario name
     * @param currentUser              - the current user
     * @return - the object of ComponentInfoBuilder
     */
    public ComponentInfoBuilder uploadsAndOpenAssembly(String assemblyName,
                                                       String assemblyExtension,
                                                       ProcessGroupEnum assemblyProcessGroup,
                                                       List<String> subComponentNames,
                                                       String subComponentExtension,
                                                       ProcessGroupEnum subComponentProcessGroup,
                                                       String scenarioName,
                                                       UserCredentials currentUser) {

        ComponentInfoBuilder componentAssembly = associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        return componentAssembly;
    }

    /**
     * Shallow publishes an assembly
     *
     * @param componentAssembly - the component assembly
     * @return current object
     */
    public AssemblyUtils shallowPublishAssembly(ComponentInfoBuilder componentAssembly) {
        uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        costAssembly(componentAssembly);
        publishSubComponents(componentAssembly);
        publishAssembly(componentAssembly);
        return this;
    }

    /**
     * Deletes an assembly and all it's subcomponents
     *
     * @param componentAssembly - the component assembly
     * @return - Current Object
     */
    public AssemblyUtils deleteAssemblyAndComponents(ComponentInfoBuilder componentAssembly) {
        scenariosUtil.deleteScenario(componentAssembly.getComponentIdentity(), componentAssembly.getScenarioIdentity(), componentAssembly.getUser());
        componentAssembly.getSubComponents().forEach(
            subComponent -> scenariosUtil.deleteScenario(subComponent.getComponentIdentity(), subComponent.getScenarioIdentity(), componentAssembly.getUser()));

        return this;
    }

    /**
     * Disassociates a subcomponent from an assembly
     *
     * @param componentAssembly - the component assembly
     * @param subcomponents     - the list of subcomponents
     * @return current object
     */
    public AssemblyUtils disassociateSubcomponents(ComponentInfoBuilder componentAssembly, String... subcomponents) {
        Arrays.stream(subcomponents).forEach(subcomponent -> componentAssembly.getSubComponents().removeIf(o -> o.getComponentName().equalsIgnoreCase(subcomponent)));
        return this;
    }

    /**
     * Get the ComponentInfoBuilder object of a specified subcomponent
     *
     * @param componentAssembly - The Component Assembly
     * @param subComponentName  - The name of the desired subcomponent
     * @return ComponentInfoBuilder object of desired subcomponent
     */
    public ComponentInfoBuilder getSubComponent(ComponentInfoBuilder componentAssembly, String subComponentName) {
        return componentAssembly.getSubComponents().stream()
            .filter(subComponent -> subComponent.getComponentName().equals(subComponentName)).findFirst().orElse(null);
    }
}
