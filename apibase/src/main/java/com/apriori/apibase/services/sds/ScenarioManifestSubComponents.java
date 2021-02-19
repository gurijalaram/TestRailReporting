package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioManifestSubComponents.json")
public class ScenarioManifestSubComponents {

    private String occurrences;
    private String componentType;
    private String[] subComponents;
    private String componentName;
    private String componentIdentity;
    private String totalComponents;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioState;
    private String totalSubComponents;

    public String getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(String occurrences) {
        this.occurrences = occurrences;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String[] getSubComponents() {
        return subComponents;
    }

    public void setSubComponents(String[] subComponents) {
        this.subComponents = subComponents;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentIdentity() {
        return componentIdentity;
    }

    public void setComponentIdentity(String componentIdentity) {
        this.componentIdentity = componentIdentity;
    }

    public String getTotalComponents() {
        return totalComponents;
    }

    public void setTotalComponents(String totalComponents) {
        this.totalComponents = totalComponents;
    }

    public String getScenarioIdentity() {
        return scenarioIdentity;
    }

    public void setScenarioIdentity(String scenarioIdentity) {
        this.scenarioIdentity = scenarioIdentity;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getScenarioState() {
        return scenarioState;
    }

    public void setScenarioState(String scenarioState) {
        this.scenarioState = scenarioState;
    }

    public String getTotalSubComponents() {
        return totalSubComponents;
    }

    public void setTotalSubComponents(String totalSubComponents) {
        this.totalSubComponents = totalSubComponents;
    }
}
