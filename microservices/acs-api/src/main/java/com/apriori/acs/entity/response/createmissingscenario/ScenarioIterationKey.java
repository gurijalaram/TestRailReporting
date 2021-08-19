package com.apriori.acs.entity.response.createmissingscenario;

import lombok.Data;

@Data
public class ScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;
}
