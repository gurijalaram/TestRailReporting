package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey",
    "overwrite",
    "lock"
})
public class PublishResultInputs {

    @JsonProperty("scenarioIterationKey")
    private PublishInputsScenarioIteration scenarioIterationKey;
    @JsonProperty("overwrite")
    private Boolean overwrite;
    @JsonProperty("lock")
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