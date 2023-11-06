package com.apriori.acs.api.models.response.acs.gcdmapping;

import lombok.Data;

@Data
public class ArtifactKeyItem {
    private String artifactTypeName;
    private Integer sequenceNumber;
    private String displayName;
}
