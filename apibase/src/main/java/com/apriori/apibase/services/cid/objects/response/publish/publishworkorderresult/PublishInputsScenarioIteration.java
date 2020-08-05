package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "iteration"
})
public class PublishInputsScenarioIteration {

    @JsonProperty("scenarioKey")
    private PublishInputsScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;

    public PublishInputsScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishInputsScenarioIteration setScenarioKey(PublishInputsScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishInputsScenarioIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}
