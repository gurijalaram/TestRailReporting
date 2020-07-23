package com.apriori.apibase.services.cid.objects.publish.publishworkorderresult;

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
    "scenarioIterationKey",
    "overwrite",
    "lock"
})
public class Inputs {

    @JsonProperty("scenarioIterationKey")
    private ScenarioIterationKey scenarioIterationKey;
    @JsonProperty("newScenarioName")
    private String newScenarioName;
    @JsonProperty("overwrite")
    private Boolean overwrite;
    @JsonProperty("lock")
    private Boolean lock;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioIterationKey")
    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    @JsonProperty("scenarioIterationKey")
    public Inputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    @JsonProperty("newScenarioName")
    public String getNewScenarioName() {
        return newScenarioName;
    }

    @JsonProperty("newScenarioName")
    public Inputs setNewScenarioName(String newScenarioName) {
        this.newScenarioName = newScenarioName;
        return this;
    }

    @JsonProperty("overwrite")
    public Boolean getOverwrite() {
        return overwrite;
    }

    @JsonProperty("overwrite")
    public Inputs setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    @JsonProperty("lock")
    public Boolean getLock() {
        return lock;
    }

    @JsonProperty("lock")
    public Inputs setLock(Boolean lock) {
        this.lock = lock;
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