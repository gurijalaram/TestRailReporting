package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileOrdersEntity {

    @JsonProperty
    private String commandType;

    @JsonProperty
    private FileOrderEntity inputs;

    public String getCommandType() {
        return commandType;
    }

    public FileOrdersEntity setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public FileOrderEntity getInputs() {
        return inputs;
    }

    public FileOrdersEntity setInputs(FileOrderEntity inputs) {
        this.inputs = inputs;
        return this;
    }
}
