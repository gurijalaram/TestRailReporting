package com.apriori.acs.api.models.response.workorders.publish.publishworkorderresult;

public class PublishInputsScenarioKey {
    private String stateName;
    private String masterName;
    private String typeName;
    private Integer workspaceId;

    public String getStateName() {
        return stateName;
    }

    public PublishInputsScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public PublishInputsScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public PublishInputsScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public PublishInputsScenarioKey setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }
}
