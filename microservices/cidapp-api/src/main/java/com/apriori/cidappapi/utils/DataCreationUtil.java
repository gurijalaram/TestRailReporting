package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.io.File;

public class DataCreationUtil {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();

    private String componentName;
    private String scenarioName;
    private ProcessGroupEnum processGroup;
    private File resourceFile;
    private UserCredentials userCredentials;
    private ComponentInfoBuilder componentBuilder;

    public DataCreationUtil(String componentName, String scenarioName, ProcessGroupEnum processGroup, File resourceFile, UserCredentials userCredentials) {
        this.componentName = componentName;
        this.scenarioName = scenarioName;
        this.processGroup = processGroup;
        this.resourceFile = resourceFile;
        this.userCredentials = userCredentials;

        this.componentBuilder = ComponentInfoBuilder.builder()
            .componentName(this.componentName)
            .scenarioName(this.scenarioName)
            .processGroup(this.processGroup)
            .resourceFile(this.resourceFile)
            .user(this.userCredentials)
            .build();
    }

    /**
     * Create a component
     *
     * @return response object
     */
    public ScenarioItem createComponent() {
        return scenariosUtil.getComponentsUtil().postComponentQueryCSSUncosted(this.componentBuilder).getScenarioItem();
    }

    /**
     * Create and publish a component
     *
     * @return response object
     */
    public ScenarioResponse createPublishComponent() {
        ScenarioItem scenarioItem = createComponent();

        ComponentInfoBuilder publishBuilder = ComponentInfoBuilder.builder()
            .componentName(this.componentBuilder.getComponentName())
            .scenarioName(this.componentBuilder.getScenarioName())
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .user(this.componentBuilder.getUser())
            .build();

        return scenariosUtil.postPublishScenario(publishBuilder).getResponseEntity();
    }

    /**
     * Create and cost a component
     *
     * @return response object
     */
    public ScenarioResponse createCostComponent() {
        createComponent();

        return scenariosUtil.postCostScenario(this.componentBuilder).getResponseEntity();
    }

    /**
     * Create, cost and publish a component
     *
     * @return response object
     */
    public ScenarioResponse createCostPublishComponent() {
        ScenarioItem scenarioItem = createComponent();

        scenariosUtil.postCostScenario(this.componentBuilder);

        ComponentInfoBuilder publishBuilder = ComponentInfoBuilder.builder()
            .componentName(this.componentBuilder.getComponentName())
            .scenarioName(this.componentBuilder.getScenarioName())
            .componentIdentity(scenarioItem.getComponentIdentity())
            .scenarioIdentity(scenarioItem.getScenarioIdentity())
            .user(this.componentBuilder.getUser())
            .build();

        return scenariosUtil.postPublishScenario(publishBuilder).getResponseEntity();
    }
}
