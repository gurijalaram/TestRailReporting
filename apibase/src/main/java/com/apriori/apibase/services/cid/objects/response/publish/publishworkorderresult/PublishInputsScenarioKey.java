package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "stateName",
    "masterName",
    "typeName",
    "workspaceId"
})
public class PublishInputsScenarioKey {

    @JsonProperty("stateName")
    private String stateName;
    @JsonProperty("masterName")
    private String masterName;
    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("workspaceId")
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
