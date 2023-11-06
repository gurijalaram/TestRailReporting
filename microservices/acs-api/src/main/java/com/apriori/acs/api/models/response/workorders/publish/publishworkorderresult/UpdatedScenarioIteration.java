package com.apriori.acs.api.models.response.workorders.publish.publishworkorderresult;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;

@Data
public class UpdatedScenarioIteration {
    private ScenarioIterationKey previousIteration;
    private ScenarioIterationKey latestIteration;
}
