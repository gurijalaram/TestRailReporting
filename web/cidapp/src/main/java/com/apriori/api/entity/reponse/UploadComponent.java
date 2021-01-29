package com.apriori.api.entity.reponse;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@Schema(location = "newcid/UploadComponentResponse.json")
public class UploadComponent {
    @JsonProperty("componentIdentity")
    private String componentIdentity;
    @JsonProperty("scenarioIdentity")
    private String scenarioIdentity;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("componentIdentity")
    public String getComponentIdentity() {
        return componentIdentity;
    }

    @JsonProperty("componentIdentity")
    public UploadComponent setComponentIdentity(String componentIdentity) {
        this.componentIdentity = componentIdentity;
        return this;
    }

    @JsonProperty("scenarioIdentity")
    public String getScenarioIdentity() {
        return scenarioIdentity;
    }

    @JsonProperty("scenarioIdentity")
    public UploadComponent setScenarioIdentity(String scenarioIdentity) {
        this.scenarioIdentity = scenarioIdentity;
        return this;
    }
}
