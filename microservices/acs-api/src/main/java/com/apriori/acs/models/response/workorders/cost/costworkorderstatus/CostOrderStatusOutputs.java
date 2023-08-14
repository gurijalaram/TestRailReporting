package com.apriori.acs.models.response.workorders.cost.costworkorderstatus;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CostOrderStatusOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String costStatus;
}
