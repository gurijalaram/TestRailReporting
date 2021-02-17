package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioKey.json")
public class ScenarioKey {

    private String stateName;
    private String typeName;
    private String masterName;
    private String workspaceId;

    public String getStateName () {
        return stateName;
    }

    public void setStateName (String stateName) {
        this.stateName = stateName;
    }

    public String getTypeName () {
        return typeName;
    }

    public void setTypeName (String typeName) {
        this.typeName = typeName;
    }

    public String getMasterName () {
        return masterName;
    }

    public void setMasterName (String masterName) {
        this.masterName = masterName;
    }

    public String getWorkspaceId () {
        return workspaceId;
    }

    public void setWorkspaceId (String workspaceId) {
        this.workspaceId = workspaceId;
    }
}