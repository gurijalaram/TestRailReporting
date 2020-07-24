package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

    public FileUploadScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public void setScenarioKey(FileUploadScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }
}
