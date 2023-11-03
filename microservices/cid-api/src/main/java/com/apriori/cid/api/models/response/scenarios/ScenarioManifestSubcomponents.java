package com.apriori.cid.api.models.response.scenarios;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScenarioManifestSubcomponents {
    private String componentIdentity;
    private String componentName;
    private String componentType;
    private String configurationName;
    private String cadMetadataIdentity;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioType;
    private String scenarioState;
    private Boolean scenarioPublished;
    private String scenarioAssociationIdentity;
    private Integer depth;
    private Boolean excluded;
    private Integer occurrences;
    private Integer totalComponents;
    private Integer totalSubComponents;
    @JsonProperty("subComponents")
    private List<ScenarioManifestSubcomponents> subcomponents;
}
