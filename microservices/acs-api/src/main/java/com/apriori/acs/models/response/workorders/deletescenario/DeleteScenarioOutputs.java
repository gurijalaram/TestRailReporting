package com.apriori.acs.models.response.workorders.deletescenario;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteScenarioOutputs {
    private ScenarioKey scenarioKey;
}
