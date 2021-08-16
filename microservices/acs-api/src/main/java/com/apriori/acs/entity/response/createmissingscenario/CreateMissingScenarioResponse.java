package com.apriori.acs.entity.response.createmissingscenario;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "CreateMissingScenarioResponse.json")
public class CreateMissingScenarioResponse {
    private ScenarioIterationKey scenarioIterationKey;
    private boolean resourceCreated;
    private boolean missing;
}
