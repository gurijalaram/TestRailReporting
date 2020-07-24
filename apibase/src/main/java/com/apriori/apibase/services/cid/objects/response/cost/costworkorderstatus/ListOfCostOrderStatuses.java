package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "items"
})

@Schema(location = "cid/CostWorkOrderStatusSchema.json")
public class ListOfCostOrderStatuses extends ArrayList<ListOfCostOrderStatuses> {

    @JsonProperty("items")
    private List<ListOfCostOrderStatus> commandType;

    public List<ListOfCostOrderStatus> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<ListOfCostOrderStatus> commandType) {
        this.commandType = commandType;
    }
}