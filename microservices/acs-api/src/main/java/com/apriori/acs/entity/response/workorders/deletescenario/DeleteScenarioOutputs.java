package com.apriori.acs.entity.response.workorders.deletescenario;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteScenarioOutputs {
    public ScenarioKey scenarioKey;
}
