package com.apriori.acs.models.response.workorders.allimages;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllImagesInputs {
    private ScenarioIterationKey scenarioIterationKey;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;
}
