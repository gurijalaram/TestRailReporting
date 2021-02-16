package com.apriori.apibase.services.cid.objects.cost.productioninfo;

public class ProductionInfoScenarioKey {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;

    public String getTypeName() {
        return typeName;
    }

    public ProductionInfoScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public ProductionInfoScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public ProductionInfoScenarioKey setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public ProductionInfoScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }
}
