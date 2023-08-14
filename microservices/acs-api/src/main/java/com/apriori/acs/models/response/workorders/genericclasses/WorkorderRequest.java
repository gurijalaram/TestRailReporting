package com.apriori.acs.models.response.workorders.genericclasses;

public class WorkorderRequest {

    private WorkorderCommand command;

    public WorkorderCommand getCommand() {
        return command;
    }

    public Object setCommand(WorkorderCommand command) {
        this.command = command;
        return this;
    }
}
