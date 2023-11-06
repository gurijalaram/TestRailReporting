package com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus;

import com.apriori.shared.util.annotations.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(location = "workorders/CostWorkOrderStatusResponse.json")
public class ListOfCostOrderStatuses extends ArrayList<ListOfCostOrderStatuses> {

    private List<ListOfCostOrderStatus> commandType;

    public List<ListOfCostOrderStatus> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<ListOfCostOrderStatus> commandType) {
        this.commandType = commandType;
    }
}