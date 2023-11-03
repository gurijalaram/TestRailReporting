package com.apriori.acs.api.models.request.workorders.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostOrderScenarioIteration {
    private CostOrderScenario scenarioKey;
    private Integer iteration;
}
