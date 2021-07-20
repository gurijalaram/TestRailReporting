package com.apriori.acs.entity.response.createmissingscenario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;
}
