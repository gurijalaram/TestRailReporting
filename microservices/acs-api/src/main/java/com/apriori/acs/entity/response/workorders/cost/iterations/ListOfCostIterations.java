package com.apriori.acs.entity.response.workorders.cost.iterations;

import com.apriori.utils.http.enums.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(location = "workorders/CostIterationResponse.json")
public class ListOfCostIterations extends ArrayList<ListOfCostIterations> {

    private List<ListOfCostIteration> commandType;

    public List<ListOfCostIteration> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<ListOfCostIteration> commandType) {
        this.commandType = commandType;
    }
}