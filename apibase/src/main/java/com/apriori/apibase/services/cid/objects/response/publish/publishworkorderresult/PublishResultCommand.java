package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commandType",
    "inputs",
    "outputs"
})
public class PublishResultCommand {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
    private PublishResultInputs inputs;
    @JsonProperty("outputs")
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
