package com.apriori.pagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostingServiceSettingsData {
    private String scenarioName;
    private String processGroup;
    private String  digitalFactory;
    private Integer batchSize;
    private Integer annualVolume;
    private Integer productionVolume;
}
