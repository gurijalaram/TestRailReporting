package com.apriori.acs.models.response.workorders.publish.publishworkorderstatus;

public class PublishStatusInputs {
    private PublishStatusScenarioIterationKey scenarioIterationKey;
    private Boolean overwrite;
    private Boolean lock;

    public PublishStatusScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public PublishStatusInputs setScenarioIterationKey(PublishStatusScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public PublishStatusInputs setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    public Boolean getLock() {
        return lock;
    }

    public PublishStatusInputs setLock(Boolean lock) {
        this.lock = lock;
        return this;
    }
}