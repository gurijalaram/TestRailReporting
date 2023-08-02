package com.apriori.acs.models.response.workorders.cost.costworkorderstatus;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;

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
