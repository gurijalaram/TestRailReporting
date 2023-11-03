package com.apriori.acs.api.models.response.acs.scenariosinfo;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScenarioIterationKeysInputs {
    public List<ScenarioIterationKey> scenarioIterationKeys;
}
