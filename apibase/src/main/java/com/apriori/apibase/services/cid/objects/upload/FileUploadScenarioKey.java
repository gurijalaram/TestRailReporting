package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "stateName",
    "masterName",
    "typeName",
    "workspaceId"
})
public class FileUploadScenarioKey {

    @JsonProperty("stateName")
    private String stateName;
    @JsonProperty("masterName")
    private String masterName;
    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("workspaceId")
    private Integer workspaceId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("stateName")
    public String getStateName() {
        return stateName;
    }

    @JsonProperty("stateName")
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @JsonProperty("masterName")
    public String getMasterName() {
        return masterName;
    }

    @JsonProperty("masterName")
    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    @JsonProperty("typeName")
    public String getTypeName() {
        return typeName;
    }

    @JsonProperty("typeName")
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @JsonProperty("workspaceId")
    public Integer getWorkspaceId() {
        return workspaceId;
    }

    @JsonProperty("workspaceId")
    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }
}
