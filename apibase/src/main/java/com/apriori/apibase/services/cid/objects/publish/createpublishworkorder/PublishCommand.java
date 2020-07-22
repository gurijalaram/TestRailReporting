package com.apriori.apibase.services.cid.objects.publish.createpublishworkorder;

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
public class PublishCommand {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
    private PublishInputs inputs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("commandType")
    public String getCommandType() {
        return commandType;
    }

    @JsonProperty("commandType")
    public PublishCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    @JsonProperty("inputs")
    public PublishInputs getInputs() {
        return inputs;
    }

    @JsonProperty("inputs")
    public PublishCommand setInputs(PublishInputs inputs) {
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
