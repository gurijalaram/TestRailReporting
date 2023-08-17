package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioManifestSubComponents.json")
@Data
@JsonRootName("response")
public class ScenarioManifestSubComponents {
    private String occurrences;
    private String componentType;
    private String[] subComponents;
    private String componentName;
    private String componentIdentity;
    private String totalComponents;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioState;
    private String totalSubComponents;
}
