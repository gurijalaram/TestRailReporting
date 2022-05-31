package com.apriori.acs.entity.request.workorders.cost.createcostworkorder;

import com.apriori.acs.entity.response.workorders.upload.AssemblyComponent;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CostOrderInputs {
    private Integer inputSetId;
    private CostOrderScenarioIteration scenarioIterationKey;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
    private List<AssemblyComponent> subComponents;
}