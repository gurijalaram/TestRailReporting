package com.apriori.cic.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentWorkflowJobPartCostingResult {
    private Double fullyBurdenedCost;
    private String dfmRisk;
    private Double totalCost;
    private Double materialCost;
    private Double cycleTime;
    private Double laborTime;
    private Double finishMass;
    private Double roughMass;
    private Double utilization;
    private String routingName;
    private Integer dfmScore;
}
