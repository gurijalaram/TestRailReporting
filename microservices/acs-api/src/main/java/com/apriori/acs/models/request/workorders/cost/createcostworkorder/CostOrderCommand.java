package com.apriori.acs.models.request.workorders.cost.createcostworkorder;

import com.apriori.annotations.Schema;

@Schema(location = "workorders/CreateCostWorkorderResponse.json")
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