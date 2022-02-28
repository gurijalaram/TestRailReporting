package com.apriori.acs.entity.request.workorders.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostOrderInputs {
    private Integer inputSetId;
    private CostOrderScenarioIteration scenarioIterationKey;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
}