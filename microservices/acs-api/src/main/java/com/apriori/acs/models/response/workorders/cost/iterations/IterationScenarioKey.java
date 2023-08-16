package com.apriori.acs.models.response.workorders.cost.iterations;

public class IterationScenarioKey {

    private String stateName;
    private String masterName;
    private String typeName;
    private Integer workspaceId;

    public String getStateName() {
        return stateName;
    }

    public IterationScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public IterationScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public IterationScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public IterationScenarioKey setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }
}
