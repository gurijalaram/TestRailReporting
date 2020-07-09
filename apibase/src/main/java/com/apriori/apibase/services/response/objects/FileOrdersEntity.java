package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "FileOrderSchema.json")
public class FileOrdersEntity {

    @JsonProperty
    private String commandType;

    @JsonProperty
    private List<FileOrderEntity> inputs;

    public String getCommandType() {
        return commandType;
    }

    public FileOrdersEntity setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public List<FileOrderEntity> getInputs() {
        return inputs;
    }

    public FileOrdersEntity setInputs(List<FileOrderEntity> inputs) {
        this.inputs = inputs;
        return this;
    }
}
