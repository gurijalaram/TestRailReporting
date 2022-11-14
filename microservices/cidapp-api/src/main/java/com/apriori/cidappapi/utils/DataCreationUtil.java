package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.io.File;

public class DataCreationUtil {

    private ScenariosUtil scenariosUtil;
    private ComponentsUtil componentsUtil;

    /**
     * Create a component
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param resourceFile  - the resource file
     * @param currentUser   - the user
     * @return response object
     */
    public ScenarioItem createComponent(String componentName, String scenarioName, File resourceFile, UserCredentials currentUser) {
        ComponentInfoBuilder componentBuilder = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();

        componentsUtil = new ComponentsUtil();

        return componentsUtil.postComponentQueryCSSUncosted(componentBuilder).getScenarioItem();
    }

    /**
     * Create and publish a component
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param processGroup  - the process group
     * @param resourceFile  - the resource file
     * @param currentUser   - the user
     * @return response object
     */
    public ScenarioResponse createPublishComponent(String componentName, String scenarioName, ProcessGroupEnum processGroup, File resourceFile, UserCredentials currentUser) {
        ComponentInfoBuilder costBuilder = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .processGroup(processGroup)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();

        componentsUtil = new ComponentsUtil();

        ScenarioItem scenarioItem = componentsUtil.postComponentQueryCSSUncosted(costBuilder).getScenarioItem();

        ComponentInfoBuilder publishBuilder = ComponentInfoBuilder.builder()
            .componentName(scenarioItem.getComponentName())
            .scenarioName(scenarioItem.getScenarioName())
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .user(currentUser)
            .build();

        scenariosUtil = new ScenariosUtil();

        return scenariosUtil.postPublishScenario(publishBuilder).getResponseEntity();
    }

    /**
     * Create and cost a component
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param processGroup  - the process group
     * @param resourceFile  - the resource file
     * @param currentUser   - the user
     * @return response object
     */
    public ScenarioResponse createCostComponent(String componentName, String scenarioName, ProcessGroupEnum processGroup, File resourceFile, UserCredentials currentUser) {
        ScenarioItem scenarioItem = createComponent(componentName, scenarioName, resourceFile, currentUser);

        ComponentInfoBuilder costBuilder = ComponentInfoBuilder.builder()
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .processGroup(processGroup)
            .user(currentUser)
            .build();

        scenariosUtil = new ScenariosUtil();

        return scenariosUtil.postCostScenario(costBuilder).getResponseEntity();
    }

    /**
     * Create, cost and publish a component
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param processGroup  - the process group
     * @param resourceFile  - the resource file
     * @param currentUser   - the user
     * @return response object
     */
    public ScenarioResponse createCostPublishComponent(String componentName, String scenarioName, ProcessGroupEnum processGroup, File resourceFile, UserCredentials currentUser) {
        ScenarioItem scenarioItem = createComponent(componentName, scenarioName, resourceFile, currentUser);

        ComponentInfoBuilder costBuilder = ComponentInfoBuilder.builder()
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .processGroup(processGroup)
            .user(currentUser)
            .build();

        scenariosUtil = new ScenariosUtil();

        scenariosUtil.postCostScenario(costBuilder);

        ComponentInfoBuilder publishBuilder = ComponentInfoBuilder.builder()
            .componentName(scenarioItem.getComponentName())
            .scenarioName(scenarioItem.getScenarioName())
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .user(currentUser)
            .build();

        return scenariosUtil.postPublishScenario(publishBuilder).getResponseEntity();
    }
}
