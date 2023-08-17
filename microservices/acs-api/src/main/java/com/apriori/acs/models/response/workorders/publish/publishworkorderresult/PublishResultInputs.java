package com.apriori.acs.models.response.workorders.publish.publishworkorderresult;

public class PublishResultInputs {
    private PublishInputsScenarioIteration scenarioIterationKey;
    private Boolean overwrite;
    private Boolean lock;

    public PublishInputsScenarioIteration getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public PublishResultInputs setScenarioIterationKey(PublishInputsScenarioIteration scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public PublishResultInputs setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    public Boolean getLock() {
        return lock;
    }

    public PublishResultInputs setLock(Boolean lock) {
        this.lock = lock;
        return this;
    }
}