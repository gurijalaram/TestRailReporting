package com.apriori.cic.api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentWorkflowJobPartCostingInput {
    private String materialName;
    private String processGroupName;
    private String vpeName;
    private Integer annualVolume;
    private Integer batchSize;
    private Double productionLife;
    private String machiningMode;
}
