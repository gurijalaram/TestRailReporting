package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileOrdersUpload {

    @JsonProperty
    private String commandType;

    @JsonProperty
    private FileUploadOrder inputs;

    public String getCommandType() {
        return commandType;
    }

    public FileOrdersUpload setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public FileUploadOrder getInputs() {
        return inputs;
    }

    public FileOrdersUpload setInputs(FileUploadOrder inputs) {
        this.inputs = inputs;
        return this;
    }
}
