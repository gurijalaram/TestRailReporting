package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Schema(location = "sds/ScenarioManifestSubComponents.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
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
