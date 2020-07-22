package com.apriori.apibase.services.cid.objects.cost.costworkorderstatus;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey",
    "costStatus"
})
public class Outputs {

    @JsonProperty("scenarioIterationKey")
    private ScenarioIterationKey scenarioIterationKey;
    @JsonProperty("costStatus")
    private String costStatus;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioIterationKey")
    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    @JsonProperty("scenarioIterationKey")
    public Outputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    @JsonProperty("costStatus")
    public String getCostStatus() {
        return costStatus;
    }

    @JsonProperty("costStatus")
    public Outputs setCostStatus(String costStatus) {
        this.costStatus = costStatus;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
