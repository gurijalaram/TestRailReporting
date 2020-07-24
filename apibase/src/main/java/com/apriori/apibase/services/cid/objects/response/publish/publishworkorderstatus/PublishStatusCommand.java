package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commandType",
    "inputs"
})
public class PublishStatusCommand {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
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