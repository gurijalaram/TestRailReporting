package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey"
})
public class FileUploadOutputs {

    @JsonProperty("scenarioIterationKey")
    private FileUploadScenarioIterationKey scenarioIterationKey;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioIterationKey")
    public FileUploadScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    @JsonProperty("scenarioIterationKey")
    public void setScenarioIterationKey(FileUploadScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
    }
}
