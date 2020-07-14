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
    "commandType",
    "inputs",
    "outputs"
})

public class FileUploadCommand {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
    private FileUploadInputs inputs;
    @JsonProperty("outputs")
    private FileUploadOutputs outputs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("commandType")
    public String getCommandType() {
        return commandType;
    }

    @JsonProperty("commandType")
    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    @JsonProperty("inputs")
    public FileUploadInputs getInputs() {
        return inputs;
    }

    @JsonProperty("inputs")
    public void setInputs(FileUploadInputs inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("outputs")
    public FileUploadOutputs getOutputs() {
        return outputs;
    }

    @JsonProperty("outputs")
    public void setOutputs(FileUploadOutputs outputs) {
        this.outputs = outputs;
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
