package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ScenarioManifest.json")
@Data
@JsonRootName("response")
public class ScenarioManifest {
    private String occurrences;
    private String componentType;
    private ScenarioManifestSubComponents[] subComponents;
    private String componentName;
    private String componentIdentity;
    private String cadMetadataIdentity;
    private String totalComponents;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioState;
    private String totalSubComponents;
    private String scenarioType;
}
