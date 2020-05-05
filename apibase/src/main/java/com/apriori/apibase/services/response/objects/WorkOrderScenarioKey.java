package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkOrderScenarioKey {

    @JsonProperty
    private Integer workspaceID;

    @JsonProperty
    private String stateName;

    @JsonProperty
    private String masterName;

    @JsonProperty
    private String typeName;

    public Integer getWorkspaceID() {
        return workspaceID;
    }

    public WorkOrderScenarioKey setWorkspaceID(Integer workspaceID) {
        this.workspaceID = workspaceID;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public WorkOrderScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public WorkOrderScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public WorkOrderScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }
}
