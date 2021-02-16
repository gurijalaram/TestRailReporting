package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

public class CostOrderStatusInputs {
    private CostOrderStatusScenarioIterationKey scenarioIterationKey;
    private Integer inputSetId;

    public CostOrderStatusScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderStatusInputs setScenarioIterationKey(CostOrderStatusScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public Integer getInputSetId() {
        return inputSetId;
    }

    public CostOrderStatusInputs setInputSetId(Integer inputSetId) {
        this.inputSetId = inputSetId;
        return this;
    }
}
