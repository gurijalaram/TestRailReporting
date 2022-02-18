package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.enums.ProcessGroupEnum;
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
     * @param assemblyName             - the assembly name
     * @param assemblyExtension        - the assembly extension
     * @param assemblyProcessGroup     - the assembly process group
     * @param subComponentNames        - the subcomponent names
     * @param subComponentExtension    - the subcomponent extension
     * @param subComponentProcessGroup - the subcomponent process group
     * @param scenarioName             - the scenario name
     * @param currentUser              - the current user
     * @return component object
     */
    public ComponentInfoBuilder uploadPublishAssemblyAndSubComponents(String assemblyName,
                                                                      String assemblyExtension,
                                                                      ProcessGroupEnum assemblyProcessGroup,
                                                                      List<String> subComponentNames,
                                                                      String subComponentExtension,
                                                                      ProcessGroupEnum subComponentProcessGroup,
                                                                      String scenarioName,
                                                                      UserCredentials currentUser) {

        List<ComponentInfoBuilder> subComponents = subComponentNames.stream().map(
            subComponentName -> ComponentInfoBuilder.builder()
                .componentName(subComponentName)
                .extension(subComponentExtension)
                .scenarioName(scenarioName)
                .processGroup(subComponentProcessGroup)
                .user(currentUser)
                .build()).collect(Collectors.toList());

        ComponentInfoBuilder componentAssembly = ComponentInfoBuilder.builder()
            .componentName(assemblyName)
            .extension(assemblyExtension)
            .scenarioName(scenarioName)
            .processGroup(assemblyProcessGroup)
            .subComponents(subComponents)
            .user(currentUser)
            .build();

        componentAssembly.getSubComponents().forEach(subComponent -> scenariosUtil.uploadAndPublishComponent(
            subComponent));

        ScenarioItem componentAssemblyResponse = scenariosUtil.uploadAndPublishComponent(componentAssembly);

        componentAssembly.setComponentId(componentAssemblyResponse.getComponentIdentity());
        componentAssembly.setScenarioId(componentAssemblyResponse.getScenarioIdentity());

        return componentAssembly;
    }
}
