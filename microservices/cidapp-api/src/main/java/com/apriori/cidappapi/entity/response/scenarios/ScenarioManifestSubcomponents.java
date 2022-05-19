package com.apriori.cidappapi.entity.response.scenarios;

import lombok.Data;

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
    private Boolean included;
    private Integer occurrences;
    private Integer totalComponents;
    private Integer totalSubComponents;
}
