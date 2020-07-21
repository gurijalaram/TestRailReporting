package com.apriori.apibase.services.cid.objects.cost.costworkorderstatus;

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
    "inputs"
})
public class Command {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
    private Inputs inputs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("commandType")
    public String getCommandType() {
        return commandType;
    }

    @JsonProperty("commandType")
    public Command setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    @JsonProperty("inputs")
    public Inputs getInputs() {
        return inputs;
    }

    @JsonProperty("inputs")
    public Command setInputs(Inputs inputs) {
        this.inputs = inputs;
        return this;
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
