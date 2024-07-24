package com.apriori.acs.api.models.request.workorders.cost.createcostworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostOrderInputsBomLoader {
    private Integer inputSetId;
    private CostOrderScenarioIteration scenarioIterationKey;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
    private String defaultScenarioProcessingRule;
    private MappingObject mapping;
}
