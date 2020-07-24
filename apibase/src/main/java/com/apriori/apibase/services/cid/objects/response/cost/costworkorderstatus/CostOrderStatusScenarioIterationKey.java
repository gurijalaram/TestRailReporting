package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "iteration"
})
public class CostOrderStatusScenarioIterationKey {

    @JsonProperty("scenarioKey")
    private CostOrderScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;

    public CostOrderScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public CostOrderStatusScenarioIterationKey setScenarioKey(CostOrderScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public CostOrderStatusScenarioIterationKey setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}
