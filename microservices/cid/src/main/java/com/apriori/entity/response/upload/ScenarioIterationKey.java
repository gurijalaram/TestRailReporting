package com.apriori.entity.response.upload;

public class ScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;

    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public ScenarioIterationKey setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public ScenarioIterationKey setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}
