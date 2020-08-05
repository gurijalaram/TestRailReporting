package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "iteration"
})
public class PublishStatusScenarioIterationKey {

    @JsonProperty("scenarioKey")
    private PublishStatusScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;

    public PublishStatusScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishStatusScenarioIterationKey setScenarioKey(PublishStatusScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishStatusScenarioIterationKey setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}