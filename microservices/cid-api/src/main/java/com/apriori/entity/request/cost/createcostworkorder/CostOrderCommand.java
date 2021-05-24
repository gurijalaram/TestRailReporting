package com.apriori.entity.request.cost.createcostworkorder;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "CreateCostWorkOrderResponse.json")
public class CostOrderCommand {
    private CostOrderCommandType command;

    public CostOrderCommandType getCommand() {
        return command;
    }

    public CostOrderCommand setCommand(CostOrderCommandType command) {
        this.command = command;
        return this;
    }
}