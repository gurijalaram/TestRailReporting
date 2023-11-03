package com.apriori.acs.api.models.response.workorders.editscenario;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditScenarioInputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String newScenarioName;
}
