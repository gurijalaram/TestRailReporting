package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderstatus;

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
public class PublishStatusCommand {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
    private PublishStatusInputs inputs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}