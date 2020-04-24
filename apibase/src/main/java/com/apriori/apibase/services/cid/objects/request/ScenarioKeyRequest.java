package com.apriori.apibase.services.cid.objects.request;

public class ScenarioKeyRequest {

    private String typeName;
    private String masterName;
    private String stateName;

    public String getTypeName() {
        return typeName;
    }

    public ScenarioKeyRequest setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public ScenarioKeyRequest setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public ScenarioKeyRequest setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }
}
