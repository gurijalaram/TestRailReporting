package com.apriori.entity.response.publish.publishworkorderresult;

import com.apriori.entity.response.upload.ScenarioKey;

public class PublishOutputsScenarioIteration {
    private ScenarioKey scenarioKey;
    private Integer iteration;

    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishOutputsScenarioIteration setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishOutputsScenarioIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}