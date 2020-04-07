package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkOrderInputs {

    @JsonProperty
    private WorkOrderScenarioIteration workOrderScenarioIteration;

    @JsonProperty
    private boolean includeOtherWorkspace;

    public WorkOrderScenarioIteration getWorkOrderScenarioIteration() {
        return workOrderScenarioIteration;
    }

    public WorkOrderInputs setWorkOrderScenarioIteration(WorkOrderScenarioIteration workOrderScenarioIteration) {
        this.workOrderScenarioIteration = workOrderScenarioIteration;
        return this;
    }

    public boolean isIncludeOtherWorkspace() {
        return includeOtherWorkspace;
    }

    public WorkOrderInputs setIncludeOtherWorkspace(boolean includeOtherWorkspace) {
        this.includeOtherWorkspace = includeOtherWorkspace;
        return this;
    }
}
