package com.apriori.entity.request.publish.createpublishworkorder;

public class PublishScenarioKey {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;

    public String getTypeName() {
        return typeName;
    }

    public PublishScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public PublishScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public PublishScenarioKey setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public PublishScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }
}
