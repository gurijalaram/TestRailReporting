package com.apriori.entity.request.publish.createpublishworkorder;

public class PublishInputs {
    private Boolean overwrite;
    private Boolean lock;
    private PublishScenarioIterationKey scenarioIterationKey;

    public Boolean getOverwrite() {
        return overwrite;
    }

    public PublishInputs setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    public Boolean getLock() {
        return lock;
    }

    public PublishInputs setLock(Boolean lock) {
        this.lock = lock;
        return this;
    }

    public PublishScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public PublishInputs setScenarioIterationKey(PublishScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

}
