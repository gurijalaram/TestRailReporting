package com.apriori.acs.entity.response.getscenariosinfo;

import com.apriori.entity.response.upload.ScenarioIterationKey;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScenarioIterationKeysInputs {
    public ScenarioIterationKey scenarioIterationKeyOne;
    public ScenarioIterationKey scenarioIterationKeyTwo;
}
