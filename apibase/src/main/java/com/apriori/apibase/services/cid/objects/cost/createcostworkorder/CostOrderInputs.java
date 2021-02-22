package com.apriori.apibase.services.cid.objects.cost.createcostworkorder;

public class CostOrderInputs {
    private Integer inputSetId;
    private CostOrderScenarioIteration scenarioIterationKey;

    public Integer getInputSetId() {
        return inputSetId;
    }

    public CostOrderInputs setInputSetId(Integer inputSetId) {
        this.inputSetId = inputSetId;
        return this;
    }

    public CostOrderScenarioIteration getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderInputs setScenarioIterationKey(CostOrderScenarioIteration scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }
}