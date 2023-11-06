package com.apriori.acs.api.models.response.workorders.genericclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;
}
