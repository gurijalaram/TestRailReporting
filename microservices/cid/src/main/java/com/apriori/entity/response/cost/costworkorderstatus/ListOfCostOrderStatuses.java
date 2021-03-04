package com.apriori.entity.response.cost.costworkorderstatus;

import com.apriori.utils.http.enums.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(location = "cid/CostWorkOrderStatusResponse.json")
public class ListOfCostOrderStatuses extends ArrayList<ListOfCostOrderStatuses> {

    private List<ListOfCostOrderStatus> commandType;

    public List<ListOfCostOrderStatus> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<ListOfCostOrderStatus> commandType) {
        this.commandType = commandType;
    }
}