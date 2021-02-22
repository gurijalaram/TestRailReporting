package com.apriori.entity.request.cost.createcostworkorder;

public class CostOrderScenario {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;

    public String getTypeName() {
        return typeName;
    }

    public CostOrderScenario setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public CostOrderScenario setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public CostOrderScenario setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public CostOrderScenario setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }
}
