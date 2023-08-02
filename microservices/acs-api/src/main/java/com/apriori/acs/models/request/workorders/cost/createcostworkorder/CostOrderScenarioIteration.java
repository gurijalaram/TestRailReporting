package com.apriori.acs.models.request.workorders.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostOrderScenarioIteration {
    private CostOrderScenario scenarioKey;
    private Integer iteration;
}
