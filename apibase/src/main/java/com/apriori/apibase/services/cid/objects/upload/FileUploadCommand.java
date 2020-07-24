package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public FileUploadInputs getInputs() {
        return inputs;
    }

    public void setInputs(FileUploadInputs inputs) {
        this.inputs = inputs;
    }

    public FileUploadOutputs getOutputs() {
        return outputs;
    }

    public void setOutputs(FileUploadOutputs outputs) {
        this.outputs = outputs;
    }
}