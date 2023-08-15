package com.apriori.acs.models.response.workorders.upload;

public class FileOrdersUpload {
    private String commandType;
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
