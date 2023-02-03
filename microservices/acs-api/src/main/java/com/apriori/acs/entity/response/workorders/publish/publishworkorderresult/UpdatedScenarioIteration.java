package com.apriori.acs.entity.response.workorders.publish.publishworkorderresult;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;

@Data
public class UpdatedScenarioIteration {
    private ScenarioIterationKey previousIteration;
    private ScenarioIterationKey latestIteration;
}
