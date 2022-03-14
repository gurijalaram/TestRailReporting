package com.apriori.acs.entity.response.workorders.editscenario;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditScenarioInputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String newScenarioName;
}
