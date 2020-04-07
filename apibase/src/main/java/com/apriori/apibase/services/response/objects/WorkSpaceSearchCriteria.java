package com.apriori.apibase.services.response.objects;

import java.util.List;

public class WorkSpaceSearchCriteria {

    private List<Integer> workspaceIDs;

    private List<String> scenarioTypes;

    private String scenarioName;

    private boolean includeAllStateIterations;

    public List<Integer> getWorkspaceIDs() {
        return workspaceIDs;
    }

    public WorkSpaceSearchCriteria setWorkspaceIDs(List<Integer> workspaceIDs) {
        this.workspaceIDs = workspaceIDs;
        return this;
    }

    public List<String> getScenarioTypes() {
        return scenarioTypes;
    }

    public WorkSpaceSearchCriteria setScenarioTypes(List<String> scenarioTypes) {
        this.scenarioTypes = scenarioTypes;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public WorkSpaceSearchCriteria setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public boolean isIncludeAllStateIterations() {
        return includeAllStateIterations;
    }

    public WorkSpaceSearchCriteria setIncludeAllStateIterations(boolean includeAllStateIterations) {
        this.includeAllStateIterations = includeAllStateIterations;
        return this;
    }
}
