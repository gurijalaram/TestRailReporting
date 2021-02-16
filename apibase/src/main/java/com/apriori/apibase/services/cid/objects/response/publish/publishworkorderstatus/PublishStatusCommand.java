package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderstatus;

public class PublishStatusCommand {
    private String commandType;
    private PublishStatusInputs inputs;

    public String getCommandType() {
        return commandType;
    }

    public PublishStatusCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public PublishStatusInputs getInputs() {
        return inputs;
    }

    public PublishStatusCommand setInputs(PublishStatusInputs inputs) {
        this.inputs = inputs;
        return this;
    }
}