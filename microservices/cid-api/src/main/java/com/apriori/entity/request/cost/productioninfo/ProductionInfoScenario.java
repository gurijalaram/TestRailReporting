package com.apriori.entity.request.cost.productioninfo;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "FileCostWorkOrderResponse.json")
public class ProductionInfoScenario {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
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