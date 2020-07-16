package com.apriori.apibase.services.cid.objects.cost.createcostworkorder;

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
    "inputSetId",
    "scenarioIterationKey"
})
public class Inputs {

    @JsonProperty("inputSetId")
    private Integer inputSetId;
    @JsonProperty("scenarioIterationKey")
    private ScenarioIterationKey scenarioIterationKey;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("inputSetId")
    public Integer getInputSetId() {
        return inputSetId;
    }

    @JsonProperty("inputSetId")
    public Inputs setInputSetId(Integer inputSetId) {
        this.inputSetId = inputSetId;
        return this;
    }

    @JsonProperty("scenarioIterationKey")
    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    @JsonProperty("scenarioIterationKey")
    public Inputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
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