package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;


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
    "inputSetId"
})
public class Inputs {

    @JsonProperty("scenarioIterationKey")
    private ScenarioIterationKey scenarioIterationKey;
    @JsonProperty("inputSetId")
    private Integer inputSetId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioIterationKey")
    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    @JsonProperty("scenarioIterationKey")
    public Inputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    @JsonProperty("inputSetId")
    public Integer getInputSetId() {
        return inputSetId;
    }

    @JsonProperty("inputSetId")
    public Inputs setInputSetId(Integer inputSetId) {
        this.inputSetId = inputSetId;
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
