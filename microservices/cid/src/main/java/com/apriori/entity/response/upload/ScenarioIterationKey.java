package com.apriori.entity.response.upload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScenarioIterationKey {
    private ScenarioKey scenarioKey;
    private Integer iteration;
}
