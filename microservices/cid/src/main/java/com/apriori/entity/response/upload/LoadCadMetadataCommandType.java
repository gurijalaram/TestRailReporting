package com.apriori.entity.response.upload;

public class LoadCadMetadataCommandType {

    private String commandType;
    private LoadCadMetadataInputs inputs;

    public String getCommandType() {
        return commandType;
    }

    public LoadCadMetadataCommandType setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public LoadCadMetadataInputs getInputs() {
        return inputs;
    }

    public LoadCadMetadataCommandType setInputs(LoadCadMetadataInputs inputs) {
        this.inputs = inputs;
        return this;
    }
}
