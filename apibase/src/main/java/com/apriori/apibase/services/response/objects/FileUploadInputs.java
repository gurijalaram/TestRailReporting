package com.apriori.apibase.services.response.objects;

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
    "scenarioName",
    "fileKey",
    "fileName"
})
public class FileUploadInputs {

    @JsonProperty("scenarioName")
    private String scenarioName;
    @JsonProperty("fileKey")
    private String fileKey;
    @JsonProperty("fileName")
    private String fileName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioName")
    public String getScenarioName() {
        return scenarioName;
    }

    @JsonProperty("scenarioName")
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    @JsonProperty("fileKey")
    public String getFileKey() {
        return fileKey;
    }

    @JsonProperty("fileKey")
    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
