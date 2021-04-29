package com.apriori.entity.response.upload;

public class FileUploadOutputs {

    private ScenarioIterationKey scenarioIterationKey;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public FileUploadOutputs FileUploadOutputs(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }
}
