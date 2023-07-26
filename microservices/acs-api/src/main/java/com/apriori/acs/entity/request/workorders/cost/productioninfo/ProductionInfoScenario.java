package com.apriori.acs.entity.request.workorders.cost.productioninfo;

import com.apriori.annotations.Schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(location = "workorders/FileCostWorkorderResponse.json")
public class ProductionInfoScenario {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;
}