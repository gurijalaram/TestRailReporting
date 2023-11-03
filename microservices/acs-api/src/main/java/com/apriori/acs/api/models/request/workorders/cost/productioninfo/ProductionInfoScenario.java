package com.apriori.acs.api.models.request.workorders.cost.productioninfo;

import com.apriori.shared.util.annotations.Schema;

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