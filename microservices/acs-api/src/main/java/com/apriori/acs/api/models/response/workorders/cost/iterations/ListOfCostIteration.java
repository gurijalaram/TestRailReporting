package com.apriori.acs.api.models.response.workorders.cost.iterations;

import java.util.List;

public class ListOfCostIteration {

    private List<IterationScenario> commandType;

    public List<IterationScenario> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<IterationScenario> commandType) {
        this.commandType = commandType;
    }
}
