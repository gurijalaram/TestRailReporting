package com.apriori.acs.api.models.response.workorders.bomloader;

public class BomLoaderCommand {

    private String commandType;
    private BomLoaderStatusInputs inputs;
    private BomLoaderStatusOutputs outputs;

    public String getCommandType() {
        return commandType;
    }

    public BomLoaderCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public BomLoaderStatusInputs getInputs() {
        return inputs;
    }

    public BomLoaderCommand setInputs(BomLoaderStatusInputs inputs) {
        this.inputs = inputs;
        return this;
    }

    public BomLoaderStatusOutputs getOutputs() {
        return outputs;
    }

    public BomLoaderCommand setOutputs(BomLoaderStatusOutputs outputs) {
        this.outputs = outputs;
        return this;
    }
}
