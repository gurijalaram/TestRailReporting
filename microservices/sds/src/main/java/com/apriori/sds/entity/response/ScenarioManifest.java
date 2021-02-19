package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioManifest.json")
public class ScenarioManifest {

    private ScenarioManifest response;
    private String occurrences;
    private String componentType;
    private ScenarioManifestSubComponents[] subComponents;
    private String componentName;
    private String componentIdentity;
    private String cadMetadataIdentity;
    private String totalComponents;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioState;
    private String totalSubComponents;

    public ScenarioManifest getResponse() {
        return response;
    }

    public void setResponse(ScenarioManifest response) {
        this.response = response;
    }

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

    public ScenarioManifestSubComponents[] getSubComponents() {
        return subComponents;
    }

    public void setSubComponents(ScenarioManifestSubComponents[] subComponents) {
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

    public String getCadMetadataIdentity() {
        return cadMetadataIdentity;
    }

    public void setCadMetadataIdentity(String cadMetadataIdentity) {
        this.cadMetadataIdentity = cadMetadataIdentity;
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
