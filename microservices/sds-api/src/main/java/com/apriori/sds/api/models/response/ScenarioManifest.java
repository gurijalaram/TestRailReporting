package com.apriori.sds.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

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
    private String scenarioPublished;
    private String totalSubComponents;
    private String scenarioType;
    private Integer depth;
    private Boolean excluded;
}
