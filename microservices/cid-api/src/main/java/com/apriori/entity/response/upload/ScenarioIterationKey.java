package com.apriori.entity.response.upload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;
}
