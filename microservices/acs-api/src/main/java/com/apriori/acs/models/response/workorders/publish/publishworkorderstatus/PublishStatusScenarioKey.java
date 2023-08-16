package com.apriori.acs.models.response.workorders.publish.publishworkorderstatus;

public class PublishStatusScenarioKey {
    private String stateName;
    private String masterName;
    private String typeName;
    private Integer workspaceId;

    public String getStateName() {
        return stateName;
    }

    public PublishStatusScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public PublishStatusScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public PublishStatusScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public PublishStatusScenarioKey setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }
}