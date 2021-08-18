package com.apriori.acs.entity.response.createmissingscenario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMissingScenarioInputs {
    private String baseName;
    private String configurationName;
    private String modelName;
    private String scenarioName;
    private String scenarioType;
    private boolean missing;
    private String createdBy;
}
