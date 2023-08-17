package com.apriori.acs.models.response.workorders.upload;

public class FileUploadCommand {
    private String commandType;
    private FileUploadInputs inputs;
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