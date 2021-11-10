package com.apriori.acs.entity.response.getscenarioinfobyscenarioiterationkey;

import com.apriori.entity.response.upload.ScenarioIterationKey;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "GetScenarioInfoByScenarioIterationKeyResponse.json")
public class GetScenarioInfoByScenarioIterationKeyResponse {
    private boolean initialized;
    private boolean missing;
    private boolean virtual;
    private String componentName;
    private String componentType;
    private String configurationName;
    private String scenarioName;
    private String locked;
    private String fileName;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private ScenarioIterationKey scenarioIterationKey;
}
