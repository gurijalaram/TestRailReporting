package com.apriori.entity.response.upload;

public class FileUploadScenarioIterationKey {
    private FileUploadScenarioKey scenarioKey;
    private Integer iteration;

    public FileUploadScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public void setScenarioKey(FileUploadScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }
}
