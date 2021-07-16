package com.apriori.acs.entity.response.publish.getscenarioinfobyscenarioiterationkey;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GetScenarioInfoByScenarioIterationKeyResponse.json")
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
    private String updateddBy;
    private String updatedAt;
}
