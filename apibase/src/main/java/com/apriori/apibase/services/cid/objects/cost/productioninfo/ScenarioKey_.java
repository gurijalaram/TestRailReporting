package com.apriori.apibase.services.cid.objects.cost.productioninfo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "typeName",
    "stateName",
    "workspaceId",
    "masterName"
})
public class ScenarioKey_ {

    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("stateName")
    private String stateName;
    @JsonProperty("workspaceId")
    private Integer workspaceId;
    @JsonProperty("masterName")
    private String masterName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("typeName")
    public String getTypeName() {
        return typeName;
    }

    @JsonProperty("typeName")
    public ScenarioKey_ setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    @JsonProperty("stateName")
    public String getStateName() {
        return stateName;
    }

    @JsonProperty("stateName")
    public ScenarioKey_ setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    @JsonProperty("workspaceId")
    public Integer getWorkspaceId() {
        return workspaceId;
    }

    @JsonProperty("workspaceId")
    public ScenarioKey_ setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }

    @JsonProperty("masterName")
    public String getMasterName() {
        return masterName;
    }

    @JsonProperty("masterName")
    public ScenarioKey_ setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
