package com.apriori.entity.request.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CostOrderInputs {
    private Integer inputSetId;
    private CostOrderScenarioIteration scenarioIterationKey;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
}