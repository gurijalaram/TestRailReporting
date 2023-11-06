package com.apriori.acs.api.models.response.workorders.deletescenario;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteScenarioInputs {
    private ScenarioIterationKey scenarioIterationKey;
    private int iteration;
    private boolean includeOtherWorkspace;
}
