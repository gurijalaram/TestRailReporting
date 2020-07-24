package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey",
    "overwrite",
    "lock"
})
public class PublishStatusInputs {

    @JsonProperty("scenarioIterationKey")
    private PublishStatusScenarioIterationKey scenarioIterationKey;
    @JsonProperty("overwrite")
    private Boolean overwrite;
    @JsonProperty("lock")
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