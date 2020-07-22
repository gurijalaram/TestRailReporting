package com.apriori.apibase.services.cid.objects.publish.publishworkorderresult;

import com.apriori.apibase.services.cid.objects.cost.costworkorderstatus.Outputs;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commandType",
    "inputs",
    "outputs"
})
public class PublishCommand {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
    private Inputs inputs;
    @JsonProperty("outputs")
    private Outputs outputs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("commandType")
    public String getCommandType() {
        return commandType;
    }

    @JsonProperty("commandType")
    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    @JsonProperty("inputs")
    public Inputs getInputs() {
        return inputs;
    }

    @JsonProperty("inputs")
    public void setInputs(Inputs inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("outputs")
    public Outputs getOutputs() {
        return outputs;
    }

    @JsonProperty("outputs")
    public void setOutputs(Outputs outputs) {
        this.outputs = outputs;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
