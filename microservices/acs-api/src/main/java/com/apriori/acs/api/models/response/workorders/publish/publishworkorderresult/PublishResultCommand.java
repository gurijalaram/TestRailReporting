package com.apriori.acs.api.models.response.workorders.publish.publishworkorderresult;

public class PublishResultCommand {
    private String commandType;
    private PublishResultInputs inputs;
    private PublishResultOutputs outputs;

    public String getCommandType() {
        return commandType;
    }

    public PublishResultCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public PublishResultInputs getInputs() {
        return inputs;
    }

    public PublishResultCommand setInputs(PublishResultInputs inputs) {
        this.inputs = inputs;
        return this;
    }

    public PublishResultOutputs getOutputs() {
        return outputs;
    }

    public PublishResultCommand setOutputs(PublishResultOutputs outputs) {
        this.outputs = outputs;
        return this;
    }
}
