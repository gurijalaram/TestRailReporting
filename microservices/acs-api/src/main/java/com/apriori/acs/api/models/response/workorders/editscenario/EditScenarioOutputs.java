package com.apriori.acs.api.models.response.workorders.editscenario;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditScenarioOutputs {
    private ScenarioIterationKey scenarioIterationKey;
}
