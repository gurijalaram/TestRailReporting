package com.apriori.entity.response.upload;

public class FileUploadScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;

    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public void setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }
}
