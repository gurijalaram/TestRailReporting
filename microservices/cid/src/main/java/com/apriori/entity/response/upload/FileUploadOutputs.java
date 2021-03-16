package com.apriori.entity.response.upload;

public class FileUploadOutputs {

    private FileUploadScenarioIterationKey scenarioIterationKey;

    public FileUploadScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public FileUploadOutputs(FileUploadOutputs outputs) {

    }

    public void setScenarioIterationKey(FileUploadScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
    }
}
