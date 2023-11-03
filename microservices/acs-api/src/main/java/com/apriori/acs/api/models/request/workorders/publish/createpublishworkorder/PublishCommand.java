package com.apriori.acs.api.models.request.workorders.publish.createpublishworkorder;

public class PublishCommand {
    private String commandType;
    private PublishInputs inputs;

    public String getCommandType() {
        return commandType;
    }

    public PublishCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public PublishInputs getInputs() {
        return inputs;
    }

    public PublishCommand setInputs(PublishInputs inputs) {
        this.inputs = inputs;
        return this;
    }
}
