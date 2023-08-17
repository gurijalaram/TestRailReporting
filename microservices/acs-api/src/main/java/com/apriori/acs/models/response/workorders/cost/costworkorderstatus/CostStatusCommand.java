package com.apriori.acs.models.response.workorders.cost.costworkorderstatus;

public class CostStatusCommand {

    private String commandType;
    private CostOrderStatusInputs inputs;
    private CostOrderStatusOutputs outputs;

    public String getCommandType() {
        return commandType;
    }

    public CostStatusCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public CostOrderStatusInputs getInputs() {
        return inputs;
    }

    public CostStatusCommand setInputs(CostOrderStatusInputs inputs) {
        this.inputs = inputs;
        return this;
    }

    public CostOrderStatusOutputs getOutputs() {
        return outputs;
    }

    public CostStatusCommand setOutputs(CostOrderStatusOutputs outputs) {
        this.outputs = outputs;
        return this;
    }
}
