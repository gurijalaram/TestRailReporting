package com.apriori.entity.response.cost.costworkorderstatus;

import com.apriori.entity.response.upload.ScenarioKey;

public class CostOrderStatusScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;

    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public CostOrderStatusScenarioIterationKey setScenarioKey(ScenarioKey scenarioKey) {
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
