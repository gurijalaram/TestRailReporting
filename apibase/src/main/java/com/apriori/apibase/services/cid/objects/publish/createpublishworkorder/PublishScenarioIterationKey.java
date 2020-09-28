package com.apriori.apibase.services.cid.objects.publish.createpublishworkorder;

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
public class PublishScenarioIterationKey {

    @JsonProperty("scenarioKey")
    private PublishScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioKey")
    public PublishScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    @JsonProperty("scenarioKey")
    public PublishScenarioIterationKey setScenarioKey(PublishScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    @JsonProperty("iteration")
    public Integer getIteration() {
        return iteration;
    }

    @JsonProperty("iteration")
    public PublishScenarioIterationKey setIteration(Integer iteration) {
        this.iteration = iteration;
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