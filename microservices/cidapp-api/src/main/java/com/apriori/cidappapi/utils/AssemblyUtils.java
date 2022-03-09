package com.apriori.cidappapi.utils;

import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AssemblyUtils {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();

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
            .subComponents(buildSubComponents(subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser))
            .user(currentUser)
            .build();
    }

    /**
     * Upload assembly and sub-components
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
    public ComponentInfoBuilder uploadAssemblyAndSubComponents(String assemblyName,
                                                               String assemblyExtension,
                                                               ProcessGroupEnum assemblyProcessGroup,
                                                               List<String> subComponentNames,
                                                               String subComponentExtension,
                                                               ProcessGroupEnum subComponentProcessGroup,
                                                               String scenarioName,
                                                               UserCredentials currentUser) {

        ComponentInfoBuilder componentAssembly = associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        componentAssembly.getSubComponents().forEach(subComponent -> {
            ScenarioItem subComponentScenarioItem = scenariosUtil.uploadComponent(subComponent);
            subComponent.setComponentIdentity(subComponentScenarioItem.getComponentIdentity());
            subComponent.setScenarioIdentity(subComponentScenarioItem.getScenarioIdentity());
        });

        ScenarioItem assemblyScenarioItem = scenariosUtil.uploadComponent(componentAssembly);
        componentAssembly.setComponentIdentity(assemblyScenarioItem.getComponentIdentity());
        componentAssembly.setScenarioIdentity(assemblyScenarioItem.getScenarioIdentity());

        return componentAssembly;
    }

    /**
     * Publish assembly
     *
     * @param assembly - the assembly
     * @return scenario object
     */
    public ResponseWrapper<ScenarioResponse> publishAssembly(ComponentInfoBuilder assembly) {
        return scenariosUtil.postPublishScenario(assembly);
    }

    /**
     * Publish sub-components
     *
     * @param assemblySubComponent - assembly sub-components
     * @return current object
     */
    public AssemblyUtils publishSubComponents(ComponentInfoBuilder assemblySubComponent) {
        assemblySubComponent.getSubComponents().forEach(subComponent -> scenariosUtil.postPublishScenario(subComponent));
        return this;
    }

    /**
     * Upload component expecting an error
     *
     * @param component - the component
     * @return - scenario object
     */
    public ResponseWrapper<ScenarioResponse> publishAssemblyExpectError(ComponentInfoBuilder component) {
        return scenariosUtil.publishScenario(component, ErrorMessage.class);
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
    public List<ScenarioItem> costAssembly(ComponentInfoBuilder assembly) {
        return scenariosUtil.postCostScenario(assembly);
    }

    /**
     * Uploads an assembly with all subcomponents, cost and publish all
     *
     * @param assemblyName - the assembly name
     * @param assemblyExtension - the assembly extension
     * @param assemblyProcessGroup - the assembly process group
     * @param subComponentNames - the subComponent names
     * @param subComponentExtension - the subComponent extension
     * @param subComponentProcessGroup - the subComponent process group
     * @param scenarioName - the scenario name
     * @param currentUser - the current user
     * @return - the object of ComponentInfoBuilder
     */
    public ComponentInfoBuilder uploadCostPublishScenario(String assemblyName,
                                                          String assemblyExtension,
                                                          ProcessGroupEnum assemblyProcessGroup,
                                                          List<String> subComponentNames,
                                                          String subComponentExtension,
                                                          ProcessGroupEnum subComponentProcessGroup,
                                                          String scenarioName,
                                                          UserCredentials currentUser) {
        ComponentInfoBuilder componentAssembly = uploadAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        costSubComponents(componentAssembly);
        costAssembly(componentAssembly);

        publishSubComponents(componentAssembly);

        publishAssembly(componentAssembly);

        return componentAssembly;
    }
}
