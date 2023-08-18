package com.apriori.acs.models.response.acs.missingscenario;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/CreateMissingScenarioResponse.json")
public class MissingScenarioResponse {
    private ScenarioIterationKey scenarioIterationKey;
    private boolean resourceCreated;
    private boolean missing;
}