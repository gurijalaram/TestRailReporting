package com.apriori.apibase.services.cid.objects.cost.createcostworkorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commandType",
    "inputs"
})

public class CostOrderCommandType {

    @JsonProperty("commandType")
    private String commandType;
    @JsonProperty("inputs")
    private CostOrderInputs inputs;

    public String getCommandType() {
        return commandType;
    }

    public CostOrderCommandType setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public CostOrderInputs getInputs() {
        return inputs;
    }

    public CostOrderCommandType setInputs(CostOrderInputs inputs) {
        this.inputs = inputs;
        return this;
    }
}
