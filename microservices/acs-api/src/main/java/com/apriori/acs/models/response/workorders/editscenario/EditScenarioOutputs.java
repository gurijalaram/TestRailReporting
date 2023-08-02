package com.apriori.acs.models.response.workorders.editscenario;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditScenarioOutputs {
    private ScenarioIterationKey scenarioIterationKey;
}
