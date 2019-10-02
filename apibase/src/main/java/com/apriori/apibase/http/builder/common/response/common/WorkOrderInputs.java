package com.apriori.apibase.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkOrderInputs {

    @JsonProperty
    private ScenarioIterationKey scenarioIterationKey;

    @JsonProperty
    private boolean includeOtherWorkspace;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public WorkOrderInputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
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
