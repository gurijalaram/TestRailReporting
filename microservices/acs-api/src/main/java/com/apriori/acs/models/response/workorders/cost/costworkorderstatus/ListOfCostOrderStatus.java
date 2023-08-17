package com.apriori.acs.models.response.workorders.cost.costworkorderstatus;

import java.util.List;

public class ListOfCostOrderStatus {

    private List<CostOrderStatusInfo> commandType;

    public List<CostOrderStatusInfo> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<CostOrderStatusInfo> commandType) {
        this.commandType = commandType;
    }
}
