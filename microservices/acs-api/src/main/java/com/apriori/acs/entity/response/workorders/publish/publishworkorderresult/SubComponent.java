package com.apriori.acs.entity.response.workorders.publish.publishworkorderresult;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;

@Data
public class SubComponent {
    private ScenarioIterationKey scenarioIterationKey;
    private Boolean ignored;
}
