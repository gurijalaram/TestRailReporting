package com.apriori.entity.response.publish.publishworkorderresult;

import com.apriori.entity.response.cost.costworkorderstatus.ScenarioIterationKey;

public class PublishResultOutputs {
    private ScenarioIterationKey scenarioIterationKey;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public PublishResultOutputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }
}
