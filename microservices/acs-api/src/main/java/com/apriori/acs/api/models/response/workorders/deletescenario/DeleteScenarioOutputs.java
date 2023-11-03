package com.apriori.acs.api.models.response.workorders.deletescenario;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteScenarioOutputs {
    private ScenarioKey scenarioKey;
}
