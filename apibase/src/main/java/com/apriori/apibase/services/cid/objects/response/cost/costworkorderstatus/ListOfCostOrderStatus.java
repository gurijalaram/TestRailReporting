package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListOfCostOrderStatus {

    @JsonProperty("items")
    private List<CostOrderStatusInfo> commandType;

    public List<CostOrderStatusInfo> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<CostOrderStatusInfo> commandType) {
        this.commandType = commandType;
    }
}
