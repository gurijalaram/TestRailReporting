package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

public class CostOrderStatusOutputs {
    private CostOrderStatusScenarioIterationKey scenarioIterationKey;
    private String costStatus;

    public CostOrderStatusScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderStatusOutputs setScenarioIterationKey(CostOrderStatusScenarioIterationKey scenarioIterationKey) {
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
