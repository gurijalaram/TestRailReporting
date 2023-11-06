package com.apriori.acs.api.models.response.workorders.genericclasses;

public class WorkorderCommand {

    private String commandType;
    private Object inputs;

    public WorkorderCommand(String commandType, Object inputs) {
        this.commandType = commandType;
        this.inputs = inputs;
    }

    public String getCommandType() {
        return commandType;
    }

    public WorkorderCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public Object getInputs() {
        return inputs;
    }

    public WorkorderCommand setInputs(Object inputs) {
        this.inputs = inputs;
        return this;
    }
}
