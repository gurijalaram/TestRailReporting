package com.apriori.acs.api.models.request.workorders.cost.createcostworkorder;

public class CostOrderCommandType {
    private String commandType;
    private CostOrderInputs inputs;

    public String getCommandType() {
        return commandType;
    }

    public CostOrderCommandType setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public CostOrderInputs getInputs() {
        return inputs;
    }

    public CostOrderCommandType setInputs(CostOrderInputs inputs) {
        this.inputs = inputs;
        return this;
    }
}
