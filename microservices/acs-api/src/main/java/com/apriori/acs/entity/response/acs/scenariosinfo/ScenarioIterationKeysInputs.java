package com.apriori.acs.entity.response.acs.scenariosinfo;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScenarioIterationKeysInputs {
    public List<ScenarioIterationKey> scenarioIterationKeys;
}
