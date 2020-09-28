package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "iteration"
})
public class PublishOutputsScenarioIteration {

    @JsonProperty("scenarioKey")
    private PublishOutputsScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;

    public PublishOutputsScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishOutputsScenarioIteration setScenarioKey(PublishOutputsScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishOutputsScenarioIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}