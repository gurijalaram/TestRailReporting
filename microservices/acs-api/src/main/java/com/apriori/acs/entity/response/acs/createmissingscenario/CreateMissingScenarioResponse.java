package com.apriori.acs.entity.response.acs.createmissingscenario;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/CreateMissingScenarioResponse.json")
public class CreateMissingScenarioResponse {
    private ScenarioIterationKey scenarioIterationKey;
    private boolean resourceCreated;
    private boolean missing;
}
