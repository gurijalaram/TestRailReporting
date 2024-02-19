package com.apriori.cid.api.utils;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.models.response.component.ScenarioItem;

import java.util.Optional;

public class DataCreationUtil {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CssComponent cssComponent = new CssComponent();
    private ComponentInfoBuilder componentInfo;

    public DataCreationUtil(ComponentInfoBuilder componentInfo) {
        this.componentInfo = componentInfo;
    }

    /**
     * Create a component
     *
     * @return response object
     */
    public ComponentInfoBuilder createComponent() {
        return scenariosUtil.getComponentsUtil().postComponent(this.componentInfo);
    }

    /**
     * Search for a component and create if no component exist matching the component/scenario name
     *
     * @return response object
     */
    public ComponentInfoBuilder searchCreateComponent() {
        Optional<ScenarioItem> scenarioItem = cssComponent.getBaseCssComponents(this.componentInfo.getUser(), COMPONENT_NAME_EQ.getKey() +
                this.componentInfo.getComponentName(), SCENARIO_NAME_EQ.getKey() + this.componentInfo.getScenarioName())
            .stream().findFirst();

        if (scenarioItem.isPresent()) {
            this.componentInfo.setComponentIdentity(scenarioItem.get().getComponentIdentity());
            this.componentInfo.setScenarioIdentity(scenarioItem.get().getScenarioIdentity());
            return componentInfo;
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
            .componentName(this.componentInfo.getComponentName())
            .scenarioName(this.componentInfo.getScenarioName())
            .componentIdentity(component.getComponentIdentity())
            .scenarioIdentity(component.getScenarioIdentity())
            .user(this.componentInfo.getUser())
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

        scenariosUtil.postGroupCostScenarios(this.componentInfo);
        return scenariosUtil.getScenarioCompleted(this.componentInfo);
    }

    /**
     * Create, cost and publish a component
     *
     * @return response object
     */
    public ScenarioResponse createCostPublishComponent() {
        ComponentInfoBuilder component = createComponent();

        scenariosUtil.postGroupCostScenarios(this.componentInfo);

        ComponentInfoBuilder publishBuilder = ComponentInfoBuilder.builder()
            .componentName(this.componentInfo.getComponentName())
            .scenarioName(this.componentInfo.getScenarioName())
            .componentIdentity(component.getComponentIdentity())
            .scenarioIdentity(component.getScenarioIdentity())
            .user(this.componentInfo.getUser())
            .build();

        return scenariosUtil.postPublishScenario(publishBuilder);
    }
}
