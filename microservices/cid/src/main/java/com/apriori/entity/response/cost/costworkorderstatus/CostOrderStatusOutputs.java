package com.apriori.entity.response.cost.costworkorderstatus;

public class CostOrderStatusOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String costStatus;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderStatusOutputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public CostOrderStatusOutputs setCostStatus(String costStatus) {
        this.costStatus = costStatus;
        return this;
    }
}
