package com.apriori.apibase.services.cid.objects.publish.publishworkorderresult;

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
    "scenarioKey",
    "iteration"
})
public class ScenarioIterationKey {

    @JsonProperty("scenarioKey")
    private ScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioKey")
    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    @JsonProperty("scenarioKey")
    public void setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    @JsonProperty("iteration")
    public Integer getIteration() {
        return iteration;
    }

    @JsonProperty("iteration")
    public void setIteration(Integer iteration) {
        this.iteration = iteration;
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
