package com.apriori.entity.response.cost.costworkorderstatus;

public class CostOrderStatusScenarioIterationKey {
    private CostOrderScenarioKey scenarioKey;
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
