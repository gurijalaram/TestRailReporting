package com.apriori.entity.response.cost.costworkorderstatus;

import com.apriori.entity.response.upload.ScenarioIterationKey;

public class CostOrderStatusInputs {
    private ScenarioIterationKey scenarioIterationKey;
    private Integer inputSetId;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderStatusInputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
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
