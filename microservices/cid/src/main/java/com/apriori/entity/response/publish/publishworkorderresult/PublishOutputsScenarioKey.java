package com.apriori.entity.response.publish.publishworkorderresult;

public class PublishOutputsScenarioKey {
    private String stateName;
    private String masterName;
    private String typeName;
    private Integer workspaceId;

    public String getStateName() {
        return stateName;
    }

    public PublishOutputsScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public PublishOutputsScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public PublishOutputsScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public PublishOutputsScenarioKey setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }
}
