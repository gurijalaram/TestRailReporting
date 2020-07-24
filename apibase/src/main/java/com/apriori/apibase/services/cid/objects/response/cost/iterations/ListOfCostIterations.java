package com.apriori.apibase.services.cid.objects.response.cost.iterations;

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

@Schema(location = "cid/CostIterationSchema.json")
public class ListOfCostIterations extends ArrayList<ListOfCostIterations> {

    @JsonProperty("items")
    private List<ListOfCostIteration> commandType;

    public List<ListOfCostIteration> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<ListOfCostIteration> commandType) {
        this.commandType = commandType;
    }
}
