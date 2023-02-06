package com.apriori.acs.entity.response.acs.costresults;

import lombok.Data;

@Data
public class ArtifactKey {
    private String artifactTypeName;
    private Integer sequenceNumber;
    private String displayName;
}
