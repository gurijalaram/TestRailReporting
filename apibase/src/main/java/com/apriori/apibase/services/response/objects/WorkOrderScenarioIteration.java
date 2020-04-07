package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkOrderScenarioIteration {

    @JsonProperty
    private WorkOrderScenarioKey workOrderScenarioKey;

    @JsonProperty
    private Integer iteration;

    public WorkOrderScenarioKey getWorkOrderScenarioKey() {
        return workOrderScenarioKey;
    }

    public WorkOrderScenarioIteration setWorkOrderScenarioKey(WorkOrderScenarioKey workOrderScenarioKey) {
        this.workOrderScenarioKey = workOrderScenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public WorkOrderScenarioIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}
