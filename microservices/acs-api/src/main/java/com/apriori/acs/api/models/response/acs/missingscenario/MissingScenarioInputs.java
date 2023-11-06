package com.apriori.acs.api.models.response.acs.missingscenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissingScenarioInputs {
    private String baseName;
    private String configurationName;
    private String modelName;
    private String scenarioName;
    private String scenarioType;
    private boolean missing;
    @JsonProperty("public")
    private boolean publicItem;
    private String createdBy;
    private String userId;
}
