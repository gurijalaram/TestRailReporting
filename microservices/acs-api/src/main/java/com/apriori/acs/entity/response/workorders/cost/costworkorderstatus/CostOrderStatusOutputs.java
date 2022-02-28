package com.apriori.acs.entity.response.workorders.cost.costworkorderstatus;

import com.apriori.acs.entity.response.workorders.upload.ScenarioIterationKey;

import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CostOrderStatusOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String costStatus;
}
