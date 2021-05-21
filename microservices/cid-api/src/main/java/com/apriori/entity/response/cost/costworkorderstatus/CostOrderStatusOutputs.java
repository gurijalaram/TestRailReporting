package com.apriori.entity.response.cost.costworkorderstatus;

import com.apriori.entity.response.upload.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CostOrderStatusOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String costStatus;
}
