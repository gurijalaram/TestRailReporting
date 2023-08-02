package com.apriori.cic.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostingInputs {
    private String processGroupName;
    private String materialName;
    private String vpeName;
    private String scenarioName;
    private Integer annualVolume;
    private Integer batchSize;
    private String description;
}
