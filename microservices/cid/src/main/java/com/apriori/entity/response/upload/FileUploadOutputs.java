package com.apriori.entity.response.upload;

import com.apriori.entity.response.upload.ScenarioIterationKey;

public class FileUploadOutputs {

    private ScenarioIterationKey scenarioIterationKey;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public FileUploadOutputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }
}
