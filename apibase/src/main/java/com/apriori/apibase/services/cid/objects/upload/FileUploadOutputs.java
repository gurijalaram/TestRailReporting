package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey"
})
public class FileUploadOutputs {

    @JsonProperty("scenarioIterationKey")
    private FileUploadScenarioIterationKey scenarioIterationKey;

    @JsonProperty("scenarioIterationKey")
    public FileUploadScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    @JsonProperty("scenarioIterationKey")
    public void setScenarioIterationKey(FileUploadScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
    }
}
