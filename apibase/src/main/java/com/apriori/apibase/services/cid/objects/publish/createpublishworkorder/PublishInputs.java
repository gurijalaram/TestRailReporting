package com.apriori.apibase.services.cid.objects.publish.createpublishworkorder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "overwrite",
    "lock",
    "scenarioIterationKey"
})
public class PublishInputs {

    @JsonProperty("overwrite")
    private Boolean overwrite;
    @JsonProperty("lock")
    private Boolean lock;
    @JsonProperty("scenarioIterationKey")
    private PublishScenarioIterationKey scenarioIterationKey;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("overwrite")
    public Boolean getOverwrite() {
        return overwrite;
    }

    @JsonProperty("overwrite")
    public PublishInputs setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    @JsonProperty("lock")
    public Boolean getLock() {
        return lock;
    }

    @JsonProperty("lock")
    public PublishInputs setLock(Boolean lock) {
        this.lock = lock;
        return this;
    }

    @JsonProperty("scenarioIterationKey")
    public PublishScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    @JsonProperty("scenarioIterationKey")
    public PublishInputs setScenarioIterationKey(PublishScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
