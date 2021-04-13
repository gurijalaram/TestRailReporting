package com.apriori.entity.response.upload;

public class FileUploadOutputs {

    private FileUploadScenarioIterationKey scenarioIterationKey;

    public FileUploadScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public FileUploadOutputs FileUploadOutputs(FileUploadScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }
}
