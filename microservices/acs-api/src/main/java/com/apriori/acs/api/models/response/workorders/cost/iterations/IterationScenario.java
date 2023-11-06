package com.apriori.acs.api.models.response.workorders.cost.iterations;

public class IterationScenario {
    private IterationScenarioKey scenarioKey;
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
