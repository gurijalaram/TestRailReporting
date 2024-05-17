package com.apriori.acs.api.models.response.workorders.bomloader;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BomLoaderStatusOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String costStatus;
}
