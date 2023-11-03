package com.apriori.cid.api.utils;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.ScenarioItem;

import java.io.File;
import java.util.Optional;

public class DataCreationUtil {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CssComponent cssComponent = new CssComponent();

    private String componentName;
    private String scenarioName;
    private ProcessGroupEnum processGroup;
    private File resourceFile;
    private UserCredentials userCredentials;
    private ComponentInfoBuilder componentBuilder;
    private CostingTemplate costingTemplate;

    public DataCreationUtil(String componentName, String scenarioName, ProcessGroupEnum processGroup, File resourceFile, CostingTemplate costingTemplate, UserCredentials userCredentials) {
        this.componentName = componentName;
        this.scenarioName = scenarioName;
        this.processGroup = processGroup;
        this.resourceFile = resourceFile;
        this.costingTemplate = costingTemplate;
        this.userCredentials = userCredentials;

        this.componentBuilder = ComponentInfoBuilder.builder()
            .componentName(this.componentName)
            .scenarioName(this.scenarioName)
            .processGroup(this.processGroup)
            .resourceFile(this.resourceFile)
            .costingTemplate(this.costingTemplate)
            .user(this.userCredentials)
            .build();
    }

    /**
     * Create a component
     *
     * @return response object
     */
    public ComponentInfoBuilder createComponent() {
        return scenariosUtil.getComponentsUtil().postComponentQueryCID(this.componentBuilder);
    }

    /**
     * Search for a component and create if no component exist matching the component/scenario name
     *
     * @return response object
     */
    public ComponentInfoBuilder searchCreateComponent() {
        Optional<ScenarioItem> scenarioItem = cssComponent.getBaseCssComponents(this.userCredentials, COMPONENT_NAME_EQ.getKey() + this.componentName, SCENARIO_NAME_EQ.getKey() + this.scenarioName)
            .stream().findFirst();

        if (scenarioItem.isPresent()) {
            this.componentBuilder.setComponentIdentity(scenarioItem.get().getComponentIdentity());
            this.componentBuilder.setScenarioIdentity(scenarioItem.get().getScenarioIdentity());
            return componentBuilder;
        }
        return createComponent();
    }

    /**
     * Create and publish a component
     *
     * @return response object
     */
    public ScenarioResponse createPublishComponent() {
        ComponentInfoBuilder component = createComponent();

        ComponentInfoBuilder publishBuilder = ComponentInfoBuilder.builder()
            .componentName(this.componentBuilder.getComponentName())
            .scenarioName(this.componentBuilder.getScenarioName())
            .componentIdentity(component.getComponentIdentity())
            .scenarioIdentity(component.getScenarioIdentity())
            .user(this.componentBuilder.getUser())
            .build();

        return scenariosUtil.postPublishScenario(publishBuilder);
    }

    /**
     * Create and cost a component
     *
     * @return response object
     */
    public ScenarioResponse createCostComponent() {
        createComponent();

        return scenariosUtil.postCostScenario(this.componentBuilder);
    }

    /**
     * Create, cost and publish a component
     *
     * @return response object
     */
    public ScenarioResponse createCostPublishComponent() {
        ComponentInfoBuilder component = createComponent();

        scenariosUtil.postCostScenario(this.componentBuilder);

        ComponentInfoBuilder publishBuilder = ComponentInfoBuilder.builder()
            .componentName(this.componentBuilder.getComponentName())
            .scenarioName(this.componentBuilder.getScenarioName())
            .componentIdentity(component.getComponentIdentity())
            .scenarioIdentity(component.getScenarioIdentity())
            .user(this.componentBuilder.getUser())
            .build();

        return scenariosUtil.postPublishScenario(publishBuilder);
    }
}
