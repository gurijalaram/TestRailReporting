package com.apriori.apibase.services.cid.objects.cost.createcostworkorder;

public class CostOrderScenarioIteration {
    private CostOrderScenario scenarioKey;
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
