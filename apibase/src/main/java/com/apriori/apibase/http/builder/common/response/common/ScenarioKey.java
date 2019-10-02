package com.apriori.apibase.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScenarioKey {

    @JsonProperty
    private Integer workspaceID;

    @JsonProperty
    private String stateName;

    @JsonProperty
    private String masterName ;

    @JsonProperty
    private String typeName;

    public Integer getWorkspaceID() {
        return workspaceID;
    }

    public ScenarioKey setWorkspaceID(Integer workspaceID) {
        this.workspaceID = workspaceID;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public ScenarioKey setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public ScenarioKey setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public ScenarioKey setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }
}
