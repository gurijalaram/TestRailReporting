package com.apriori.acs.entity.response.acs.getscenariosinfo;

import com.apriori.acs.entity.response.workorders.upload.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScenarioIterationKeysInputs {
    public List<ScenarioIterationKey> scenarioIterationKeys;
}
