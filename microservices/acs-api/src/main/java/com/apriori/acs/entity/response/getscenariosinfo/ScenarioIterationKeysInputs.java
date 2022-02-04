package com.apriori.acs.entity.response.getscenariosinfo;

import com.apriori.entity.response.upload.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScenarioIterationKeysInputs {
    public List<ScenarioIterationKey> scenarioIterationKeys;
}
