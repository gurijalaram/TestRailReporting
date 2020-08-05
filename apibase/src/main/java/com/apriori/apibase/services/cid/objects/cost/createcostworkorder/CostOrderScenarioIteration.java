package com.apriori.apibase.services.cid.objects.cost.createcostworkorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "iteration"
})
public class CostOrderScenarioIteration {

    @JsonProperty("scenarioKey")
    private CostOrderScenario scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;

    public CostOrderScenario getScenarioKey() {
        return scenarioKey;
    }

    public CostOrderScenarioIteration setScenarioKey(CostOrderScenario scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public CostOrderScenarioIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}
