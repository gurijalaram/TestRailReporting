package com.apriori.apibase.services.cid.objects.cost.productioninfo;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "typeName",
    "stateName",
    "workspaceId",
    "masterName"
})

@Schema(location = "FileCostWorkOrderResponse.json")
public class ProductionInfoScenario {

    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("stateName")
    private String stateName;
    @JsonProperty("workspaceId")
    private Integer workspaceId;
    @JsonProperty("masterName")
    private String masterName;

    public String getTypeName() {
        return typeName;
    }

    public ProductionInfoScenario setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getStateName() {
        return stateName;
    }

    public ProductionInfoScenario setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public ProductionInfoScenario setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }

    public String getMasterName() {
        return masterName;
    }

    public ProductionInfoScenario setMasterName(String masterName) {
        this.masterName = masterName;
        return this;
    }
}