package com.apriori.acs.api.models.response.acs.scenarioinfobyscenarioiterationkey;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/ScenarioInfoByScenarioIterationKeyResponse.json")
public class ScenarioInfoByScenarioIterationKeyResponse {
    private boolean initialized;
    private boolean missing;
    private boolean virtual;
    private String componentName;
    private String componentType;
    private String configurationName;
    private String scenarioName;
    private String description;
    private String notes;
    private String locked;
    private String fileName;
    private String fileMetadataIdentity;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private ScenarioIterationKey scenarioIterationKey;
}
