package com.apriori.acs.entity.request.workorders.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostOrderScenario {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;
}
