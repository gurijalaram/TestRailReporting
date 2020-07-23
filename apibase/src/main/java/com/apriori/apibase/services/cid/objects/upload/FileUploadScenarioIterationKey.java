package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "iteration"
})
public class FileUploadScenarioIterationKey {

    @JsonProperty("scenarioKey")
    private FileUploadScenarioKey scenarioKey;
    @JsonProperty("iteration")
    private Integer iteration;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioKey")
    public FileUploadScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    @JsonProperty("scenarioKey")
    public void setScenarioKey(FileUploadScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    @JsonProperty("iteration")
    public Integer getIteration() {
        return iteration;
    }

    @JsonProperty("iteration")
    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }
}
