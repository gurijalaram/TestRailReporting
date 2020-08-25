package com.apriori.apibase.services.cid.objects.cost.createcostworkorder;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "command"
})

@Schema(location = "CreateCostWorkOrderResponse.json")
public class CostOrderCommand {

    @JsonProperty("command")
    private CostOrderCommandType command;

    public CostOrderCommandType getCommand() {
        return command;
    }

    public CostOrderCommand setCommand(CostOrderCommandType command) {
        this.command = command;
        return this;
    }
}