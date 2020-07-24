package com.apriori.apibase.services.cid.objects.response.cost.iterations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "iteration"
})
public class IterationScenario {

    @JsonProperty("scenarioKey")
    private IterationScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;

    public IterationScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public IterationScenario setScenarioKey(IterationScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public IterationScenario setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}
