package com.apriori.acs.models.request.workorders.cost.productioninfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductionInfoScenarioKey {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;
}
