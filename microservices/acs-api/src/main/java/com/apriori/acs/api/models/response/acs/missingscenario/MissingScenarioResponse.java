package com.apriori.acs.api.models.response.acs.missingscenario;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/CreateMissingScenarioResponse.json")
public class MissingScenarioResponse {
    private ScenarioIterationKey scenarioIterationKey;
    private boolean resourceCreated;
    private boolean missing;
}
